package com.nyt.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nyt.R;
import com.nyt.models.Result;
import com.nyt.utils.Constants;

import java.util.ArrayList;
import java.util.List;


public class ArticlesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ARTICLE_TYPE = 1;
    private static final int LOADING_TYPE = 2;
    private static final int CATEGORY_TYPE = 3;
    private static final int EXHAUSTED_TYPE = 4;

    private List<Result> mArticles;
    private OnArticleListener mOnArticleListener;

    public ArticlesRecyclerAdapter(OnArticleListener mOnArticleListener) {
        this.mOnArticleListener = mOnArticleListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = null;
        switch (i) {

            case ARTICLE_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_article_list_item, viewGroup, false);
                return new ArticleViewHolder(view, mOnArticleListener);
            }

            case LOADING_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_loading_list_item, viewGroup, false);
                return new LoadingViewHolder(view);
            }

            case EXHAUSTED_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_search_exhausted, viewGroup, false);
                return new SearchExhaustedViewHolder(view);
            }

            case CATEGORY_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_category_list_item, viewGroup, false);
                return new CategoryViewHolder(view, mOnArticleListener);
            }

            default: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_article_list_item, viewGroup, false);
                return new ArticleViewHolder(view, mOnArticleListener);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        int itemViewType = getItemViewType(i);
        if (itemViewType == ARTICLE_TYPE) {
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);

            Log.d("NYT", "image : " + mArticles.get(i).media.get(0).mediaMetadata.get(1).url);

            Glide.with(viewHolder.itemView.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(mArticles.get(i).media.get(0).mediaMetadata.get(0).url)
                    .into(((ArticleViewHolder) viewHolder).image);

            ((ArticleViewHolder) viewHolder).title.setText(mArticles.get(i).title);
//            ((ArticleViewHolder) viewHolder).publisher.setText(mArticles.get(i).source);
            ((ArticleViewHolder) viewHolder).socialScore.setText(String.valueOf(mArticles.get(i).views));
        } else if (itemViewType == CATEGORY_TYPE) {

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);

//            Glide.with(viewHolder.itemView.getContext())
//                    .setDefaultRequestOptions(requestOptions)
//                    .load(path)
//                    .into(((CategoryViewHolder) viewHolder).categoryImage);

            ((CategoryViewHolder) viewHolder).categoryTitle.setText(mArticles.get(i).title);

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mArticles.get(position).views == -1) {
            return CATEGORY_TYPE;
        } else if (mArticles.get(position).title.equals("LOADING...")) {
            return LOADING_TYPE;
        } else if (mArticles.get(position).title.equals("EXHAUSTED...")) {
            return EXHAUSTED_TYPE;
        } else if (position == mArticles.size() - 1
                && position != 0
                && !mArticles.get(position).title.equals("EXHAUSTED...")) {
            return LOADING_TYPE;
        } else {
            return ARTICLE_TYPE;
        }
    }

    public void setQueryExhausted() {
        hideLoading();
        Result exhaustedArticle = new Result();
        exhaustedArticle.title = "EXHAUSTED...";
        mArticles.add(exhaustedArticle);
        notifyDataSetChanged();
    }

    private void hideLoading() {
        if (isLoading()) {
            for (Result article : mArticles) {
                if (article.title.equals("LOADING...")) {
                    mArticles.remove(article);
                }
            }
            notifyDataSetChanged();
        }
    }

    public void displayLoading() {
        if (!isLoading()) {
            Result article = new Result();
            article.title = "LOADING...";
            List<Result> loadingList = new ArrayList<>();
            loadingList.add(article);
            mArticles = loadingList;
            notifyDataSetChanged();
        }
    }

    private boolean isLoading() {
        if (mArticles != null) {
            if (mArticles.size() > 0) {
                return mArticles.get(mArticles.size() - 1).title.equals("LOADING...");
            }
        }
        return false;
    }

    public void displaySearchCategories() {
        List<Result> categories = new ArrayList<>();
        for (int i = 0; i < Constants.DEFAULT_SEARCH_CATEGORIES.length; i++) {
            Result article = new Result();
            article.title = Constants.DEFAULT_SEARCH_CATEGORIES[i];
            article.views = -1;
            categories.add(article);
        }
        mArticles = categories;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mArticles != null) {
            return mArticles.size();
        }
        return 0;
    }

    public void setArticles(List<Result> articles) {
        mArticles = articles;
        notifyDataSetChanged();
    }

    public Result getSelectedArticle(int position) {
        if (mArticles != null) {
            if (mArticles.size() > 0) {
                return mArticles.get(position);
            }
        }
        return null;
    }
}