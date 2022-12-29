package com.task.demi.pojo;

public class GetUsersCountryResponse {

    public String name;

    public GetUsersUserResponse[] users;

    public void setName(String name) {
        this.name = name;
    }

    public void setUsers(GetUsersUserResponse[] users) {
        this.users = users;
    }
}
