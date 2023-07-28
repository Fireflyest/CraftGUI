package org.fireflyest.crafttext.formal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.util.NumberConversions;
import org.fireflyest.crafttext.data.ClickEventDTO;
import org.fireflyest.crafttext.data.HoverEventContentsDTO;
import org.fireflyest.crafttext.data.HoverEventDTO;
import org.fireflyest.crafttext.data.InteractExtraDTO;
import org.fireflyest.crafttext.data.InteractText;
import org.fireflyest.util.ColorUtils;

import com.google.gson.Gson;

public class TextInteractFormal {
    private static final Pattern attributePattern = Pattern.compile("\\$<([^<]*)>");
    private static final Pattern varPattern = Pattern.compile("[a-z]+=[#:a-z0-9A-Z][^|>]+");

    private final Gson gson;
    private final InteractText originText;
    private final InteractText formalText;

    /**
     * 格式化文本
     * @param string 源文本
     */
    public TextInteractFormal(String string) {
        this.gson = new Gson();
        this.originText = gson.fromJson(string, InteractText.class);
        this.formalText = new InteractText(originText.getText());

        this.formalText();
    }

    /**
     * 获取源文本
     * @return 源文本
     */
    public InteractText getOriginText() {
        return originText;
    }

    /**
     * 获取格式化文本
     * @return 格式化文本
     */
    public InteractText getFormalText() {
        return formalText;
    }

    /**
     * 将文本转化为数据
     * @return 文本数据
     */
    public String toString() {
        return gson.toJson(formalText);
    }

    /**
     * 格式化文本
     */
    private void formalText() {
        // 遍历每一个句子
        for (InteractExtraDTO extraDTO : originText.getExtra()) {

            String sentence = extraDTO.getText();
            // 判断是否有参数
            if (sentence == null || !sentence.contains("$")) {
                formalText.getExtra().add(extraDTO);
                continue;
            }

            Matcher attributeMatcher = attributePattern.matcher(sentence);
            String[] splitTexts = sentence.split("\\$<([^<]*)>");

            InteractExtraDTO startExtraDTO = new InteractExtraDTO();
            startExtraDTO.setBold(extraDTO.getBold());
            startExtraDTO.setItalic(extraDTO.getItalic());
            startExtraDTO.setObfuscated(extraDTO.getObfuscated());
            startExtraDTO.setStrikethrough(extraDTO.getStrikethrough());
            startExtraDTO.setUnderlined(extraDTO.getUnderlined());
            startExtraDTO.setText(splitTexts[0]);
            startExtraDTO.setColor(extraDTO.getColor());
            formalText.getExtra().add(startExtraDTO);

            int pos = 1;

            // 由属性分割字符串，然后给分割后的字符串附上属性
            while (attributeMatcher.find()) {
                String attribute = attributeMatcher.group();
                
                if (pos == splitTexts.length) break;
                String textValue = splitTexts[pos++];
                Matcher varMatcher = varPattern.matcher(attribute);

                ClickEventDTO clickEventDTO = null;
                HoverEventDTO hoverEventDTO = null;
                // 获取属性中的全部变量键值对
                while (varMatcher.find()) {
                    String[] colorVar = varMatcher.group().split("=");
                    if ("ce".equals(colorVar[0])) {
                        String[] kv = colorVar[1].split("•");
                        clickEventDTO = new ClickEventDTO();
                        clickEventDTO.setAction(kv[0]);
                        clickEventDTO.setValue(kv[1]);
                    } else if ("he".equals(colorVar[0])) {
                        String[] kv = colorVar[1].split("•");
                        if (kv.length == 2) {
                            hoverEventDTO = new HoverEventDTO();
                            hoverEventDTO.setAction(kv[0]);
                            hoverEventDTO.setValue(kv[1]);
                        } else if (kv.length == 3) {
                            hoverEventDTO = new HoverEventDTO();
                            HoverEventContentsDTO hoverEventContentsDTO = new HoverEventContentsDTO();
                            hoverEventContentsDTO.setId(kv[1]);
                            hoverEventContentsDTO.setTag(kv[2]);
                            hoverEventDTO.setAction(kv[0]);
                            hoverEventDTO.setContents(hoverEventContentsDTO);
                        }
                        
                    } else {
                        this.formalSentence(extraDTO, clickEventDTO, hoverEventDTO, textValue, colorVar[0], colorVar[1]);
                    }
                }
            }
        }
    }

