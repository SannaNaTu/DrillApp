package com.example.drillapp.Url;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.drillapp.R;

public class UrlActivity extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url);

        webView = findViewById(R.id.webView);

        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("https://www.lab.fi/");

        WebSettings webSettings =webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }
    public void onBackPressed(){
        if (
                webView.canGoBack()
        ) {
            webView.goBack();
        }
        else{
            super.onBackPressed();
        }
    }
}
