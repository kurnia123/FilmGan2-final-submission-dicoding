package com.example.filmgan2.application.film.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmgan2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmViewHolder> {

    private ArrayList<filmData> listFilm;
    private OnitemClickCallback onitemClickCallback;
    private Context context;

    public void setOnClickCallback(OnitemClickCallback onitemClickCallback) {
        this.onitemClickCallback = onitemClickCallback;
    }

    public FilmAdapter(Context context, ArrayList<filmData> list) {
        this.listFilm = list;
        this.context = context;
    }

    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_film_cardview, parent, false);
        return new FilmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FilmViewHolder holder, int position) {
        filmData film = listFilm.get(position);
        String urlPoster = "https://image.tmdb.org/t/p/w500/";
        Picasso.with(context).load(urlPoster + film.getImageFilm()).into(holder.imageFilm);

        holder.judulFilm.setText(film.getJudulfilm());
        holder.ratingFilm.setText(String.valueOf(film.getRating()));
        holder.durationFilm.setText(String.valueOf(film.getDurationFilm()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAGJJ","FilmAdapter RV");
                onitemClickCallback.onItemClicked(listFilm.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFilm.size();
    }

    public class FilmViewHolder extends RecyclerView.ViewHolder {

        ImageView imageFilm;
        TextView judulFilm;
        TextView ratingFilm;
        TextView durationFilm;

        public FilmViewHolder(@NonNull View itemView) {
            super(itemView);
            imageFilm = itemView.findViewById(R.id.rv_imageFilm);
            judulFilm = itemView.findViewById(R.id.rv_judul_film);
            ratingFilm = itemView.findViewById(R.id.rv_rating);
            durationFilm = itemView.findViewById(R.id.rv_duration);
        }
    }

    public interface OnitemClickCallback {
        void onItemClicked(filmData filmdata);
    }
}
