package com.example.ashaiaa.rofaeil.idea.MovieApp.mainClasses;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.ashaiaa.rofaeil.idea.MovieApp.HelperAndAdapters.MovieObject;
import com.example.ashaiaa.rofaeil.idea.MovieApp.R;

public class FavouriteAcivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_detail_acivity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String mMovieId = getIntent().getStringExtra("mMovieId");


        FavouriteFragment favouriteFragment = new FavouriteFragment();
        Bundle args = new Bundle();


        args.putString("mMovieId",mMovieId);
        favouriteFragment.setArguments(args);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.favorite_acivity_container, favouriteFragment)
                .commit();
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


}
