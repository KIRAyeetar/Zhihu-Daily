package com.example.administrator.internet;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
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
    String img_title;
    String css;
    String id;
    String good;
    String talk;
    ImageView imageView;
    TextView imgRes;
    Bitmap bitmap;
    TextView talk_text;
    TextView good_text;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_layout);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.news_title);

        Bundle bundle=getIntent().getExtras();
        this.id=bundle.getString("id");
        this.img_title=bundle.getString("img_title");

        talk_text=(TextView)findViewById(R.id.talk_text);
        good_text=(TextView)findViewById(R.id.good_text);
        back=(Button)findViewById(R.id.back);

        CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        imageView=(ImageView)findViewById(R.id.fun);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        TextView imgTitle=(TextView)findViewById(R.id.title_text);
        imgRes=(TextView)findViewById(R.id.img_res);
        imgTitle.setText(img_title);

        collapsingToolbarLayout.setTitle("");




        getJson(id);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                        css=jsonObject.getString("css");
                        bitmap=BitmapFactory.decodeStream(new URL(IMG_URL).openStream());
                    }

                    responseDate=jsonGetter.getResponseDate("http://news-at.zhihu.com/api/4/story-extra/"+id);
                    jsonArray=new JSONArray("["+responseDate+"]");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        good=jsonObject.getString("popularity");
                        talk=jsonObject.getString("comments");
                    }
                    setWebView(body,good,talk,image_source,css);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                }
            }
        }).start();
    }

    private void setWebView(final String body,final String good,final String talk,final  String image_source,final String css){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WebView webView=(WebView) findViewById(R.id.web_view);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient());
                webView.loadDataWithBaseURL(css, body, "text/html", "utf-8", null);
                webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                imgRes.setText(image_source);
                imageView.setImageBitmap(bitmap);
                talk_text.setText(talk);
                good_text.setText(good);
            }
        });
    }


}
