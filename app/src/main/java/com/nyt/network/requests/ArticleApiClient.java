package com.nyt.network.requests;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.nyt.models.Article;
import com.nyt.network.responses.ArticleResponse;
import com.nyt.utils.AppExecutors;
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
    private MutableLiveData<List<Article>> mArticles;
    private RetrieveArticlesRunnable mRetrieveArticlesRunnable;
    private MutableLiveData<Article> mArticle;
    private MutableLiveData<Boolean> mArticleRequestTimeout = new MutableLiveData<>();

    public static ArticleApiClient getInstance() {
        if (instance == null) {
            instance = new ArticleApiClient();
        }
        return instance;
    }

    private ArticleApiClient() {
        mArticles = new MutableLiveData<>();
        mArticle = new MutableLiveData<>();
    }

    public LiveData<List<Article>> getArticles() {
        return mArticles;
    }

    public LiveData<Boolean> isArticleRequestTimedOut() {
        return mArticleRequestTimeout;
    }

    public void searchArticlesApi(int days) {
        if (mRetrieveArticlesRunnable != null) {
            mRetrieveArticlesRunnable = null;
        }
        mRetrieveArticlesRunnable = new RetrieveArticlesRunnable(days);
        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrieveArticlesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    private class RetrieveArticlesRunnable implements Runnable {

        private int days;
        boolean cancelRequest;

        public RetrieveArticlesRunnable(int days) {
            this.days = days;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
               callApi();
            } catch (IOException e) {
                Log.e(TAG, "error : " + e.getLocalizedMessage());
                e.printStackTrace();
                mArticles.postValue(null);
            }
        }

        private void callApi() throws IOException{
            Call<ArticleResponse> call = getArticles(days);
            Log.d(TAG, "url : " + call.request().url().toString());
            Response response = call.execute();
            if (cancelRequest) {
                return;
            }
            if (response.code() == 200) {
                ArticleResponse mArticleResponse = (ArticleResponse) response.body();
                if (mArticleResponse != null && mArticleResponse.status.equals("OK")) {
                    List<Article> list = new ArrayList<>(mArticleResponse.results);
                    mArticles.postValue(list);
                }
            } else {
                String error = response.errorBody().string();
                Log.e(TAG, "run: " + error);
                mArticles.postValue(null);
            }
        }

        private Call<ArticleResponse> getArticles(int days) {
            switch (days) {
                case 1: {
                    return ServiceGenerator.getArticleApi().searchTodayArticles(
                            Constants.API_KEY);
                }
                case 7: {
                    return ServiceGenerator.getArticleApi().searchWeekArticles(
                            Constants.API_KEY);
                }
                case 30: {
                    return ServiceGenerator.getArticleApi().searchMonthArticles(
                            Constants.API_KEY);
                }
                default: {
                    return ServiceGenerator.getArticleApi().searchTodayArticles(
                            Constants.API_KEY);
                }
            }
        }

        private void cancelRequest() {
            Log.d(TAG, "cancelRequest: canceling the search request.");
            cancelRequest = true;
        }
    }

    public void cancelRequest() {
        if (mRetrieveArticlesRunnable != null) {
            mRetrieveArticlesRunnable.cancelRequest();
        }
    }
}













