package com.example.david.usafiapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.widget.RelativeLayout;

public class SalesaddActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private WebView wvwz;
    private RelativeLayout notconnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesadd);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        boolean isConnected = ConnectivityReceiver.isConnected(this);

        notconnected = (RelativeLayout) findViewById(R.id.notconnecteInternet);
        wvwz = (WebView)findViewById(R.id.wvwz);


        if (!isConnected) {
            notconnected.setVisibility(View.VISIBLE);
            wvwz.setVisibility(View.GONE);
        }else{
            notconnected.setVisibility(View.GONE);
            wvwz.setVisibility(View.VISIBLE);
        }



        wvwz.setWebViewClient(new SalesaddActivity.WebViewClient(){

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){
                // Ignore SSl certificates
                handler.proceed();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                notconnected.setVisibility(View.VISIBLE);
                wvwz.setVisibility(View.GONE);

                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.salesaddLayout), "Not connected to internet", Snackbar.LENGTH_SHORT)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent homeIntent = new Intent(SalesaddActivity.this, SalesaddActivity.class);
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
        });
        wvwz.getSettings().setJavaScriptEnabled(true);
        wvwz.getSettings().setDomStorageEnabled(true);
        wvwz.getSettings().setSupportZoom(false);
        wvwz.getSettings().setAllowFileAccess(true);
        wvwz.getSettings().setAllowContentAccess(true);
        wvwz.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        wvwz.loadUrl("http://adetechresolute.co.ke/usafi/api_retrievedata/addsale");

    }

    public class WebViewClient extends android.webkit.WebViewClient {

        final ProgressDialog progressDialog = new ProgressDialog(SalesaddActivity.this, R.style.MyAlertDialogStyle);

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);

            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            // TODO Auto-generated method stub

            super.onPageFinished(view, url);
            progressDialog.dismiss();

        }
    }

    @Override
    public void onBackPressed() {
        // do something on back.

        Intent homeIntent = new Intent(SalesaddActivity.this, SalesActivity.class);
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
