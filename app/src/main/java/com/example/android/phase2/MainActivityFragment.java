package com.example.android.phase2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.phase2.FetchFavoriteMoviesTask;
import com.example.android.phase2.FetchMoviesTask;
import com.example.android.phase2.R;
import com.example.android.phase2.MovieGridAdapter;
import com.example.android.phase2.Movie;

import java.util.ArrayList;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private GridView mGridView;

    public static MovieGridAdapter movieGridAdapter;

    //common info
    //sort key : "sort_setting"
    //sort possible values : "popularity.desc" + "vote_average.desc" + "favorite";
    private static final String MOVIES_KEY = "movies";
    private static final String Rating_DESC ="vote_avarege.decs";
    private String sortType = "popularity.desc";

    public static ArrayList<Movie> movies = null;



    public static final int COL_MOVIE_ID = 1;
    public static final int COL_TITLE = 2;
    public static final int COL_IMAGE = 3;
    public static final int COL_IMAGE2 = 4;
    public static final int COL_OVERVIEW = 5;
    public static final int COL_RATING = 6;
    public static final int COL_DATE = 7;

    public MainActivityFragment() {
    }

    public interface Callback {
        void onItemSelected(Movie movie);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_main, menu);

        if (sortType.contentEquals("popularity.desc"))
            menu.findItem(R.id.sort_popularity).setChecked(true);
        else if (sortType.contentEquals("favorite"))
            menu.findItem(R.id.sort_favorite).setChecked(true);
        else if (sortType.contentEquals("Rating_DESC"))
            menu.findItem(R.id.toprated).setChecked(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.sort_popularity:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                sortType = "popularity.desc";
                updateMovies(sortType);
                return true;
            case R.id.toprated:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                sortType = "Rating_DESC";
                updateMovies(sortType);
                return true;
            case R.id.sort_favorite:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                sortType = "favorite";
                updateMovies(sortType);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mGridView = (GridView) view.findViewById(R.id.gridview_movies);

        movieGridAdapter = new MovieGridAdapter(getActivity(), new ArrayList<Movie>());

        mGridView.setAdapter(movieGridAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = movieGridAdapter.getItem(position);
                ((Callback) getActivity()).onItemSelected(movie);
            }
        });

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("sort_setting")) {
                sortType = savedInstanceState.getString("sort_setting");
            }

            if (savedInstanceState.containsKey(MOVIES_KEY)) {
                movies = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
                movieGridAdapter.setData(movies);
            } else {
                updateMovies(sortType);
            }
        } else {
            updateMovies(sortType);
        }

        return view;
    }

    private void updateMovies(String sort_by) {
        if (!sort_by.contentEquals("favorite")) {
            new FetchMoviesTask().execute(sort_by);
        } else {
            new FetchFavoriteMoviesTask(getActivity()).execute();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (!sortType.contentEquals("popularity.desc")) {
            outState.putString("sort_setting", sortType);
        }
        if (movies != null) {
            outState.putParcelableArrayList(MOVIES_KEY, movies);
        }
        super.onSaveInstanceState(outState);
    }




}
