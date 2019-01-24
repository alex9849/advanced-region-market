package net.alex9849.arm.gui;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ClickItem extends ItemStack {


    private final ItemStack itemStack;

    private final List<ClickAction> clickActions = new ArrayList<>();

    public ClickItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ClickItem(ItemStack itemStack, String name, List<String> lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        this.itemStack = itemStack;
    }

    public ClickItem(ItemStack itemStack, String name) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        this.itemStack = itemStack;
    }

    public ClickItem addClickAction(ClickAction clickAction) {
        this.clickActions.add(clickAction);
        return this;
    }

    public List<ClickAction> getClickActions() {
        return this.clickActions;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

}
