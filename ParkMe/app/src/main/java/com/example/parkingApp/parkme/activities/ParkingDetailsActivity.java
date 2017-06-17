package com.example.parkingApp.parkme.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.parkingApp.parkme.R;
import com.example.parkingApp.parkme.model.Parking;
import com.example.parkingApp.parkme.servicecall.ApiUtils;
import com.example.parkingApp.parkme.servicecall.ParkingService;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkingDetailsActivity extends AppCompatActivity {

    private ParkingService mAPIService;
    private TextView parkingName;
    private TextView parkingAddress;
    private ProgressBar capacity;
    private TextView price;
    private TextView priceWeekend;
    private TextView opened;
    private TextView pay;
    private TextView info;
    private ImageView image;
    private String parkingTitle;
    private RatingBar parkingRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_details);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
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
        image = (ImageView) findViewById(R.id.parking_image);
        TextView rating = (TextView) findViewById(R.id.link_rating);
        parkingRate = (RatingBar) findViewById(R.id.parkingRate);
        mAPIService = ApiUtils.getAPIService();

        mAPIService.getParking(parkingTitle).enqueue(new Callback<Parking>() {
            @Override
            public void onResponse(@NonNull Call<Parking> call, @NonNull Response<Parking> response) {
                if(response.body() != null) {
                    parkingName.setText(response.body().getParkingName());
                    parkingAddress.setText(response.body().getAdress());
                    int percentage = 0;
                    if((double)response.body().getNumberOfFreeSpaces() == 0 ){
                        percentage = 100;
                    }
                    else {
                        percentage = (int) (((double) response.body().getNumberOfFreeSpaces()) / ((double) response.body().getTotalNumberOfSpaces()) * 100);
                    }
                    capacity.setProgress(percentage);
                    price.append(" "+ Double.toString(response.body().getWorkingDayPrice()) + " RSD");
                    priceWeekend.append(" " + Double.toString(response.body().getWeekendPrice()) + " RSD");
                    opened.setText(response.body().getWorkTime() + " h");
                    pay.setText(response.body().getPaymentWay());
                    info.setText(response.body().getInformations());
                    Picasso.with(ParkingDetailsActivity.this)
                            .load(response.body().getImage())
                            .resize(90, 90)
                            .into(image);
                    float rate = (float)response.body().getRatingSum() / (float)response.body().getNumberOfVotes();
                    parkingRate.setRating(rate);

                    SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("paymentWay", response.body().getPaymentWay());
                    edit.apply();

                }
            }

            @Override
            public void onFailure(@NonNull Call<Parking> call, @NonNull Throwable t) {
                Toast.makeText(ParkingDetailsActivity.this, "Greška", Toast.LENGTH_LONG).show();
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

        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ParkingDetailsActivity.this, RateActivity.class);
                startActivity(in);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mAPIService.getParking(parkingTitle).enqueue(new Callback<Parking>() {
            @Override
            public void onResponse(@NonNull Call<Parking> call, @NonNull Response<Parking> response) {
                if(response.body() != null) {
                    float rate = (float)response.body().getRatingSum() / (float)response.body().getNumberOfVotes();
                    parkingRate.setRating(rate);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Parking> call, @NonNull Throwable t) {
                Toast.makeText(ParkingDetailsActivity.this, "Greška", Toast.LENGTH_LONG).show();
            }
        });
    }


}
