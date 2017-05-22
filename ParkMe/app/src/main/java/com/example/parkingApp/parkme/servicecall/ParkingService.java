package com.example.parkingApp.parkme.servicecall;

import com.example.parkingApp.parkme.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ParkingService {
    @GET("users/all")
    Call<List<User>> listUsers();
}
