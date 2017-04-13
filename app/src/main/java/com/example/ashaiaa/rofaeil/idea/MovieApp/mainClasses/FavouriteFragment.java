package com.example.ashaiaa.rofaeil.idea.MovieApp.mainClasses;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ashaiaa.rofaeil.idea.MovieApp.HelperAndAdapters.Firebase_Helper;
import com.example.ashaiaa.rofaeil.idea.MovieApp.HelperAndAdapters.MovieObject;
import com.example.ashaiaa.rofaeil.idea.MovieApp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavouriteFragment extends android.app.Fragment {

     private MovieObject mMovieObject ;
     private View mView ;
    Bundle bundle ;

    private  int user_id;
    private int mMovieId ;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase_usres;
    FirebaseDatabase rootDatabase ;
    private  ArrayList<MovieObject> movieObjectArrayList_in_loop ;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootDatabase = Firebase_Helper.getDatabase();

        mMovieObject = new MovieObject() ;

         bundle = this.getArguments();


    }

    private void getMovieObjectFromArraylistMovies() {

        int number_of_movies = movieObjectArrayList_in_loop.size();

        for (int i = 0 ; i<number_of_movies ; i++) {
            if (mMovieId == movieObjectArrayList_in_loop.get(i).getmMovieId() ){
                mMovieObject = movieObjectArrayList_in_loop.get(i) ;
            }
        }


        TextView textView1 = (TextView) mView.findViewById(R.id.movie_name);
        textView1.setText(mMovieObject.getmOriginalTitle());
        TextView textView2 = (TextView) mView.findViewById(R.id.date_text);
        textView2.setText(mMovieObject.getmReleaseDate());
        TextView textView3 = (TextView) mView.findViewById(R.id.duration);
        textView3.setText(mMovieObject.getRuntime());
        TextView textView4 = (TextView) mView.findViewById(R.id.rate_text);
        textView4.setText(mMovieObject.getmVoteAverage());
        TextView textView5 = (TextView) mView.findViewById(R.id.overview_text);
        textView5.setText(mMovieObject.getmMovieOverview());
        ImageView imageView = (ImageView) mView.findViewById(R.id.image_poster);
        imageView.setImageBitmap(StringToBitMap(mMovieObject.getmMoviePoster()));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        View view =  inflater.inflate(R.layout.favourite_fragment, container, false);
        mView = view ;

        ExtractMoviesDB();


        return mView ;
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

    private void ExtractMoviesDB() {

        movieObjectArrayList_in_loop = new ArrayList<>();

        mDatabase = rootDatabase.getInstance().getReference("mydb");
        mDatabase.keepSynced(true);

        mDatabase_usres = rootDatabase.getInstance().getReference("mydb").child("users");
        mDatabase_usres.keepSynced(true);

        //get shared prefernces object
        final SharedPreferences sharedPreferences = getActivity().getBaseContext().getSharedPreferences
                (getString(R.string.preference_file_name), Context.MODE_PRIVATE);

        //get value from shared prefernces
        user_id = sharedPreferences.getInt(getString(R.string.user_id), -1);

        if (user_id != -1) { //value not exist i.e. user does't exist in DB

            ValueEventListener listener = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    long child_count = dataSnapshot.child(Integer.toString(user_id)).getChildrenCount();

                    for (int i = 0 ;i < child_count ; i++ ){

                        MovieObject movieObject = new MovieObject() ;
                        movieObject =  dataSnapshot.child(Integer.toString(user_id))
                                .child(Integer.toString(i))
                                .getValue(MovieObject.class);

                        movieObjectArrayList_in_loop.add(movieObject) ;

                    }
                    movieObjectArrayList_in_loop = movieObjectArrayList_in_loop;


                    mMovieId = Integer.parseInt(  bundle.getString("mMovieId")  );

                    getMovieObjectFromArraylistMovies();



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mDatabase_usres.addListenerForSingleValueEvent(listener);
        }


    }

}
