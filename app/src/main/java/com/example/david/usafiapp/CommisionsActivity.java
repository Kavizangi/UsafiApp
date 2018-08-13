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
import android.widget.RelativeLayout;
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
import helper.SQLiteHandler;

public class CommisionsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private SQLiteHandler db;

    private String id;

    private static final String TAG = ProfileActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commisions);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        boolean isConnected = ConnectivityReceiver.isConnected(this);


        if (isConnected) {
            prepareData();

        }else{


            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.commisionLayout), "Not connected to internet", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent homeIntent = new Intent(CommisionsActivity.this, CommisionsActivity.class);
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
        String url = "https://adetechresolute.co.ke/usafi/api_retrievedata/commision";

        id = AppController.getInstance().getUserDetails();

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Commission Response: " + response.toString());
                progressDialog.dismiss();


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {


                        TextView commissionSummary = (TextView) findViewById(R.id.commissionSummary);
                        TextView summary1 = (TextView) findViewById(R.id.summary1);
                        TextView summary2 = (TextView) findViewById(R.id.summary2);
                        TextView summary3 = (TextView) findViewById(R.id.summary3);

                        JSONObject user = jObj.getJSONObject("user");

                        commissionSummary.setText("Total Commission: Ksh "+user.getString("total"));
                        summary1.setText("Unpaid: Ksh "+user.getString("unpaid"));
                        summary2.setText("Pending: Ksh "+user.getString("pending"));
                        summary3.setText("Paid: Ksh "+user.getString("paid"));

                        summary1.setPaintFlags(summary1.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
                        summary2.setPaintFlags(summary2.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
                        summary3.setPaintFlags(summary3.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                "Technical error occurred, try later!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Technical error occurred, try later!", Toast.LENGTH_LONG).show();
                }



            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Profile Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Technical error occurred, tyr later!", Toast.LENGTH_LONG).show();
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

        Intent homeIntent = new Intent(CommisionsActivity.this, MainActivity.class);
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
