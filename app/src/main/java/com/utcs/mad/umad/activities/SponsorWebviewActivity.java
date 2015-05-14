package com.utcs.mad.umad.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.utcs.mad.umad.R;


public class SponsorWebviewActivity extends ActionBarActivity {

    private WebView webview;
    private Toolbar toolbar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle(getIntent().getStringExtra("sponsorName"));

        if(hasConnection()) {
            setupWebview();
            setupProgressBar();
        } else {
            Toast.makeText(this, getString(R.string.webview_no_connection_error), Toast.LENGTH_SHORT).show();
        }
    }

    // Checks if the device has a connection
    private boolean hasConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }

    // Sets the settings for the webview and loads the webpage to the view
    private void setupWebview() {
        webview = (WebView) findViewById(R.id.webview);

        // Enable javascript, necessary for some sponsor pages
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        webview.getSettings().setSupportMultipleWindows(false);
        webview.getSettings().setSupportZoom(false);
        webview.setVerticalScrollBarEnabled(false);
        webview.setHorizontalScrollBarEnabled(false);

        // Override redirects
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

        });

        webview.loadUrl(getIntent().getStringExtra("sponsorWebsite"));
    }

    // Visual aid to the user
    private void setupProgressBar() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                if(progress < 100 && progressBar.getVisibility() == ProgressBar.GONE){
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                }
                progressBar.setProgress(progress);
                if(progress == 100) {
                    progressBar.setVisibility(ProgressBar.GONE);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.about_us) {
            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
