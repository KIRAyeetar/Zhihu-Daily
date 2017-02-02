package com.example.administrator.internet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

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
    private static final String LOG_TAG = HomePage.ACCESSIBILITY_SERVICE;
    private TextView textView;
    private ViewPager viewPager;
    private ImageView[] tips;
    private ImageView[] mImageViews;
    private GoogleApiClient client;
    private String responseDate;
    private Bitmap bitmap[];
    private String title[];
    ViewGroup group;
    private int LOAD_FINISHED=1;
    private Activity homepage;

    Handler imageHandler=new Handler(){
        public void handleMessage(Message msg){
            if(msg.what==LOAD_FINISHED){
                setViewPager(homepage);
            }
        }
    };
    @SuppressLint("NewApi")
    public void setSlideView(final Activity homepage) {
        this.homepage=homepage;
        textView = (TextView) homepage.findViewById(R.id.viewPager_text);
        group = (ViewGroup) homepage.findViewById(R.id.viewGroup);
        viewPager = (ViewPager) homepage.findViewById(R.id.viewPager);
        top_stories_URL=new String[5];
        bitmap=new Bitmap[5];
        tips = new ImageView[ top_stories_URL.length];
        title=new String[5];
        setIMGFromInternet("http://news-at.zhihu.com/api/4/news/latest");
    }

    @SuppressLint("NewApi")
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.length; i++) {
            if (i == selectItems) {
                tips[i].setBackgroundResource(R.drawable.ic_radio_button_checked_black_24dp);
                textView.setText(title[i]+"");
                Drawable drawable=new BitmapDrawable(bitmap[i]);
                viewPager.setBackground(drawable);
            } else {
                tips[i].setBackgroundResource(R.drawable.ic_radio_button2_unchecked_black_24dp);
            }
        }
    }
    private void setIMGFromInternet(final String url) {
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

    public class MyAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(mImageViews[position % mImageViews.length]);
        }
        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(mImageViews[position % mImageViews.length], 0);
            return mImageViews[position % mImageViews.length];
        }
    }
    private static class ImageHandler extends Handler {
        protected static final int MSG_UPDATE_IMAGE = 1;
        protected static final int MSG_KEEP_SILENT = 2;
        protected static final int MSG_BREAK_SILENT = 3;
        protected static final int MSG_PAGE_CHANGED = 4;
        protected static final long MSG_DELAY = 5000;
        private WeakReference<SlideSetter> weakReference;
        private int currentItem = 0;
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
    private void setViewPager(Activity context){
        for (int i = 0; i < tips.length; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
            tips[i] = imageView;
            if (i == 0) {
                tips[i].setBackgroundResource(R.drawable.ic_radio_button_checked_black_24dp);
            } else {
                tips[i].setBackgroundResource(R.drawable.ic_radio_button2_unchecked_black_24dp);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            group.addView(imageView, layoutParams);
        }
        mImageViews = new ImageView[5];
        for (int i = 0; i <top_stories_URL.length; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setImageBitmap(bitmap[i]);
            mImageViews[i] = imageView;
        }

        viewPager.setAdapter(new SlideSetter.MyAdapter());
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
        viewPager.setCurrentItem((mImageViews.length) * 100);
        handler.sendEmptyMessageDelayed(SlideSetter.ImageHandler.MSG_UPDATE_IMAGE, SlideSetter.ImageHandler.MSG_DELAY);
        client = new GoogleApiClient.Builder(context).addApi(AppIndex.API).build();
    }
}







