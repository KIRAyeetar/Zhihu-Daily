package com.example.administrator.internet.News;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.internet.Comment.CommentContent;
import com.example.administrator.internet.R;
import com.example.administrator.internet.Theme.ThemeSetter;
import com.example.administrator.internet.ToolClass.AppContext;
import com.example.administrator.internet.ToolClass.JSONGetter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

public class NewsContent extends Activity {
    String responseDate;
    String body;
    String IMG_URL;
    String image_source;
    String title;
    String img_title;
    String css;
    String id;
    String good;
    String talk;
    ImageView imageView;
    TextView imgRes;
    Bitmap bitmap;
    TextView talk_text;
    TextView good_text;
    TextView imgTitle;
    Button comment;
    Button back;
    Button share;
    String share_url;
    String long_comments;
    String short_comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_layout);
        Bundle bundle=getIntent().getExtras();
        this.id=bundle.getString("id");
        this.img_title=bundle.getString("img_title");

        comment=(Button)findViewById(R.id.comment_button);
        talk_text=(TextView)findViewById(R.id.talk_text);
        good_text=(TextView)findViewById(R.id.good_text);
        share=(Button)findViewById(R.id.share_button);
        imageView=(ImageView)findViewById(R.id.fun);
        imgTitle=(TextView)findViewById(R.id.title_text);
        imgRes=(TextView)findViewById(R.id.img_res);
        imgTitle.setText(img_title);

        //获取额外信息并设置界面
        getJson(id);

        //设置返回键
        back=(Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getJson(final String id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONGetter jsonGetter=new JSONGetter();
                responseDate=jsonGetter.getResponseDate("http://news-at.zhihu.com/api/4/news/"+id);
                try {
                    JSONArray jsonArray=new JSONArray("["+responseDate+"]");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        body=jsonObject.getString("body");
                        title=jsonObject.getString("title");
                        css=jsonObject.getString("css");
                        share_url=jsonObject.getString("share_url");
                        if(jsonObject.has("image")){
                            IMG_URL=jsonObject.getString("image");
                            bitmap=BitmapFactory.decodeStream(new URL(IMG_URL).openStream());
                        }
                        if(jsonObject.has("image_source")){
                            image_source=jsonObject.getString("image_source");
                        }
                    }
                    responseDate=jsonGetter.getResponseDate("http://news-at.zhihu.com/api/4/story-extra/"+id);
                    jsonArray=new JSONArray("["+responseDate+"]");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        long_comments=jsonObject.getString("long_comments");
                        short_comments=jsonObject.getString("short_comments");
                        good=jsonObject.getString("popularity");
                        talk=jsonObject.getString("comments");
                    }
                    setWebView(body,good,talk,image_source,css.substring(2,css.length()-2));
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                }
            }
        }).start();
    }

    private void setWebView(final String body,final String good,final String talk,final  String image_source,final String css){

        //主新闻界面
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WebView webView=(WebView) findViewById(R.id.web_view);
                String linkCss = "<LINK REL=\"stylesheet\" HREF=\""+css+"\" TYPE=\"text/css\">";
                String body1 = "<html><head> " + linkCss + "</head><body>" + body + " </body> </html>";
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                webView.setWebViewClient(new WebViewClient());
                webView.loadDataWithBaseURL("about:blank", body1, "text/html", "utf-8", null);
                imgRes.setText(image_source);
                talk_text.setText(talk);
                good_text.setText(good);
                if(bitmap==null){
                    imageView.setImageBitmap(ThemeSetter.getBitmap());
                }else {
                    imageView.setImageBitmap(bitmap);
                }

                //分享提示
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        cm.setText(share_url);
                        Toast.makeText(NewsContent.this, "链接复制成功喽，可以发给朋友们了", Toast.LENGTH_LONG).show();
                    }
                });

                //跳入评论界面
                comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommentContent.TYPE="short";
                        Intent intent=new Intent(AppContext.getContext(),CommentContent.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("id", id);
                        bundle.putString("comment_num", talk);
                        bundle.putString("long_comment_num", long_comments);
                        bundle.putString("short_comment_num", short_comments);
                        intent.putExtras(bundle);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        NewsContent.this.startActivity(intent);
                    }
                });

            }
        });
    }
}
