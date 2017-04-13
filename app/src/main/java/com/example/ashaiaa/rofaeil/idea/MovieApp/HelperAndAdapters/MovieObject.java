package com.example.ashaiaa.rofaeil.idea.MovieApp.HelperAndAdapters;


public class MovieObject {

    private int mMovieId;
    private String mMoviePoster;
    private String mMovieOverview;
    private String mReleaseDate;
    private String mMovieRuntime;
    private String mOriginalTitle;
    private String mVoteAverage;

    public String getmMoviePoster() {
        return mMoviePoster;
    }

    public void setmMoviePoster(String mMoviePoster) {
        this.mMoviePoster = mMoviePoster;
    }

    public String getRuntime() {
        return mMovieRuntime;
    }

    public void setRuntime(String runtime) {
        this.mMovieRuntime = runtime;
    }

    public String getmMovieOverview() {
        return mMovieOverview;
    }

    public void setmMovieOverview(String mMovieOverview) {
        this.mMovieOverview = mMovieOverview;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public int getmMovieId() {
        return mMovieId;
    }

    public void setmMovieId(int mMovieId) {
        this.mMovieId = mMovieId;
    }

    public String getmOriginalTitle() {
        return mOriginalTitle;
    }

    public void setmOriginalTitle(String mOriginalTitle) {
        this.mOriginalTitle = mOriginalTitle;
    }


    public String getmVoteAverage() {
        return mVoteAverage;
    }

    public void setmVoteAverage(String mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    public MovieObject() {
    }

    public MovieObject(int mMovieId, String mMoviePoster, String mMovieOverview, String mReleaseDate,
                       String mMovieRuntime, String mOriginalTitle, String mVoteAverage) {
        this.mMovieId = mMovieId;
        this.mMoviePoster = mMoviePoster;
        this.mMovieOverview = mMovieOverview;
        this.mReleaseDate = mReleaseDate;
        this.mMovieRuntime = mMovieRuntime;
        this.mOriginalTitle = mOriginalTitle;
        this.mVoteAverage = mVoteAverage;
    }
}
