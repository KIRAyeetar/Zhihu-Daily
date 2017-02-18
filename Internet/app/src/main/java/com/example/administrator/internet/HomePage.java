package com.example.administrator.internet;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.Window;
import android.widget.Button;

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
        //设置侧拉
        Button menu=(Button) findViewById(R.id.menu_button);
        final DrawerLayout drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout_homepage);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        //滚动新闻
        SlideSetter main=new SlideSetter();
        main.setSlideView(this);
        //最新新闻
        NewsSetter newsSetter=new NewsSetter();
        newsSetter.setIMGFromInternet("http://news-at.zhihu.com/api/4/news/latest",homepage);newsSetter.setNews(homepage);
    }


}
