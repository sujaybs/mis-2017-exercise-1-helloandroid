package com.example.sujaybshalawadi.mis1;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.xwalk.core.XWalkView;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends Activity {

    private Button button;
    private EditText editText;

    // 3rd party web view module
    private XWalkView xWalkView;

    // Volley infrastructure
    private RequestQueue volleyRequestQueue;

    // ConnectivityManager for network checks
    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // request connectivity service
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);

        // init web view module
        xWalkView = new XWalkView(this, this);

        // grab linear layout and add web view to it
        LinearLayout webViewLayout = (LinearLayout) findViewById(R.id.view);
        webViewLayout.addView(xWalkView);

        // listen for clicks
        button.setOnClickListener((view) -> checkURLAndLoad());

        // listen for IME_ACTION_DONE event
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                checkURLAndLoad();
                return true;
            }
            return false;
        });

        // init volley request queue
        volleyRequestQueue = Volley.newRequestQueue(this);
    }

    private void checkURLAndLoad() {
        URL url = null;

        try {
            // check URL sanity
            if (!Patterns.WEB_URL.matcher(editText.getText().toString()).matches())
                throw new MalformedURLException();

            // build formal URL using HTTPS for safety
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
            loadURL(url.toString());
        } else if (!isNetworkAccessible()) {
            // handle network inaccessibility case (display a toast)
            Toast.makeText(
                    getApplicationContext(),
                    R.string.err_net, Toast.LENGTH_LONG).show();
        }
    }

    private void loadURL(String url) {
        volleyRequestQueue.add(
                new StringRequest(
                        url,
                        this::handleResponse,
                        this::handleError
                )
        );
    }

    private void handleResponse(String response) {
        xWalkView.load(null, new String(response.getBytes()));
    }

    private void handleError(VolleyError error) {
        String errorMessage;

        if (error instanceof TimeoutError) {
            errorMessage = getString(R.string.net_timeout);
        } else if (error instanceof ServerError) {
            errorMessage = getString(R.string.net_serv_err);
        } else if (error instanceof AuthFailureError) {
            errorMessage = getString(R.string.net_auth_err);
        } else if (error instanceof NetworkError) {
            errorMessage = getString(R.string.net_err_req);
        } else if (error instanceof ParseError) {
            errorMessage = getString(R.string.net_par_err);
        } else {
            errorMessage = getString(R.string.net_unid_err);
        }

        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    private boolean isNetworkAccessible() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
