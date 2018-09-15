package net.alex9849.arm.gui;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ClickItem extends ItemStack {


    public final ItemStack itemStack;
    public final int poistion;

    public final List<ClickAction> clickActions = new ArrayList<>();

    public ClickItem(ItemStack itemStack, int poistion) {
        this.itemStack = itemStack;
        this.poistion = poistion;
    }

    public ClickItem addClickAction(ClickAction clickAction) {
        this.clickActions.add(clickAction);
        return this;
    }

    public List<ClickAction> getClickActions() {
        return this.clickActions;
    }

}
