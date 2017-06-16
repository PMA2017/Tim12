package com.example.parkingApp.parkme.servicecall;

public class ApiUtils {

    private ApiUtils() {}

    //public static final String BASE_URL = "http://fast-hamlet-58581.herokuapp.com/";
    public static final String BASE_URL = "http://192.168.0.101:9000/";

    public static ParkingService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(ParkingService.class);
    }
}
