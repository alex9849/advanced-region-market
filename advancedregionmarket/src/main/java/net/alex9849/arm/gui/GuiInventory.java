package net.alex9849.arm.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GuiInventory implements InventoryHolder {

    private final ClickItem[] icons;

    private int size;
    private final String title;

    public GuiInventory(int size, String title) {
        this.size = size;
        this.title = title;
        this.icons = new ClickItem[this.size];
    }

    public void addIcon(ClickItem icon, int position) {
        this.icons[position] = icon;
    }

    public ClickItem getIcon(int position) {
        return this.icons[position];
    }

    public int getSize() {
        return this.size;
    }

    @Override
    public Inventory getInventory() {

        Inventory inventory = Bukkit.createInventory(this, this.size, this.title);

        for (int i = 0; i < this.icons.length; i++) {
            if(this.icons[i] == null) {
                inventory.setItem(i, null);
            } else {
                inventory.setItem(i, this.icons[i].getItemStack());
            }

        }

        return inventory;
    }
}