package net.alex9849.arm.gui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ClickItem extends ItemStack {


    private final ItemStack itemStack;

    private final List<ClickAction> clickActions = new ArrayList<>();

    public ClickItem(Material mat) {
        this(new ItemStack(mat));
    }

    public ClickItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ClickItem setName(String name) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ClickItem setLore(List<String> lore) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return this;
    }
    
    public ClickItem setCustomItemModel(int customItemModel) {
    	ItemMeta itemMeta = this.itemStack.getItemMeta();
        if(customItemModel != -1) { itemMeta.setCustomModelData(customItemModel); }
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ClickItem addClickAction(@Nullable ClickAction clickAction) {
        if(clickAction != null) {
            this.clickActions.add(clickAction);
        }
        return this;
    }

    public List<ClickAction> getClickActions() {
        return this.clickActions;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

}
