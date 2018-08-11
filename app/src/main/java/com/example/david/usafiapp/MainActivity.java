package com.example.david.usafiapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import helper.SQLiteHandler;
import helper.SessionManager;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SessionManager session;
    private SQLiteHandler db;
    private CardView products;
    private CardView sales;
    private CardView commisions;
    private CardView agents;
    private CardView customers;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }


        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        // Handling all dashboard clicks

        products = (CardView) findViewById(R.id.productsCard);
        agents = (CardView) findViewById(R.id.agentsCard);
        customers = (CardView) findViewById(R.id.customersCard);
        commisions = (CardView) findViewById(R.id.commissionCard);
        sales = (CardView) findViewById(R.id.salesCard);


        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launching the  activity
                Intent intent = new Intent(MainActivity.this, ProductsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

        agents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launching the  activity
                Intent intent = new Intent(MainActivity.this, AgentsviewActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

        customers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launching the  activity
                Intent intent = new Intent(MainActivity.this, CustomersActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

        commisions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launching the  activity
                Intent intent = new Intent(MainActivity.this, CommisionsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launching the  activity
                Intent intent = new Intent(MainActivity.this, SalesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logoutUser();
                return true;
            case R.id.action_account:

                // Launching the  activity
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}