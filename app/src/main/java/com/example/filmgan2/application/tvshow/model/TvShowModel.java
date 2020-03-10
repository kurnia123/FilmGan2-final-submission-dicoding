package com.example.filmgan2.application.tvshow.model;

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

public class TvShowModel extends ViewModel {
    public final static String NAME_CLASS = TvShowModel.class.getSimpleName();
    private MutableLiveData<ArrayList<TvShowData>> listTvShow = new MutableLiveData<>();

    public void setTvShow(final String language){
        GetServiceTVShow apiClient = ApiClient.getRetrofitInstance().create(GetServiceTVShow.class);
        Call<TvResponse> call = apiClient.getTvShow(BuildConfig.API_KEY,language.equals("in") ? "id":language);

        call.enqueue(new Callback<TvResponse>() {
            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                listTvShow.postValue(response.body().getResults());
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {
                Log.e(  NAME_CLASS+" onFailure", t.getMessage());
            }
        });
    }

    public LiveData<ArrayList<TvShowData>> getTvShow(){
        return listTvShow;
    }

}
