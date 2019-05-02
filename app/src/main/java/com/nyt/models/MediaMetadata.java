
package com.nyt.models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MediaMetadata implements Serializable
{

    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("format")
    @Expose
    public String format;
    @SerializedName("height")
    @Expose
    public Long height;
    @SerializedName("width")
    @Expose
    public Long width;
    private final static long serialVersionUID = -2155652227056068443L;

}
