package com.nyt.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nyt.R;
import com.nyt.models.Article;

public class ArticleActivity extends BaseActivity {

    // UI components
    private ImageView mArticleImage;
    private TextView mArticleTitle, mArticleViews, mArticleAbstract,
            mArticleSource, mArticleDate, mArticleByline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        mArticleImage = findViewById(R.id.article_image);
        mArticleTitle = findViewById(R.id.article_title);
        mArticleViews = findViewById(R.id.article_views);
        mArticleSource = findViewById(R.id.article_source);
        mArticleByline = findViewById(R.id.article_byline);
        mArticleAbstract = findViewById(R.id.article_abstract);
        mArticleDate = findViewById(R.id.article_date);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getIncomingIntent();
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("article")) {
            Article article = (Article) getIntent().getSerializableExtra("article");
            Log.d(TAG, "getIncomingIntent: " + article.title);
            setArticleProperties(article);
        }
    }

    private void setArticleProperties(Article article) {
        if (article != null) {
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);

            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .load(article.media.get(0).mediaMetadata.get(2).url)
                    .into(mArticleImage);

            mArticleTitle.setText(article.title);
            mArticleViews.setText(String.valueOf(article.views));
            mArticleSource.setText(article.source);
            mArticleByline.setText(article.byline);
            mArticleDate.setText(article.publishedDate);
            mArticleDate.setText(article._abstract);

        }
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return super.onNavigateUp();
    }
}