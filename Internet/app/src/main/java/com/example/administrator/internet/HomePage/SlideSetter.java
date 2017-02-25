package com.example.administrator.internet.HomePage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.internet.News.NewsContent;
import com.example.administrator.internet.R;
import com.example.administrator.internet.ToolClass.AppContext;
import com.example.administrator.internet.ToolClass.JSONGetter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Created by Administrator on 2017/1/18 0018.
 */

public class SlideSetter extends Activity{
    private String top_stories_json;
    private String top_stories_URL[];
    private ImageHandler handler = new ImageHandler(new WeakReference<SlideSetter>(this));
    private static final String LOG_TAG = ACCESSIBILITY_SERVICE;
    private TextView textView;
    private ViewPager viewPager;
    private ImageView[] tips;
    private ImageView[] mImageViews;
    private String responseDate;
    private Bitmap bitmap[];
    private String title[];
    private String id[];
    ViewGroup group;
    private int LOAD_FINISHED=-1;
    Handler imageHandler=new Handler(){
        public void handleMessage(Message msg){
            if(msg.what==LOAD_FINISHED){
                setViewPager(AppContext.getContext());
            }
        }
    };
    @SuppressLint("NewApi")
    public void setSlideView(final View view) {
        textView = (TextView) view.findViewById(R.id.viewPager_text);
        group = (ViewGroup) view.findViewById(R.id.viewGroup);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        top_stories_URL=new String[5];
        bitmap=new Bitmap[5];
        tips = new ImageView[5];
        title=new String[5];
        id=new String[5];
        getJson("http://news-at.zhihu.com/api/4/news/latest");
    }
    private void getJson(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONGetter jsonGetter=new JSONGetter();
                responseDate=jsonGetter.getResponseDate(url);
                try {
                    JSONArray jsonArray1=new JSONArray("["+responseDate+"]");
                    for (int i=0;i<jsonArray1.length();i++){
                        JSONObject jsonObject=jsonArray1.getJSONObject(i);
                        top_stories_json=jsonObject.getString("top_stories");
                    }
                    JSONArray jsonArray2=new JSONArray(top_stories_json);
                    for (int i=0;i<jsonArray2.length();i++){
                        JSONObject jsonObject=jsonArray2.getJSONObject(i);
                        top_stories_URL[i]=jsonObject.getString("image");
                        title[i]=jsonObject.getString("title");
                        id[i]=jsonObject.getString("id");
                        bitmap[i]= getIMG(top_stories_URL[i]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message message=new Message();
                            message.what=LOAD_FINISHED;
                            imageHandler.sendMessage(message);
                        }
                    }).start();
                }
            }
        }).start();
    }

    @SuppressLint("NewApi")
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.length; i++) {
            if (i == selectItems) {
                tips[i].setBackgroundResource(R.drawable.white_dot);
                textView.setText(title[i]+"");
            } else {
                tips[i].setBackgroundResource(R.drawable.gray_dot);
            }
        }
    }

    //设置Handler
    public static class ImageHandler extends Handler {
        protected static final int MSG_UPDATE_IMAGE = 1;
        protected static final int MSG_KEEP_SILENT = 2;
        protected static final int MSG_BREAK_SILENT = 3;
        protected static final int MSG_PAGE_CHANGED = 4;
        protected static final long MSG_DELAY = 5000;
        private WeakReference<SlideSetter> weakReference;
        public static int currentItem = 0;
        protected ImageHandler(WeakReference<SlideSetter> wk) {
            weakReference = wk;
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(LOG_TAG, "receive message" + msg.what);
            SlideSetter activity = weakReference.get();
            if (activity == null) {
                return;
            }
            if (activity.handler.hasMessages(MSG_UPDATE_IMAGE)) {
                activity.handler.removeMessages(MSG_UPDATE_IMAGE);
            }
            //根据收到的信息变换pager
            switch (msg.what) {
                case MSG_UPDATE_IMAGE:
                    currentItem++;
                    activity.viewPager.setCurrentItem(currentItem);
                    activity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_KEEP_SILENT:
                    break;
                case MSG_BREAK_SILENT:
                    activity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_PAGE_CHANGED:
                    currentItem = msg.arg1;
                    break;
                default:
                    break;
            }
        }
    }


    private void setViewPager(Context context){
        //设置Pager下方的点
        group.removeAllViews();
        for (int i = 0; i < tips.length; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            tips[i] = imageView;
            if (i == 0) {
                tips[i].setBackgroundResource(R.drawable.white_dot);
            } else {
                tips[i].setBackgroundResource(R.drawable.gray_dot);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(30,
                   30));
            layoutParams.leftMargin = 2;
            layoutParams.rightMargin = 2;
            layoutParams.bottomMargin=4;
            group.addView(imageView, layoutParams);
        }

        //设置pager的图和点击事件
        mImageViews = new ImageView[5];
        for (int i = 0; i <5; i++) {
            final int finalI = i;
            ImageView imageView = new ImageView(context);
            imageView.setImageBitmap(bitmap[i]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(AppContext.getContext(),NewsContent.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("id", id[finalI]);
                    bundle.putString("img_title", title[finalI]);
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    AppContext.getContext().startActivity(intent);

                }
            });
            mImageViews[i] = imageView;
        }

        //设置Adapter和监听事件
        viewPager.setAdapter(new SlidePagerAdapter(mImageViews));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                handler.sendMessage(Message.obtain(handler, SlideSetter.ImageHandler.MSG_PAGE_CHANGED, arg0, 0));
                setImageBackground(arg0 % mImageViews.length);
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
            @Override
            public void onPageScrollStateChanged(int arg0) {
                switch (arg0) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        handler.sendEmptyMessage(SlideSetter.ImageHandler.MSG_KEEP_SILENT);
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        handler.sendEmptyMessageDelayed(SlideSetter.ImageHandler.MSG_UPDATE_IMAGE, SlideSetter.ImageHandler.MSG_DELAY);
                        break;
                    default:
                        break;
                }
            }
        });
        viewPager.setCurrentItem(ImageHandler.currentItem);
        setImageBackground(ImageHandler.currentItem);
        handler.sendEmptyMessageDelayed(SlideSetter.ImageHandler.MSG_UPDATE_IMAGE, SlideSetter.ImageHandler.MSG_DELAY);
    }

    private Bitmap getIMG (String url){
        Bitmap bitmap=null;
        try {
            bitmap= BitmapFactory.decodeStream(new URL(url).openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return bitmap;
        }
    }
}







