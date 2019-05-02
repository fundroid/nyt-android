package com.nyt.utils;

import android.util.Log;
import com.nyt.models.Result;

import java.util.List;

public class Testing {

    public static void printArticles(List<Result>list, String tag){
        for(Result article: list){
            Log.d(tag, "onChanged: " + article.title);
        }
    }
}