package com.example.parkingApp.parkme.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ReservationBack {

    @SerializedName("timeFrom")
    @Expose
    public Date timeFrom;

    @SerializedName("timeTo")
    @Expose
    public Date timeTo;

    @SerializedName("resUser")
    @Expose
    public String user;

    @SerializedName("parking")
    @Expose
    public String parking;

    public ReservationBack(){

    }

    public ReservationBack(Date timeFrom, Date timeTo, String user, String parking) {
        super();
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.user = user;
        this.parking = parking;
    }

    public Date getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(Date timeFrom) {
        this.timeFrom = timeFrom;
    }

    public Date getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(Date timeTo) {
        this.timeTo = timeTo;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    @Override
    public String toString() {
        return "ReservationBack{" +
                "timeFrom=" + timeFrom +
                ", timeTo=" + timeTo +
                ", user='" + user + '\'' +
                ", parking='" + parking + '\'' +
                '}';
    }
}
