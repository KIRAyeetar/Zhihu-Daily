package com.example.administrator.internet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class SplashScreen extends Activity {
    String responseDate;
    ImageView imageView;
    TextView textView;
    Bitmap bitmap=null;
    String text=null;
    String imgURL=null;
    final Activity activity=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.splashscreen, null);
        setContentView(view);
        textView = (TextView) findViewById(R.id.splashscreen_text);
        imageView = (ImageView) findViewById(R.id.splashscreen_photo);

        setIMGFromInternet("http://news-at.zhihu.com/api/4/start-image/1080*1776");
        setSplash(view);
    }
    private void setSplash (View view){
        AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
        aa.setDuration(2000);
        view.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationEnd(Animation arg0) {
                Intent mainIntent = new Intent(SplashScreen.this,HomePage.class);
                SplashScreen.this.startActivity(mainIntent);
                activity.finish();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationStart(Animation animation) {}
        });
    }

    private void setIMGFromInternet(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONGetter jsonGetter=new JSONGetter();
                responseDate=jsonGetter.getResponseDate(url);
                    try {
                        JSONArray jsonArray=new JSONArray("["+responseDate+"]");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(0);
                            text=jsonObject.getString("text");
                            imgURL=jsonObject.getString("img");
                            try {
                                bitmap= BitmapFactory.decodeStream(new URL(imgURL).openStream());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showResponse(text,bitmap);
            }
        }).start();
    }

    private void showResponse(final String response,final Bitmap bitmap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(response);
                imageView.setImageBitmap(bitmap);
            }
        });
    }
}

