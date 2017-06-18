package com.example.parkingApp.parkme.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MyReservation {

    @SerializedName("timeFrom")
    @Expose
    public Date timeFrom;

    @SerializedName("timeTo")
    @Expose
    public Date timeTo;

    @SerializedName("resUser")
    @Expose
    public String resUser;

    @SerializedName("parking")
    @Expose
    public String parking;

    public MyReservation(){

    }

    public MyReservation(Date timeFrom, Date timeTo, String resUser, String parking) {
        super();
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.resUser = resUser;
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

    public String getResUser() {
        return resUser;
    }

    public void setResUser(String resUser) {
        this.resUser = resUser;
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
                ", resUser='" + resUser + '\'' +
                ", parking='" + parking + '\'' +
                '}';
    }
}
