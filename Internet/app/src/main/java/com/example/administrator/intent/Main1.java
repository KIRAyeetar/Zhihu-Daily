package com.example.administrator.intent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        set();
    }

    public void set(){
        Button button=(Button)findViewById(R.id.one);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AppContext.getContext(),Main2.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                AppContext.getContext().startActivity(intent);
                Main1.this.finish();
            }
        });
    }
}
