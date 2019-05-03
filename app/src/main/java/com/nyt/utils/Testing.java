package com.nyt.utils;

import android.util.Log;
import com.nyt.models.Article;

import java.util.List;

public class Testing {

    public static void printArticles(List<Article>list, String tag){
        for(Article article: list){
            Log.d(tag, "onChanged: " + article.title);
        }
    }
}