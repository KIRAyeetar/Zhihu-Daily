package com.example.administrator.internet.Comment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.internet.R;
import com.example.administrator.internet.ToolClass.GetTime;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/2/20 0020.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<Comment> commentsList=new ArrayList();
    public static final int ITEM_HEADER = 0;
    public static final int ITEM_CONTENT = 1;
    int viewType;

    public CommentAdapter(List<Comment> commentsList){
        this.commentsList=commentsList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView author;
        private TextView time;
        private CircleImageView avatar;
        private TextView content;
        private TextView likes;
        private TextView headerNum;
        View simpleCommentView;
        public ViewHolder(View view) {
            super(view);
            author=(TextView) view.findViewById(R.id.author);
            time=(TextView) view.findViewById(R.id.comment_time);
            avatar=(CircleImageView) view.findViewById(R.id.comment_author_img);
            content=(TextView) view.findViewById(R.id.comment_text);
            likes=(TextView) view.findViewById(R.id.good_text);
            headerNum=(TextView) view.findViewById(R.id.comment_num_text);
            simpleCommentView = view;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.viewType=viewType;
        if (viewType == ITEM_HEADER) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_header,parent,false);
            CommentAdapter.ViewHolder holder=new CommentAdapter.ViewHolder(view);
            return holder;
        }
        else{
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_comment,parent,false);
            CommentAdapter.ViewHolder holder=new CommentAdapter.ViewHolder(view);
            return holder;
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment comment=getItem(position);
        if (viewType!=ITEM_HEADER&&position>0) {
            holder.author.setText(comment.getAuthor());
            holder.avatar.setImageBitmap(comment.getAvatar());
            holder.content.setText(comment.getContent());
            holder.likes.setText(comment.getLikes());
            holder.time.setText(GetTime.getDateToString(comment.getTime()));
        }else {
            if(CommentContent.getType()=="long")
                holder.headerNum.setText("共"+CommentContent.getLong_comments_num()+"条长评");
            else
                holder.headerNum.setText("共"+CommentContent.getShort_comments_num()+"条短评");
        }
    }
    @Override
    public int getItemCount() {
        return commentsList.size()+1;
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
    private Comment getItem(int position) {
        if(position>0){
            return commentsList.get(position - 1);
        }
        return null;
    }
}
