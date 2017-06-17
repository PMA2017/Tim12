package com.example.parkingApp.parkme.model;

import com.orm.SugarRecord;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Milana on 5/22/2017.
 */

public class Reservation extends SugarRecord {

    private String date;

    private String timeFrom;

    private String timeTo;

    private String user;

    private String parking;

    private boolean active;

    public Reservation() {

    }

    public Reservation(String date, String timeFrom, String timeTo, String user, String parking, boolean active) {
        this.date = date;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.user = user;
        this.parking = parking;
        this.active = active;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getDate() {
        return date;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public String getUser() {
        return user;
    }

    public String getParking() {
        return parking;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "date='" + date + '\'' +
                ", timeFrom='" + timeFrom + '\'' +
                ", timeTo='" + timeTo + '\'' +
                ", user='" + user + '\'' +
                ", parking='" + parking + '\'' +
                ", active=" + active +
                '}';
    }
}
