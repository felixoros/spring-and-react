package com.task.demi.service;

import com.task.demi.db.models.User;
import com.task.demi.db.repositories.UserRepository;
import com.task.demi.pojo.GetUsersCountryResponse;
import com.task.demi.pojo.GetUsersResponse;
import com.task.demi.pojo.GetUsersUserResponse;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private void AddUserToGetUserByCountryHashMap(
            HashMap<String, List<User>> hashMap,
            User user){
        String userCountry = user.getCountry();

        if (!hashMap.containsKey(userCountry)) {
            List<User> list = new ArrayList<User>();
            list.add(user);

            hashMap.put(userCountry, list);
        } else {
            hashMap.get(userCountry).add(user);
        }
    }
    public GetUsersResponse GetUsersByCountry(){
        HashMap<String, List<User>> getUserByCountryHashMap =
                new HashMap<>();

        List<User> users = this.userRepository.findAll();
        for (User user: users) {
            AddUserToGetUserByCountryHashMap(getUserByCountryHashMap, user);
        }

        ArrayList<GetUsersCountryResponse> countriesArr = new ArrayList<>();
        for (Map.Entry<String, List<User>> entry : getUserByCountryHashMap.entrySet()) {
            String key = entry.getKey();
            List<User> value = entry.getValue();

            GetUsersCountryResponse getUsersCountryResponse = new GetUsersCountryResponse();

            ArrayList<GetUsersUserResponse> usersArr = new ArrayList<>();
            for(User user: value){
                GetUsersUserResponse userResponse = new GetUsersUserResponse();

                userResponse.setName(user.getFirstName() + " " +user.getLastName());
                userResponse.setEmail(user.getEmail());
                userResponse.setGender(user.getGender());

                usersArr.add(userResponse);
            }

            getUsersCountryResponse.setName(key);
            getUsersCountryResponse.setUsers(usersArr.stream().toArray( n -> new GetUsersUserResponse[n]));
            countriesArr.add(getUsersCountryResponse);
        }

        GetUsersResponse response = new GetUsersResponse();
        response.setCountries(countriesArr.stream().toArray( n -> new GetUsersCountryResponse[n]));
        return response;
    }
}
