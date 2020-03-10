package com.example.filmfavorite.db;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetServiceFavorite {

    @GET("movie/{id}")
    Observable<filmData> getMovie(@Path("id") int id, @Query("api_key") String apiKey, @Query("language") String language);

    @GET("tv/{id}")
    Observable<TvShowData> getTvShow(@Path("id") int id, @Query("api_key") String apiKey, @Query("language") String language);
}
