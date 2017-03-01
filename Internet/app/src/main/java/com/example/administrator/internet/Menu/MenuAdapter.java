package com.example.administrator.internet.Menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.internet.HomePage.HomePage;
import com.example.administrator.internet.HomePage.SlideSetter;
import com.example.administrator.internet.R;
import com.example.administrator.internet.Theme.ThemeContent;

import java.util.List;

/**
 * Created by Administrator on 2017/2/18 0018.
 */

public class  MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    public static final int ITEM_HEADER = 0;
    public static final int ITEM_CONTENT = 1;
    public static int TOBE_GRAY=0;
    private View grayView;
    int viewType;
    Activity activity;


    private List<Menu> mMenuList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View simpleMenuView;
        TextView MenuText;
        LinearLayout linearLayout;
        public ViewHolder(View view) {
            super(view);
            simpleMenuView = view;
            linearLayout=(LinearLayout) view.findViewById(R.id.back_home);
            MenuText = (TextView) view.findViewById(R.id.menu_text);
        }
    }
    public MenuAdapter(List<Menu> menuList, Activity activity,int toBe_gray){
        mMenuList=menuList;this.activity=activity;
        //接收需要变成灰色栏的序号
        this.TOBE_GRAY=toBe_gray;
    }

    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.viewType=viewType;
        if (viewType == ITEM_CONTENT) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_menu,parent,false);
            ViewHolder holder=new ViewHolder(view);
            return holder;
        } else{
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_header,parent,false);
            ViewHolder holder=new ViewHolder(view);
            return holder;
        }
    }
    @Override
    public void onBindViewHolder(final MenuAdapter.ViewHolder holder, final int position) {
        if (viewType!=ITEM_HEADER&&position>0) {

            //如果是需要变灰的“栏目”就变灰
            if (position==TOBE_GRAY){
                holder.simpleMenuView.setBackgroundColor(Color.argb(255,228,228,228));
                this.grayView=holder.simpleMenuView;
            }

            String name=getItem(position).getName();
            holder.MenuText.setText(name);
            holder.simpleMenuView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //已经灰了的变白，点击的变灰
                    grayView.setBackgroundColor(Color.argb(255,255,255,255));
                    holder.simpleMenuView.setBackgroundColor(Color.argb(255,228,228,228));

                    Intent intent=new Intent(activity,ThemeContent.class);
                    Bundle bundle=new Bundle();
                    bundle.putInt("gray_position",position);
                    bundle.putString("id", getItem(position).getId());
                    bundle.putString("img_url", getItem(position).getImg_url());
                    bundle.putString("description", getItem(position).getDescription());
                    bundle.putString("name", getItem(position).getName());
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                    activity.finish();
                }
            });
        }else {

            //如果“主页”需要变灰就变灰
            if (position==TOBE_GRAY){
                holder.linearLayout.setBackgroundColor(Color.argb(255,228,228,228));
                this.grayView=holder.linearLayout;
            }
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    grayView.setBackgroundColor(Color.argb(255,255,255,255));
                    SlideSetter.ImageHandler.currentItem=0;
                    holder.linearLayout.setBackgroundColor(Color.argb(255,228,228,228));
                    TOBE_GRAY=0;
                    Intent intent=new Intent(activity,HomePage.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return mMenuList.size()+1;
    }
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
        return ITEM_HEADER;
        return ITEM_CONTENT;
    }
    private boolean isPositionHeader(int position) {
        return position == 0;
    }
    private Menu getItem(int position) {
        if(position>0){
            return mMenuList.get(position - 1);
        }
        return null;
    }
}
