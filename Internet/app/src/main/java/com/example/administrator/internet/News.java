package com.example.administrator.internet;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/1/18 0018.
 */

public class News {
    private String newsTitle;
    private String bitmap_url;
    private String id;
    public News(String newsTitle,String bitmap_url,String id){
        this.id=id;
        this.newsTitle=newsTitle;
        this.bitmap_url=bitmap_url;
    }
    public String getId(){return id;}
    public String getNews(){
        return newsTitle;
    }
    public String getBitmap_url(){
        return bitmap_url;
    }

    public static class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
        private List<News> mNewsList;
        public static class ViewHolder extends RecyclerView.ViewHolder{
            View simpleNewsView;
            ImageView newsImage;
            TextView newsTitle;
            public ViewHolder(View view){
                super(view);
                simpleNewsView=view;
                newsImage=(ImageView) view.findViewById(R.id.news_img);
                newsTitle=(TextView)view.findViewById(R.id.news_text);
            }
        }
        public NewsAdapter(List<News> newsList){
            mNewsList=newsList;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_news,parent,false);
            ViewHolder holder=new ViewHolder(view);
            return holder;
        }
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            News news=mNewsList.get(position);
            AsyncIMG asyncIMG=new AsyncIMG(holder.newsImage,holder.newsTitle,holder.simpleNewsView);
            asyncIMG.execute(news);
        }
        @Override
        public int getItemCount() {
            return mNewsList.size();
        }


    }
}
