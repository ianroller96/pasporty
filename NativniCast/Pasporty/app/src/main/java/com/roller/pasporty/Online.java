package com.roller.pasporty;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tbruyelle.rxpermissions2.RxPermissions;

public class Online extends AppCompatActivity {
    final RxPermissions rxPermissions = new RxPermissions(this);

    private WebView myWebView;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);

        Intent intent = getIntent();
        String soubor = intent.getStringExtra(SelectOnline.EXTRA_TEXT);

        myWebView = (WebView)findViewById(R.id.webView);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);

        webSettings.setAppCacheEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        webSettings.setDatabaseEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);

        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        if (savedInstanceState == null)
                        {
                            myWebView.loadUrl(getString(R.string.web) + "map.php?src=" + soubor);
                        }
                        myWebView.setWebChromeClient(new WebChromeClient() {
                            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                                callback.invoke(origin, true, false);
                            }
                        });
                        myWebView.setWebViewClient(new WebViewClient());
                    } else {
                        if (savedInstanceState == null)
                        {
                            myWebView.loadUrl(getString(R.string.web) + "map.php?src=" + soubor);
                        }
                        myWebView.setWebViewClient(new WebViewClient());
                    }
                });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Opravdu chcete opustit Online Editor?")
                .setCancelable(false)
                .setPositiveButton("Ano", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Online.this.finish();
                    }
                })
                .setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState )
    {
        super.onSaveInstanceState(outState);
        myWebView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        myWebView.restoreState(savedInstanceState);
    }
}