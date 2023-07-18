package org.fireflyest.craftitem.interact;

public class InteractAction {
    
    public static final String INTERACT_COOLDOWN = "craft-interact-cooldown";
    public static final String INTERACT_DURABILITY = "craft-interact-durability";


    // value actionName;durability;cooldown;num
    // 食物 吃完触发行为
    public static final String TRIGGER_EAT = "craft-interact-eat";
    // 消耗品 右键触发行为
    public static final String TRIGGER_USE = "craft-interact-use";
    // 武器 攻击到人后触发行为
    public static final String TRIGGER_ATTACK = "craft-interact-attack";
    public static final String TRIGGER_KILL = "craft-interact-kill";
    // 道具 右键生物或者方块触发行为
    public static final String TRIGGER_ENTITY = "craft-interact-entity";
    public static final String TRIGGER_BLOCK = "craft-interact-block";
    public static final String TRIGGER_PLACE = "craft-interact-place";
    public static final String TRIGGER_BREAK = "craft-interact-break";
    // 切换到手上时触发
    public static final String TRIGGER_HOLD = "craft-interact-hold";



    public static final String ACTION_CUSTOM = "custom";
    public static final String ACTION_CUSTOM_DISPOSABLE = "custom-disposable";
    public static final String ACTION_POTION = "potion";
    public static final String ACTION_COMMAND = "command";
    public static final String ACTION_CONSOLE = "console";
    public static final String ACTION_CONSOLE_DISPOSABLE = "console-disposable";


}
