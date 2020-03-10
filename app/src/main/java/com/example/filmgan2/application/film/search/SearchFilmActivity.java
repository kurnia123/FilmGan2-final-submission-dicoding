package com.example.filmgan2.application.film.search;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmgan2.ApiClient;
import com.example.filmgan2.BuildConfig;
import com.example.filmgan2.R;
import com.example.filmgan2.application.film.DetailFilm;
import com.example.filmgan2.application.film.model.FilmAdapter;
import com.example.filmgan2.application.film.model.filmData;
import com.example.filmgan2.application.film.model.filmResponse;
import com.example.filmgan2.settings.localization.LocaleHelper;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFilmActivity extends AppCompatActivity {

    RecyclerView rvFilmSearch;
    ProgressBar progressBar;
    //    private MutableLiveData<ArrayList<filmData>> listFilm = new MutableLiveData<>();
    private final static String TAG = SearchFilmActivity.class.getSimpleName();
    private MutableLiveData<ArrayList<filmData>> listFilm = new MutableLiveData<>();
    public static final String KEY_NAME = "item_search_film";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateLanguage();
        setContentView(R.layout.activity_search_film);

        rvFilmSearch = findViewById(R.id.rv_film_search);
        progressBar = findViewById(R.id.activity_film_search_progressbarr);

        String itemName = getIntent().getStringExtra(KEY_NAME);
        showLoading(true);

        setTitle(R.string.search_film);
        setData(Locale.getDefault().getLanguage(), itemName);
        getDataFilm().observe(SearchFilmActivity.this, new Observer<ArrayList<filmData>>() {
            @Override
            public void onChanged(ArrayList<filmData> filmData) {
                showRecyclerView(filmData);
                showLoading(false);
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    public void setData(final String language, final String searchName) {
        GetServiceSearchFilms apiClient = ApiClient.getSearchRetrofitInstance().create(GetServiceSearchFilms.class);
        Call<filmResponse> call = apiClient.getMovieSearch(BuildConfig.API_KEY, language.equals("in") ? "id" : language, searchName);

        call.enqueue(new Callback<filmResponse>() {
            @Override
            public void onResponse(Call<filmResponse> call, Response<filmResponse> response) {
                listFilm.postValue(response.body().getResults());
                //Log.d(TAG, "GET DATA : " + response.raw().request().url());
            }

            @Override
            public void onFailure(Call<filmResponse> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
            }
        });
    }

    public LiveData<ArrayList<filmData>> getDataFilm() {
        return listFilm;
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

    private void showRecyclerView(ArrayList<filmData> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvFilmSearch.setLayoutManager(layoutManager);
        FilmAdapter filmAdapter = new FilmAdapter(this, list);
        rvFilmSearch.setAdapter(filmAdapter);
        filmAdapter.notifyDataSetChanged();
        filmAdapter.setOnClickCallback(new FilmAdapter.OnitemClickCallback() {
            @Override
            public void onItemClicked(filmData filmdata) {
                moveActicity(filmdata);
            }
        });
    }

    private void moveActicity(filmData filmData) {
        Intent intent = new Intent(this, DetailFilm.class);
        intent.putExtra(DetailFilm.EXTRA_NAME, filmData);
        startActivity(intent);
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

}
