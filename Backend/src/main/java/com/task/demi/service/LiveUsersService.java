package com.task.demi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.demi.configuration.UserConfiguration;
import com.task.demi.db.models.User;
import com.task.demi.db.repositories.UserRepository;
import com.task.demi.helper.StringHelper;
import com.task.demi.pojo.FetchUsersFromApiResponse;
import com.task.demi.pojo.UserResponse;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class LiveUsersService {
    private UserConfiguration userConfiguration;
    private UserRepository userRepository;
    private CacheManager cacheManager;

    private static int counter = 0;

    public LiveUsersService(
            UserConfiguration userConfiguration,
            UserRepository userRepository,
            CacheManager cacheManager) {
        this.userConfiguration = userConfiguration;
        this.userRepository = userRepository;
        this.cacheManager = cacheManager;
    }

    private String getUserRequestUri() {
        String uri = userConfiguration.getUrl() + "?inc=gender,name,location,email&results=";
        String defaultResultSize = "10";

        String userResultsSize = userConfiguration.getSize();
        return userResultsSize != null && userResultsSize.length() != 0 && StringHelper.isInteger(userResultsSize) ?
                uri + userResultsSize : uri + defaultResultSize;
    }

    private ResponseEntity<String> getUsersEntitiesFromApi() {
        counter = counter + 1;

        SimpleClientHttpRequestFactory clientHttpRequestFactory  = new SimpleClientHttpRequestFactory();

        if (counter > 3) {
            clientHttpRequestFactory.setConnectTimeout(2);
            clientHttpRequestFactory.setReadTimeout(2);
        } else {
            clientHttpRequestFactory.setConnectTimeout(10_000);
            clientHttpRequestFactory.setReadTimeout(10_000);
        }

        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("parameters", httpHeaders);

        return restTemplate.getForEntity(getUserRequestUri(), String.class, entity);
    }

    private ArrayList<User> ConvertUsersResponseToUserArrayList(
            FetchUsersFromApiResponse fetchUsersFromApiResponse,
            boolean checkDuplicates) {
        ArrayList<User> users = new ArrayList<>() {};

        for (UserResponse userResponse : fetchUsersFromApiResponse.results) {
            User user = new User(userResponse.name.first, userResponse.name.last, userResponse.email, userResponse.gender, userResponse.location.country);

            if (!checkDuplicates) {
                users.add(user);
                continue;
            }

            List<User> similarUsersByEmail = userRepository.findByEmail(user.getEmail());
            if (similarUsersByEmail.size() != 0) {
                continue;
            }

            users.add(user);
        }

        return users;
    }

    private ArrayList<User> GetCachedUsersFromLiveApi(boolean checkDuplicates) {
        Cache.ValueWrapper usersArray = cacheManager.getCache("users").get(checkDuplicates);
        return usersArray != null ? (ArrayList<User>) usersArray.get() : new ArrayList<>();
    }

    @CachePut(value = "users")
    public ArrayList<User> GetUsersFromLiveApi(boolean checkDuplicates) {
        try {
            ResponseEntity<String> usersResponseEntities = getUsersEntitiesFromApi();
            HttpStatusCode responseStatusCode = usersResponseEntities.getStatusCode();

            if (responseStatusCode == HttpStatus.OK) {
                String stringifiedBody = usersResponseEntities.getBody();

                FetchUsersFromApiResponse fetchUsersFromApiResponse = new ObjectMapper()
                        .readValue(stringifiedBody, FetchUsersFromApiResponse.class);

                return ConvertUsersResponseToUserArrayList(fetchUsersFromApiResponse, checkDuplicates);
            } else {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            ArrayList<User> cachedUsers = GetCachedUsersFromLiveApi(checkDuplicates);
            if (cachedUsers.size() != 0) { return cachedUsers; }

            return new ArrayList<>();
        }
    }

    @Scheduled(cron = "0/${user.sync.period.seconds:10} * * * * ?")
    public void SyncUserData() {
        ArrayList<User> users = GetUsersFromLiveApi(true);
        if (users.size() == 0) { return; }

        userRepository.saveAll(users);
    }
}
