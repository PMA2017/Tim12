package com.example.parkingApp.parkme.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
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
    private TextView parkingName;
    private TextView parkingAddress;
    private ProgressBar capacity;
    private TextView price;
    private TextView priceWeekend;
    private TextView opened;
    private TextView pay;
    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_details);


        String parkingTitle;
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        parkingTitle = sharedPreferences.getString("parkingTitle", "");
        setTitle(parkingTitle);

        parkingName = (TextView) findViewById(R.id.parking_name);
        parkingAddress = (TextView) findViewById(R.id.parking_address);
        capacity = (ProgressBar) findViewById(R.id.customProgressBar);
        price = (TextView) findViewById(R.id.price_over_week);
        priceWeekend = (TextView) findViewById(R.id.price_weekend);
        opened = (TextView) findViewById(R.id.from_to);
        pay = (TextView) findViewById(R.id.pay);
        info = (TextView) findViewById(R.id.informations);

        mAPIService = ApiUtils.getAPIService();

        mAPIService.getParking(parkingTitle).enqueue(new Callback<Parking>() {
            @Override
            public void onResponse(Call<Parking> call, Response<Parking> response) {
                if(response.body() != null) {
                    parkingName.setText(response.body().getParkingName());
                    parkingAddress.setText(response.body().getAdress());
                    int percentage = (int)(((double)response.body().getNumberOfFreeSpaces())/((double)response.body().getTotalNumberOfSpaces()) * 100);
                    capacity.setProgress(percentage);
                    price.append(" "+ Double.toString(response.body().getWorkingDayPrice()) + " RSD");
                    priceWeekend.append(" " + Double.toString(response.body().getWeekendPrice()) + " RSD");
                    opened.setText(response.body().getWorkTime() + " h");
                    pay.setText(response.body().getPaymentWay());
                    info.setText(response.body().getInformations());

                    SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("paymentWay", response.body().getPaymentWay());
                    edit.commit();

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
