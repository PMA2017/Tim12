package com.example.parkingApp.parkme.model;

import com.orm.SugarRecord;

/**
 * Created by MarkoIvetic on 5/17/2017.
 */

public class User extends SugarRecord {

    public String userName;

    public String password;

    public User(){

    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
