package com.nyt.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.nyt.models.Result;
import com.nyt.repositories.ArticleRepository;

import java.util.List;

public class ArticleListViewModel extends ViewModel {

    private ArticleRepository mArticleRepository;
    private boolean mIsViewingArticles;
    private boolean mIsPerformingQuery;

    public ArticleListViewModel() {
        mArticleRepository = ArticleRepository.getInstance();
        mIsPerformingQuery = false;
    }

    public LiveData<List<Result>> getArticles(){
        return mArticleRepository.getArticles();
    }

    public LiveData<Boolean> isQueryExhausted(){
        return mArticleRepository.isQueryExhausted();
    }

    public void searchArticlesApi(int days, int pageNumber){
        mIsViewingArticles = true;
        mIsPerformingQuery = true;
        mArticleRepository.searchArticlesApi(days, pageNumber);
    }

    public void searchNextPage(){
        if(!mIsPerformingQuery
                && mIsViewingArticles
                && !isQueryExhausted().getValue()){
            mArticleRepository.searchNextPage();
        }
    }

    public boolean isViewingArticles(){
        return mIsViewingArticles;
    }

    public void setIsViewingArticles(boolean isViewingArticles){
        mIsViewingArticles = isViewingArticles;
    }

    public void setIsPerformingQuery(Boolean isPerformingQuery){
        mIsPerformingQuery = isPerformingQuery;
    }

    public boolean isPerformingQuery(){
        return mIsPerformingQuery;
    }

    public boolean onBackPressed(){
        if(mIsPerformingQuery){
            // cancel the query
            mArticleRepository.cancelRequest();
            mIsPerformingQuery = false;
        }
        if(mIsViewingArticles){
            mIsViewingArticles = false;
            return false;
        }
        return true;
    }
}