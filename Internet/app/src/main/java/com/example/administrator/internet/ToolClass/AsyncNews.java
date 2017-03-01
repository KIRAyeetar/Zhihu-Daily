package com.example.administrator.internet.ToolClass;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.internet.News.News;
import com.example.administrator.internet.News.NewsContent;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/22 0022.
 */
//异步加载单条新闻
public class AsyncNews extends AsyncTask<News,Void,Void>{
    ImageView mImageView;
    TextView mTextView;
    View simpleNewsView;
    Bitmap bitmap;
    String title;
    String id;
    News news;
    private Map<String, SoftReference<Bitmap>> imageCache =
            new HashMap<String, SoftReference<Bitmap>>();


    public AsyncNews(ImageView mImageView, TextView mTextView, View simpleNewsView){
        this.simpleNewsView=simpleNewsView;
        this.mImageView=mImageView;
        this.mTextView=mTextView;
    }

    @Override
    protected Void doInBackground(News... news) {
        this.news=news[0];
        bitmap=getIMG(news[0].getBitmap_url());
        title=news[0].getNews();
        id=news[0].getId();
        return null;
    }
    @Override
    protected void onPostExecute(Void v) {
        if (mImageView!=null){
            mImageView.setImageBitmap(bitmap);
        }
        if (mTextView!=null)
        mTextView.setText(title);
        if(News.beenReadNewsList.contains(news.getId())){
            mTextView.setTextColor(Color.argb(255,215,215,215));
        }
        //进入单个新闻界面
        simpleNewsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextView.setTextColor(Color.argb(255,215,215,215));

                if(News.beenReadNewsList.contains(news)==false){
                    News.beenReadNewsList.add(news.getId());
                }
                Intent intent=new Intent(AppContext.getContext(),NewsContent.class);
                Bundle bundle=new Bundle();

                bundle.putString("id", id);
                bundle.putString("img_title", title);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                AppContext.getContext().startActivity(intent);
            }
        });
    }
    private Bitmap getIMG (String url){
        if(imageCache.containsKey(url)){
            SoftReference<Bitmap> softReference=imageCache.get(url);
            if(softReference.get()!=null){
                return softReference.get();
            }
        }
        Bitmap bitmap=null;
        try {
            bitmap= BitmapFactory.decodeStream(new URL(url).openStream());
            imageCache.put(url,new SoftReference<Bitmap>(bitmap));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return bitmap;
        }
    }
}
