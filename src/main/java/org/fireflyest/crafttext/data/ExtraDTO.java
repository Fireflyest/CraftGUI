package org.fireflyest.crafttext.data;

import com.google.gson.annotations.SerializedName;

public class ExtraDTO {

    @SerializedName("bold")
    private Boolean bold;
    @SerializedName("italic")
    private Boolean italic;
    @SerializedName("underlined")
    private Boolean underlined;
    @SerializedName("strikethrough")
    private Boolean strikethrough;
    @SerializedName("obfuscated")
    private Boolean obfuscated;
    @SerializedName("color")
    private String color;
    @SerializedName("text")
    private String text;

    public Boolean getBold() {
        return bold;
    }

    public void setBold(Boolean bold) {
        this.bold = bold;
    }

    public Boolean getItalic() {
        return italic;
    }

    public void setItalic(Boolean italic) {
        this.italic = italic;
    }

    public Boolean getUnderlined() {
        return underlined;
    }

    public void setUnderlined(Boolean underlined) {
        this.underlined = underlined;
    }

    public Boolean getStrikethrough() {
        return strikethrough;
    }

    public void setStrikethrough(Boolean strikethrough) {
        this.strikethrough = strikethrough;
    }

    public Boolean getObfuscated() {
        return obfuscated;
    }

    public void setObfuscated(Boolean obfuscated) {
        this.obfuscated = obfuscated;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
