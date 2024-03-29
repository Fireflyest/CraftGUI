package org.fireflyest.craftgui.util;

import com.cryptomorin.xseries.SkullUtils;
import com.cryptomorin.xseries.XEnchantment;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import org.bukkit.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.fireflyest.CraftGUI;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

@Deprecated
public class SerializeCustomUtil {

    private SerializeCustomUtil(){
    }

    private static Method deserialize;
    private static final Map<String, ItemMeta> metaStorage = new HashMap<>();
    private static final Map<String, ItemStack> stackStorage = new HashMap<>();

    private static boolean debug = false;

    static {
        Class<?> clazz = null;
        try {
            String versionPacket = "";
            // 找到版本包
            for (Package pack : Package.getPackages()) {
                String name = pack.getName();
                if ( ! name.startsWith("org.bukkit.craftbukkit.v")) continue;
                versionPacket = pack.getName().split("\\.")[3];
                break;
            }
            if ("".equals(versionPacket)) Bukkit.getLogger().severe("[CraftGUI] The versionPacket not found!");
            // 获取类
            clazz = Class.forName(
                    String.format("org.bukkit.craftbukkit.%s.inventory.CraftMetaItem$SerializableMeta", versionPacket));
        } catch (ClassNotFoundException ignore) {}
        if (clazz != null) {
            try {
                deserialize = clazz.getMethod("deserialize", Map.class);
            } catch (NoSuchMethodException e) {
                Bukkit.getLogger().severe("[CraftGUI] The method of deserialize not found!");
                e.printStackTrace();
            }
        }else {
            Bukkit.getLogger().severe("[CraftGUI] The SerializableMeta.class not found!");
        }
    }

    public static void setDebug(boolean debug) {
        SerializeCustomUtil.debug = debug;
    }

    /**
     * 析构堆
     * @param itemStack 物品
     * @return 堆析构后的文本
     */
    public static String serializeItemStack(ItemStack itemStack) {
        Gson gson = new Gson();
        ItemStack item = itemStack.clone();
        item.setItemMeta(null);
        return gson.toJson(item.serialize());
    }

    /**
     * 析构元
     * @param itemStack 物品
     * @return 元析构后的文本
     */
    public static String serializeItemMeta(ItemStack itemStack){
        return serializeItemMeta(itemStack, false);
    }

    public static String serializeItemMeta(ItemStack itemStack, boolean mysql){
        Gson gson = new Gson();

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return "";

        // 1.18之后头颅meta存储方式改变，不存在nbt中了
        Map<String, Object> objectMap = itemMeta.serialize();
        if (itemMeta instanceof SkullMeta && CraftGUI.BUKKIT_VERSION > 17) {
            SkullMeta skullMeta = ((SkullMeta) itemMeta);
            if (objectMap.containsKey("skull-owner")) {
                objectMap = new HashMap<>(objectMap);
                objectMap.put("skull-owner", skullMeta.getOwner()+ "@" + SkullUtils.getSkinValue(skullMeta));
            }
        }

        String json = gson.toJson(objectMap);
        if (mysql){
            json = formalText(json);
        }
        return json;
    }
    /**
     * 解析物品
     * @param itemStack 堆
     * @param itemMeta 元
     * @return 物品
     */
    public static ItemStack deserialize(String itemStack, String itemMeta) {
        Gson gson = new Gson();
        Map<String, Object> itemMap;
        Map<String, Object> metaMap;
        Type type = new TypeToken<LinkedHashMap<String, Object>>() {}.getType();

        ItemStack item;
        itemMap = gson.fromJson(itemStack, type);

        // 先从缓存查是否解析过
        if (stackStorage.containsKey(itemStack)){
            item = stackStorage.get(itemStack).clone();
        }else {
            item = ItemStack.deserialize(itemMap);

            // 缓存
            stackStorage.put(itemStack, item);
        }

        // 判断是否有解析过这个元
        if(!"".equals(itemMeta)){
            ItemMeta meta;
            if (metaStorage.containsKey(itemMeta) && metaStorage.get(itemMeta) != null){
                meta = metaStorage.get(itemMeta).clone();
            }else {
                metaMap = gson.fromJson(itemMeta, type);
                meta = deserializeItemMeta(metaMap);

                // 缓存
                metaStorage.put(itemMeta, meta);
            }
            item.setItemMeta(meta);
        }

        return item;
    }

