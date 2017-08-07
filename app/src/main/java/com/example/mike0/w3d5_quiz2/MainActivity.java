package com.example.mike0.w3d5_quiz2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mike0.w3d5_quiz2.model.Result;
import com.example.mike0.w3d5_quiz2.util.RandomParser;

import com.example.mike0.w3d5_quiz2.FeedReaderContract.FeedEntry;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName() + "_TAG";

    private DBHelper helper;
    private SQLiteDatabase database;

    Button randomBtn;

    TextView nameTV;
    TextView addressTV;
    TextView emailTV;

    ImageView imageTV;
    String imageURL;

    Button saveBtn;

    EditText nameET;

    Button searchBtn;

    TextView resultTV;

    OkHttpClient client;
    List<Result> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new DBHelper(this);
        database = helper.getWritableDatabase();

        randomBtn = (Button) findViewById(R.id.btn_random);
        randomBtn.setOnClickListener(this);

        nameTV = (TextView) findViewById(R.id.lbl_name);
        addressTV = (TextView) findViewById(R.id.lbl_address);
        emailTV = (TextView) findViewById(R.id.lbl_email);

        imageTV = (ImageView) findViewById(R.id.lbl_img);

        saveBtn = (Button) findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(this);

        nameET = (EditText) findViewById(R.id.lbl_et_name);

        searchBtn = (Button) findViewById(R.id.btn_search);
        searchBtn.setOnClickListener(this);

        resultTV = (TextView) findViewById(R.id.lbl_tv_result);
        resultTV.setText("");

        client = new OkHttpClient();
        results = new ArrayList<>();
    }

    public void showResults() {
        final Request request = new Request.Builder()
                .url("https://randomuser.me/api")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: ", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        results.clear();

                        final JSONObject apiRes = new JSONObject(response.body().string());

                        for (int i = 0; i < apiRes.getJSONArray("results").length(); i++) {
                            results.add(RandomParser.parseResult(apiRes.getJSONArray("results").getJSONObject(i)));
                        }

                        for (final Result result : results) {
                            Log.d(TAG, "onResponse: " + result);
                        }

                        JSONArray results = apiRes.getJSONArray("results");
                        JSONObject first = results.getJSONObject(0);

                        JSONObject nameValue = first.getJSONObject("name");
                        final String titleValue = nameValue.getString("title");
                        final String firstValue = nameValue.getString("first");
                        final String lastValue = nameValue.getString("last");

                        final String fullNameValue = titleValue + " " + firstValue + " " + lastValue;

                        JSONObject locationValue = first.getJSONObject("location");
                        final String streetValue = locationValue.getString("street");
                        final String cityValue = locationValue.getString("city");
                        final String stateValue = locationValue.getString("state");
                        final String postcodeValue = locationValue.getString("postcode");

                        final String addressValue = streetValue + ", " + cityValue + ", " + stateValue + ", " + postcodeValue;

                        final String emailValue = first.getString("email");

                        JSONObject imageLoc = first.getJSONObject("picture");
                        final String lgValue = imageLoc.getString("large");
                        final String medValue = imageLoc.getString("medium");
                        final String thumbnailValue = imageLoc.getString("thumbnail");
                        imageURL = lgValue;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                nameTV.setText(String.format(getString(R.string.lbl_tv_name), toTitleCase(fullNameValue)));
                                addressTV.setText(String.format(getString(R.string.lbl_tv_address), toTitleCase(addressValue)));
                                emailTV.setText(String.format(getString(R.string.lbl_tv_email), emailValue));
                                Log.d(TAG, "large url: " +  lgValue);
                                Log.d(TAG, "medium url: " +  medValue);
                                Log.d(TAG, "thumbnail url: " +  thumbnailValue);
                                Picasso.with(getApplicationContext()).load(lgValue).into(imageTV);
                            }
                        });

                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: ", e);
                    }

                } else {
                    Log.e(TAG, "onResponse: Application error");
                }
            }
        });
    }

    public void saveEntry() {
        String name = nameTV.getText().toString();
        String address = addressTV.getText().toString();
        String email = emailTV.getText().toString();
        String imagePath = imageURL;

        ContentValues values = new ContentValues(); // Prevents SQL injection
        values.put(FeedEntry.COLUMN_NAME, name);
        values.put(FeedEntry.COLUMN_ADDRESS, address);
        values.put(FeedEntry.COLUMN_EMAIL, email);
        values.put(FeedEntry.COLUMN_IMAGE, imagePath);

        long recordId = database.insert(
                FeedEntry.TABLE_NAME,
                null,
                values
        );

        if (recordId > 0) {
            Log.d(TAG, "Name Entry: " + name + " Was Successfully Saved!");
            Toast.makeText(this, "Data Saved.", Toast.LENGTH_SHORT).show();
            resultTV.setText("Name Entry: " + name + " Was Successfully Saved!");
        } else  {
            Log.d(TAG, "Data Not Saved.");
            Toast.makeText(this, "Data Not Saved.", Toast.LENGTH_SHORT).show();
        }
    }

    public void searchEntry() {
        String entrySearch = nameET.getText().toString();

        String[] projection = {
                FeedEntry.COLUMN_NAME,
                FeedEntry.COLUMN_ADDRESS,
                FeedEntry.COLUMN_EMAIL,
                FeedEntry.COLUMN_IMAGE
        };

        Cursor cursor = database.query(
                FeedEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String actualNameEntry = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME));
            Log.d(TAG, "Current Cursor Name: " + actualNameEntry);

            if (actualNameEntry.equals(entrySearch)) {
                String addressEntry = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_ADDRESS));
                String emailEntry = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_EMAIL));
                String imageEntry = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_IMAGE));

                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("nameValue", actualNameEntry);
                intent.putExtra("addressValue", addressEntry);
                intent.putExtra("emailValue", emailEntry);
                intent.putExtra("imageValue", imageEntry);

                startActivity(intent);
                break;
            } else if (cursor.isLast()) {
                Log.d(TAG, "Entry Search Name: " + entrySearch + " Does Not Exist!");
                Toast.makeText(this, "Entry Search Name: " + entrySearch + " Does Not Exist!", Toast.LENGTH_SHORT).show();
            } else if (entrySearch.equals("")) {
                Toast.makeText(this, "Please Enter a Name Entry.", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    public static String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_random:
                showResults();
                break;
            case R.id.btn_save:
                saveEntry();
                break;
            case R.id.btn_search:
                searchEntry();
                break;
        }
    }
}
