package com.nyt.network.requests;

import com.nyt.network.responses.ArticleResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TimesApi {

    @GET("mostpopular/v2/viewed/1.json")
    Call<ArticleResponse> searchTodayArticles(
            @Query("api-key") String key
    );

    @GET("mostpopular/v2/viewed/7.json")
    Call<ArticleResponse> searchWeekArticles(
            @Query("api-key") String key
    );

    @GET("mostpopular/v2/viewed/30.json")
    Call<ArticleResponse> searchMonthArticles(
            @Query("api-key") String key
    );
}