    private static String formalText(String text){
        String value = text.substring(1, text.length()-1);
        if (!value.contains("{")) return text;

        StringBuilder textBuilder = new StringBuilder("{");
        int index;
        if ((index = value.indexOf("\"display-name\":\"")) != -1){
            textBuilder.append(value, 0, index+="\"display-name\":\"".length());
            StringBuilder valueBuilder = new StringBuilder();
            int layer = 0;
            for ( ; index < value.length(); index++){
                char c = value.charAt(index);
                valueBuilder.append(c == '\"' ? "\\\"" : c);
                if (c == '{') {
                    layer++;
                }else  if (c == '}') {
                    layer--;
                    if (layer == 0) break;
                }
            }
            textBuilder.append(valueBuilder);
            value = value.substring(index+1);
        }
        if ((index = value.indexOf("\"lore\":")) != -1){
            textBuilder.append(value, 0, index+="\"lore\":".length());
            StringBuilder valueBuilder = new StringBuilder();
            int layer = 0, loreLayer = 0;
            for ( ; index < value.length(); index++){
                char c = value.charAt(index);
                valueBuilder.append(c == '\"' && layer > 0 ? "\\\"" : c);
                if (c == '{') {
                    layer++;
                } else  if (c == '}') {
                    layer--;
                } else if (c == '[') {
                    loreLayer++;
                } else if (c == ']') {
                    loreLayer--;
                    if (loreLayer == 0) break;
                }
            }
            textBuilder.append(valueBuilder);
            value = value.substring(index+1);
        }
        if ((index = value.indexOf("\"pages\":")) != -1){
            textBuilder.append(value, 0, index+="\"pages\":".length());
            StringBuilder valueBuilder = new StringBuilder();
            int layer = 0, loreLayer = 0;
            for ( ; index < value.length(); index++){
                char c = value.charAt(index);
                valueBuilder.append(c == '\"' && layer > 0 ? "\\\"" : c);
                if (c == '{') {
                    layer++;
                } else  if (c == '}') {
                    layer--;
                } else if (c == '[') {
                    loreLayer++;
                } else if (c == ']') {
                    loreLayer--;
                    if (loreLayer == 0) break;
                }
            }
            textBuilder.append(valueBuilder);
            value = value.substring(index+1);
        }
        textBuilder.append(value)
                .append("}");
        return textBuilder.toString();
    }

