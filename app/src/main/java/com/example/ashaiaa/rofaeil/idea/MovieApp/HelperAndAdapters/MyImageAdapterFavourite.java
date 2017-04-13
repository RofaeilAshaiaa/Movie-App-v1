package com.example.ashaiaa.rofaeil.idea.MovieApp.HelperAndAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyImageAdapterFavourite extends BaseAdapter {
    private final Context mContext;
    private  Activity mActivity ;
    private ArrayList<MovieObject> movies_ArrayList;
    private int numberOfMovies;

    public MyImageAdapterFavourite(Context c, ArrayList<MovieObject> list, Activity activity) {
        mContext = c;
        this.movies_ArrayList = list;
        mActivity = activity ;
        if (list != null){
            numberOfMovies = list.size();
        }else{
            Toast.makeText(mContext, "No Movies Added to Favourites", Toast.LENGTH_SHORT).show();
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

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);

            DisplayMetrics displaymetrics = new DisplayMetrics();
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int screenhight = displaymetrics.heightPixels;
            int targetY = 2 * (screenhight /5) ;
            Log.v("targetY" , targetY+"");


            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, targetY));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        } else {
            imageView = (ImageView) convertView;
        }

        String poster = movies_ArrayList.get(position).getmMoviePoster();
        imageView.setImageBitmap(StringToBitMap(poster));


        return imageView;
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}