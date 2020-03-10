package com.example.filmgan2.application.film.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.filmgan2.ApiClient;
import com.example.filmgan2.BuildConfig;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilmModel extends ViewModel {
    private MutableLiveData<ArrayList<filmData>> listFilm = new MutableLiveData<>();

    public void setFilm(final String language){
        GetService apiClient = ApiClient.getRetrofitInstance().create(GetService.class);
        Call<filmResponse> call = apiClient.getTopRatedMovies(BuildConfig.API_KEY,language.equals("in") ? "id":language);

        call.enqueue(new Callback<filmResponse>() {
            @Override
            public void onResponse(Call<filmResponse> call, Response<filmResponse> response) {
                //Log.d("FilmModel","FilmFragment TAG : setData");
                listFilm.postValue(response.body().getResults());
                //Log.d("FilmModel","data : " + response.raw().request().url());
            }

            @Override
            public void onFailure(Call<filmResponse> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
            }
        });
    }

    public LiveData<ArrayList<filmData>> getFilm(){
        //Log.d("FilmModel","FilmFragment TAG : getData");
        return listFilm;
    }

}
