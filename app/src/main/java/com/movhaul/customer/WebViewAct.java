package com.movhaul.customer;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
/**
 * Created by SQINDIA on 1/30/2017.
 * webview shows full web page of movhaual web in app
 */
public class WebViewAct extends Activity {
    WebView web;
    int i =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.movhaul.customer.R.layout.webview);
        web = (WebView) findViewById(com.movhaul.customer.R.id.webviews);
        web.setWebViewClient(new myWebClient());
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl("http://104.197.80.225:8080/movehaul/");
        // web.loadUrl("http://my.opiniion.com/login");
        Log.e("tag", "webview");
        //http://www.passafaila.com/remita/processpayment.php?amt=1&payerName=siva&payerEmail=siva@sqindia.net&payerPhone=9790280707&paymenttype=VISA
        //web.loadUrl("http://www.passafaila.com/remita/processpayment.php?amt=5&payerName=siva&payerEmail=siva@sqindia.net&payerPhone=9790280707&paymenttype=MASTERCARD");
    }
    @SuppressWarnings("deprecation")
    private class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            i=i+1;
            Log.e("tag",i+" start: "+url);
            super.onPageStarted(view, url, favicon);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            i=i+1;
            Log.e("tag",i+ " load: "+url);
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            i=i+1;
            Log.e("tag",i+" finish "+url);
            super.onPageFinished(view, url);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
        web.goBack();
        return true;
    }
        return super.onKeyDown(keyCode, event);
    }
}
