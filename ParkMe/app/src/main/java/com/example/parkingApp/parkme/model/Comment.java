package com.example.parkingApp.parkme.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {

    @SerializedName("parkingName")
    @Expose
    public String parkingName;

    @SerializedName("username")
    @Expose
    public String username;

    @SerializedName("comment")
    @Expose
    public String comment;

    public Comment(){
        super();
    }

    public Comment(String parkingName, String username, String comment) {
        super();
        this.parkingName = parkingName;
        this.username = username;
        this.comment = comment;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "parkingName='" + parkingName + '\'' +
                ", username='" + username + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
