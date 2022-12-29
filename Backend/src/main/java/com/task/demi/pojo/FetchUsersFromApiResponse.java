package com.task.demi.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FetchUsersFromApiResponse {
    public UserResponse[] results;
    @JsonIgnore
    public Object info;
}
