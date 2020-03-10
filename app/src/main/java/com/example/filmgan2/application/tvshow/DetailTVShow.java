package com.example.filmgan2.application.tvshow;

import android.content.ContentValues;
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
import com.example.filmgan2.application.tvshow.model.TvShowData;
import com.example.filmgan2.db.DatabaseContract;
import com.example.filmgan2.db.query.TvShowHelper;
import com.example.filmgan2.settings.localization.LocaleHelper;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import static com.example.filmgan2.db.DatabaseContract.CONTENT_URI_TV_SHOW;
import static com.example.filmgan2.db.DatabaseContract.TvShowColumns.ID_IMAGE_TV;
import static com.example.filmgan2.db.DatabaseContract.TvShowColumns.ID_TV;

public class DetailTVShow extends AppCompatActivity implements View.OnClickListener {

    TvShowData data;
    int idTvShow = 0;
    public static final String TAG = DetailTVShow.class.getSimpleName();
    public static String EXTRA_NAME = "data_tv";
    ImageView imagebackground;
    ImageView imagedetail;
    ImageView like;
    ImageView detailBack;
    TextView judul;
    TextView rating;
    TextView description;
    TextView date_first_liris;
    ProgressBar progressBar;
    RatingBar ratingBarTv;
    String urlPoster = "https://image.tmdb.org/t/p/w780/";
    Uri uriWithId;

    private TvShowHelper tvShowHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateLanguage();
        setContentView(R.layout.activity_detail_tvshow);
        this.data = getIntent().getParcelableExtra(EXTRA_NAME);


        date_first_liris = findViewById(R.id.tvshow_first_rilis);
        detailBack = findViewById(R.id.detail_back_tv);
        like = findViewById(R.id.detail_fav_tv);
        imagebackground = findViewById(R.id.image_tv_background);
        imagedetail = findViewById(R.id.detail_image_tv);
        judul = findViewById(R.id.detail_judul_tv);
        rating = findViewById(R.id.detail_rating);
        description = findViewById(R.id.detail_description_tv);
        progressBar = findViewById(R.id.activity_tv_show_detail_progressbarr);
        ratingBarTv = findViewById(R.id.appCompatRatingBarTv);

        ratingBarTv.setRating((float) data.getRating() / 2);
        progressBar.setVisibility(View.VISIBLE);

        Picasso.with(this).load(urlPoster + data.getImageTv()).into(imagedetail);
        Picasso.with(this).load(urlPoster + data.getImageBackdrop()).into(imagebackground, new com.squareup.picasso.Callback() {
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

        judul.setText(data.getJudulTv());
        rating.setText(String.valueOf(data.getRating()));
        description.setText(data.getDescription());

        detailBack.setOnClickListener(this);
        like.setOnClickListener(this);
        setActionBarTitle(getString(R.string.actionbar_detail_tv));

        //Log.d(TAG, "onCreate : " + LocaleHelper.getLanguage(this));
        //Log.d(TAG, "onCreate : " + this.data.getIdTvShow());
    }

    public void cekDataFavorite() {
        //Log.d(TAG, "DetailTVShow cekDataFavorite ");

        Cursor cursor = getContentResolver().query(CONTENT_URI_TV_SHOW,null,null,null,null);
        while (cursor.moveToNext()) {
            if (this.data.getIdTvShow() == cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.ID_TV))) {
                //Log.d(TAG, "DetailTVShow cekDataFavorite : " + this.data.getIdTvShow());
                like.setImageResource(R.drawable.ic_favorite_pink_24dp);
                this.idTvShow = this.data.getIdTvShow();
            }
        }
        cursor.close();
    }


    private void updateLanguage() {
        Locale locale = new Locale(LocaleHelper.getLanguage(this));
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
            case R.id.detail_fav_tv:
                if (this.data.getIdTvShow() == idTvShow) {
                    uriWithId = Uri.parse(CONTENT_URI_TV_SHOW + "/" + this.data.getIdTvShow());
                    getContentResolver().delete(uriWithId,null,null);
                    like.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    this.idTvShow = 0;
                    Toast.makeText(this, getString(R.string.success_delete_data), Toast.LENGTH_SHORT).show();
                } else {

                    ContentValues values = new ContentValues();
                    values.put(ID_TV, this.data.getIdTvShow());
                    values.put(ID_IMAGE_TV, this.data.getImageTv());
                    getContentResolver().insert(CONTENT_URI_TV_SHOW,values);

                    like.setImageResource(R.drawable.ic_favorite_pink_24dp);
                    this.idTvShow = this.data.getIdTvShow();
                    Toast.makeText(this, getString(R.string.succes_save_data), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.detail_back_tv:
                finish();
                break;
        }
    }

    private void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
