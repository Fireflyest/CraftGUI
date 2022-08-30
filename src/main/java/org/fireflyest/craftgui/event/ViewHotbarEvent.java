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
 * 玩家使用数字键盘或者F键切换容器中物品的时候调用，指针在按钮上才起效
 */
public class ViewHotbarEvent extends InventoryInteractEvent {

    private static final HandlerList handlers = new HandlerList();

    private final ClickType click;
    private final int whichSlot;
    private final int rawSlot;
    private ItemStack current;

    private final int hotbarKey;

    private final String viewName;
    private final String value;
    private final int action;

    public ViewHotbarEvent(@Nonnull InventoryView view,
                           @Nonnull ClickType click,
                           int slot,
                           ItemStack current,
                           int key,
                           String viewName,
                           String value,
                           int action) {
        super(view);
        this.click = click;
        this.rawSlot = slot;
        this.whichSlot = view.convertSlot(slot);
        this.current = current;
        this.hotbarKey = key;
        this.viewName = viewName;
        this.value = value;
        this.action = action;
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

    public int getHotbarButton() {
        return hotbarKey;
    }

    @Nonnull
    public ClickType getClick() {
        return this.click;
    }
}
