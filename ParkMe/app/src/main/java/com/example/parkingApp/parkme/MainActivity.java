package com.example.parkingApp.parkme;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.stetho.Stetho;

import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button login;
    EditText email;
    EditText password;
    private LoginButton fbLoginButton;
    private ParkingService mAPIService;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();

        TextView reg = (TextView) findViewById(R.id.link_signup);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.sign_in_button);
        fbLoginButton = (LoginButton) findViewById(R.id.login_button);
        fbLoginButton.setReadPermissions(Arrays.asList("email"));

        mAPIService = ApiUtils.getAPIService();

        Stetho.initializeWithDefaults(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
                Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                startActivity(intent);
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2 = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(in2);
            }
        });

        fbLoginCallback();
    }

    private void fbLoginCallback() {
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getUserInfo(loginResult);
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Login attempt canceled.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(MainActivity.this, "Login attempt failed.", Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void getUserInfo(LoginResult login_result) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                login_result.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object, GraphResponse response) {
                        Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                        intent.putExtra("jsondata", json_object.toString());
                        startActivity(intent);
                    }
                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();
    }

    private void login() {

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");

        //get the username and password
        final String username = email.getText().toString();
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

                                        SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor edit = pref.edit();
                                        edit.putString("username", username);
                                        edit.commit();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

}
