package org.fireflyest.crafttext.data;

import com.google.gson.annotations.SerializedName;

public class InteractExtraDTO extends ExtraDTO {

    @SerializedName("clickEvent")
    private ClickEventDTO clickEvent;
    @SerializedName("hoverEvent")
    private HoverEventDTO hoverEvent;

    public ClickEventDTO getClickEvent() {
        return clickEvent;
    }

    public void setClickEvent(ClickEventDTO clickEvent) {
        this.clickEvent = clickEvent;
    }

    public HoverEventDTO getHoverEvent() {
        return hoverEvent;
    }

    public void setHoverEvent(HoverEventDTO hoverEvent) {
        this.hoverEvent = hoverEvent;
    }

   

}
