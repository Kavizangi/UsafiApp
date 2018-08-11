package com.example.david.usafiapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import helper.SQLiteHandler;
import helper.SessionManager;

public class AgentsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SessionManager session;
    private SQLiteHandler db;
    private CardView viewagentCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agents);


        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        viewagentCard = (CardView) findViewById(R.id.viewagentCard);


        viewagentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launching the  activity
                Intent intent = new Intent(AgentsActivity.this, AgentsviewActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        // do something on back.

        Intent homeIntent = new Intent(AgentsActivity.this, MainActivity.class);
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
