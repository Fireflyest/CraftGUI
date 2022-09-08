package org.fireflyest.crafttext.formal;

import com.google.gson.Gson;
import org.bukkit.Color;
import org.fireflyest.crafttext.data.Text;
import org.fireflyest.util.ColorUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public TextColorful(String string) {
        this.gson = new Gson();
        this.text = gson.fromJson(string, Text.class);
        this.formalText = new Text(text.getText());

        // 遍历每一行
        for (Text.ExtraDTO extraDTO : text.getExtra()) {
            String aText = extraDTO.getText();
            Matcher attributeMatcher = attributePattern.matcher(aText);
            String[] splitTexts = null;
            int pos = 0;

            // 由属性分割字符串，然后给分割后的字符串附上属性
            while (attributeMatcher.find()){
                if (splitTexts == null) splitTexts = aText.split("\\$<([^<]*)>");
                String attribute = attributeMatcher.group();
                String textValue = splitTexts[++pos];
                Matcher varMatcher = varPattern.matcher(attribute);

                // 获取属性中的全部变量键值对
                while (varMatcher.find()){
                    String[] var = varMatcher.group().split("=");
                    String varKey = var[0], varValue = var[1];
                    switch (varKey){
                        case "colors":
                            int num = textValue.length();
                            String startColor = varValue.split(":")[0];
                            String endColor = varValue.split(":")[1];
                            int charPos = 0;
                            for (Color color : ColorUtils.gradient(startColor, endColor, num)) {
                                Text.ExtraDTO partExtraDTO = new Text.ExtraDTO();
                                partExtraDTO.setBold(extraDTO.getBold());
                                partExtraDTO.setItalic(extraDTO.getItalic());
                                partExtraDTO.setObfuscated(extraDTO.getObfuscated());
                                partExtraDTO.setStrikethrough(extraDTO.getStrikethrough());
                                partExtraDTO.setUnderlined(extraDTO.getUnderlined());
                                partExtraDTO.setText(String.valueOf(textValue.charAt(charPos++)));
                                partExtraDTO.setColor(ColorUtils.toString(color));
                                formalText.getExtra().add(partExtraDTO);
                            }
                            break;
                        case "color":
                            Text.ExtraDTO partExtraDTO = new Text.ExtraDTO();
                            partExtraDTO.setBold(extraDTO.getBold());
                            partExtraDTO.setItalic(extraDTO.getItalic());
                            partExtraDTO.setObfuscated(extraDTO.getObfuscated());
                            partExtraDTO.setStrikethrough(extraDTO.getStrikethrough());
                            partExtraDTO.setUnderlined(extraDTO.getUnderlined());
                            partExtraDTO.setText(String.valueOf(textValue));
                            partExtraDTO.setColor(varValue);
                            formalText.getExtra().add(partExtraDTO);
                            break;
                        default:
                    }
                }
            }
        }
    }

    public Text getText() {
        return text;
    }

    public Text getFormalText() {
        return formalText;
    }

    public String toString(){
        return gson.toJson(formalText);
    }

}
