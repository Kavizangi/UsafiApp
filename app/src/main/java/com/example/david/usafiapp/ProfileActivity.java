package com.example.david.usafiapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private SQLiteHandler db;

    private String id;

    private static final String TAG = ProfileActivity.class.getSimpleName();

    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent homeIntent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(homeIntent);

                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                finish();

            }
        });


        ImageView editProfile = (ImageView) findViewById(R.id.editProfile);

        editProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your code here

                Toast.makeText(getApplicationContext(), "Note: Coming soon" , Toast.LENGTH_LONG).show();

            }
        });

        ImageView editIcon = (ImageView) findViewById(R.id.profile);

        editIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your code here

                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PICK_IMAGE);

            }
        });

        boolean isConnected = ConnectivityReceiver.isConnected(this);


            if (isConnected) {

                prepareData();

            }else{

            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.productLayout), "Sorry! Not connected to internet", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent homeIntent = new Intent(ProfileActivity.this, ProfileActivity.class);
                            startActivity(homeIntent);

                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                            finish();

                        }
                    });

            // Changing message text color
            snackbar.setActionTextColor(Color.RED);
            snackbar.getView().setBackgroundColor(Color.rgb(0,0, 5));

            snackbar.show();

        }

    }

    private void prepareData() {

        // Tag used to cancel the request
        String tag_string_req = "req_profile";

        // Server user register url
        String url = "https://adetechresolute.co.ke/usafi/api_retrievedata/profile";

         id = AppController.getInstance().getUserDetails();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching...");
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Profile Response: " + response.toString());
                progressDialog.dismiss();


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {


                        TextView name = (TextView) findViewById(R.id.name);
                        TextView email = (TextView) findViewById(R.id.email);
                        TextView type = (TextView) findViewById(R.id.type);
                        TextView mobile = (TextView) findViewById(R.id.mobile);
                        TextView refferes = (TextView) findViewById(R.id.referrers);
                        TextView commission = (TextView) findViewById(R.id.commission);
                        TextView county = (TextView) findViewById(R.id.county);
                        TextView subcounty = (TextView) findViewById(R.id.subcounty);
                        TextView joined = (TextView) findViewById(R.id.joined);

                        JSONObject user = jObj.getJSONObject("user");

                        name.setText(user.getString("name"));
                        email.setText("Email Address: "+user.getString("email"));
                        mobile.setText("Mobile Number: "+user.getString("mobile"));
                        refferes.setText("My Referres: "+user.getString("referres"));
                        type.setText(user.getString("type"));
                        commission.setText("Commission: "+user.getString("commission"));
                        county.setText("County: "+user.getString("county"));
                        subcounty.setText("Sub County: "+user.getString("subcounty"));
                        joined.setText("Joined On: "+user.getString("created_on"));





                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }



            }

        }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Profile Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            "Sorry, try again later", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", id);

                    return params;
                }

            };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    @Override
    public void onBackPressed() {
        // do something on back.

        Intent homeIntent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(homeIntent);

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        finish();

        return;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {
            //TODO: action

            Toast.makeText(getApplicationContext(), "Upload Coming Soon!", Toast.LENGTH_LONG).show();
        }
    }


}
