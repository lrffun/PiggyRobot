package com.example.piggyrobot;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MyConnection {

    public static void getResponse(final String url,final GetConnection getConnection){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection;
                try{
                    URL Url = new URL(url);
                    connection = (HttpURLConnection) Url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(2000);
                    connection.setReadTimeout(3000);
                    connection.setDoInput(true);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    Log.e("result",response.toString());
                    if (getConnection != null) {
                        getConnection.onFinish(response.toString());
                    }else {
                        Log.e("WeiNull","WeiNull");
                    }
                }catch(IOException io) {
                    if (getConnection != null) {
                        getConnection.onError(io);
                    }
                }
            }
        }).start();

    }
}

