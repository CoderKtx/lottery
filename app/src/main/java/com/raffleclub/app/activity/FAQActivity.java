package com.raffleclub.app.activity;

import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.raffleclub.app.R;

public class FAQActivity extends AppCompatActivity {


    private WebView webView;

    private String type = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq_web_view);
//        setToolbar()
//        var urlLike = intent.getStringExtra(ContextApp.LINK)?:""

        webView = findViewById(R.id.webView);

        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setDatabaseEnabled(true);
//        webView.setWebViewClient(mWebViewClient);

//        webView.addJavascriptInterface(JavaScriptInterface(this), "AndroidFunction");

        if (getIntent().getStringExtra("faq")!=null)
        type = getIntent().getStringExtra("faq");

        if (type != null && type.equals("faq")) {
            getSupportActionBar().setTitle(getString(R.string.menu_faq));
            webView.loadUrl("https://www.raffleclub.in/faq");

        } else if (type != null && type.equals("refund")) {
            getSupportActionBar().setTitle(getString(R.string.menu_refund_policy));
//            webView.loadUrl("https://www.raffleclub.in/refund-policy");
            webView.loadUrl("https://www.raffleclub.in/app-refund-policy");
        }



    }
}
