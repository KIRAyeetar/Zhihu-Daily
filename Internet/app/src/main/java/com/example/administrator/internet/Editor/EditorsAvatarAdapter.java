package com.example.administrator.internet.Editor;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.internet.ToolClass.AppContext;
import com.example.administrator.internet.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/2/21 0021.
 */

public class EditorsAvatarAdapter extends RecyclerView.Adapter<EditorsAvatarAdapter.ViewHolder> {
    private List <Editors>editorsList=new ArrayList<>();

    public EditorsAvatarAdapter(List editorsList){
        this.editorsList=editorsList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatar;
        View simpleAvatar;
        public ViewHolder(View itemView) {
            super(itemView);
            simpleAvatar=itemView;
            avatar=(CircleImageView) itemView.findViewById(R.id.editor_avatar_img);
        }
    }

    @Override
    public EditorsAvatarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_editor_avatar,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(EditorsAvatarAdapter.ViewHolder holder, int position) {
        holder.avatar.setImageBitmap(editorsList.get(position).getAvatar());
        holder.simpleAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AppContext.getContext(),EditorsContent.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                AppContext.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return editorsList.size();
    }
}
