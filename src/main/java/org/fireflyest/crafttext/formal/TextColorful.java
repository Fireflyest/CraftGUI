package org.fireflyest.crafttext.formal;

import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.util.NumberConversions;
import org.fireflyest.crafttext.data.Text;
import org.fireflyest.util.ColorUtils;

/**
 * @author Fireflyest
 * @since 2022/8/30
 */
public class TextColorful {

    private static final Pattern attributePattern = Pattern.compile("\\$<([^<]*)>");
    private static final Pattern varPattern = Pattern.compile("[a-z]{0,}=[#:a-z0-9A-Z]{0,}");
    private final Gson gson;
    private final Text text;
    private final Text formalText;

    /**
     * 格式化文本
     * @param string 源文本
     */
    public TextColorful(String string) {
        this.gson = new Gson();
        this.text = gson.fromJson(string, Text.class);
        this.formalText = new Text(text.getText());

        this.formalText();
    }

    /**
     * 获取源文本
     * @return 源文本
     */
    public Text getText() {
        return text;
    }

    /**
     * 获取格式化文本
     * @return 格式化文本
     */
    public Text getFormalText() {
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
        for (Text.ExtraDTO extraDTO : text.getExtra()) {
            String sentence = extraDTO.getText();
            Matcher attributeMatcher = attributePattern.matcher(sentence);
            String[] splitTexts = null;
            int pos = 0;

            // 由属性分割字符串，然后给分割后的字符串附上属性
            while (attributeMatcher.find()) {
                if (splitTexts == null) splitTexts = sentence.split("\\$<([^<]*)>");
                String attribute = attributeMatcher.group();
                String textValue = splitTexts[++pos];
                Matcher varMatcher = varPattern.matcher(attribute);

                // 获取属性中的全部变量键值对
                while (varMatcher.find()) {
                    String[] colorVar = varMatcher.group().split("=");
                    this.formalSentence(extraDTO, textValue, colorVar[0], colorVar[1]);                    
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
    private void formalSentence(Text.ExtraDTO extraDTO, String textValue, String key, String value) {
        Text.ExtraDTO partExtraDTO;
        String startColor;
        String endColor;
        switch (key) {
            case "hg": // 水平渐变 $<hg=#FFFFFF:#000000>
                startColor = value.split(":")[0];
                endColor = value.split(":")[1];
                if (startColor == null || endColor == null) return;
                int charPos = 0;
                for (String color : ColorUtils.gradient(startColor, endColor, textValue.length())) {
                    partExtraDTO = new Text.ExtraDTO();
                    partExtraDTO.setBold(extraDTO.getBold());
                    partExtraDTO.setItalic(extraDTO.getItalic());
                    partExtraDTO.setObfuscated(extraDTO.getObfuscated());
                    partExtraDTO.setStrikethrough(extraDTO.getStrikethrough());
                    partExtraDTO.setUnderlined(extraDTO.getUnderlined());
                    partExtraDTO.setText(String.valueOf(textValue.charAt(charPos++)));
                    partExtraDTO.setColor(color);
                    formalText.getExtra().add(partExtraDTO);
                }
                break;
            case "vg": // 垂直渐变 $<vg=#FFFFFF:#000000:5:4>
                startColor = value.split(":")[0];
                endColor = value.split(":")[1];
                if (startColor == null || endColor == null) return;
                // 计算渐变颜色
                int num = NumberConversions.toInt(value.split(":")[2]);
                String[] colors = ColorUtils.gradient(startColor, endColor, num);
                // 获取对应的颜色
                int level = NumberConversions.toInt(value.split(":")[3]);
                if (level >= colors.length) level = colors.length - 1;
                if (level < 0) level = 0;
                partExtraDTO = new Text.ExtraDTO();
                partExtraDTO.setBold(extraDTO.getBold());
                partExtraDTO.setItalic(extraDTO.getItalic());
                partExtraDTO.setObfuscated(extraDTO.getObfuscated());
                partExtraDTO.setStrikethrough(extraDTO.getStrikethrough());
                partExtraDTO.setUnderlined(extraDTO.getUnderlined());
                partExtraDTO.setText(textValue);
                partExtraDTO.setColor(colors[level]);
                formalText.getExtra().add(partExtraDTO);
                break;
            case "c": // 颜色 $<c=#FFFFFF>
                partExtraDTO = new Text.ExtraDTO();
                partExtraDTO.setBold(extraDTO.getBold());
                partExtraDTO.setItalic(extraDTO.getItalic());
                partExtraDTO.setObfuscated(extraDTO.getObfuscated());
                partExtraDTO.setStrikethrough(extraDTO.getStrikethrough());
                partExtraDTO.setUnderlined(extraDTO.getUnderlined());
                partExtraDTO.setText(textValue);
                partExtraDTO.setColor(value);
                formalText.getExtra().add(partExtraDTO);
                break;
            default:
        }
    }

}