    /**
     * 解析元
     * @param map 键值
     * @return 元
     */
    private static ItemMeta deserializeItemMeta(Map<String, Object> map) {
        if (deserialize == null) {
            if (debug)CraftGUI.getPlugin().getLogger().info("error：the method of deserialize not found!");
            return null;
        }

        ItemMeta meta;
        String metaType = String.valueOf(map.get("meta-type"));

        // 耐久装备
        double damage = 0.0, repairCost = 0.0;
        // 附魔装备 附魔书
        String enchants = null;
        // 皮革甲颜色
        boolean isLeather = false;
        double red = 0, green = 0, blue = 0;
        // 附魔书
        String storageEnchants = null;
        // 烟花核
        boolean fireworkEffect = false;
        List<LinkedTreeMap<? ,?>> effectTreeMapList = new ArrayList<>();
        // 烟花
        boolean firework = false;
        double power = -1;
        // 旗帜图案
        boolean hasPatterns = false;
        List<Pattern> patternList = new ArrayList<>();
        // 地图
        double mapId = -1;
        // 美西螈
        float axolotlVariant;
        // 热带雨
        float tropicalFishVariant;
        // 头颅
        String skull = null;

        // 先处理部分meta数据
        switch (metaType){
            case "LEATHER_ARMOR" : {
                if ( !map.containsKey("color") ) break;
                isLeather = true;
                LinkedTreeMap<? ,?> treeMap = ((LinkedTreeMap<? ,?>) map.get("color"));
                red = (Double)(treeMap.get("red"));
                green = (Double)(treeMap.get("green"));
                blue = (Double)(treeMap.get("blue"));
                if (red < 0) red += 256;
                if (green < 0) green += 256;
                if (blue < 0) blue += 256;
                map.remove("color");
                break;
            }
            case "ENCHANTED" : {
                if ( !map.containsKey("stored-enchants") ) break;
                storageEnchants =
                        map.get("stored-enchants")
                                .toString()
                                .replace("{", "")
                                .replace("}", "")
                                .replace("\"", "");
                map.remove("stored-enchants");
                break;
            }
            case "SKULL": {
                if ( map.containsKey("skull-owner") && CraftGUI.BUKKIT_VERSION > 17){
                    String[] skullData = map.get("skull-owner").toString().split("@");
                    if (skullData.length > 1) {
                        skull = skullData[1];
                        map.put("skull-owner", skullData[0]);
                    }
                }
                break;
            }
            case "FIREWORK_EFFECT" : {
                if ( map.containsKey("firework-effect") ){
                    fireworkEffect = true;
                    effectTreeMapList.add((LinkedTreeMap<? ,?>) map.get("firework-effect"));
                    map.remove("firework-effect");
                }
                if (debug) CraftGUI.getPlugin().getLogger().info(String.format("lose key %s for firework-effect", map.get("type")));
                map.remove("type");
                break;
            }
            case "FIREWORK" : {
                if (map.containsKey("firework-effects")){
                    firework = true;
                    ArrayList<?> effectList = ((ArrayList<?>) map.get("firework-effects"));
                    if (effectList != null) {
                        for (Object o : effectList) effectTreeMapList.add((LinkedTreeMap<? ,?>)o);
                    }
                    map.remove("firework-effects");
                }
                if (map.containsKey("power")){
                    power = (Double) map.get("power");
                    map.remove("power");
                }
                break;
            }
            case "BANNER" : {
                if ( !map.containsKey("patterns") ) break;
                hasPatterns = true;
                List<?>patternMapList = (List<?>) map.get("patterns");
                if (patternMapList != null) {
                    for (Object o : patternMapList) {
                        LinkedTreeMap<?,?> treeMap = (LinkedTreeMap<?, ?>)o;
                        String color = treeMap.get("color").toString();
                        String pattern = treeMap.get("pattern").toString();
                        Pattern p = new Pattern(DyeColor.valueOf(color), PatternType.valueOf(pattern));
                        patternList.add(p);
                    }
                }
                map.remove("patterns");
                break;
            }
            case "MAP" : {
                if ( !map.containsKey("map-id") ) break;
                mapId = (Double) map.get("map-id");
                map.remove("map-id");
                break;
            }
            case "AXOLOTL_BUCKET":{
                if ( !map.containsKey("axolotl-variant") ) break;
                axolotlVariant = Float.parseFloat(map.get("axolotl-variant").toString());
                if (debug) CraftGUI.getPlugin().getLogger().info(String.format("lose key %s for axolotl-variant", axolotlVariant));
                map.remove("axolotl-variant");
                break;
            }
            case "TROPICAL_FISH_BUCKET":{
                if ( !map.containsKey("fish-variant") ) break;
                tropicalFishVariant = Float.parseFloat(map.get("fish-variant").toString());
                if (debug) CraftGUI.getPlugin().getLogger().info(String.format("lose key %s for fish-variant", tropicalFishVariant));
                map.remove("fish-variant");
                break;
            }
            default : {}
        }

        // 遍历其他nbt
        Iterator<Map.Entry<String, Object>> mapIterator = map.entrySet().iterator();
        while (mapIterator.hasNext()){
            Map.Entry<String, Object> entry = mapIterator.next();
            switch (entry.getKey()){
                case "custom-model-data" : {
                    // 针对某插件修复
                    String value = map.get("custom-model-data").toString();
                    if (! value.contains(".")) break;
                    // 转为整数类型
                    map.put("custom-model-data", ((int) Double.parseDouble(value)));
                    break;
                }
                case "Damage" :{
                    damage = (double)map.get("Damage");
                    mapIterator.remove();
                    break;
                }
                case "enchants" :{
                    enchants = map.get("enchants").toString().replace("{", "").replace("}", "").replace("\"", "");
                    mapIterator.remove();
                    break;
                }
                case "repair-cost" :{
                    repairCost = (double) map.get("repair-cost");
                    mapIterator.remove();
                    break;
                }
                default : {}
            }
        }

        meta = invokeMeta(map, metaType);
        if (meta == null) return null;

        if (isLeather) ((LeatherArmorMeta)meta).setColor(Color.fromRGB((int)red, (int)green, (int)blue));
        if (damage != 0) ((Damageable)meta).setDamage((int)damage);
        if (enchants != null) addEnchantments(meta, enchants.split(","));
        if (storageEnchants != null) addStorageEnchantments(meta, storageEnchants.split(","));
        if (fireworkEffect) {
            addFireworkEffects(meta, effectTreeMapList, false);
        }
        if (firework){
            addFireworkEffects(meta, effectTreeMapList, true);
        }
        if(power != -1){
            ((FireworkMeta)meta).setPower((int)power);
        }
        if (mapId != -1)
            ((MapMeta)meta).setMapId((int)mapId);
        if (repairCost != 0){
            ((Repairable)meta).setRepairCost(((int) repairCost));
        }
        if (hasPatterns) ((BannerMeta)meta).setPatterns(patternList);
        if (skull != null && CraftGUI.BUKKIT_VERSION > 17)  meta = SkullUtils.applySkin(meta, skull);
        return meta;
    }

