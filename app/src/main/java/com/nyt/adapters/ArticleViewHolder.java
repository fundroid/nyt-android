package com.nyt.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nyt.R;

public class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView title, publisher, socialScore;
    AppCompatImageView image;
    OnArticleListener onArticleListener;

    public ArticleViewHolder(@NonNull View itemView, OnArticleListener onArticleListener) {
        super(itemView);

        this.onArticleListener = onArticleListener;

        title = itemView.findViewById(R.id.article_title);
        publisher = itemView.findViewById(R.id.article_publisher);
        socialScore = itemView.findViewById(R.id.article_social_score);
        image = itemView.findViewById(R.id.article_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onArticleListener.onArticleClick(getAdapterPosition());
    }
}
