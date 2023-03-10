package org.fireflyest.crafttext.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fireflyest
 * @since 1.2
 */
public class ColorText {

    @SerializedName("extra")
    private List<ExtraDTO> extra;

    @SerializedName("text")
    private String text;

    public ColorText() {
        this("");
    }

    public ColorText(String text) {
        this.extra = new ArrayList<>();
        this.text = text;
    }

    public List<ExtraDTO> getExtra() {
        return extra;
    }

    public void setExtra(List<ExtraDTO> extra) {
        this.extra = extra;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
