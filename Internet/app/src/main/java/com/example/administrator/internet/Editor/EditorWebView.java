package com.example.administrator.internet.Editor;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.internet.R;

/**
 * Created by Administrator on 2017/2/22 0022.
 */

public class EditorWebView extends Activity {
    private String name;
    private String url;
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle=getIntent().getExtras();
        this.url=bundle.getString("url");
        this.name=bundle.getString("name");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_webview);

        setWebView(url);

        TextView textView=(TextView) findViewById(R.id.editor_content_title);
        textView.setText(name+"的主页");

        Button back=(Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void  setWebView(String url){
                WebView webView=(WebView) findViewById(R.id.editor_web);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl(url);
    }
}
