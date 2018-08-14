package com.example.david.usafiapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;
import helper.SessionManager;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private SQLiteHandler db;
    private SessionManager session;
    private static final String TABLE_USER = "user";

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

        TextView name = (TextView) findViewById(R.id.name);
        TextView mobile = (TextView) findViewById(R.id.mobile);
        TextView type = (TextView) findViewById(R.id.type);

        mobile.setText("Mobile No: "+AppController.getInstance().getUserEmail());
        type.setText(AppController.getInstance().getUserType());
        name.setText(AppController.getInstance().getUserName());

        ImageView editProfile = (ImageView) findViewById(R.id.editProfile);

        editProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your code here
                // Launching the  activity
                Intent intent = new Intent(ProfileActivity.this, ProfileeditActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();

            }
        });

        ImageView editIcon = (ImageView) findViewById(R.id.profile);

        if(AppController.getInstance().getUserProfile()==null)
        {

            editIcon.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // your code here

                    ActivityCompat.requestPermissions(ProfileActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);

                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, PICK_IMAGE);

                }
            });

        }else{
            editIcon.setImageBitmap(StringToBitMap(AppController.getInstance().getUserProfile()));
        }

        boolean isConnected = ConnectivityReceiver.isConnected(this);


            if (isConnected) {

                prepareData();

            }else{
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.profileLayout), "Not connected to internet", Snackbar.LENGTH_SHORT)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent homeIntent = new Intent(ProfileActivity.this, ProfileActivity.class);
                                startActivity(homeIntent);

                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                                finish();

                            }
                        });

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
        super.onActivityResult(requestCode, resultCode, data);

         String selectedImagePath, filemanagerstring;
         File myFile = null;
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        try {
            switch (requestCode) {

                case PICK_IMAGE:
                    if (resultCode == Activity.RESULT_OK) {

                        Uri selectedImageUri = data.getData();
                        filemanagerstring = selectedImageUri.getPath();
                        selectedImagePath = getPath(selectedImageUri);

                        progressDialog.show();

                        if (selectedImagePath != null)
                            myFile = new File(selectedImagePath);
                        else if (filemanagerstring != null)
                            myFile = new File(filemanagerstring);

                        if (myFile != null) {

                            Bitmap bitmap = null;
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

                            try {
                                bitmap = BitmapFactory.decodeStream(new FileInputStream(myFile), null, options);
                            } catch (FileNotFoundException e) {

                                Toast.makeText(getApplicationContext(),
                                        e.getMessage(), Toast.LENGTH_LONG).show();

                            }

                            /*ImageView prof_pic = (ImageView) findViewById(R.id.profile);

                            prof_pic.setImageBitmap(bitmap);

                            // saving to sqlite

                            SQLiteHandler helper = new SQLiteHandler(this);

                            SQLiteDatabase dbs = helper.getReadableDatabase();

                            id = AppController.getInstance().getUserDetails();

                            ContentValues cv = new ContentValues();
                            cv.put("pic", BitMapToString(bitmap));
                            dbs.update(TABLE_USER, cv, "uid="+id, null);

                            dbs.close(); // Closing database connection*/

                            progressDialog.dismiss();


                        } else {

                            Toast.makeText(getApplicationContext(),
                                    "is null", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();

                        }

                        break;
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        Log.e(TAG, "Selecting picture cancelled");

                        Toast.makeText(getApplicationContext(),
                                "Selecting picture cancelled", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in onActivityResult : " + e.getMessage());

            Toast.makeText(getApplicationContext(),
                    "Exception in onActivityResult : " + e.getMessage(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();

        }

    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(ProfileActivity.this, "Enable that permission, to upload your profile", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }


}
