package com.example.administrator.internet.News;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.internet.HomePage.SlideSetter;
import com.example.administrator.internet.R;
import com.example.administrator.internet.ToolClass.AsyncNews;
import com.example.administrator.internet.ToolClass.GetTime;
import com.example.administrator.internet.ToolClass.JSONGetter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/23 0023.
 */

public  class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
    private List<News> mNewsList=new ArrayList<>();
    private final int HEADER=0;
    private final int CONTENT=1;
    private final int FOOTER=2;
    private final int LOAD_FINISHED=3;
    private int viewType;
    public static int time=Integer.parseInt(GetTime.getNowTime());

    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            if(msg.what==LOAD_FINISHED){
                NewsAdapter.this.notifyDataSetChanged();
            }
        }
    };


    public  class ViewHolder extends RecyclerView.ViewHolder{
        View simpleNewsView;
        ImageView newsImage;
        TextView newsTitle;
        TextView footerText;
        public ViewHolder(View view){
            super(view);
            simpleNewsView=view;
            newsImage=(ImageView) view.findViewById(R.id.news_img);
            newsTitle=(TextView)view.findViewById(R.id.news_text);
            footerText=(TextView)view.findViewById(R.id.homepage_footer);
        }
    }
    public NewsAdapter(List<News> newsList){
        mNewsList=newsList;
        //添加为空的news替换footer
        mNewsList.add(new News(null,null,null));
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.viewType=viewType;
        if(viewType==CONTENT){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_news,parent,false);
            ViewHolder holder=new ViewHolder(view);
            return holder;
        }else if(viewType==HEADER){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_header,parent,false);
            ViewHolder holder=new ViewHolder(view);
            return holder;
        }else {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_footer,parent,false);
            ViewHolder holder=new ViewHolder(view);
            return holder;
        }
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.simpleNewsView.setMinimumHeight(300);
        if(position==0){
            SlideSetter main=new SlideSetter();
            main.setSlideView(holder.simpleNewsView);
        }else if(getItem(position).getId()==null){
            //Bitmap_url为空就说明此footer未被加载过
            if(getItem(position).getBitmap_url()==null){
                holder.footerText.setText(GetTime.getYesterdayTime(time));
                addNews(time+"");
                time=GetTime.timeReduce(time);
                getItem(position).setBitmap_url("ADD_FINISH");
            }
        } else{
            News news=getItem(position);
            holder.newsTitle.setMaxWidth(660);
            //异步设置单条新闻
            AsyncNews asyncNews =new AsyncNews(holder.newsImage,holder.newsTitle,holder.simpleNewsView);
            asyncNews.execute(news);
        }
    }
    @Override
    public int getItemCount() {
        return mNewsList.size()+1;
    }
    @Override
    public int getItemViewType(int position) {
        if(position==0)
            return HEADER;
        if(getItem(position).getId()==null)
            return FOOTER;
        return CONTENT;
    }
    private News getItem(int position) {
        if(position>0){
            return mNewsList.get(position - 1);
        }
        return null;
    }

    //上拉添加新闻
    public void addNews(final String time){
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONGetter jsonGetter=new JSONGetter();
                String responseDate=jsonGetter.getResponseDate("http://news.at.zhihu.com/api/4/news/before/"+time);
                try {
                    JSONArray jsonArray1=new JSONArray("["+responseDate+"]");
                    for (int i=0;i<jsonArray1.length();i++){
                        JSONObject jsonObject=jsonArray1.getJSONObject(i);
                        responseDate=jsonObject.getString("stories");
                    }
                    JSONArray jsonArray2=new JSONArray(responseDate);
                    String news_IMG_URL[]=new String[jsonArray2.length()];
                    String title[]=new String[jsonArray2.length()];
                    String id[]=new String[jsonArray2.length()];

                    for (int i=0;i<jsonArray2.length();i++){
                        JSONObject jsonObject=jsonArray2.getJSONObject(i);
                        news_IMG_URL[i]=jsonObject.getString("images");
                        news_IMG_URL[i]=news_IMG_URL[i].substring(2,news_IMG_URL[0].length()-2);
                        title[i]=jsonObject.getString("title");
                        id[i]=jsonObject.getString("id");
                        News news=new News(title[i],news_IMG_URL[i],id[i]);
                        mNewsList.add(news);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    mNewsList.add(new News(null,null,null));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message message=new Message();
                            message.what=LOAD_FINISHED;
                            handler.sendMessage(message);
                        }
                    }).start();
                }
            }
        }).start();
    }
}