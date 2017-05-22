package com.example.parkingApp.parkme.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.example.parkingApp.parkme.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AdminAddParkingActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    private static final String TIME_PATTERN = "HH:mm";
    EditText workingHoursFrom;
    EditText workingHoursTo;
    private SimpleDateFormat timeFormat;
    private Calendar calendar;
    private Button addParking;
    private int whichEditTextIsPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_parking);

        workingHoursFrom = (EditText) findViewById(R.id.workingHours);
        workingHoursTo = (EditText) findViewById(R.id.workingHoursTo);
        calendar = Calendar.getInstance();
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());
        addParking = (Button) findViewById(R.id.addParking);

        workingHoursFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.newInstance(AdminAddParkingActivity.this, calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
            }
        });

        workingHoursTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.newInstance(AdminAddParkingActivity.this, calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
            }
        });

        workingHoursFrom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                whichEditTextIsPressed = 1;
                return false;
            }
        });

        workingHoursTo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                whichEditTextIsPressed = 2;
                return false;
            }
        });

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        String time = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(calendar.get(Calendar.MINUTE)) + "h";

        //provjera vremena

        if(whichEditTextIsPressed == 1)
            workingHoursFrom.setText(time);
        else if(whichEditTextIsPressed == 2)
            workingHoursTo.setText(time);

    }
}
