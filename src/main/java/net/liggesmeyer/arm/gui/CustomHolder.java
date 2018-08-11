package net.liggesmeyer.arm.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class CustomHolder implements InventoryHolder {

    private final LinkedList<Icon> icons = new LinkedList<>();

    private int size;
    private final String title;

    public CustomHolder(int size, String title) {
        this.size = size;
        this.title = title;
    }

    public void addIcon(Icon icon) {
        this.icons.add(icon);
    }

    public Icon getIcon(int position) {
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