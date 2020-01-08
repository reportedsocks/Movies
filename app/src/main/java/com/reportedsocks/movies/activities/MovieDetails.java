package com.reportedsocks.movies.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.reportedsocks.movies.R;
import com.reportedsocks.movies.data.ReviewListAdapter;
import com.reportedsocks.movies.model.MovieReview;
import com.reportedsocks.movies.utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieDetails extends AppCompatActivity {

    private RequestQueue requestQueue;
    private ImageView posterImageView;
    private TextView titleTextView;
    private TextView yearTextView;
    private TextView ratingTextView;
    private TextView runtimeTextView;
    private TextView genreTextView;
    private TextView directorTextView;
    private TextView writerTextView;
    private TextView actorsTextView;
    private TextView languageTextView;
    private TextView descriptionTextView;

    private ListView ratingsListView;
    private ArrayList<MovieReview> movieReviews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ratingsListView = findViewById(R.id.movieRatingsListView);
        ratingsListView.setEnabled(false);
        movieReviews = new ArrayList<MovieReview>();

        posterImageView = findViewById(R.id.posterDetailsImageView);
        titleTextView = findViewById(R.id.titleDetailsTextView);
        yearTextView = findViewById(R.id.yearDetailsTextView);
        ratingTextView = findViewById(R.id.ratingTextView);
        runtimeTextView = findViewById(R.id.runtimeTextView);
        genreTextView = findViewById(R.id.genreTextView);
        directorTextView = findViewById(R.id.directorTextView);
        writerTextView = findViewById(R.id.writerTextView);
        actorsTextView = findViewById(R.id.actorsTextView);
        languageTextView = findViewById(R.id.languageTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);


        Intent intent = getIntent();
        String imdbId = intent.getStringExtra("imdbId");

        requestQueue = Volley.newRequestQueue(this);
        new getMovieDetailsAsyncTask(imdbId).execute();

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getMovieDetails(String imdbId) {
        String url = Utils.REQUEST + "&i=" + imdbId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    setTitle(response.getString("Title"));

                    titleTextView.setText(response.getString("Title"));
                    yearTextView.setText(response.getString("Year"));
                    ratingTextView.setText(response.getString("Rated"));
                    runtimeTextView.setText(response.getString("Runtime"));
                    genreTextView.setText(response.getString("Genre"));
                    directorTextView.setText(response.getString("Director"));
                    writerTextView.setText(response.getString("Writer"));
                    actorsTextView.setText(response.getString("Actors"));
                    languageTextView.setText(response.getString("Language"));
                    descriptionTextView.setText(response.getString("Plot"));

                    Picasso.get().load(response.getString("Poster"))
                            .fit().centerInside().into(posterImageView);

                    JSONArray jsonArray = response.getJSONArray("Ratings");
                    for(int i = 0; i < jsonArray.length(); i++){
                        String source = jsonArray.getJSONObject(i).getString("Source");
                        String rating = jsonArray.getJSONObject(i).getString("Value");
                        movieReviews.add(new MovieReview(source, rating));
                    }
                    ratingsListView.setAdapter(new ReviewListAdapter(MovieDetails.this, movieReviews));
                    Utils.setListViewHeightBasedOnChildren(ratingsListView);

                } catch (JSONException e) {
                    e.printStackTrace();
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

    private class getMovieDetailsAsyncTask extends AsyncTask<Void, Void, Void>{
        private String imdbId;
        getMovieDetailsAsyncTask( String imdbId){
            this.imdbId = imdbId;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            getMovieDetails(imdbId);
            return null;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
