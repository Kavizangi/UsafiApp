package com.example.david.usafiapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.support.design.widget.Snackbar;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;
import helper.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private Button login;
    private Button register;
    private EditText username;
    private EditText password;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

         login = (Button)findViewById(R.id.Login);
        register = (Button)findViewById(R.id.Register);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("https://adetechresolute.co.ke/usafi/register/selfregister"); // missing 'http://' will cause crash
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkLogin();
            }
        });

    }


    // Method to manually check connection and credentials coreectness status

    private void checkLogin() {

        boolean isConnected = ConnectivityReceiver.isConnected(this);


        if (isConnected) {

            // Progress dialog
            pDialog = new ProgressDialog(this);
            pDialog.setCancelable(false);
            username = (EditText) findViewById(R.id.Username);
            password = (EditText) findViewById(R.id.Password);

            String usr = username.getText().toString().trim();
            String pass = password.getText().toString().trim();

            // Check for empty data in the form
            if (!usr.isEmpty() && !pass.isEmpty()) {
                // login user

                password.setText("");

                loginToServer(usr, pass);

            } else {
                // Prompt user to enter credentials
                Toast.makeText(getApplicationContext(),
                        "Please enter the credentials!", Toast.LENGTH_LONG)
                        .show();
            }


        } else {

            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.loginLayout), "Sorry! Not connected to internet", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent homeIntent = new Intent(LoginActivity.this, LoginActivity.class);
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


    /**
     * function to verify login details in mysql db
     * */
    private void loginToServer(final String usr, final String pass) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");
                        String password = user.getString("password");

                        // Inserting row in users table
                        db.addUser(name, email, uid, created_at, password);

                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
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
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Sorry, try again later", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", usr);
                params.put("password", pass);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();


    }


}
