package com.nyt.network.requests;

import android.util.Log;

import com.nyt.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class ServiceGenerator {

    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static TimesApi articleApi = retrofit.create(TimesApi.class);

    static TimesApi getArticleApi(){
        Log.d("NYT", " getArticlessapi in service generator ");
        return articleApi;
    }
}
