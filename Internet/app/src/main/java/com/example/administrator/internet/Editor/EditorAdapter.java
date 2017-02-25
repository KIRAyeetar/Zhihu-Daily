package com.example.administrator.internet.Editor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.internet.ToolClass.AppContext;
import com.example.administrator.internet.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/2/22 0022.
 */

public class EditorAdapter extends RecyclerView.Adapter<EditorAdapter.ViewHolder> {
    private List<Editors>mEditorList=new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView bio;
        private CircleImageView avatar;
        private View simpleView;
        public ViewHolder(View itemView) {
            super(itemView);
            simpleView=itemView;
            name=(TextView) itemView.findViewById(R.id.editor_name);
            bio=(TextView) itemView.findViewById(R.id.editor_bio);
            avatar=(CircleImageView) itemView.findViewById(R.id.editor_avatar);
        }
    }
    public EditorAdapter(List<Editors> mEditorList){
        this.mEditorList=mEditorList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_editor,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(EditorAdapter.ViewHolder holder, final int position) {
        holder.avatar.setImageBitmap(mEditorList.get(position).getAvatar());
        holder.name.setText(mEditorList.get(position).getName());
        holder.bio.setText(mEditorList.get(position).getBio());
        holder.simpleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AppContext.getContext(),EditorWebView.class);
                Bundle bundle=new Bundle();
                bundle.putString("name", mEditorList.get(position).getName());
                bundle.putString("url",  mEditorList.get(position).getUrl());
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                AppContext.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEditorList.size();
    }
}
