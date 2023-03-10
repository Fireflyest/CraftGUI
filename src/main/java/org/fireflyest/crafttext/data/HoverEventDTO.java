package org.fireflyest.crafttext.data;

import com.google.gson.annotations.SerializedName;

public class HoverEventDTO {

        @SerializedName("action")
        private String action;
        @SerializedName("value")
        private String value;
        @SerializedName("contents")
        private HoverEventContentsDTO contents;

        public String getAction() {
            return action;
        }
        public void setAction(String action) {
            this.action = action;
        }
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }
        public HoverEventContentsDTO getContents() {
            return contents;
        }
        public void setContents(HoverEventContentsDTO contents) {
            this.contents = contents;
        }
}
