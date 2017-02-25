package com.example.administrator.internet.Editor;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.administrator.internet.R;
import com.example.administrator.internet.Theme.ThemeSetter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/22 0022.
 */

public class EditorsContent extends Activity{
    private List<Editors> mEditorList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_layout);
        mEditorList= ThemeSetter.getEditorList();


        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.editor_rc);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        EditorAdapter adapter=new EditorAdapter(mEditorList);
        recyclerView.setAdapter(adapter);

        if(mEditorList==null){
            finish();
        }

        Button back=(Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
