package com.example.parkingApp.parkme.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.parkingApp.parkme.MainActivity;
import com.example.parkingApp.parkme.R;
import com.example.parkingApp.parkme.model.Parking;
import com.example.parkingApp.parkme.model.User;
import com.example.parkingApp.parkme.servicecall.ApiUtils;
import com.example.parkingApp.parkme.servicecall.ParkingService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkingDetailsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private ParkingService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_details);


        String parkingTitle;
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        parkingTitle = sharedPreferences.getString("parkingTitle", "");
        setTitle(parkingTitle);

        mAPIService = ApiUtils.getAPIService();

        mAPIService.getParking(parkingTitle).enqueue(new Callback<Parking>() {
            @Override
            public void onResponse(Call<Parking> call, Response<Parking> response) {
                if(response.body() != null) {

                }
            }

            @Override
            public void onFailure(Call<Parking> call, Throwable t) {
                Toast.makeText(ParkingDetailsActivity.this, "Failure", Toast.LENGTH_LONG).show();
            }
        });

        Button res = (Button) findViewById(R.id.reservation);
        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), ReservationActivity.class);
                startActivity(in);
            }
        });
    }
}
