package com.example.parkingApp.parkme.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkingApp.parkme.R;
import com.example.parkingApp.parkme.model.Parking;
import com.example.parkingApp.parkme.servicecall.ApiUtils;
import com.example.parkingApp.parkme.servicecall.ParkingService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditParking extends AppCompatActivity {

    TextView name;
    TextView address;
    TextView totalNumber;
    TextView workDayPrice;
    TextView weekendPrice;
    EditText informations;
    EditText numberOfFreeSpaces;
    Button edit;
    ParkingService mAPIService;
    String parkingTitle;
    SharedPreferences preferences;
    Parking parking = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_parking);

        name = (TextView) findViewById(R.id.nameValue);
        address = (TextView) findViewById(R.id.addressValue);
        totalNumber = (TextView) findViewById(R.id.totalNumberValue);
        numberOfFreeSpaces = (EditText) findViewById(R.id.numberOfFreeSpaces);
        workDayPrice = (TextView) findViewById(R.id.workPriceTxt);
        weekendPrice = (TextView) findViewById(R.id.weekendPriceTxt);
        informations = (EditText) findViewById(R.id.infoTxt);
        edit = (Button) findViewById(R.id.edit);

        preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        parkingTitle = preferences.getString("parkingTitle", "");

        mAPIService = ApiUtils.getAPIService();

        getParking();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            updateParking();
            }
        });
    }

    private void getParking(){
        mAPIService.getParking(parkingTitle).enqueue(new Callback<Parking>() {
            @Override
            public void onResponse(@NonNull Call<Parking> call, @NonNull Response<Parking> response) {
                parking = response.body();
                name.setText(parking.getParkingName());
                address.setText(parking.getAdress());
                totalNumber.setText(String.valueOf(parking.getTotalNumberOfSpaces()));
                numberOfFreeSpaces.setText(String.valueOf(parking.getNumberOfFreeSpaces()));
                workDayPrice.setText(String.valueOf(parking.getWorkingDayPrice()));
                weekendPrice.setText(String.valueOf(parking.getWeekendPrice()));
                informations.setText(String.valueOf(parking.getInformations()));
            }

            @Override
            public void onFailure(@NonNull Call<Parking> call, @NonNull Throwable t) {
                Toast.makeText(EditParking.this, "Failure", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateParking(){
        parking.setNumberOfFreeSpaces(Integer.parseInt(numberOfFreeSpaces.getText().toString()));
        parking.setInformations(informations.getText().toString());
        if(numberOfFreeSpaces.getText().toString().isEmpty()){
            Toast.makeText(EditParking.this, "Broj slobodnih mesta ne može biti prazan!", Toast.LENGTH_LONG).show();
            return;
        }

        mAPIService.updateParking(parking).enqueue(new Callback<Parking>() {
            @Override
            public void onResponse(@NonNull Call<Parking> call, @NonNull Response<Parking> response) {
                Toast.makeText(EditParking.this, "Uspešno ste izmenili parking", Toast.LENGTH_LONG).show();
                Intent in = new Intent(EditParking.this, MainPageActivity.class);
                startActivity(in);
            }

            @Override
            public void onFailure(@NonNull Call<Parking> call, @NonNull Throwable t) {
                Toast.makeText(EditParking.this, "Greška", Toast.LENGTH_LONG).show();
            }
        });
    }
}
