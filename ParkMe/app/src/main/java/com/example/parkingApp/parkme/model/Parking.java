package com.example.parkingApp.parkme.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

/**
 * Created by Milana on 5/22/2017.
 */

public class Parking {

    @SerializedName("parkingName")
    @Expose
    public String parkingName;

    @SerializedName("adress")
    @Expose
    public String adress;

    @SerializedName("totalNumberOfSpaces")
    @Expose
    public int totalNumberOfSpaces;

    @SerializedName("numberOfFreeSpaces")
    @Expose
    public int  numberOfFreeSpaces;

    @SerializedName("workingDayPrice")
    @Expose
    public double workingDayPrice;

    @SerializedName("weekendPrice")
    @Expose
    public double weekendPrice;

    @SerializedName("workTime")
    @Expose
    public String workTime;

    @SerializedName("paymentWay")
    @Expose
    public PaymentWay paymentWay;

    @SerializedName("informations")
    @Expose
    public String informations;

    @SerializedName("image")
    @Expose
    public String image;

    public enum PaymentWay {
        Keš, Kartica, Vaučer
    }

    public Parking() {

    }

    public Parking(String parkingName, String adress, int totalNumberOfSpaces, int numberOfFreeSpaces, double workingDayPrice, double weekendPrice, String workTime, PaymentWay paymentWay, String informations, String image) {
        this.parkingName = parkingName;
        this.adress = adress;
        this.totalNumberOfSpaces = totalNumberOfSpaces;
        this.numberOfFreeSpaces = numberOfFreeSpaces;
        this.workingDayPrice = workingDayPrice;
        this.weekendPrice = weekendPrice;
        this.workTime = workTime;
        this.paymentWay = paymentWay;
        this.informations = informations;
        this.image = image;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public int getTotalNumberOfSpaces() {
        return totalNumberOfSpaces;
    }

    public void setTotalNumberOfSpaces(int totalNumberOfSpaces) {
        this.totalNumberOfSpaces = totalNumberOfSpaces;
    }

    public int getNumberOfFreeSpaces() {
        return numberOfFreeSpaces;
    }

    public void setNumberOfFreeSpaces(int numberOfFreeSpaces) {
        this.numberOfFreeSpaces = numberOfFreeSpaces;
    }

    public double getWorkingDayPrice() {
        return workingDayPrice;
    }

    public void setWorkingDayPrice(double workingDayPrice) {
        this.workingDayPrice = workingDayPrice;
    }

    public double getWeekendPrice() {
        return weekendPrice;
    }

    public void setWeekendPrice(double weekendPrice) {
        this.weekendPrice = weekendPrice;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public PaymentWay getPaymentWay() {
        return paymentWay;
    }

    public void setPaymentWay(PaymentWay paymentWay) {
        this.paymentWay = paymentWay;
    }

    public String getInformations() {
        return informations;
    }

    public void setInformations(String informations) {
        this.informations = informations;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Parking{" +
                "parkingName='" + parkingName + '\'' +
                ", adress='" + adress + '\'' +
                ", totalNumberOfSpaces=" + totalNumberOfSpaces +
                ", numberOfFreeSpaces=" + numberOfFreeSpaces +
                ", workingDayPrice=" + workingDayPrice +
                ", weekendPrice=" + weekendPrice +
                ", workTime='" + workTime + '\'' +
                ", paymentWay=" + paymentWay +
                ", informations='" + informations + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
