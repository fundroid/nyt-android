package com.nyt.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.nyt.R;
import com.nyt.adapters.ArticlesRecyclerAdapter;
import com.nyt.adapters.OnArticleListener;
import com.nyt.models.Article;
import com.nyt.utils.Testing;
import com.nyt.utils.VerticalSpacingItemDecorator;
import com.nyt.viewmodels.ArticleListViewModel;

import java.util.List;

public class ArticleListActivity extends BaseActivity implements OnArticleListener {

    private RecyclerView mRecyclerView;
    private ArticlesRecyclerAdapter mAdapter;
    private ArticleListViewModel mArticleListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        mRecyclerView = findViewById(R.id.recycler_articles);

        mArticleListViewModel = ViewModelProviders.of(this).get(ArticleListViewModel.class);
        initRecyclerView();
        subscribeObservers();

        if (!mArticleListViewModel.isViewingArticles()) {
            displaySearchCategories();
        }
    }

    private void subscribeObservers() {
        mArticleListViewModel.getArticles().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable List<Article> articles) {
                if (articles != null) {
                    Log.d(TAG, "articles size : " + articles.size());
                    if (mArticleListViewModel.isViewingArticles()) {
                        Testing.printArticles(articles, "article test");
                        mArticleListViewModel.setIsPerformingQuery(false);
                        mAdapter.setArticles(articles);
                    }
                }
            }
        });

    }

    private void initRecyclerView() {
        mAdapter = new ArticlesRecyclerAdapter(this);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(30);
        mRecyclerView.addItemDecoration(itemDecorator);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void displaySearchCategories() {
        mArticleListViewModel.setIsViewingArticles(false);
        mAdapter.displaySearchCategories();
    }

    @Override
    public void onArticleClick(int position) {
        Log.d(TAG, "Article clicked");
        Intent intent = new Intent(this, ArticleActivity.class);
        intent.putExtra("article", mAdapter.getSelectedArticle(position));
        startActivity(intent);
    }

    @Override
    public void onDaysClick(int days) {
        Log.d(TAG, "category clicked" + days);

        mAdapter.displayLoading();
        mArticleListViewModel.searchArticlesApi(days);
    }

    @Override
    public void onBackPressed() {
        if (mArticleListViewModel.onBackPressed()) {
            super.onBackPressed();
        } else {
            displaySearchCategories();
        }
    }
}