package org.fireflyest.craftitem.interact;

public class InteractAction {

    // 类型 触发方式
    public static final String NBT_INTERACT_TRIGGER = "craft-interact-trigger";
    // 冷却
    public static final String NBT_INTERACT_COOLDOWN = "craft-interact-cooldown";
    // 耐久
    public static final String NBT_INTERACT_DURABILITY = "craft-interact-durability";


    public static final int TRIGGER_NONE = 0;
    
    // 食物 吃完触发行为
    public static final String TRIGGER_EAT = "eat";
    // 消耗品 右键触发行为
    public static final String TRIGGER_USE = "use";
    // 武器 攻击到人后触发行为
    public static final String TRIGGER_ATTACK = "attack";
    // 道具 右键生物或者方块触发行为
    public static final String TRIGGER_ENTITY = "entity";
    public static final String TRIGGER_BLOCK = "block";
    // 切换到手上时触发
    public static final String TRIGGER_HOLD = "hold";


}
