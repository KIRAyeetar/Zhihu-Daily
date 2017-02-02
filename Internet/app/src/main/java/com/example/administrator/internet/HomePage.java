package com.example.administrator.internet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

/**
 * Created by Administrator on 2017/1/13 0013.
 */

public class HomePage extends Activity {
    Activity homepage=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        //自定义标题
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.home_title);
        //滚动新闻
        SlideSetter main=new SlideSetter();
        main.setSlideView(this);
        //最新新闻
        NewsSetter newsSetter=new NewsSetter();
        newsSetter.setIMGFromInternet("http://news-at.zhihu.com/api/4/news/latest",homepage);newsSetter.setNews(homepage);
    }

    public void CreateNewNews(View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomePage.this,NewsContent.class);
        /*Bundle bundle=new Bundle();
        bundle.putString("id", id);
        intent.putExtras(bundle);*/
                startActivity(intent);
            }
        });

    }
}
