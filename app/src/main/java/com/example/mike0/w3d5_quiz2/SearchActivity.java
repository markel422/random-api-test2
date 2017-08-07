package com.example.mike0.w3d5_quiz2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName() + "_TAG";

    TextView nameTV;
    TextView addressTV;
    TextView emailTV;

    ImageView imageTV;
    String imageURL;

    TextView resultTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        nameTV = (TextView) findViewById(R.id.lbl_name);
        addressTV = (TextView) findViewById(R.id.lbl_address);
        emailTV = (TextView) findViewById(R.id.lbl_email);

        imageTV = (ImageView) findViewById(R.id.lbl_img);

        resultTV = (TextView) findViewById(R.id.lbl_tv_result);
        resultTV.setText("");

        Intent intent = getIntent();

        String nameValue, addressValue, emailValue;

        nameValue = intent.getStringExtra("nameValue");
        addressValue = intent.getStringExtra("addressValue");
        emailValue = intent.getStringExtra("emailValue");
        imageURL = intent.getStringExtra("imageValue");

        nameTV.setText(nameValue);
        addressTV.setText(addressValue);
        emailTV.setText(emailValue);

        Picasso.with(getApplicationContext()).load(imageURL).into(imageTV);

        Log.d(TAG, "Current Image URL: " +  imageURL);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        nameTV.setText("");
        addressTV.setText("");
        emailTV.setText("");
    }
}
