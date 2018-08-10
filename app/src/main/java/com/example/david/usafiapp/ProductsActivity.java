package com.example.david.usafiapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private List<Products> productsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent homeIntent = new Intent(ProductsActivity.this, MainActivity.class);
                startActivity(homeIntent);

                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                finish();

            }
        });


        // recycler view

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new ProductsAdapter(productsList);

        recyclerView.setHasFixedSize(true);

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.HORIZONTAL, 16));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);

        boolean isConnected = ConnectivityReceiver.isConnected(this);


        if (isConnected) {

            prepareData();
        }else{

            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.productLayout), "Sorry! Not connected to internet", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent homeIntent = new Intent(ProductsActivity.this, ProductsActivity.class);
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
     * Prepares sample data to provide data set to adapter
     */
    private void prepareData() {

        // Server user register url
         String url = "https://adetechresolute.co.ke/usafi/api_retrievedata/products";

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Fetching...");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        Products product = new Products();
                        product.setName(jsonObject.getString("name"));
                        product.setDate(jsonObject.getString("createdon"));

                        productsList.add(product);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }

                // notify adapter about data set changes
                // so that it will render the list with new data

                mAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                Toast.makeText(getApplicationContext(),
                        "Sorry, an error occurred. Try again later!", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

                Intent homeIntent = new Intent(ProductsActivity.this, MainActivity.class);
                startActivity(homeIntent);

                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                finish();

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);


    }

    @Override
    public void onBackPressed() {
        // do something on back.

        Intent homeIntent = new Intent(ProductsActivity.this, MainActivity.class);
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
