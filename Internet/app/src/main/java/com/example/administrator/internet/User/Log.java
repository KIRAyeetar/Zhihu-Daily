package com.example.administrator.internet.User;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.internet.Menu.MenuSetter;
import com.example.administrator.internet.R;
import com.example.administrator.internet.ToolClass.AppContext;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Log extends Activity {
    private Button toRegiste;
    private Button back;
    String response;
    int responseCode=1;
    Button logIn;
    private EditText getPassword;
    private EditText getName;
    private String password;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        logIn=(Button)findViewById(R.id.login_button);
        back=(Button) findViewById(R.id.back_button);
        toRegiste=(Button) findViewById(R.id.Registe_button);
        toRegiste.setOnClickListener(new View.OnClickListener(){
           public void onClick(View v){
               Intent i=new Intent(Log.this,Register.class);
               startActivity(i);
            }
        });

        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPassword = (EditText) findViewById(R.id.log_password);
                getName = (EditText) findViewById(R.id.log_name);
                password = getPassword.getText().toString();
                name = getName.getText().toString();
                post("https://api.caoyue.com.cn/bihu/login.php","username="+name+"&password="+password);
            }
        });
    }

    public void post(final String url, final String content) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn=null ;
                try {
                    URL mURL = new URL(url);
                    conn = (HttpURLConnection) mURL.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(10000);
                    conn.setDoOutput(true);
                    String data = content;
                    OutputStream out = conn.getOutputStream();
                    out.write(data.getBytes());
                    out.flush();
                    out.close();
                    responseCode = conn.getResponseCode();
                    if (responseCode == 200) {
                        InputStream is = conn.getInputStream();
                        response = getStringFromInputStream(is);
                    } else {
                        throw new NetworkErrorException("response status is "+responseCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                        getJson(response);
                    }
                }
            }
        }).start();
    }

    private String getStringFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        is.close();
        String state = os.toString();
        os.close();
        return state;
    }

    public void getJson(final String responseDate){
        new Thread(new Runnable() {

            @Override
            public void run() {
                String info = null;
                String date=null;
                try {
                    JSONArray jsonArray1=new JSONArray("["+responseDate+"]");
                    for (int i=0;i<jsonArray1.length();i++){
                        JSONObject jsonObject=jsonArray1.getJSONObject(i);
                        info=jsonObject.getString("info");
                        date=jsonObject.getString("date");
                        name=null;
                    }
                    JSONArray jsonArray2=new JSONArray("["+date+"]");
                    for (int i=0;i<jsonArray2.length();i++){
                        JSONObject jsonObject=jsonArray1.getJSONObject(i);
                        name=jsonObject.getString("username");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    showResult(info);
                }
            }
        }).start();
    }

    private void showResult(final String info){
        final String success="success";
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //info=="success"老是等不起来
                if(info.length()==7){
                    Toast.makeText(Log.this,"登录成功", Toast.LENGTH_LONG).show();
                    finish();
                    MenuSetter menuSetter=new MenuSetter();
                    menuSetter.getMenuList(AppContext.activity,0);
                    User.name=name;

                }else {
                    Toast.makeText(Log.this, info+" ", Toast.LENGTH_LONG).show();
                    getName.setText(null);
                    getPassword.setText(null);
                }
            }
        });
    }
}



