package com.example.android.phase2;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.android.phase2.MainActivityFragment;
import com.example.android.phase2.Movie;

import java.util.ArrayList;
import java.util.List;

public class FetchFavoriteMoviesTask extends AsyncTask<Void, Void, List<Movie>> {

    private Context mContext;

    //private MovieGridAdapter movieGridAdapter;


    //private ArrayList<Movie> movies = null;

        public FetchFavoriteMoviesTask(Context context) {
            mContext = context;
        }

        private List<Movie> getFavoriteMoviesDataFromCursor(Cursor cursor) {
            List<Movie> results = new ArrayList<>();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Movie movie = new Movie(cursor);
                    results.add(movie);
                } while (cursor.moveToNext());
                cursor.close();
            }
            return results;
        }

        @Override
        protected List<Movie> doInBackground(Void... params) {
            Cursor cursor = mContext.getContentResolver().query(
                    MovieContract.MovieEntry.CONTENT_URI,
                    MOVIE_COLUMNS,
                    null,
                    null,
                    null
            );
            return getFavoriteMoviesDataFromCursor(cursor);
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (movies != null) {
                if (MainActivityFragment.movieGridAdapter != null) {
                    MainActivityFragment.movieGridAdapter.setData(movies);
                }
                MainActivityFragment.movies = new ArrayList<>();
                MainActivityFragment.movies.addAll(movies);
            }
        }

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_IMAGE,
            MovieContract.MovieEntry.COLUMN_IMAGE2,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_DATE
    };

    }