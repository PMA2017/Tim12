package com.example.parkingApp.parkme.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.parkingApp.parkme.R;
import com.example.parkingApp.parkme.model.Comment;
import com.example.parkingApp.parkme.model.Parking;
import com.example.parkingApp.parkme.model.Reservation;
import com.example.parkingApp.parkme.servicecall.ApiUtils;
import com.example.parkingApp.parkme.servicecall.ParkingService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyReservationsActivity extends AppCompatActivity {

    private String username;
    public static int integer = 0;
    LinearLayout linear;
    private ParkingService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservations);

        mAPIService = ApiUtils.getAPIService();

        linear = (LinearLayout) findViewById(R.id.my_res);

        SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        username = preferences.getString("username", "");

        List<Reservation> reservationList = Reservation.find(Reservation.class, "user = ?", username);

        if(!reservationList.isEmpty()){
            for(Reservation r:reservationList){
                createNewTextView(r);
            }
        }else{
            final EditText textView = new EditText(this);
            textView.setEnabled(false);
            textView.setText("Nemate trenutnih rezervacija");
            textView.setTextColor(Color.parseColor("#000000"));
            linear.addView(textView);
        }
    }

    private void createNewTextView(Reservation reservation) {
        //final EditText textView = new EditText(this);
        final Button textView = new Button(this);
        //textView.setEnabled(false);
        textView.setId(integer);
        textView.setText(reservation.getParking() + " "  + System.getProperty("line.separator") + "Datum: " + reservation.getDate() +  " Od: " + reservation.getTimeFrom() + " Do: " + reservation.getTimeTo());
        textView.setTextColor(Color.parseColor("#000000"));
        linear.addView(textView);
        integer += 1;

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String parking = textView.getText().toString().split(" ")[0];
                new AlertDialog.Builder(MyReservationsActivity.this)
                        .setTitle("Brisanje rezervacije")
                        .setMessage("Da li zelite da obrisete vasu rezervaciju?")
                        .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialog, int which) {
                                mAPIService.increaseCapacity(parking).enqueue(new Callback<Parking>() {
                                    @Override
                                    public void onResponse(Call<Parking> call, Response<Parking> response) {
                                        Toast.makeText(MyReservationsActivity.this,"Uspesno ste obrisali rezervaciju!",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(MyReservationsActivity.this, MainPageActivity.class);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onFailure(Call<Parking> call, Throwable t) {
                                        Toast.makeText(MyReservationsActivity.this,"Greska!",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        })
                        .create()
                        .show();
            }
        });
    }


}
