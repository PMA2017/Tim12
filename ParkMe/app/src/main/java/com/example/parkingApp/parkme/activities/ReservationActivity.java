package com.example.parkingApp.parkme.activities;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.example.parkingApp.parkme.R;

public class ReservationActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    private static final String TIME_PATTERN = "HH:mm";
    private Calendar calendar;
    private java.text.DateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    EditText dropDate;
    EditText endDate;
    Spinner dropdown;
    private int whichEditTextIsPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        dropDate = (EditText) findViewById(R.id.dateDrop);
        endDate = (EditText) findViewById(R.id.dateEnd);
        Button confirmRes = (Button) findViewById(R.id.confirmReservation);

        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());

        dropdown = (Spinner)findViewById(R.id.spinner1);



        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.payment_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        dropdown.setAdapter(staticAdapter);

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
                Bundle bundle = new Bundle();
                bundle.putString("value", "Uspesno ste rezervisali parking mesto!");
                startActivity(new Intent(getApplicationContext(), MainPageActivity.class).putExtras(bundle));
                finish();
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
