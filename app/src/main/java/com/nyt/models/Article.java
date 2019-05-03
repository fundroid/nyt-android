
package com.nyt.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Article implements Serializable {

    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("adx_keywords")
    @Expose
    public String adxKeywords;
    @SerializedName("column")
    @Expose
    public Object column;
    @SerializedName("section")
    @Expose
    public String section;
    @SerializedName("byline")
    @Expose
    public String byline;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("abstract")
    @Expose
    public String _abstract;
    @SerializedName("published_date")
    @Expose
    public String publishedDate;
    @SerializedName("source")
    @Expose
    public String source;
    @SerializedName("views")
    @Expose
    public int views;
    @SerializedName("media")
    @Expose
    public List<Media> media = null;

    private final static long serialVersionUID = -6964970351707308230L;

}
