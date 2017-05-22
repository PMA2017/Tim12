package com.example.parkingApp.parkme.servicecall;

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "http://192.168.0.20:9000/";

    public static ParkingService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(ParkingService.class);
    }
}
