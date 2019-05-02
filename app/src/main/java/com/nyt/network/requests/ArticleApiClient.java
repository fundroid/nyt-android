package com.nyt.network.requests;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.nyt.models.Result;
import com.nyt.utils.AppExecutors;
import com.nyt.network.responses.ArticleResponse;
import com.nyt.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

import static com.nyt.utils.Constants.NETWORK_TIMEOUT;

public class ArticleApiClient {

    private static final String TAG = "NYT";

    private static ArticleApiClient instance;
    private MutableLiveData<List<Result>> mArticles;
    private RetrieveArticlesRunnable mRetrieveArticlesRunnable;
    private MutableLiveData<Result> mArticle;
    private MutableLiveData<Boolean> mArticleRequestTimeout = new MutableLiveData<>();

    public static ArticleApiClient getInstance(){
        if(instance == null){
            instance = new ArticleApiClient();
        }
        return instance;
    }

    private ArticleApiClient(){
        mArticles = new MutableLiveData<>();
        mArticle = new MutableLiveData<>();
    }

    public LiveData<List<Result>> getArticles(){
        return mArticles;
    }

    public LiveData<Boolean> isArticleRequestTimedOut(){
        return mArticleRequestTimeout;
    }

    public void searchArticlesApi(int days, int pageNumber){
        if(mRetrieveArticlesRunnable != null){
            mRetrieveArticlesRunnable = null;
        }
        mRetrieveArticlesRunnable = new RetrieveArticlesRunnable(days, pageNumber);
        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrieveArticlesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Time out");
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    private class RetrieveArticlesRunnable implements Runnable{

        private int days;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveArticlesRunnable(int days, int pageNumber) {
            this.days = days;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Call<ArticleResponse> call = getArticles(days, pageNumber);
                Log.d(TAG, "url : " + call.request().url().toString());
                Response response = call.execute();
//                Log.w(TAG, "Response : " +  new GsonBuilder().setPrettyPrinting().create().toJson(response));
                if(cancelRequest){
                    return;
                }
                if(response.code() == 200){
                    Log.w(TAG, new GsonBuilder().setPrettyPrinting().create().toJson(response));
                    ArticleResponse mArticleResponse = (ArticleResponse)response.body();
                    if(mArticleResponse != null && mArticleResponse.status .equals("OK")) {
                        List<Result> list = new ArrayList<>(mArticleResponse.results);
                        mArticles.postValue(list);
                    }
                }
                else{
                    String error = response.errorBody().string();
                    Log.e(TAG, "run: " + error );
                    mArticles.postValue(null);
                }
            } catch (IOException e) {
                Log.e(TAG, "error : " + e.getLocalizedMessage());
                e.printStackTrace();
                mArticles.postValue(null);
            }
        }

        private Call<ArticleResponse> getArticles(int days, int pageNumber){
            return ServiceGenerator.getArticleApi().searchArticles(
                    Constants.API_KEY
            );
        }

        private void cancelRequest(){
            Log.d(TAG, "cancelRequest: canceling the search request.");
            cancelRequest = true;
        }
    }

    public void cancelRequest(){
        if(mRetrieveArticlesRunnable != null){
            mRetrieveArticlesRunnable.cancelRequest();
        }
    }
}













