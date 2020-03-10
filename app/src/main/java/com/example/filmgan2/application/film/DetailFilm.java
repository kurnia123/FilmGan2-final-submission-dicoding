package com.example.filmgan2.application.film;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.filmgan2.R;
import com.example.filmgan2.application.film.model.filmData;
import com.example.filmgan2.db.DatabaseContract;
import com.example.filmgan2.settings.localization.LocaleHelper;
import com.example.filmgan2.widget.StackWidgetFavoriteFilm;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import static com.example.filmgan2.db.DatabaseContract.CONTENT_URI_FILM;
import static com.example.filmgan2.db.DatabaseContract.FilmColumns.ID_FILM;
import static com.example.filmgan2.db.DatabaseContract.FilmColumns.ID_IMAGE_FILM;

public class DetailFilm extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = DetailFilm.class.getSimpleName();
    public static String EXTRA_NAME = "data_film";
    int idFilm = 0;
    private filmData data;
    private String urlPoster = "https://image.tmdb.org/t/p/w780/";
    private ImageView imageFilm;
    private ImageView detailBack;
    private ImageView fav;
    private ImageView image_background;
    private TextView rating;
    private TextView duration;
    private TextView movieTitle;
    private TextView description;
    private RatingBar ratingbar;
    private ProgressBar progressBar;
    Uri uriWithId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateLanguage();
        setContentView(R.layout.activity_detail_film);

        this.data = getIntent().getParcelableExtra(EXTRA_NAME);

        rating = findViewById(R.id.detail_rating);
        duration = findViewById(R.id.detail_duration);
        imageFilm = findViewById(R.id.detail_image_film);
        movieTitle = findViewById(R.id.detail_judul_film);
        detailBack = findViewById(R.id.detail_back);
        fav = findViewById(R.id.detail_fav_film);
        image_background = findViewById(R.id.image_film_background);
        description = findViewById(R.id.detail_description);
        progressBar = findViewById(R.id.activity_film_detail_progressbarr);
        ratingbar = findViewById(R.id.appCompatRatingBarFilm);

        ratingbar.setRating((float) data.getRating() / 2);
        rating.setText(String.valueOf(data.getRating()));
        duration.setText(data.getDurationFilm());
        progressBar.setVisibility(View.VISIBLE);

        // Akes data base

        Picasso.with(this).load(urlPoster + data.getImageBackdop()).into(image_background);
        Picasso.with(this).load(urlPoster + data.getImageFilm()).into(imageFilm, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                Log.e(TAG, "not load image");
            }
        });

        cekDataFavorite();

        movieTitle.setText(data.getJudulfilm());
        description.setText(data.getDescription());

        detailBack.setOnClickListener(this);
        fav.setOnClickListener(this);

        setActionBarTitle(getString(R.string.actionbar_detail_film));
        //Log.d(TAG, "onCreate : " + this.idFilm);
    }

    public void cekDataFavorite() {
        Cursor cursor = getContentResolver().query(CONTENT_URI_FILM,null,null,null,null);
        while (cursor.moveToNext()) {
            if (this.data.getIdFilm() == cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.FilmColumns.ID_FILM))) {
                //Log.d(TAG, "CEK DATA 1 : " + String.valueOf(this.data.getIdFilm() == this.data.getIdFilm()));
                fav.setImageResource(R.drawable.ic_favorite_pink_24dp);
                this.idFilm = this.data.getIdFilm();
            }
        }
        cursor.close();
    }

    private void updateLanguage() {
        Locale locale = new Locale(LocaleHelper.getLanguage(DetailFilm.this));
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        getResources().updateConfiguration(
                config,
                getResources().getDisplayMetrics()
        );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_fav_film:
                if (this.data.getIdFilm() == idFilm) {
                    uriWithId = Uri.parse(CONTENT_URI_FILM + "/" + this.data.getIdFilm());
                    getContentResolver().delete(uriWithId,null,null);
                    fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    notifyUpdateWidget(this);
                    this.idFilm = 0;
                    Toast.makeText(this, getString(R.string.success_delete_data), Toast.LENGTH_SHORT).show();
                } else {
                    ContentValues values = new ContentValues();
                    values.put(ID_FILM, this.data.getIdFilm());
                    values.put(ID_IMAGE_FILM, this.data.getImageFilm());
                    getContentResolver().insert(CONTENT_URI_FILM, values);

                    notifyUpdateWidget(this);
                    fav.setImageResource(R.drawable.ic_favorite_pink_24dp);
                    this.idFilm = this.data.getIdFilm();

                    Toast.makeText(this, getString(R.string.succes_save_data), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.detail_back:
                finish();
                break;
            default:
                break;
        }
    }

    private void notifyUpdateWidget(Context context){
        Intent send = new Intent(context, StackWidgetFavoriteFilm.class);
        send.setAction(StackWidgetFavoriteFilm.ACTION_UPDATE);
        context.sendBroadcast(send);
    }

    private void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    @Override
//    public void onBackPressed() {
//        Intent i = new Intent(this, MainActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(i);
//        super.onBackPressed();
//    }
}

