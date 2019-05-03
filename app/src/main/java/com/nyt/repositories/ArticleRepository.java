package com.nyt.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.nyt.models.Article;
import com.nyt.network.requests.ArticleApiClient;

import java.util.List;

public class ArticleRepository {

    private static ArticleRepository instance;
    private ArticleApiClient mArticleApiClient;
    private MutableLiveData<Boolean> mIsQueryExhausted = new MutableLiveData<>();
    private MediatorLiveData<List<Article>> mArticles = new MediatorLiveData<>();

    public static ArticleRepository getInstance() {
        if (instance == null) {
            instance = new ArticleRepository();
        }
        return instance;
    }

    private ArticleRepository() {
        mArticleApiClient = ArticleApiClient.getInstance();
        initMediators();
    }

    private void initMediators() {
        LiveData<List<Article>> articleListApiSource = mArticleApiClient.getArticles();
        mArticles.addSource(articleListApiSource, new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable List<Article> articles) {

                if (articles != null) {
                    mArticles.setValue(articles);
                    doneQuery(articles);
                } else {
                    // search database cache
                    doneQuery(null);
                }
            }
        });
    }

    private void doneQuery(List<Article> list) {
        if (list != null) {
            if (list.size() % 30 != 0) {
                mIsQueryExhausted.setValue(true);
            }
        } else {
            mIsQueryExhausted.setValue(true);
        }
    }

    public LiveData<List<Article>> getArticles() {
        return mArticles;
    }

    public void searchArticlesApi(int days) {
        mIsQueryExhausted.setValue(false);
        mArticleApiClient.searchArticlesApi(days);
    }

    public void cancelRequest() {
        mArticleApiClient.cancelRequest();
    }

}


