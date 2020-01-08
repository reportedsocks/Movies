package com.reportedsocks.movies.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.reportedsocks.movies.R;
import com.reportedsocks.movies.data.MovieAdapter;
import com.reportedsocks.movies.model.Movie;
import com.reportedsocks.movies.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private RequestQueue requestQueue;
    private ArrayList<Movie> movieArrayList;
    private LinearLayoutManager linearLayoutManager;
    private EditText searchEditText;
    private Button searchButton;
    private String searchText;
    private int pageNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieArrayList = new ArrayList<Movie>();
        pageNumber = 1;
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);

        //hiding keyboard on click outside
        Utils.setupUI(findViewById(R.id.parent), MainActivity.this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        requestQueue = Volley.newRequestQueue(this);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText = String.valueOf(searchEditText.getText());
                if (!searchText.isEmpty()) {
                    searchText.trim();
                    new getMoviesAsyncTask(false).execute();
                } else {
                    Toast.makeText(MainActivity.this, "Enter a movie first", Toast.LENGTH_LONG).show();
                }
                Utils.hideSoftKeyboard(MainActivity.this);

            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    new getMoviesAsyncTask(true).execute();
                }
            }
        });


    }

    private void getMovies(Boolean loadingNextPage) {

        String url;

        if (loadingNextPage) {
            url = Utils.REQUEST + "&s=" + searchText + "&page=" + ++pageNumber;
        } else {
            pageNumber = 1;
            movieArrayList.clear();
            url = Utils.REQUEST + "&s=" + searchText;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("Search");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String title = jsonObject.getString("Title");
                        String year = jsonObject.getString("Year");
                        String poster = jsonObject.getString("Poster");
                        String imdbId = jsonObject.getString("imdbID");

                        Movie movie = new Movie(title, year, poster, imdbId);
                        movieArrayList.add(movie);
                    }
                    // save scroll here
                    int index = linearLayoutManager.findFirstVisibleItemPosition();
                    View v = linearLayoutManager.getChildAt(0);
                    int top = (v == null) ? 0 : (v.getTop() - linearLayoutManager.getPaddingTop());

                    movieAdapter = new MovieAdapter(MainActivity.this, movieArrayList);
                    recyclerView.setAdapter(movieAdapter);
                    // set scroll
                    linearLayoutManager.scrollToPositionWithOffset(index,top);

                } catch (JSONException e) {
                    e.printStackTrace();
                    if (e.getMessage().equals("No value for Search")) {
                        Toast.makeText(MainActivity.this, "No results for your search", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    private class getMoviesAsyncTask extends AsyncTask<Void, Void, Void>{
        private Boolean loadingNextPage;
        getMoviesAsyncTask(Boolean loadingNextPage){
            this.loadingNextPage = loadingNextPage;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            getMovies(loadingNextPage);
            return null;
        }
    }
}
