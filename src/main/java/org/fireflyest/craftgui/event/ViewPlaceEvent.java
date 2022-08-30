package org.fireflyest.craftgui.event;

import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 玩家将东西放置到容器里面的时候调用，需要放置到按钮上才起效
 */
public class ViewPlaceEvent extends InventoryInteractEvent {
    private static final HandlerList handlers = new HandlerList();

    private final ClickType click;
    private final int whichSlot;
    private final int rawSlot;
    // 光标上的物品
    private ItemStack cursor;
    // 按钮物品
    private ItemStack current;

    private boolean handBack;

    private final String viewName;
    private final String value;
    private final int action;

    public ViewPlaceEvent(@Nonnull InventoryView view,
                          @Nonnull ClickType click,
                          int slot,
                          ItemStack current,
                          ItemStack cursor,
                          String viewName,
                          String value,
                          int action) {
        super(view);
        this.click = click;
        this.rawSlot = slot;
        this.whichSlot = view.convertSlot(slot);
        this.current = current;
        this.cursor = cursor;
        this.handBack = true;
        this.viewName = viewName;
        this.value = value;
        this.action = action;
    }

    public boolean handBack() {
        return handBack;
    }

    public void setHandBack(boolean handBack){
        this.handBack = handBack;
    }

    @Override
    @Nonnull
    public HandlerList getHandlers() {
        return handlers;
    }

    @Nonnull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ItemStack getCurrentItem() {
        return this.current ;
    }

    public ItemStack getCursorItem() {
        return cursor;
    }

    public boolean isRightClick() {
        return this.click.isRightClick();
    }

    public boolean isLeftClick() {
        return this.click.isLeftClick();
    }

    public boolean isShiftClick() {
        return this.click.isShiftClick();
    }

    public void setCurrent(ItemStack current) {
        this.current = current;
    }

    public void setCursor(ItemStack cursor) {
        this.cursor = cursor;
    }

    public String getViewName() {
        return viewName;
    }

    public String getValue() {
        return value;
    }

    public int getAction() {
        return action;
    }

    @Nullable
    public Inventory getClickedInventory() {
        return this.getView().getInventory(this.rawSlot);
    }

    public int getSlot() {
        return this.whichSlot;
    }

    public int getRawSlot() {
        return this.rawSlot;
    }

    @Nonnull
    public ClickType getClick() {
        return this.click;
    }

}
