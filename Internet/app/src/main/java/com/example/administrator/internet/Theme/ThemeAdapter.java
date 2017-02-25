package com.example.administrator.internet.Theme;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.internet.Editor.Editors;
import com.example.administrator.internet.Editor.EditorsAvatarAdapter;
import com.example.administrator.internet.News.News;
import com.example.administrator.internet.R;
import com.example.administrator.internet.ToolClass.AppContext;
import com.example.administrator.internet.ToolClass.AsyncNews;

import java.util.List;

/**
 * Created by Administrator on 2017/2/19 0019.
 */

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ViewHolder> {
    public static final int ITEM_HEADER = 0;
    public static final int ITEM_CONTENT = 1;
    public static final int ITEM_NO_PHOTO_CONTENT = 2;
    int viewType;
    private List<Editors> mEditorList;
    private List<News> mNewsList;
    public static class ViewHolder extends RecyclerView.ViewHolder{
        View simpleNewsView;
        ImageView newsImage;
        TextView newsTitle;
        ImageView themeImage;
        TextView themeText;
        public ViewHolder(View view){
            super(view);
            simpleNewsView=view;
            newsImage=(ImageView) view.findViewById(R.id.news_img);
            themeImage=(ImageView) view.findViewById(R.id.theme_img);
            newsTitle=(TextView)view.findViewById(R.id.news_text);
            themeText=(TextView) view.findViewById(R.id.theme_text);
        }
    }
    public ThemeAdapter(List<News> newsList,List<Editors> mEditorList){
        this.mNewsList=newsList;this.mEditorList=mEditorList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        this.viewType=viewType;
        if (viewType == ITEM_HEADER) {
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.theme_header,parent,false);
            ThemeAdapter.ViewHolder holder=new ThemeAdapter.ViewHolder(view);
            return holder;
        }
        //这里是分为有图新闻和没图新闻分别的布局
        else if(viewType==ITEM_NO_PHOTO_CONTENT){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_news_nophoto,parent,false);
            ThemeAdapter.ViewHolder holder=new ThemeAdapter.ViewHolder(view);
            return holder;
        }else {
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_news,parent,false);
            ThemeAdapter.ViewHolder holder=new ThemeAdapter.ViewHolder(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (viewType!=ITEM_HEADER&&position>0) {
            //无图新闻
            if(getItem(position).getBitmap_url()=="NO_IMG"){
                holder.simpleNewsView.setMinimumHeight(250);
                News news=getItem(position);
                AsyncNews asyncNews =new AsyncNews(null,holder.newsTitle,holder.simpleNewsView);
                asyncNews.execute(news);
            }else {
                //有图新闻
                News news=getItem(position);
                holder.newsTitle.setMaxWidth(660);
                AsyncNews asyncNews =new AsyncNews(holder.newsImage,holder.newsTitle,holder.simpleNewsView);
                asyncNews.execute(news);
            }
        } else {
            //Header
            holder.themeText.setText(ThemeContent.getDescription());
            holder.themeImage.setImageBitmap(ThemeSetter.getBitmap());
            holder.themeImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //加载主编的横向头像
            setEditor(mEditorList,holder.simpleNewsView);
        }
    }
    @Override
    public int getItemCount() {
        return mNewsList.size()+1;
    }
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return ITEM_HEADER;
        if(getItem(position).getBitmap_url()=="NO_IMG")
            return ITEM_NO_PHOTO_CONTENT;
        return ITEM_CONTENT;
    }
    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private News getItem(int position) {
        if(position>0){
            return mNewsList.get(position - 1);
        }
        return null;
    }

    public void setEditor(List editorsList,View view){
        RecyclerView recyclerView=(RecyclerView) view.findViewById(R.id.editor_avatar_rc);
        LinearLayoutManager layoutManager=new LinearLayoutManager(AppContext.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        EditorsAvatarAdapter editorsAvatarAdapter =new EditorsAvatarAdapter(editorsList);
        recyclerView.setAdapter(editorsAvatarAdapter);
    }
}
