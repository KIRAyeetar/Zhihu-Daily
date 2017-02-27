package com.example.administrator.internet.Theme;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.internet.Menu.MenuSetter;
import com.example.administrator.internet.R;

/**
 * Created by Administrator on 2017/2/19 0019.
 */

public class ThemeContent extends Activity{
    public static String id;
    private static String name;
    private static String description;
    private static String img_url;
    private static int gray_position=0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.theme_layout);
        Bundle bundle=getIntent().getExtras();

        this.gray_position=bundle.getInt("gray_position");
        this.id=bundle.getString("id");
        this.img_url=bundle.getString("img_url");
        this.description=bundle.getString("description");
        this.name=bundle.getString("name");

        MenuSetter menuSetter=new MenuSetter();
        menuSetter.getMenuList(this,getGray_position());

        TextView textView=(TextView)findViewById(R.id.theme_title_text);
        textView.setText(name);


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
        ThemeSetter themeSetter=new ThemeSetter();
        themeSetter.setIMGFromInternet("http://news-at.zhihu.com/api/4/theme/"+id,this,img_url);

    }
    public static String getDescription(){
        return description;
    }
    public static int getGray_position(){return gray_position;}
}
