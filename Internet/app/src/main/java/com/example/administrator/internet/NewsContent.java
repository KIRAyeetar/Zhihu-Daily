package com.example.administrator.internet;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

public class NewsContent extends Activity {
    String responseDate;
    String body;
    String IMG_URL;
    String image_source;
    String title;
    String id;
    String good;
    String talk;
    Bitmap bitmap;
    TextView talk_text;
    TextView good_text;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_layout);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.news_title);

        Bundle bundle=getIntent().getExtras();
        this.id=bundle.getString("id");

        talk_text=(TextView)findViewById(R.id.talk_text);
        good_text=(TextView)findViewById(R.id.good_text);
        Button button=(Button) findViewById(R.id.send_button);
        back=(Button)findViewById(R.id.back);
        button.setText(id);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getJson(id);
            }
        });
    }
    private void getJson(final String id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONGetter jsonGetter=new JSONGetter();
                responseDate=jsonGetter.getResponseDate("http://news-at.zhihu.com/api/4/news/"+id);
                try {
                    JSONArray jsonArray=new JSONArray("["+responseDate+"]");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        body=jsonObject.getString("body");
                        IMG_URL=jsonObject.getString("image");
                        title=jsonObject.getString("title");
                        image_source=jsonObject.getString("image_source");
                        bitmap=BitmapFactory.decodeStream(new URL(IMG_URL).openStream());
                    }

                    responseDate=jsonGetter.getResponseDate("http://news-at.zhihu.com/api/4/story-extra/"+id);
                    jsonArray=new JSONArray("["+responseDate+"]");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        good=jsonObject.getString("popularity");
                        talk=jsonObject.getString("comments");
                    }
                    setWebView(body,good,talk);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                }
            }
        }).start();
    }

    private void setWebView(final String body,final String good,final String talk){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WebView webView=(WebView) findViewById(R.id.web_view);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient());
                webView.loadDataWithBaseURL("about:blank", body, "text/html", "utf-8", null);
                talk_text.setText(talk);
                good_text.setText(good);
            }
        });
    }


}
