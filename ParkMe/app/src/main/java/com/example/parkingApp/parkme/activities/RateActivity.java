package com.example.parkingApp.parkme.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;
import com.example.parkingApp.parkme.R;
import com.example.parkingApp.parkme.model.Comment;
import com.example.parkingApp.parkme.model.Parking;
import com.example.parkingApp.parkme.servicecall.ApiUtils;
import com.example.parkingApp.parkme.servicecall.ParkingService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateActivity extends AppCompatActivity {

    private LinearLayout commentList;
    private EditText comm;
    private ParkingService mAPIService;
    private String parkingTitle;
    private String username;
    private List<Comment> listOfComments;
    public static int integer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        commentList = (LinearLayout) findViewById(R.id.show_comment);
        final RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        comm = (EditText) findViewById(R.id.comment);
        Button submit = (Button) findViewById(R.id.submit);

        mAPIService = ApiUtils.getAPIService();

        SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        parkingTitle = preferences.getString("parkingTitle", "");

        preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        username = preferences.getString("username", "");

        loadComments();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ratingBar.getRating() <= 0){
                    Toast.makeText(RateActivity.this, "Molimo vas da ocenite uslugu prilikom komentarisanja!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(comm.getText().toString().isEmpty()){
                    Toast.makeText(RateActivity.this, "Tekst komentara ne moze biti prazan!", Toast.LENGTH_LONG).show();
                    return;
                }
                insertComment();
                Parking rp = new Parking();
                rp.parkingName = parkingTitle;
                rp.ratingSum = (int)ratingBar.getRating();

                mAPIService.rate(rp).enqueue(new Callback<Parking>() {
                    @Override
                    public void onResponse(@NonNull Call<Parking> call, @NonNull Response<Parking> response) {
                        ratingBar.setRating(0F);
                    }

                    @Override
                    public void onFailure(@NonNull Call<Parking> call, @NonNull Throwable t) {
                        Toast.makeText(RateActivity.this, "Failure", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    private void insertComment(){
        String message = comm.getText().toString();
        final Comment comment = new Comment(parkingTitle, username, message);

        mAPIService.createComment(comment).enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(@NonNull Call<Comment> call, @NonNull Response<Comment> response) {
                //Toast.makeText(RateActivity.this,"Poslao je komentar!!!", Toast.LENGTH_LONG).show();
                createNewTextView(comment);
                comm.setText("");
            }

            @Override
            public void onFailure(@NonNull Call<Comment> call, @NonNull Throwable t) {
                Toast.makeText(RateActivity.this,"Greska!!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadComments(){
        mAPIService.listCommentsByParking(parkingTitle).enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(@NonNull Call<List<Comment>> call, @NonNull Response<List<Comment>> response) {
                listOfComments = response.body();
                if(!listOfComments.isEmpty()){
                    for (int i=listOfComments.size()-1; i >= 0;  i--){
                        createNewTextView(listOfComments.get(i));
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Comment>> call, @NonNull Throwable t) {
                Toast.makeText(RateActivity.this,"Greska!!!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createNewTextView(Comment comment) {
        LinearLayout lay = new LinearLayout(this);

        final EditText textView = new EditText(this);
        textView.setId(integer);
        textView.setText(comment.getComment()+System.getProperty("line.separator") + System.getProperty("line.separator") + "Korisnik:"+comment.getUsername());
        //textView.append(System.getProperty("line.separator") + "user:"+comment.getUsername());

        textView.setTextColor(Color.parseColor("#000000"));
        LinearLayout.LayoutParams params1 =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lay.setLayoutParams(params1);

        commentList.addView(textView);
        integer += 1;
    }

}