    /**
     * 利用反射执行解析
     * @param map 键值
     * @return 元
     */
    private static ItemMeta invokeMeta(Map<String, Object> map, String metaType){
        // 解析
        try{
            return  (ItemMeta) deserialize.invoke(null, map);
        }catch (InvocationTargetException | IllegalAccessException e){
            if (debug) {
                CraftGUI.getPlugin().getLogger().severe(String.format("There were some errors deserializing item：%s", map));
            }else{
                CraftGUI.getPlugin().getLogger().severe(String.format("There were some errors deserializing item：%s", metaType));
            }
            e.printStackTrace();
        }
        return null;
    }

    private static void addFireworkEffects(ItemMeta meta, List<LinkedTreeMap<? ,?>> effectTreeMapList, boolean isFirework){
        List<FireworkEffect> effects = new ArrayList<>();
        for (LinkedTreeMap<?, ?> linkedTreeMap : effectTreeMapList) {
            boolean fireworkFlicker, fireworkTrail;
            double fadeRed = 0, fadeGreen = 0, fadeBlue = 0; // 淡化颜色
            double red = 0, green = 0, blue = 0;

            fireworkFlicker = (Boolean)(linkedTreeMap.get("flicker"));
            fireworkTrail = (Boolean)(linkedTreeMap.get("trail"));
            ArrayList<?> colorList = (ArrayList<?>)linkedTreeMap.get("colors");
            ArrayList<?> fadeColorList = (ArrayList<?>)linkedTreeMap.get("colors");
            LinkedTreeMap<?,?> colorsMap = null, fadeColorsMap = null;
            if (!colorList.isEmpty()){
                colorsMap = (LinkedTreeMap<?,?>) (colorList.get(0));
            }
            if (!fadeColorList.isEmpty()){
                fadeColorsMap = ((LinkedTreeMap<?,?>) (fadeColorList).get(0));
            }
            if (colorsMap != null) {
                red = (Double)(colorsMap.get("red"));
                green = (Double)(colorsMap.get("green"));
                blue = (Double)(colorsMap.get("blue"));
                if (red < 0) red += 256;
                if (green < 0) green += 256;
                if (blue < 0) blue += 256;
            }
            if (fadeColorsMap != null) {
                fadeRed = (Double)(fadeColorsMap.get("red"));
                fadeGreen = (Double)(fadeColorsMap.get("green"));
                fadeBlue = (Double)(fadeColorsMap.get("blue"));
                if (fadeRed < 0) fadeRed += 256;
                if (fadeGreen < 0) fadeGreen += 256;
                if (fadeBlue < 0) fadeBlue += 256;
            }

            FireworkEffect fe = FireworkEffect.builder()
                    .flicker(fireworkFlicker)
                    .trail(fireworkTrail)
                    .withColor(Color.fromRGB((int)red, (int)green, (int)blue))
                    .withFade(Color.fromRGB((int)fadeRed, (int)fadeGreen, (int)fadeBlue))
                    .build();
            effects.add(fe);
        }
        if (isFirework){
            ((FireworkMeta)meta).addEffects(effects);
        }else if (effects.size() != 0){
            ((FireworkEffectMeta)meta).setEffect(effects.get(0));
        }
    }

    private static void addEnchantments(ItemMeta meta, String[] enchantments){
        for (String s : enchantments) {
            String[] split = s.split("=");
            if (split.length > 0) {
                Optional<XEnchantment> xEnchantment = XEnchantment.matchXEnchantment(split[0]);
                Enchantment enchantment = null;
                if (xEnchantment.isPresent()){
                    enchantment = xEnchantment.get().getEnchant();
                }
                int level = (int) Float.parseFloat(split[1]);
                if (enchantment != null) meta.addEnchant(enchantment, level, true);
            }
        }
    }

    private static void addStorageEnchantments(ItemMeta meta, String[] enchantments){
        for (String s : enchantments) {
            String[] split = s.split("=");
            if (split.length > 0) {
                Optional<XEnchantment> xEnchantment = XEnchantment.matchXEnchantment(split[0]);
                Enchantment enchantment = null;
                if (xEnchantment.isPresent()){
                    enchantment = xEnchantment.get().getEnchant();
                }
                int level = (int) Float.parseFloat(split[1]);
                if (enchantment != null) ((EnchantmentStorageMeta)meta).addStoredEnchant(enchantment, level, true);
            }
        }
    }

}
