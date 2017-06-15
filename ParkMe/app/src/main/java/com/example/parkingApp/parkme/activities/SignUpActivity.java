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
import android.widget.Toast;
import com.example.parkingApp.parkme.R;
import com.example.parkingApp.parkme.model.User;
import com.example.parkingApp.parkme.servicecall.ApiUtils;
import com.example.parkingApp.parkme.servicecall.ParkingService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    EditText name;
    EditText surname;
    EditText username;
    EditText email;
    EditText password;
    EditText confirmPassword;
    EditText registrationNumber;

    private ParkingService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAPIService = ApiUtils.getAPIService();

        name = (EditText) findViewById(R.id.editTextName);
        surname = (EditText) findViewById(R.id.editTextSurname);
        username = (EditText) findViewById(R.id.editTextUserName);
        email = (EditText) findViewById(R.id.editTextEmail);
        password = (EditText) findViewById(R.id.editTextPassword);
        confirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
        registrationNumber = (EditText) findViewById(R.id.editTextRegistrationNum);

        Button createAccount = (Button) findViewById(R.id.buttonCreateAccount);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            createNewUser();
            }
        });
    }

    private void createNewUser() {

        String nameTxt = name.getText().toString();
        String surnameTxt = surname.getText().toString();
        final String usernameTxt = username.getText().toString();
        String emailTxt = email.getText().toString();
        String passwordTxt = password.getText().toString();
        String confirmPasswordTxt = confirmPassword.getText().toString();
        String registrationNumberTxt = registrationNumber.getText().toString();

        User user = new User(nameTxt,surnameTxt,usernameTxt,passwordTxt,emailTxt,false,registrationNumberTxt);

        if(usernameTxt.length() < 3){
            Toast.makeText(getApplicationContext(), "Korisničko ime mora sadržati najmanje 3 karaktera", Toast.LENGTH_LONG).show();
            return;
        }
        if (nameTxt.equals("") || surnameTxt.equals("") || usernameTxt.equals("") || emailTxt.equals("") || passwordTxt.equals("") || confirmPasswordTxt.equals("")) {
            Toast.makeText(getApplicationContext(), "Morate popuniti sva polja!", Toast.LENGTH_LONG).show();
            return;
        }
        if (!passwordTxt.equals(confirmPasswordTxt)) {
            Toast.makeText(getApplicationContext(), "Lozinke se ne poklapaju!", Toast.LENGTH_LONG).show();
            return;
        } else {
            mAPIService.createUser(user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                    Toast.makeText(getApplicationContext(), "Uspešno kreiran korisnik!", Toast.LENGTH_LONG).show();

                    SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("username", usernameTxt);
                    edit.apply();

                    Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                    Toast.makeText(SignUpActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
                }
            });


        }
    }
}
