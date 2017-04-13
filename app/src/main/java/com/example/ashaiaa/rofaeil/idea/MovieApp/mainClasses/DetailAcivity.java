package com.example.ashaiaa.rofaeil.idea.MovieApp.mainClasses;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.ashaiaa.rofaeil.idea.MovieApp.R;

public class DetailAcivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_acivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        FirebaseDatabase rootDatabase ;
//        if ( ! MainActivity.calledAlready ) {
//            // enable offline mode with Persistence
//            rootDatabase  =  FirebaseDatabase.getInstance();
//            rootDatabase.setPersistenceEnabled(true) ;
//            MainActivity.calledAlready = true ;
//
//        }

        int id = getIntent().getExtras().getInt("id");

        DetailFragment fragment = new DetailFragment() ;

        Bundle args = new Bundle();
        args.putString("id", Integer.toString(id) );
        fragment.setArguments(args);

        getFragmentManager()
                .beginTransaction()
                .add( R.id.container_detail_acivity,fragment)
                .commit();


    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

}
