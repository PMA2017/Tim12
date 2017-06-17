package com.example.parkingApp.parkme.servicecall;

import com.example.parkingApp.parkme.model.Comment;
import com.example.parkingApp.parkme.model.Parking;
import com.example.parkingApp.parkme.model.Reservation;
import com.example.parkingApp.parkme.model.ReservationBack;
import com.example.parkingApp.parkme.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    @GET("parkings/get/title/{title}")
    Call<Parking> getParking(@Path("title") String title);

    @POST("parkings/rate")
    Call<Parking> rate(@Body Parking parking);

    @POST("parkings/updateCapacity")
    Call<Parking> updateCapacity(@Body String title);

    @POST("parkings/increaseCapacity")
    Call<Parking> increaseCapacity(@Body String title);

    @POST("parkings/update")
    Call<Parking> updateParking(@Body Parking parking);

    @POST("parkings/sendPushNot")
    Call<Integer> sendPushNot(@Body PushParams pushParams);

    @GET("comments/get/{title}")
    Call<List<Comment>> listCommentsByParking(@Path("title") String title);

    @PUT("comments/create")
    Call<Comment> createComment(@Body Comment comment);

    @POST("/reservations/reserve")
    Call<Integer> reserve(@Body ReservationBack reservation);

    @POST("/reservations/delete")
    Call<Integer> deleteRes(@Body ReservationBack reservation);

}
