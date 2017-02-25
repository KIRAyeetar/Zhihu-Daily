package com.example.administrator.internet.HomePage;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;

import com.example.administrator.internet.Menu.MenuSetter;
import com.example.administrator.internet.News.NewsAdapter;
import com.example.administrator.internet.News.NewsSetter;
import com.example.administrator.internet.R;
import com.example.administrator.internet.ToolClass.GetTime;

/**
 * Created by Administrator on 2017/1/13 0013.
 */

public class HomePage extends Activity {
    Activity homepage=this;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        NewsAdapter.time=Integer.parseInt(GetTime.getNowTime());

        //设置下拉刷新
        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                SlideSetter.ImageHandler.currentItem=0;
                NewsAdapter.time=Integer.parseInt(GetTime.getNowTime());
                NewsSetter newsSetter=new NewsSetter();
                newsSetter.setIMGFromInternet("http://news-at.zhihu.com/api/4/news/latest",homepage);
            }
        });

        //设置侧拉
        Button menu=(Button) findViewById(R.id.menu_button);
        final DrawerLayout drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout_homepage);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //最新新闻
        NewsSetter newsSetter=new NewsSetter();
        newsSetter.setIMGFromInternet("http://news-at.zhihu.com/api/4/news/latest",homepage);

        //菜单
        MenuSetter menuSetter=new MenuSetter();
        menuSetter.getMenuList(this,0);
    }

}
