package com.nyt.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.nyt.R;
import com.nyt.adapters.ArticlesRecyclerAdapter;
import com.nyt.adapters.OnArticleListener;
import com.nyt.models.Result;
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
        mArticleListViewModel.getArticles().observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(@Nullable List<Result> articles) {
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

        mArticleListViewModel.isQueryExhausted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                Log.d(TAG, "onChanged: the query is exhausted..." + aBoolean);
                if (aBoolean) {
                    mAdapter.setQueryExhausted();
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

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!mRecyclerView.canScrollVertically(1)) {
                    // search the next page
                    mArticleListViewModel.searchNextPage();
                }
            }
        });
    }

    private void displaySearchCategories() {
        mArticleListViewModel.setIsViewingArticles(false);
        mAdapter.displaySearchCategories();
    }

    @Override
    public void onArticleClick(int position) {
        Log.d(TAG, "Result clicked");
        Intent intent = new Intent(this, ArticleActivity.class);
        intent.putExtra("Result", mAdapter.getSelectedArticle(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {
        Log.d(TAG, "category clicked" + category);
        mAdapter.displayLoading();
        mArticleListViewModel.searchArticlesApi(1, 1);
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