package com.example.ashaiaa.rofaeil.idea.MovieApp.mainClasses;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashaiaa.rofaeil.idea.MovieApp.HelperAndAdapters.Firebase_Helper;
import com.example.ashaiaa.rofaeil.idea.MovieApp.HelperAndAdapters.MovieObject;
import com.example.ashaiaa.rofaeil.idea.MovieApp.HelperAndAdapters.MyAsyncTask;
import com.example.ashaiaa.rofaeil.idea.MovieApp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class DetailFragment extends android.app.Fragment {

    StringBuilder mUrl;
    String fullUrl;

    JSONObject mDetailsOfMovie = null;
    JSONObject mVideosOfMovie = null;
    JSONObject mReviewOfMovie = null;

    int id;
    View mView;

    int user_id;
    MovieObject movieObjectFavourite;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase_usres;
    FirebaseDatabase rootDatabase ;
    private String number_of_users;


    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //get the reference to the database
//
//        if (mDatabase == null && savedInstanceState == null ) {
//            // enable offline mode with Persistence
//            rootDatabase  =  FirebaseDatabase.getInstance();
//            rootDatabase.setPersistenceEnabled(true) ;
//
//        }

        rootDatabase = Firebase_Helper.getDatabase();


        Bundle bundle = this.getArguments();
        id = Integer.parseInt(bundle.getString("id", "0"));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.detail_fragment, container, false);
        mView = view;

        Button button = (Button) view.findViewById(R.id.favorite_me);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMovieToFavourite();
            }
        });

        startBaicDetailsOfMovieRequest();
        startTrailorsOfMovieRequest();
        startReviewsOfMovieRequest();

        return view;
    }

    private void beginMyAsyncTask(String url) {

        MyAsyncTask asyncTask = new MyAsyncTask() {
            @Override
            protected void onPostExecute(String response) {

                if (mDetailsOfMovie == null) {
                    try {
                        mDetailsOfMovie = new JSONObject(response);

                    } catch (JSONException e) {
                    } finally {
                        try {
                            //set main details from Json object of movie
                            setBaicDataMovie();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (mVideosOfMovie == null) {
                    try {
                        mVideosOfMovie = new JSONObject(response);

                    } catch (JSONException e) {
                    } finally {
                        try {
                            //set main details from Json object of movie
                            setTrailorsMovie();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } else if (mReviewOfMovie == null) {
                    try {
                        mReviewOfMovie = new JSONObject(response);
                    } catch (JSONException e) {
                    } finally {
                        try {
                            //set main details from Json object of movie
                            setReviewsMovie();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        };

        //execute the async task
        asyncTask.execute(url);
    }

    private void startReviewsOfMovieRequest() {
        mUrl = new StringBuilder("http://api.themoviedb.org/3/movie/");
        mUrl.append(id + "/reviews").append("?api_key=27c5319da038dffe1e6957609d9797a0");
        fullUrl = mUrl.toString();
        beginMyAsyncTask(fullUrl);
    }

    private void startTrailorsOfMovieRequest() {
        mUrl = new StringBuilder("http://api.themoviedb.org/3/movie/");
        mUrl.append(id + "/videos").append("?api_key=27c5319da038dffe1e6957609d9797a0");
        fullUrl = mUrl.toString();
        beginMyAsyncTask(fullUrl);
    }

    private void startBaicDetailsOfMovieRequest() {
        mUrl = new StringBuilder("http://api.themoviedb.org/3/movie/");
        mUrl.append(id).append("?api_key=27c5319da038dffe1e6957609d9797a0");
        fullUrl = mUrl.toString();
        beginMyAsyncTask(fullUrl);
    }

    private void setTrailorsMovie() throws JSONException {

        JSONArray videos_array = mVideosOfMovie.getJSONArray("results");
        int numberOfTrailors = videos_array.length();


        JSONObject video_content_json = (JSONObject) videos_array.get(0);
        String key = video_content_json.getString("key");

        mUrl = new StringBuilder("https://www.youtube.com/watch?v=");
        mUrl.append(key);
        fullUrl = mUrl.toString();


        for (int i = 0; i < numberOfTrailors; i++) {


            JSONObject video_content_json_in_loop = (JSONObject) videos_array.get(i);
            String mVideoKey = video_content_json_in_loop.getString("key");



            LayoutInflater inflater = LayoutInflater.from(getActivity());
            LinearLayout linearLayout_trailor = (LinearLayout) inflater.inflate(R.layout.trailor_item, null, false);

            TextView textView2 = (TextView) linearLayout_trailor.findViewById(R.id.trailor);
            textView2.setClickable(true);
            textView2.setMovementMethod(LinkMovementMethod.getInstance());

            mUrl = new StringBuilder("https://www.youtube.com/watch?v=");
            mUrl.append(mVideoKey);
            fullUrl = mUrl.toString();

            String text2 = "<a href='" + fullUrl + "'>" + "Trailor " + (i + 1) + "</a>";
            textView2.setText(Html.fromHtml(text2));
            textView2.setTextColor(Color.parseColor("#000000"));
            textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);


            LinearLayout linearLayout = (LinearLayout) mView.findViewById(R.id.trailors_containor_of_movie);
            linearLayout.addView(linearLayout_trailor);

        }
    }

    private void setReviewsMovie() throws JSONException {


        JSONArray review_array = mReviewOfMovie.getJSONArray("results");
        int number_of_reviews = review_array.length();

        if (number_of_reviews != 0) {

            JSONObject review_content_json = (JSONObject) review_array.get(0);
            String review_content = review_content_json.getString("content");

            TextView view = (TextView) mView.findViewById(R.id.review_content);
            view.setText(review_content);
        }

        for (int i = 1; i < number_of_reviews; i++) {

            JSONObject review_content_json_in_loop = (JSONObject) review_array.get(i);
            String review_content_in_loop = review_content_json_in_loop.getString("content");

            TextView review_number = new TextView(getActivity());
            review_number.setText("Review " + (i + 1) + " :");
            review_number.setTextColor(Color.parseColor("#000000"));
            review_number.setPadding(0, 24, 0, 0);
            review_number.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            TextView textView = new TextView(getActivity());
            textView.setText(review_content_in_loop);
            textView.setTextColor(Color.parseColor("#000000"));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            LinearLayout linearLayout = (LinearLayout) mView.findViewById(R.id.reviews_container);
            linearLayout.addView(review_number);
            linearLayout.addView(textView);
        }

    }

    private void setBaicDataMovie() throws JSONException {

        String original_title = mDetailsOfMovie.getString("original_title");
        TextView view = (TextView) mView.findViewById(R.id.movie_name);
        view.setText(original_title);

        String poster_path = mDetailsOfMovie.getString("poster_path");
        ImageView imageView = (ImageView) mView.findViewById(R.id.image_poster);
        StringBuilder base_url = new StringBuilder("http://image.tmdb.org/t/p/");
        base_url.append("w185/").append(poster_path);
        String url = base_url.toString();
        Picasso.with(getActivity().getBaseContext()).load(url).placeholder(R.drawable.bad_connection).
                error(R.drawable.bad_connection).fit().tag(getActivity().getBaseContext()).into(imageView);

        String release_date = mDetailsOfMovie.getString("release_date");
        view = (TextView) mView.findViewById(R.id.date_text);
        view.setText(release_date);

        String runtime = mDetailsOfMovie.getString("runtime");
        view = (TextView) mView.findViewById(R.id.duration);
        view.setText(runtime + " " + "min.");

        String vote_average = mDetailsOfMovie.getString("vote_average");
        view = (TextView) mView.findViewById(R.id.rate_text);
        view.setText(vote_average + "/10.");

        String overview = mDetailsOfMovie.getString("overview");
        view = (TextView) mView.findViewById(R.id.overview_text);
        view.setText(overview);


    }

    public void addMovieToFavourite() {

        TextView mOriginalTitle = (TextView) mView.findViewById(R.id.movie_name);
        TextView mReleaseDate = (TextView) mView.findViewById(R.id.date_text);
        TextView mMovieRuntime = (TextView) mView.findViewById(R.id.duration);
        TextView mVoteAverage = (TextView) mView.findViewById(R.id.rate_text);
        TextView mMovieOverview = (TextView) mView.findViewById(R.id.overview_text);

        ImageView mMoviePoster = (ImageView) mView.findViewById(R.id.image_poster);
        mMoviePoster.buildDrawingCache(true);
        mMoviePoster.setDrawingCacheEnabled(true);
        mMoviePoster.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        mMoviePoster.layout(0, 0, mMoviePoster.getMeasuredWidth(), mMoviePoster.getMeasuredHeight());
        mMoviePoster.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(mMoviePoster.getDrawingCache());
        mMoviePoster.setDrawingCacheEnabled(false);

        // create movie object contain all movie info
        movieObjectFavourite = new MovieObject();
        movieObjectFavourite.setmMovieId(id);
        movieObjectFavourite.setmMovieOverview(mMovieOverview.getText().toString());
        movieObjectFavourite.setmMoviePoster(BitMapToString(bitmap));
        movieObjectFavourite.setmOriginalTitle(mOriginalTitle.getText().toString());
        movieObjectFavourite.setmReleaseDate(mReleaseDate.getText().toString());
        movieObjectFavourite.setmVoteAverage(mVoteAverage.getText().toString());
        movieObjectFavourite.setRuntime(mMovieRuntime.getText().toString());


        mDatabase = rootDatabase.getInstance().getReference("mydb");
        mDatabase.keepSynced(true);
        mDatabase_usres = rootDatabase.getInstance().getReference("mydb").child("users");


        //get shared prefernces object
        final SharedPreferences sharedPreferences = getActivity().getBaseContext().getSharedPreferences
                (getString(R.string.preference_file_name), Context.MODE_PRIVATE);

        //get value from shared Preferences
        user_id = sharedPreferences.getInt(getString(R.string.user_id), -1);

        if (user_id == -1) { //value not exist i.e. user does't have an id yet
            ValueEventListener nom_listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    number_of_users = dataSnapshot.child("number_of_users").getValue().toString();
                    if (number_of_users != null) {
                        Log.v("number_of_users", number_of_users);
                    } else {
                        Log.v("number_of_users", "no Data Returned");
                    }
                    //write value to shared prefernces
                    SharedPreferences.Editor mySharedPreferencesEditor = sharedPreferences.edit();
                    mySharedPreferencesEditor.putInt(getString(R.string.user_id), Integer.parseInt(number_of_users) + 1);
                    mySharedPreferencesEditor.commit();
                    user_id = Integer.parseInt(number_of_users) + 1;

                    ArrayList<MovieObject> movieObjectArrayList = new ArrayList<>();
                    movieObjectArrayList.add(movieObjectFavourite);
                    mDatabase_usres.child(Integer.toString(user_id)).setValue(movieObjectArrayList);

                    mDatabase.child("number_of_users").setValue(Integer.toString(user_id)) ;

                    Toast.makeText(getActivity(), "Saved to Favourite", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mDatabase.addListenerForSingleValueEvent(nom_listener);

        } else {
            ValueEventListener nom_listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    long child_count = dataSnapshot.child(Integer.toString(user_id)).getChildrenCount();

                    mDatabase_usres.child(Integer.toString(user_id)).child(Long.toString(child_count))
                            .setValue(movieObjectFavourite);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mDatabase_usres.addListenerForSingleValueEvent(nom_listener);

            Toast.makeText(getActivity(), "Saved to Favourite", Toast.LENGTH_LONG).show();
        }

    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

}
