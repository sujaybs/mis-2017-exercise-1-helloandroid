package com.example.sujaybshalawadi.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.xwalk.core.XWalkView;

public class MainActivity extends Activity {

    private Button button;
    private EditText editText;

    // 3rd party web view module
    private XWalkView xWalkView;

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

    // TODO: handle wrong urls, inform user of errors, handle incomplete urls

    private void loadURL() {
        // perform null checks and the correctness of the URL
        if (xWalkView != null && editText != null && URLUtil.isNetworkUrl(editText.getText().toString()))
            xWalkView.load(editText.getText().toString(), null);
    }
}
