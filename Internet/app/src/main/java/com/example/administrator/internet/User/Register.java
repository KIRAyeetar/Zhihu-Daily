package com.example.administrator.internet.User;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.internet.R;
import com.example.administrator.internet.Start.SplashScreen;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class Register extends Activity {
    private Button toLog;
    private EditText getPassword;
    private EditText getName;
    private ImageView imageView;
    String response;
    int responseCode=1;
    private String password;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe);
        imageView=(ImageView)findViewById(R.id.register_img);
        imageView.setImageBitmap(SplashScreen.bitmap);

        toLog = (Button) findViewById(R.id.toLog_button);
        toLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                getPassword = (EditText) findViewById(R.id.password_edittext);
                getName = (EditText) findViewById(R.id.name_edittext);
                password = getPassword.getText().toString();
                name = getName.getText().toString();
                post("https://api.caoyue.com.cn/bihu/register.php","username="+name+"&password="+password);

            }
        });
    }

    public void post(final String url, final String content) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn=null ;
                try {
                    // 创建一个URL对象
                    URL mURL = new URL(url);
                    // 调用URL的openConnection()方法,获取HttpURLConnection对象
                    conn = (HttpURLConnection) mURL.openConnection();
                    conn.setRequestMethod("POST");// 设置请求方法为post
                    conn.setReadTimeout(5000);// 设置读取超时为5秒
                    conn.setConnectTimeout(10000);// 设置连接网络超时为10秒
                    conn.setDoOutput(true);// 设置此方法,允许向服务器输出内容

                    // post请求的参数
                    String data = content;
                    // 获得一个输出流,向服务器写数据,默认情况下,系统不允许向服务器输出内容
                    OutputStream out = conn.getOutputStream();// 获得一个输出流,向服务器写数据
                    out.write(data.getBytes());
                    out.flush();
                    out.close();

                    responseCode = conn.getResponseCode();// 调用此方法就不必再使用conn.connect()方法

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
                        conn.disconnect();// 关闭连接
                        getJson(response);
                    }
                }
            }
        }).start();
    }

    public void getJson(final String responseDate){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String info = null;
                try {
                    JSONArray jsonArray1=new JSONArray("["+responseDate+"]");
                    for (int i=0;i<jsonArray1.length();i++){
                        JSONObject jsonObject=jsonArray1.getJSONObject(i);
                        info=jsonObject.getString("info");
                        response=jsonObject.getString("date");
                        name=null;
                    }
                    JSONArray jsonArray2=new JSONArray("["+response+"]");
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

    private String getStringFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        is.close();
        String state = os.toString();// 把流中的数据转换成字符串,采用的编码是utf-8(模拟器默认编码)
        os.close();
        return state;
    }

    private void showResult(final String info){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(info.length()==7){
                    Toast.makeText(Register.this,"注册成功", Toast.LENGTH_LONG).show();
                    finish();
                    User.name=name;
                }else {
                    Toast.makeText(Register.this, info+" ", Toast.LENGTH_LONG).show();
                    getName.setText(null);
                    getPassword.setText(null);
                }

            }
        });
    }
}

