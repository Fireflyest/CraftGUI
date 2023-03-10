package org.fireflyest.crafttext.data;

import com.google.gson.annotations.SerializedName;

public class HoverEventContentsDTO {
    @SerializedName("id")
    private String id;
    @SerializedName("tag")
    private String tag;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
}
