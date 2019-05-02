package com.nyt.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.nyt.models.Result;
import com.nyt.network.requests.ArticleApiClient;

import java.util.List;

public class ArticleRepository {


    private static ArticleRepository instance;
    private ArticleApiClient mArticleApiClient;
    private int mDays;
    private int mPageNumber;
    private MutableLiveData<Boolean> mIsQueryExhausted = new MutableLiveData<>();
    private MediatorLiveData<List<Result>> mArticles = new MediatorLiveData<>();

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
        LiveData<List<Result>> articleListApiSource = mArticleApiClient.getArticles();
        mArticles.addSource(articleListApiSource, new Observer<List<Result>>() {
            @Override
            public void onChanged(@Nullable List<Result> articles) {

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

    private void doneQuery(List<Result> list) {
        if (list != null) {
            if (list.size() % 30 != 0) {
                mIsQueryExhausted.setValue(true);
            }
        } else {
            mIsQueryExhausted.setValue(true);
        }
    }

    public LiveData<Boolean> isQueryExhausted() {
        return mIsQueryExhausted;
    }

    public LiveData<List<Result>> getArticles() {
        return mArticles;
    }

//    public LiveData<Result> getArticle(){
//        return mArticleApiClient.getArticle();
//    }

//    public void searchArticleById(String articleId){
//        mArticleApiClient.searchArticleById(articleId);
//    }

    public void searchArticlesApi(int days, int pageNumber) {
        if (pageNumber == 0) {
            pageNumber = 1;
        }
        mDays = days;
        mPageNumber = pageNumber;
        mIsQueryExhausted.setValue(false);
        mArticleApiClient.searchArticlesApi(days, pageNumber);
    }

    public void searchNextPage() {
        searchArticlesApi(mDays, mPageNumber + 1);
    }

    public void cancelRequest() {
        mArticleApiClient.cancelRequest();
    }

    public LiveData<Boolean> isArticleRequestTimedOut() {
        return mArticleApiClient.isArticleRequestTimedOut();
    }
}
    
    /*
    private static ArticleRepository instance;
    private ArticleApiClient mArticleApiClient;
    private String mDays;
    private int mPageNumber;
    private MutableLiveData<Boolean> mIsQueryExhausted = new MutableLiveData<>();
    private MediatorLiveData<List<Result>> mArticles = new MediatorLiveData<>();

    public static ArticleRepository getInstance(){
        if(instance == null){
            instance = new ArticleRepository();
        }
        return instance;
    }

    private ArticleRepository(){
        mArticleApiClient = ArticleApiClient.getInstance();
        initMediators();
    }

    private void initMediators(){
        LiveData<List<Result>> articleListApiSource = mArticleApiClient.getArticles();
        mArticles.addSource(articleListApiSource, new Observer<List<Result>>() {
            @Override
            public void onChanged(@Nullable List<Result> articles) {

                if(articles != null){
                    mArticles.setValue(articles);
                    doneQuery(articles);
                }
                else{
                    // search database cache
                    doneQuery(null);
                }
            }
        });
    }

    private void doneQuery(List<Result> list){
        if(list != null){
            if (list.size() % 30 != 0) {
                mIsQueryExhausted.setValue(true);
            }
        }
        else{
            mIsQueryExhausted.setValue(true);
        }
    }

    public LiveData<Boolean> isQueryExhausted(){
        return mIsQueryExhausted;
    }

    public LiveData<List<Result>> getArticles(){
        return mArticles;
    }

//    public LiveData<Result> getArticle(){
//        return mArticleApiClient.getArticles();
//    }

//    public void searchArticleById(String articleId){
//        mArticleApiClient.searchArticleById(articleId);
//    }

    public void searchArticlesApi(int pageNumber){
        if(pageNumber == 0){
            pageNumber = 1;
        }
        mPageNumber = pageNumber;
        mIsQueryExhausted.setValue(false);
        mArticleApiClient.searchArticlesApi(pageNumber);
    }

    public void searchNextPage(){
        searchArticlesApi(mPageNumber + 1);
    }

    public void cancelRequest(){
        mArticleApiClient.cancelRequest();
    }

    public LiveData<Boolean> isArticleRequestTimedOut(){
        return mArticleApiClient.isArticleRequestTimedOut();
    }
}
*/



