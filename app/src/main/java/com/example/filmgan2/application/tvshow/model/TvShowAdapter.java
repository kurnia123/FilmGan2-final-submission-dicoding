package com.example.filmgan2.application.tvshow.model;

import android.content.Context;
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

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.TvHolder> {

    private ArrayList<TvShowData> listTV;
    private OnitemClickCallback onitemClickCallback;
    private Context context;

    public void setOnClickCallback(OnitemClickCallback onitemClickCallback) {
        this.onitemClickCallback = onitemClickCallback;
    }

    public TvShowAdapter(Context context, ArrayList<TvShowData> list) {
        this.listTV = list;
        this.context = context;
    }

    @NonNull
    @Override
    public TvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_tv_show_cardview, parent, false);
        return new TvHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TvHolder holder, int position) {
        TvShowData tvData = listTV.get(position);
        String urlPoster = "https://image.tmdb.org/t/p/w500/";
        Picasso.with(context).load(urlPoster + tvData.getImageTv()).into(holder.image);

        holder.dateFirstLiris.setText(tvData.getDateFirstLiris());
        holder.judulTV.setText(tvData.getJudulTv());
        holder.rating.setText(String.valueOf(tvData.getRating()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onitemClickCallback.onItemClicked(listTV.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return listTV.size();
    }

    public class TvHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView judulTV;
        TextView rating;
        TextView dateFirstLiris;

        public TvHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.rv_imageTV);
            judulTV = itemView.findViewById(R.id.rv_judul_tvshow);
            rating = itemView.findViewById(R.id.rv_tvshow_rating);
            dateFirstLiris = itemView.findViewById(R.id.first_date_rilis);
        }
    }

    public interface OnitemClickCallback {
        void onItemClicked(TvShowData tvdata);
    }
}
