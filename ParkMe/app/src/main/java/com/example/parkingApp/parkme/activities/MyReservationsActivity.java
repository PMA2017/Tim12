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
import com.example.parkingApp.parkme.model.Parking;
import com.example.parkingApp.parkme.model.Reservation;
import com.example.parkingApp.parkme.model.ReservationBack;
import com.example.parkingApp.parkme.servicecall.ApiUtils;
import com.example.parkingApp.parkme.servicecall.ParkingService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyReservationsActivity extends AppCompatActivity {

    public static int integer = 0;
    LinearLayout linear;
    private ParkingService mAPIService;
    String username;

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
            for(int i=reservationList.size()-1; i>=0; i--){
                createNewTextView(reservationList.get(i));
            }
        }else{
            final EditText textView = new EditText(this);
            textView.setEnabled(false);
            textView.setText("Nemate trenutnih rezervacija");
            textView.setTextColor(Color.parseColor("#000000"));
            linear.addView(textView);
        }


    }

    private void createNewTextView(final Reservation reservation) {
        //final EditText button = new EditText(this);
        final Button button = new Button(this);
        //button.setEnabled(false);
        button.setId(integer);
        button.setText(reservation.getParking() + " "  + System.getProperty("line.separator") + "Datum: " + reservation.getDate() +  " Od: " + reservation.getTimeFrom() + " Do: " + reservation.getTimeTo());

        button.setTextColor(Color.parseColor("#000000"));
        linear.addView(button);

        if(!reservation.isActive()){
            button.setEnabled(false);
        }

        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date result = new Date();
            String tempDate = reservation.getDate() + " " + reservation.getTimeTo();
            result = formatter.parse(tempDate);

            Calendar c = Calendar.getInstance();

            if(c.getTime().after(result)){
                button.setEnabled(false);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        integer += 1;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String parking = button.getText().toString().split(" ")[0];

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String currentDateandTime = sdf.format(new Date());
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date resultFrom = new Date();
                Date resultTo = new Date();
                String tempDateFrom = currentDateandTime + " " + button.getText().toString().split(" ")[4];
                String tempDateTo = currentDateandTime + " " + button.getText().toString().split(" ")[6];
                try {
                    resultFrom = formatter.parse(tempDateFrom);
                    resultTo = formatter.parse(tempDateTo);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                final ReservationBack res = new ReservationBack(resultFrom,resultTo,username,parking);

                new AlertDialog.Builder(MyReservationsActivity.this)
                        .setTitle("Brisanje rezervacije")
                        .setMessage("Da li želite da obrišete vašu rezervaciju?")
                        .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialog, int which) {
                                mAPIService.deleteRes(res).enqueue(new Callback<Integer>() {
                                    @Override
                                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                                        mAPIService.increaseCapacity(parking).enqueue(new Callback<Parking>() {
                                            @Override
                                            public void onResponse(Call<Parking> call, Response<Parking> response) {
                                                Toast.makeText(MyReservationsActivity.this,"Uspešno ste obrisali rezervaciju!",Toast.LENGTH_LONG).show();
                                                String[] splt = button.getText().toString().split(" ");
                                                String prking = splt[0];
                                                String datum = splt[2];
                                                String ddo = splt[4];
                                                List<Reservation> books = Reservation.find(Reservation.class, "parking = ? and time_From = ? and date = ?", prking, ddo, datum);
                                                books.get(0).setActive(false);
                                                books.get(0).save();
                                                Intent intent = new Intent(MyReservationsActivity.this, MainPageActivity.class);
                                                startActivity(intent);
                                                ReservationActivity.getInstance().finish();
                                            }

                                            @Override
                                            public void onFailure(Call<Parking> call, Throwable t) {
                                                Toast.makeText(MyReservationsActivity.this,"Greška!",Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(Call<Integer> call, Throwable t) {
                                        Toast.makeText(MyReservationsActivity.this,"Greska",Toast.LENGTH_LONG).show();
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
