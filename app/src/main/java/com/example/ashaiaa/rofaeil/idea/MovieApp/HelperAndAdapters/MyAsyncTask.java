package com.example.ashaiaa.rofaeil.idea.MovieApp.HelperAndAdapters;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyAsyncTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... url) {

        OkHttpClient client = new OkHttpClient();

        String mJsonResponse = new String();

        Request request = new Request.Builder().url(url[0]).build();

        try {
            Response response = client.newCall(request).execute();
            mJsonResponse = response.body().string();
        } catch (IOException e) {
        }

        return mJsonResponse;
    }

}