package com.example.filmgan2.application.tvshow.search;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmgan2.ApiClient;
import com.example.filmgan2.BuildConfig;
import com.example.filmgan2.R;
import com.example.filmgan2.settings.localization.LocaleHelper;
import com.example.filmgan2.application.tvshow.DetailTVShow;
import com.example.filmgan2.application.tvshow.model.TvResponse;
import com.example.filmgan2.application.tvshow.model.TvShowAdapter;
import com.example.filmgan2.application.tvshow.model.TvShowData;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchTvShowActivity extends AppCompatActivity {

    RecyclerView rvTvShowSearch;
    ProgressBar progressBar;

    private static final String TAG = SearchTvShowActivity.class.getSimpleName();
    private MutableLiveData<ArrayList<TvShowData>> listTvShow = new MutableLiveData<>();
    public static final String KEY_NAME = "item_search_tv_show";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateLanguage();
        setContentView(R.layout.activity_search_tv_show);

        rvTvShowSearch = findViewById(R.id.rv_tv_show_search);
        progressBar = findViewById(R.id.activity_tv_show_search_progressbarr);

        String itemName = getIntent().getStringExtra(KEY_NAME);
        showLoading(true);

        setTitle(R.string.search_tv);
        setData(Locale.getDefault().getLanguage(), itemName);
        getDataFilm().observe(SearchTvShowActivity.this, new Observer<ArrayList<TvShowData>>() {
            @Override
            public void onChanged(ArrayList<TvShowData> tvShowData) {
                showRecycler(tvShowData);
                showLoading(false);
            }
        });
    }

    private void setData(final String language, final String searchName) {
        GetServiceSearchTvShow apiClient = ApiClient.getSearchRetrofitInstance().create(GetServiceSearchTvShow.class);
        Call<TvResponse> call = apiClient.getTvShowSearch(BuildConfig.API_KEY, language.equals("in") ? "id" : language, searchName);

        call.enqueue(new Callback<TvResponse>() {
            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                listTvShow.postValue(response.body().getResults());
                //Log.d(TAG, "GET DATA : " + response.raw().request().url());
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
            }
        });
    }

    private LiveData<ArrayList<TvShowData>> getDataFilm() {
        return listTvShow;
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

    public void showRecycler(ArrayList<TvShowData> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvTvShowSearch.setLayoutManager(layoutManager);
        TvShowAdapter tvShowAdapter = new TvShowAdapter(this, list);
        rvTvShowSearch.setAdapter(tvShowAdapter);

        tvShowAdapter.setOnClickCallback(new TvShowAdapter.OnitemClickCallback() {
            @Override
            public void onItemClicked(TvShowData tvdata) {
                moveActicity(tvdata);
            }
        });
    }

    public void moveActicity(TvShowData tvData) {
        Intent intent = new Intent(this, DetailTVShow.class);
        intent.putExtra(DetailTVShow.EXTRA_NAME, tvData);
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
