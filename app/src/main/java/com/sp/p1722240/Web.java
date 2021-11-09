package com.sp.p1722240;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Web extends AppCompatActivity {
private WebView web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);

        web = (WebView) findViewById(R.id.wb);
        web.loadUrl("https://vrl.lta.gov.sg/lta/vrl/action/pubfunc?ID=FuelCostCalculator");
        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient());

    }
}
