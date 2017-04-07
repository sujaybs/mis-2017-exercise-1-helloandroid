package com.example.sujaybshalawadi.myapplication;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.xwalk.core.XWalkView;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends Activity {

    private Button button;
    private EditText editText;

    // 3rd party web view module
    private XWalkView xWalkView;

    private URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);

        // init web view module
        xWalkView = new XWalkView(this, this);

        // grab linear layout and add web view to it
        LinearLayout webViewLayout = (LinearLayout) findViewById(R.id.view);
        webViewLayout.addView(xWalkView);

        button.setOnClickListener((view) -> loadURL());
    }

    private void loadURL() {
        URL url = null;

        try {
            // check URL sanity
            if (!Patterns.WEB_URL.matcher(editText.getText().toString()).matches())
                throw new MalformedURLException();

            // build formal URL using HTTPS
            Uri builtUri = Uri.parse(editText.getText().toString())
                    .buildUpon()
                    .scheme("https")
                    .build();

            // check format for safety
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            // handle malformed url case (display a toast)
            Toast.makeText(getApplicationContext(), R.string.err_url, Toast.LENGTH_LONG).show();
        }

        // perform various checks
        if (xWalkView != null && editText != null && url != null && isNetworkAccessible()) {
            xWalkView.load(url.toString(), null);
        } else if (!isNetworkAccessible()) {
            // handle network inaccessibility case (display a toast)
            Toast.makeText(getApplicationContext(), R.string.err_net, Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAccessible() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
