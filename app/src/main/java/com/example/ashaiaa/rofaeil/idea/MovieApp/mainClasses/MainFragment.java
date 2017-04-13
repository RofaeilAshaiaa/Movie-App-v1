package com.example.ashaiaa.rofaeil.idea.MovieApp.mainClasses;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.ashaiaa.rofaeil.idea.MovieApp.HelperAndAdapters.Firebase_Helper;
import com.example.ashaiaa.rofaeil.idea.MovieApp.HelperAndAdapters.MovieObject;
import com.example.ashaiaa.rofaeil.idea.MovieApp.HelperAndAdapters.MyAsyncTask;
import com.example.ashaiaa.rofaeil.idea.MovieApp.HelperAndAdapters.MyImageAdapter;
import com.example.ashaiaa.rofaeil.idea.MovieApp.HelperAndAdapters.MyImageAdapterFavourite;
import com.example.ashaiaa.rofaeil.idea.MovieApp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private String[] mURL_array =
                        {"http://api.themoviedb.org/3/movie/popular?api_key=27c5319da038dffe1e6957609d9797a0",
                         "http://api.themoviedb.org/3/movie/top_rated?api_key=27c5319da038dffe1e6957609d9797a0"};
    private ArrayList<MovieObject> mMoviesList;
    private GridView mGridview;
    private  String mResponse;
    private Context mContext;

    private  int user_id;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase_usres;
    FirebaseDatabase rootDatabase ;
    private  ArrayList<MovieObject> movieObjectArrayList_in_loop ;

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

