package com.example.administrator.internet;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/2/18 0018.
 */

public class  MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    public static final int ITEM_HEADER = 0;
    public static final int ITEM_CONTENT = 1;
    int viewType;

    private List<Menu> mMenuList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View simpleMenuView;
        TextView MenuText;
        public ViewHolder(View view) {
            super(view);
            simpleMenuView = view;
            MenuText = (TextView) view.findViewById(R.id.menu_text);
        }
    }
    public MenuAdapter(List<Menu> menuList){
        mMenuList=menuList;
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
    public void onBindViewHolder(MenuAdapter.ViewHolder holder, int position) {
        if (viewType!=ITEM_HEADER&&position>0) {
            String name=getItem(position);
            holder.MenuText.setText(name);
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
    private String getItem(int position) {
        if(position>0){
            return mMenuList.get(position - 1).getName();
        }
        return null;
    }
}
