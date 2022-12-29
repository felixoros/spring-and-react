package com.task.demi.pojo;

import java.util.Date;

class UserStreetResponse {
    public int number;
    public String name;
}

class UserLocationCoordinates {
    public double latitude;
    public double longitude;
}

class UserLocationTimezone {
    public String offset;
    public String description;
}

class UserLoginResponse {
    public String uuid;
    public String username;
    public String password;
    public String salt;
    public String md5;
    public String sha1;
    public String sha256;
}

class UserDobResponse {
    public Date date;
    public int age;
}

class UserIdResponse {
    public String name;
    public String value;
}

class UserPictureResponse {
    public String large;
    public String medium;
    public String thumbnail;
}

public class UserResponse {
    public String gender;
    public UserNameResponse name;
    public UserLocationResponse location;
    public String email;
    public UserLoginResponse login;
    public UserDobResponse dob;
    public UserDobResponse registered;
    public String phone;
    public String cell;
    public UserIdResponse id;
    public UserPictureResponse picture;
    public String nat;
}
