
package com.nyt.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Result implements Serializable {

    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("adx_keywords")
    @Expose
    public String adxKeywords;
    //    @SerializedName("column")
//    @Expose
//    public Object column;
//    @SerializedName("section")
//    @Expose
//    public String section;
//    @SerializedName("byline")
//    @Expose
//    public String byline;
//    @SerializedName("type")
//    @Expose
//    public String type;
    @SerializedName("title")
    @Expose
    public String title;
    //    @SerializedName("abstract")
//    @Expose
//    public String _abstract;
//    @SerializedName("published_date")
//    @Expose
//    public String publishedDate;
//    @SerializedName("source")
//    @Expose
//    public String source;
//    @SerializedName("id")
//    @Expose
//    public Long id;
//    @SerializedName("asset_id")
//    @Expose
//    public Long assetId;
    @SerializedName("views")
    @Expose
    public int views;
    //    @SerializedName("des_facet")
//    @Expose
//    public List<String> desFacet = null;
//    @SerializedName("org_facet")
//    @Expose
//    public String orgFacet;
//    @SerializedName("per_facet")
//    @Expose
//    public List<String> perFacet = null;
//    @SerializedName("geo_facet")
//    @Expose
//    public String geoFacet;
    @SerializedName("media")
    @Expose
    public List<Media> media = null;
    //    @SerializedName("uri")
//    @Expose
//    public String uri;
    private final static long serialVersionUID = -6964970351707308230L;

}
