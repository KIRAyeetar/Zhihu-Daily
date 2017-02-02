package com.example.administrator.internet;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JSONGetter {
    private String responseDate;
    public String getResponseDate(final String url) {

        try {
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            responseDate = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return responseDate;
        }
    }
}
