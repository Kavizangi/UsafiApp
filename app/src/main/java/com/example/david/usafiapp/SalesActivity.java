package com.example.david.usafiapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SalesActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CardView viewsalesCard;
    private CardView addsalesCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        viewsalesCard = (CardView) findViewById(R.id.viewsalesCard);
        addsalesCard = (CardView) findViewById(R.id.addsalesCard);


        viewsalesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launching the  activity
                Intent intent = new Intent(SalesActivity.this, SalesviewActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

        addsalesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launching the  activity
                Intent intent = new Intent(SalesActivity.this, SalesaddActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        // do something on back.

        Intent homeIntent = new Intent(SalesActivity.this, MainActivity.class);
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
