package com.example.ashaiaa.rofaeil.idea.MovieApp.mainClasses;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.ashaiaa.rofaeil.idea.MovieApp.R;

public class MainActivity extends AppCompatActivity {

//     public  static boolean calledAlready = false;

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        FirebaseDatabase rootDatabase ;
//        if ( ! calledAlready ) {
//            // enable offline mode with Persistence
//            rootDatabase  =  FirebaseDatabase.getInstance();
//            rootDatabase.setPersistenceEnabled(true) ;
//            MainActivity.calledAlready = true ;
//
//        }
        setContentView(R.layout.main_activity);




    }

}


