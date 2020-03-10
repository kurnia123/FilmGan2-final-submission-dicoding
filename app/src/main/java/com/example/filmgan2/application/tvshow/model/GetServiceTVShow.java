package com.example.filmgan2.application.tvshow.model;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetServiceTVShow {
    @GET("tv")
    Call<TvResponse> getTvShow(@Query("api_key") String apiKey,@Query("language") String language);
}