    /**
     * 格式化句子
     * @param extraDTO 源数据
     * @param textValue 根据属性分割后的文本
     * @param key 属性键
     * @param value 属性值
     */
    private void formalSentence(InteractExtraDTO extraDTO, ClickEventDTO clickEventDTO, HoverEventDTO hoverEventDTO, 
            String textValue, String key, String value) {
        InteractExtraDTO partExtraDTO;
        String startColor;
        String endColor;
        switch (key) {
            case "hg": // 水平渐变§r $<hg=#FFFFFF:#000000>
                startColor = value.split(":")[0];
                endColor = value.split(":")[1];
                if (startColor == null || endColor == null) return;
                int charPos = 0;
                for (String color : ColorUtils.gradient(startColor, endColor, textValue.length())) {
                    partExtraDTO = new InteractExtraDTO();
                    partExtraDTO.setBold(extraDTO.getBold());
                    partExtraDTO.setItalic(extraDTO.getItalic());
                    partExtraDTO.setObfuscated(extraDTO.getObfuscated());
                    partExtraDTO.setStrikethrough(extraDTO.getStrikethrough());
                    partExtraDTO.setUnderlined(extraDTO.getUnderlined());
                    partExtraDTO.setText(String.valueOf(textValue.charAt(charPos++)));
                    partExtraDTO.setColor(color);
                    partExtraDTO.setClickEvent(clickEventDTO);
                    partExtraDTO.setHoverEvent(hoverEventDTO);
                    formalText.getExtra().add(partExtraDTO);
                }
                break;
            case "vg": // 垂直渐变 §r$<vg=#FFFFFF:#000000:5:4>
                startColor = value.split(":")[0];
                endColor = value.split(":")[1];
                if (startColor == null || endColor == null) return;
                // 计算渐变颜色
                int num = NumberConversions.toInt(value.split(":")[2]);
                String[] colors = ColorUtils.gradient(startColor, endColor, num);
                // 获取对应的颜色
                int phase = NumberConversions.toInt(value.split(":")[3]);
                if (phase >= colors.length) phase = colors.length - 1;
                if (phase < 0) phase = 0;
                partExtraDTO = new InteractExtraDTO();
                partExtraDTO.setBold(extraDTO.getBold());
                partExtraDTO.setItalic(extraDTO.getItalic());
                partExtraDTO.setObfuscated(extraDTO.getObfuscated());
                partExtraDTO.setStrikethrough(extraDTO.getStrikethrough());
                partExtraDTO.setUnderlined(extraDTO.getUnderlined());
                partExtraDTO.setText(textValue);
                partExtraDTO.setColor(colors[phase]);
                partExtraDTO.setClickEvent(clickEventDTO);
                partExtraDTO.setHoverEvent(hoverEventDTO);
                formalText.getExtra().add(partExtraDTO);
                break;
            case "c": // 颜色 §r$<c=#FFFFFF>
                partExtraDTO = new InteractExtraDTO();
                partExtraDTO.setBold(extraDTO.getBold());
                partExtraDTO.setItalic(extraDTO.getItalic());
                partExtraDTO.setObfuscated(extraDTO.getObfuscated());
                partExtraDTO.setStrikethrough(extraDTO.getStrikethrough());
                partExtraDTO.setUnderlined(extraDTO.getUnderlined());
                partExtraDTO.setText(textValue);
                partExtraDTO.setColor(value);
                partExtraDTO.setClickEvent(clickEventDTO);
                partExtraDTO.setHoverEvent(hoverEventDTO);
                formalText.getExtra().add(partExtraDTO);
                break;
            default:
        }
    }

}
