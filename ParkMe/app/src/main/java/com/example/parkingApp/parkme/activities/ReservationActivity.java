package com.example.parkingApp.parkme.activities;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.example.parkingApp.parkme.R;
import com.example.parkingApp.parkme.model.Parking;
import com.example.parkingApp.parkme.model.Reservation;
import com.example.parkingApp.parkme.servicecall.ApiUtils;
import com.example.parkingApp.parkme.servicecall.ParkingService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    private Calendar calendar;
    EditText dropDate;
    EditText endDate;
    Spinner dropdown;
    private int whichEditTextIsPressed;
    private ParkingService mAPIService;
    private SharedPreferences sharedPreferences;
    Bundle bundle;
    Date timeFrom;
    Date timeTo;
    String pref_userName;
    String parkingTitle;
    ArrayList<String> payWay = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        dropDate = (EditText) findViewById(R.id.dateDrop);
        endDate = (EditText) findViewById(R.id.dateEnd);
        Button confirmRes = (Button) findViewById(R.id.confirmReservation);
        calendar = Calendar.getInstance();
        mAPIService = ApiUtils.getAPIService();
        dropdown = (Spinner)findViewById(R.id.spinner1);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        parkingTitle = sharedPreferences.getString("parkingTitle", "");

        getPaymentWay();

        dropDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.newInstance(ReservationActivity.this, calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.newInstance(ReservationActivity.this, calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
            }
        });

        dropDate.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                whichEditTextIsPressed = 1;
                return false;
            }
        });

        endDate.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View arg0, MotionEvent arg1)
            {
                whichEditTextIsPressed = 2;
                return false;
            }
        });

        confirmRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmReservation();
            }
        });
    }

    private void confirmReservation() {

        final String timeFromStr = dropDate.getText().toString();
        final String timeToStr = endDate.getText().toString();

        if(timeFromStr.isEmpty()){
            Toast.makeText(ReservationActivity.this,"Morate uneti vreme rezervacije",Toast.LENGTH_LONG).show();
            return;
        }

        if(timeToStr.isEmpty()){
            Toast.makeText(ReservationActivity.this,"Morate uneti vreme rezervacije",Toast.LENGTH_LONG).show();
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        try {
            timeFrom = dateFormat.parse(timeFromStr);
            timeTo = dateFormat.parse(timeToStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(timeFrom.after(timeTo)) {
            Toast.makeText(ReservationActivity.this,"Vreme završetka rezervacije mora biti veće od početka.", Toast.LENGTH_LONG).show();
            return;
        }

        mAPIService.updateCapacity(parkingTitle).enqueue(new Callback<Parking>() {
            @Override
            public void onResponse(@NonNull Call<Parking> call, @NonNull Response<Parking> response) {
                bundle = new Bundle();
                bundle.putString("value", "Uspesno ste rezervisali parking mesto!");

                sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                pref_userName = sharedPreferences.getString("username", "");

                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                Reservation res = new Reservation(date, timeFromStr, timeToStr, pref_userName, parkingTitle);
                res.save();
                startActivity(new Intent(getApplicationContext(), MainPageActivity.class).putExtras(bundle));
                finish();
            }

            @Override
            public void onFailure(@NonNull Call<Parking> call, @NonNull Throwable t) {
                Toast.makeText(ReservationActivity.this,"Sva mesta na parkingu su zauzeta! Pokusajte ponovo kasnije!",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getPaymentWay() {
        mAPIService.getParking(parkingTitle).enqueue(new Callback<Parking>() {
            @Override
            public void onResponse(@NonNull Call<Parking> call, @NonNull Response<Parking> response) {
                String payment = response.body().getPaymentWay();
                String[] pw = payment.split(",");

                Collections.addAll(payWay, pw);

                ArrayAdapter<String> staticAdapter = new ArrayAdapter<>(ReservationActivity.this, android.R.layout.simple_spinner_item, payWay);
                // Specify the layout to use when the list of choices appears
                staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Apply the adapter to the spinner
                dropdown.setAdapter(staticAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<Parking> call, @NonNull Throwable t) {
                Toast.makeText(ReservationActivity.this,"Failure",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        String time = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(calendar.get(Calendar.MINUTE)) + "h";

        if(whichEditTextIsPressed == 1)
            dropDate.setText(time);
        else if(whichEditTextIsPressed == 2)
            endDate.setText(time);

    }

}
