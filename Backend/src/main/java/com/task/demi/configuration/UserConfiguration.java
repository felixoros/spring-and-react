package com.task.demi.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfiguration {
    @Value("${user.sync.url}")
    private String url;

    @Value("${user.sync.size}")
    private String size;

    public String getUrl() {
        return url;
    }

    public String getSize() {
        return size;
    }
}
