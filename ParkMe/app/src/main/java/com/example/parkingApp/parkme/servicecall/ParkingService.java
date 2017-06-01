package com.example.parkingApp.parkme.servicecall;

import com.example.parkingApp.parkme.model.Parking;
import com.example.parkingApp.parkme.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ParkingService {
    @GET("users/all")
    Call<List<User>> listUsers();

    @PUT("users/create")
    Call<User> createUser(@Body User user);

    @GET("users/get/username/{username}")
    Call<User> getUser(@Path("username") String username);

    @GET("parkings/all")
    Call<List<Parking>> getParkings();
}
