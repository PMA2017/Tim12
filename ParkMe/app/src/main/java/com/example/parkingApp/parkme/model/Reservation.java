package com.example.parkingApp.parkme.model;

import com.orm.SugarRecord;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Milana on 5/22/2017.
 */

public class Reservation extends SugarRecord {

    public Date date;

    public Time timeFrom;

    public Time timeTo;

    public int numberOfHours;

    public double totalPrice;

    public Reservation() {

    }

    public Reservation(Date date, Time timeFrom, Time timeTo, int numberOfHours, double totalPrice) {
        this.date = date;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.numberOfHours = numberOfHours;
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "date=" + date +
                ", timeFrom=" + timeFrom +
                ", timeTo=" + timeTo +
                ", numberOfHours=" + numberOfHours +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
