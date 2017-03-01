package com.example.administrator.internet.Theme;

import android.os.Handler;
import android.os.Message;
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
import com.example.administrator.internet.ToolClass.JSONGetter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017/2/19 0019.
 */

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ViewHolder> {
    public static final int ITEM_HEADER = 0;
    public static final int ITEM_CONTENT = 1;
    public static final int ITEM_NO_PHOTO_CONTENT = 2;
    public static final int ITEM_FOOTER = 3;
    private final int LOAD_FINISHED=4;
    private String id;
    private String last_id;
    int viewType;
    private List<Editors> mEditorList;
    private List<News> mNewsList;
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            if(msg.what==LOAD_FINISHED){
                try{
                    ThemeAdapter.this.notifyItemChanged(Integer.valueOf(last_id));
                }catch (Exception e){
                }
            }
        }
    };

    public ThemeAdapter(List<News> newsList,List<Editors> mEditorList,String id){
        this.id=id;
        this.mNewsList=newsList;
        this.mEditorList=mEditorList;
        //添加为空的news替换footer
        last_id=newsList.get(newsList.size()-1).getId();
        mNewsList.add(new News(null,null,null));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        View simpleNewsView;
        ImageView newsImage;
        TextView newsTitle;
        ImageView themeImage;
        TextView themeText;
        TextView footerText;
        public ViewHolder(View view){
            super(view);
            simpleNewsView=view;
            newsImage=(ImageView) view.findViewById(R.id.news_img);
            themeImage=(ImageView) view.findViewById(R.id.theme_img);
            newsTitle=(TextView)view.findViewById(R.id.news_text);
            themeText=(TextView) view.findViewById(R.id.theme_text);
            footerText=(TextView)view.findViewById(R.id.homepage_footer);
        }
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
        else if(viewType==ITEM_FOOTER){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_footer,parent,false);
            ViewHolder holder=new ViewHolder(view);
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
        if(position==0){
            //Header
            holder.themeText.setText(ThemeContent.getDescription());
            holder.themeImage.setImageBitmap(ThemeSetter.getBitmap());
            holder.themeImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //加载主编的横向头像
            setEditor(mEditorList,holder.simpleNewsView);
        } else if (position!=0&&getItem(position).getId()==null){
            //Footer
            if(getItem(position).getId()==null){
                holder.footerText.setText(null);
                getItem(position).setId("ADD_FINISH");
                addNews(id);
            }
        } else {
            News news=getItem(position);
            //无图新闻
            if(getItem(position).getBitmap_url()=="NO_IMG"){
                holder.simpleNewsView.setMinimumHeight(250);
                AsyncNews asyncNews =new AsyncNews(null,holder.newsTitle,holder.simpleNewsView);
                asyncNews.execute(news);
            }else {
                //有图新闻
                AsyncNews asyncNews =new AsyncNews(holder.newsImage,holder.newsTitle,holder.simpleNewsView);
                asyncNews.execute(news);
            }
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
        if(getItem(position).getBitmap_url()!=null)
            return ITEM_CONTENT;
        return ITEM_FOOTER;
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

    //上拉添加新闻
    public void addNews(final String id){

        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONGetter jsonGetter=new JSONGetter();
                String responseDate=jsonGetter.getResponseDate("http://news-at.zhihu.com/api/4/theme/"+id+"/before/"+last_id);
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
                        title[i]=jsonObject.getString("title");
                        id[i]=jsonObject.getString("id");
                        if (jsonObject.has("images")){
                            news_IMG_URL[i]=jsonObject.getString("images");
                            news_IMG_URL[i]=news_IMG_URL[i].substring(2,news_IMG_URL[i].length()-2);
                        }else {
                            news_IMG_URL[i]="NO_IMG";
                        }
                        News news=new News(title[i],news_IMG_URL[i],id[i]);
                        mNewsList.add(news);
                    }
                    last_id=mNewsList.get(mNewsList.size()-1).getId();
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
