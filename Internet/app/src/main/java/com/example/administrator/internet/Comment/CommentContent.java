package com.example.administrator.internet.Comment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.internet.R;

public class CommentContent extends Activity {
    String id;
    String comment_num;
    public static String TYPE="short";
    static String long_comments;
    static String short_comments;
    Button back;
    TextView comment_num_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final Bundle bundle=getIntent().getExtras();
        this.id=bundle.getString("id");
        comment_num=bundle.getString("comment_num");
        long_comments=bundle.getString("long_comment_num");
        short_comments=bundle.getString("short_comment_num");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_layout);

        //设置评论
        CommentSetter commentSetter=new CommentSetter();
        commentSetter.setIMGFromInternet(id,this);

        //设置标题
        back=(Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        comment_num_text=(TextView) findViewById(R.id.comment_num_text);
        comment_num_text.setText("共"+comment_num+"条点评");

        //设置长短评论交换
        final Button button=(Button)findViewById(R.id.change_comment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TYPE=="long"){
                    TYPE="short";
                    button.setText("点击查看长评");
                    CommentSetter commentSetter=new CommentSetter();
                    commentSetter.setIMGFromInternet(id,CommentContent.this);
                }else {
                    TYPE="long";
                    button.setText("点击查看短评");
                    CommentSetter commentSetter=new CommentSetter();
                    commentSetter.setIMGFromInternet(id,CommentContent.this);
                }
            }
        });
    }
    public static String getLong_comments_num(){
        return long_comments;
    }
    public static String getShort_comments_num(){
        if(short_comments==null)
            return "0";
        return short_comments;
    }
    public static String getType(){return TYPE;}
}