//
//        if (mDatabase == null && savedInstanceState == null ) {
//            // enable offline mode with Persistence
//            rootDatabase  =  FirebaseDatabase.getInstance();
//            rootDatabase.setPersistenceEnabled(true) ;
//
//        }

        rootDatabase = Firebase_Helper.getDatabase();

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.main_fragment, container, false);

        mContext = mView.getContext();
        mGridview = (GridView) mView.findViewById(R.id.gridview);

        SetClickLisenerOnGridView();

        setContentGridView();

        setHasOptionsMenu(true);

        return mView;
    }


    //chech what is last selected item form menu and set the content of the gridview according to that
    private void setContentGridView() {

        Context mContext = getActivity().getApplicationContext();
        SharedPreferences mSharedPref = mContext.getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE);

        int last_selected = mSharedPref.getInt(getString(R.string.last_selected), 199);

        if (last_selected == 99) {
            ExtractMoviesDB();
        }else {

            if (!isNetworkAvailable()) {
                Toast.makeText(getActivity(), "Check Your Internet Connection :(", Toast.LENGTH_LONG).show();
            } else {

                if (last_selected == 33) {
                    beginMyAsyncTask(mURL_array[0]);

                } else if (last_selected == 66) {
                    beginMyAsyncTask(mURL_array[1]);

                } else {
                    beginMyAsyncTask(mURL_array[0]);
                }
            }
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

                   setFavouriteMovies(movieObjectArrayList_in_loop);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mDatabase_usres.addListenerForSingleValueEvent(listener);
        }


    }

    //check if network avalible and connected to one
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void SetClickLisenerOnGridView() {
        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int movie_position, long id) {

                if ( isNetworkAvailable()) {
                    int mMovie_id = mMoviesList.get(movie_position).getmMovieId();

                    //check which layout loaded and decide according to that
                    if (getActivity().findViewById(R.id.detail_fragment_container) != null) {

                        DetailFragment fragment = new DetailFragment();

                        Bundle args = new Bundle();
                        args.putString("id", Integer.toString(mMovie_id));
                        fragment.setArguments(args);

                        getFragmentManager().beginTransaction()
                                .replace(R.id.detail_fragment_container, fragment)
                                .commit();

                    } else {
                        Intent intent = new Intent(getActivity(), DetailAcivity.class);
                        intent.putExtra("id", mMovie_id);
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(getActivity(), "No Internet Conection !", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // takes jsonarray of movies and extract each movies in array list then return the result
    private ArrayList<MovieObject> getMovieData(JSONArray movie_jsonArray) throws JSONException {
        int ctr = movie_jsonArray.length();
        ArrayList<MovieObject> moviesListInMethod = new ArrayList<>(ctr);
        //ArrayList<MovieObject> movies_ArrayList_in_method = new ArrayList<>();

        for (int i = 0; i < ctr; i++) {
            MovieObject mMovie = new MovieObject();

            JSONObject jsonObject_of_movie = movie_jsonArray.getJSONObject(i);
            mMovie.setmMoviePoster(jsonObject_of_movie.getString("poster_path"));
            mMovie.setmMovieId(jsonObject_of_movie.getInt("id"));
            moviesListInMethod.add(mMovie);
        }
        return moviesListInMethod;
    }

    private void setFavouriteMovies(final ArrayList <MovieObject> movieObjectArrayList) {

        mMoviesList =  movieObjectArrayList;

        mGridview.setAdapter(new MyImageAdapterFavourite(getActivity(), mMoviesList, getActivity()));

        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                MovieObject movieObject = mMoviesList.get(position) ;

                if (getActivity().findViewById(R.id.detail_fragment_container) != null) {

                    FavouriteFragment favouriteFragment = new FavouriteFragment();

                    Bundle args = new Bundle();
                    args.putString("mMovieId", Integer.toString( movieObject.getmMovieId() ) );

//                    args.putString("mMoviePoster", movieObject.getmMoviePoster());
//                    args.putString("mMovieOverview", movieObject.getmMovieOverview());
//                    args.putString("mReleaseDate", movieObject.getmReleaseDate());
//                    args.putString("mMovieRuntime",movieObject.getRuntime() );
//                    args.putString("mOriginalTitle", movieObject.getmOriginalTitle());
//                    args.putString("mVoteAverage", movieObject.getmVoteAverage());

                    favouriteFragment.setArguments(args);

                    getFragmentManager().beginTransaction()
                            .replace(R.id.detail_fragment_container, favouriteFragment)
                            .commit();
                } else {

                    Intent intent = new Intent(getActivity(), FavouriteAcivity.class);
                   intent.putExtra("mMovieId", Integer.toString( movieObject.getmMovieId() ) );
//
//                   intent.putExtra("mMoviePoster", movieObject.getmMoviePoster());
//                   intent.putExtra("mMovieOverview", movieObject.getmMovieOverview());
//                   intent.putExtra("mReleaseDate", movieObject.getmReleaseDate());
//                   intent.putExtra("mMovieRuntime",movieObject.getRuntime() );
//                   intent.putExtra("mOriginalTitle", movieObject.getmOriginalTitle());
//                   intent.putExtra("mVoteAverage", movieObject.getmVoteAverage());

                    startActivity(intent);
                }
            }
        });
    }

    public void beginMyAsyncTask(String url) {

        MyAsyncTask myAsyncTask = new MyAsyncTask() {
            @Override
            protected void onPostExecute(String responseJSON) {
                mResponse = responseJSON;

                JSONObject mJsonObject;
                try {
                    mJsonObject = new JSONObject(mResponse);
                    JSONArray mMoviesJsonArray = mJsonObject.getJSONArray("results");
                    mMoviesList = getMovieData(mMoviesJsonArray);

                } catch (JSONException e) {
                }

                mGridview.setAdapter(new MyImageAdapter(mContext, mMoviesList, getActivity()));
                SetClickLisenerOnGridView();


            }
        };

        //execute the async task
        myAsyncTask.execute(url);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Context context = getActivity().getApplicationContext();

        SharedPreferences sharedPreferences = context.getSharedPreferences
                (getString(R.string.preference_file_name), Context.MODE_PRIVATE);

        SharedPreferences.Editor mySharedPreferencesEditor = sharedPreferences.edit();

        switch (item.getItemId()) {
            case R.id.popular_id:

                if (!isNetworkAvailable()) {
                    Toast.makeText(getActivity(), "Check Your Internet Connection :(", Toast.LENGTH_LONG).show();
                } else {
                    mySharedPreferencesEditor.putInt(getString(R.string.last_selected), 33);
                    mySharedPreferencesEditor.commit();
                    beginMyAsyncTask(mURL_array[0]);
                }

                return true;
            case R.id.top_rated_id:

                if (!isNetworkAvailable()) {
                    Toast.makeText(getActivity(), "No Internet Conection !", Toast.LENGTH_LONG).show();
                } else {
                    mySharedPreferencesEditor.putInt(getString(R.string.last_selected), 66);
                    mySharedPreferencesEditor.commit();
                    beginMyAsyncTask(mURL_array[1]);
                }
                return true;
            case R.id.favourites:

                    mySharedPreferencesEditor.putInt(getString(R.string.last_selected), 99);
                    mySharedPreferencesEditor.commit();
                    ExtractMoviesDB();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
