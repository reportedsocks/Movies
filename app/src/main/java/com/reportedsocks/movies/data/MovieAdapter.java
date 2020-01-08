package com.reportedsocks.movies.data;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.reportedsocks.movies.R;
import com.reportedsocks.movies.activities.MovieDetails;
import com.reportedsocks.movies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context context;
    private ArrayList<Movie> movieArrayList;

    public MovieAdapter (Context context, ArrayList<Movie> movieArrayList){
        this.context = context;
        this.movieArrayList = movieArrayList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie currentMovie = movieArrayList.get(position);

        String titleText = currentMovie.getTitleText();
        String yearText = currentMovie.getYearText();
        String posterURL = currentMovie.getPosterURL();

        holder.titleTextView.setText(titleText);
        holder.yearTextView.setText(yearText);
        if(!posterURL.equals("N/A")){
            Picasso.get().load(posterURL).fit().centerInside().into(holder.posterImageView);
        } else {
            holder.posterImageView.setImageResource(R.drawable.ic_local_movies_grey_200dp);
        }

    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleTextView;
        TextView yearTextView;
        ImageView posterImageView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.titleTextView);
            yearTextView = itemView.findViewById(R.id.yearTextView);
            posterImageView = itemView.findViewById(R.id.posterImageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            Intent intent = new Intent(context, MovieDetails.class);
            intent.putExtra("imdbId", movieArrayList.get(position).getImdbId());
            context.startActivity(intent);
        }
    }
}
