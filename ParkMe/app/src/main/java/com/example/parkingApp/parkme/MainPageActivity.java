package com.example.parkingApp.parkme;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        MapFragment map = new MapFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, map);
        //ft.addToBackStack(MapFragment.TAG);
        ft.commit();

        Button sl = (Button) findViewById(R.id.sledeca);
        sl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ParkingDetailsActivity.class);
                startActivity(i);
            }
        });

        if(this.getIntent().getExtras() != null){
            Toast.makeText(this, this.getIntent().getExtras().getString("value"),Toast.LENGTH_LONG).show();
        }
    }
}
