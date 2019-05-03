
package com.nyt.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nyt.models.Article;

import java.io.Serializable;
import java.util.List;

public class ArticleResponse implements Serializable {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("copyright")
    @Expose
    public String copyright;
    @SerializedName("num_results")
    @Expose
    public Long numResults;
    @SerializedName("results")
    @Expose
    public List<Article> results = null;
    private final static long serialVersionUID = 8690888635746283891L;

}