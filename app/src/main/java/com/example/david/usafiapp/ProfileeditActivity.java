package com.example.david.usafiapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.AppController;

public class ProfileeditActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String id;

    private static final String TAG = ProfileActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileedit);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView textView = (TextView) findViewById(R.id.textViewpx);

        textView.setPaintFlags(textView.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        boolean isConnected = ConnectivityReceiver.isConnected(this);

        if (isConnected) {

            prepareData();

        }else{

            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.profileeditaddLayout), "Not connected to internet", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent homeIntent = new Intent(ProfileeditActivity.this, ProfileActivity.class);
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

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Please wait...");
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


                        TextView name = (TextView) findViewById(R.id.fullnames1);
                        TextView mobileno = (TextView) findViewById(R.id.mobileno1);
                        TextView county = (TextView) findViewById(R.id.county1);
                        TextView subcounty = (TextView) findViewById(R.id.subcounty1);
                        TextView location = (TextView) findViewById(R.id.location1);

                        JSONObject user = jObj.getJSONObject("user");

                        name.setText(user.getString("name"));
                        mobileno.setText(user.getString("mobile"));
                        county.setText(user.getString("county"));
                        subcounty.setText(user.getString("subcounty"));
                        location.setText(user.getString("location"));





                    } else {
                        // Error in Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    Toast.makeText(getApplicationContext(), "Please try again!", Toast.LENGTH_LONG).show();
                }



            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Profile Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
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

        Intent homeIntent = new Intent(ProfileeditActivity.this, ProfileActivity.class);
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

}
