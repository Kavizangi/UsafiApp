package com.example.david.usafiapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class CustomersActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CardView viewcustomerCard;
    private CardView addcustomerCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        viewcustomerCard = (CardView) findViewById(R.id.viewcustomerCard);
        addcustomerCard = (CardView) findViewById(R.id.addcustomerCard);


        viewcustomerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launching the  activity
                Intent intent = new Intent(CustomersActivity.this, CustomersviewActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

        addcustomerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launching the  activity
                Intent intent = new Intent(CustomersActivity.this, CustomeraddActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        // do something on back.

        Intent homeIntent = new Intent(CustomersActivity.this, MainActivity.class);
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
