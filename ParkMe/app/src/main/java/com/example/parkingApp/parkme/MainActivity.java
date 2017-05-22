package com.example.parkingApp.parkme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.parkingApp.parkme.activities.MainPageActivity;
import com.example.parkingApp.parkme.activities.SignUpActivity;
import com.facebook.FacebookSdk;
import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        TextView reg = (TextView) findViewById(R.id.link_signup);
        Button login = (Button) findViewById(R.id.sign_in_button);

        Stetho.initializeWithDefaults(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(),MainPageActivity.class);
                startActivity(in);
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2 = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(in2);
            }
        });
    }
}
