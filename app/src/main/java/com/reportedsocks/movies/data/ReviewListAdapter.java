package com.reportedsocks.movies.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.reportedsocks.movies.R;
import com.reportedsocks.movies.model.Movie;
import com.reportedsocks.movies.model.MovieReview;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ReviewListAdapter extends ArrayAdapter<MovieReview> {
    public ReviewListAdapter(@NonNull Context context,  @NonNull ArrayList<MovieReview> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        MovieReview movieReview = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_ratings_list_item, parent, false);
        }

        TextView reviewSourceTextView = convertView.findViewById(R.id.reviewSourceTextView);
        TextView reviewMarkTextView = convertView.findViewById(R.id.reviewMarkTextView);

        reviewSourceTextView.setText(movieReview.getSource());
        reviewMarkTextView.setText(movieReview.getRating());

        return convertView;
    }
}
