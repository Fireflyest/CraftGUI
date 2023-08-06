package org.fireflyest.craftitem.builder;

import org.bukkit.Material;
import org.fireflyest.craftitem.interact.InteractAction;

public class InteractItemBuilder extends ItemBuilder {

    public InteractItemBuilder(Material material) {
        super(material);
        nbt.put(InteractAction.INTERACT_COOLDOWN, 0);
        nbt.put(InteractAction.INTERACT_DURABILITY, -1);
    }

    public InteractItemBuilder(String material) {
        super(material);
        nbt.put(InteractAction.INTERACT_COOLDOWN, 0);
        nbt.put(InteractAction.INTERACT_DURABILITY, -1);
    }
    
    public InteractItemBuilder cooldown(int tick) {
        update = true;
        nbt.put(InteractAction.INTERACT_COOLDOWN, tick);
        return this;
    }

    public InteractItemBuilder durability(int durability) {
        update = true;
        nbt.put(InteractAction.INTERACT_DURABILITY, durability);
        return this;
    }

    public InteractItemBuilder trigger(String trigger, String action, String value){
        update = true;
        nbt.put(trigger, action + ":" + value);
        return this;
    }

    public InteractItemBuilder triggerEat(String action, String value) {
        update = true;
        nbt.put(InteractAction.TRIGGER_EAT, action + ":" + value);
        return this;
    }

    public InteractItemBuilder triggerUse(String action, String value) {
        update = true;
        nbt.put(InteractAction.TRIGGER_USE, action + ":" + value);
        return this;
    }

    public InteractItemBuilder triggerAttack(String action, String value) {
        update = true;
        nbt.put(InteractAction.TRIGGER_ATTACK, action + ":" + value);
        return this;
    }

    public InteractItemBuilder triggerKill(String action, String value) {
        update = true;
        nbt.put(InteractAction.TRIGGER_KILL, action + ":" + value);
        return this;
    }

    public InteractItemBuilder triggerEntity(String action, String value) {
        update = true;
        nbt.put(InteractAction.TRIGGER_ENTITY, action + ":" + value);
        return this;
    }

    public InteractItemBuilder triggerBlock(String action, String value) {
        update = true;
        nbt.put(InteractAction.TRIGGER_BLOCK, action + ":" + value);
        return this;
    }

    public InteractItemBuilder triggerPlace(String action, String value) {
        update = true;
        nbt.put(InteractAction.TRIGGER_PLACE, action + ":" + value);
        return this;
    }

    public InteractItemBuilder triggerBreak(String action, String value) {
        update = true;
        nbt.put(InteractAction.TRIGGER_BREAK, action + ":" + value);
        return this;
    }

    public InteractItemBuilder triggerHold(String action, String value) {
        update = true;
        nbt.put(InteractAction.TRIGGER_HOLD, action + ":" + value);
        return this;
    }

}
