package com.example.ashaiaa.rofaeil.idea.MovieApp.HelperAndAdapters;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ashaiaa.rofaeil.idea.MovieApp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyImageAdapter extends BaseAdapter {
    private final Context mContext;
    private  Activity mActivity ;
    private ArrayList<MovieObject> mMoviesArrayList;
    private int numberOfMovies;

    public MyImageAdapter(Context c, ArrayList<MovieObject> list, Activity activity) {
        mContext = c;
        this.mMoviesArrayList = list;
        mActivity = activity ;

        if (list != null){
        numberOfMovies = list.size();
        }else{
            Toast.makeText(mContext, "Check Your Inernet Connection", Toast.LENGTH_SHORT).show();
            numberOfMovies = 0 ;
        }
    }

    public int getCount() {
        return numberOfMovies;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);


            DisplayMetrics displaymetrics = new DisplayMetrics();
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int screenhight = displaymetrics.heightPixels;
            int targetY = 2 * (screenhight /5) ;
            Log.v("targetY" , targetY+"");

            imageView.setLayoutParams(
                    new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, targetY));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }


        StringBuilder mURI = new StringBuilder("http://image.tmdb.org/t/p/");
        String mMoviePoster = mMoviesArrayList.get(position).getmMoviePoster();
        mURI.append("w185/").append(mMoviePoster);
        String fullUrl = mURI.toString();

        Log.v("fullUrl", fullUrl);


        Picasso.with(mContext).load(fullUrl).placeholder(R.drawable.ic_sync_problem_black_24dp)
                .error(R.drawable.bad_connection)
                .fit().tag(mContext).into(imageView);

        return imageView;
    }


}