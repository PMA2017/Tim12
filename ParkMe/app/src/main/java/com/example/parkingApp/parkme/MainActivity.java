package com.example.parkingApp.parkme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkingApp.parkme.activities.MainPageActivity;
import com.example.parkingApp.parkme.activities.SignUpActivity;
import com.example.parkingApp.parkme.model.User;
import com.example.parkingApp.parkme.servicecall.ApiUtils;
import com.example.parkingApp.parkme.servicecall.ParkingService;
import com.facebook.FacebookSdk;
import com.facebook.stetho.Stetho;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button login;
    EditText email;
    EditText password;
    private ParkingService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        TextView reg = (TextView) findViewById(R.id.link_signup);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.sign_in_button);
        mAPIService = ApiUtils.getAPIService();

        Stetho.initializeWithDefaults(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
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

    private void login() {

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");

        //get the username and password
        String username = email.getText().toString();
        final String pass = password.getText().toString();

        progressDialog.show();
        //check if the stored password matches with password entered by user
        if (username.equals("") || pass.equals("")) {
            Toast.makeText(getApplicationContext(), "Morate popuniti sva polja", Toast.LENGTH_LONG).show();
            return;
        }

        mAPIService.getUser(username).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.body() != null) {
                    if (pass.equals(response.body().getPassword())) {
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                                        startActivity(intent);
                                        progressDialog.dismiss();
                                    }
                                }, 1000);
                    } else {
                        Toast.makeText(MainActivity.this, "Korisničko ime i/ili lozinka ne postoje u bazi", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Korisničko ime i/ili lozinka ne postoje u bazi", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Korisničko ime i/ili lozinka ne postoje u bazi", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

    }
}
