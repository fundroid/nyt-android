
package com.nyt.models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Media implements Serializable {

    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("subtype")
    @Expose
    public String subtype;
    @SerializedName("caption")
    @Expose
    public String caption;
    @SerializedName("copyright")
    @Expose
    public String copyright;
    @SerializedName("approved_for_syndication")
    @Expose
    public Long approvedForSyndication;
    @SerializedName("media-metadata")
    @Expose
    public List<MediaMetadata> mediaMetadata = null;
    private final static long serialVersionUID = -2893942914885036890L;

}
