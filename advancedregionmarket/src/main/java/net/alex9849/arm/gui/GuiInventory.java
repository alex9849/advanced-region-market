package net.alex9849.arm.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.LinkedList;

public class GuiInventory implements InventoryHolder {

    private final LinkedList<ClickItem> icons = new LinkedList<>();

    private int size;
    private final String title;

    public GuiInventory(int size, String title) {
        this.size = size;
        this.title = title;
    }

    public void addIcon(ClickItem icon) {
        this.icons.add(icon);
    }

    public ClickItem getIcon(int position) {
        for(int i = 0; i < this.icons.size(); i++){
            if(this.icons.get(i).poistion == position){
                return this.icons.get(i);
            }
        }
        return null;
    }

    public void setSize(int size){
        this.size = size;
    }

    @Override
    public Inventory getInventory() {

        Inventory inventory = Bukkit.createInventory(this, this.size, this.title);

        for (int i = 0; i < this.icons.size(); i++) {
            inventory.setItem(this.icons.get(i).poistion, this.icons.get(i).itemStack);
        }

        return inventory;
    }
}