package net.alex9849.arm.gui;

import net.alex9849.arm.Messages;
import net.alex9849.arm.exceptions.InputException;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.List;

public class GuiUtils {

    private GuiUtils() {}

    public static GuiInventory generateInventory(List<ClickItem> items, String name) {
        int guiSize = (items.size() / GuiConstants.GUI_ROW_SIZE) * GuiConstants.GUI_ROW_SIZE;
        if(items.size() % GuiConstants.GUI_ROW_SIZE != 0) {
            guiSize += GuiConstants.GUI_ROW_SIZE;
        }
        GuiInventory gui = new GuiInventory(guiSize, name);
        placeClickItems(gui, items);
        return gui;
    }

    public static void addGoBackItem(List<ClickItem> items, @Nullable ClickAction goBackAction) {
        if(goBackAction == null) {
            return;
        }
        items.add(new ClickItem(GuiConstants.getGoBackItem())
                .setName(Messages.GUI_GO_BACK)
                .addClickAction(goBackAction));
    }

    public static void placeFillItems(GuiInventory inv) {
        if (GuiConstants.getFillItem() == Material.AIR) {
            return;
        }
        for (int i = 0; i < inv.getInventory().getSize(); i++) {
            if (inv.getIcon(i) == null) {
                ClickItem fillIcon = new ClickItem(GuiConstants.getFillItem())
                        .setName(" ");
                inv.addIcon(fillIcon, i);
            }
        }
    }

    public static void placeClickItems(GuiInventory inv, List<ClickItem> clickItems) {
        placeClickItems(inv, clickItems, 0);
    }

    public static void placeClickItems(GuiInventory inv, List<ClickItem> clickItems, int rowOffset) {
        final int skipItems = rowOffset * GuiConstants.GUI_ROW_SIZE;
        if(skipItems + clickItems.size() > inv.getSize()) {
            throw new IndexOutOfBoundsException("Not enough Space in GuiInventory! InvSize = " + inv.getSize() +
                    " ClickItemsSize = " + clickItems.size() + " RowOffset = " + rowOffset);
        }
        int itemNr = 0;
        while (itemNr < clickItems.size()) {
            inv.addIcon(clickItems.get(itemNr), skipItems + getDisturbedItemPosition(itemNr++, clickItems.size()));
        }
    }

    /**
     *
     * @param itemNr begins at index 0 (the first item has the itemNr 0)
     * @param itemSize the number of item that should be placed in the inventory
     * @return the index where the item should be placed to
     */
    private static int getDisturbedItemPosition(int itemNr, int itemSize) {
        if(itemSize > GuiConstants.GUI_MAX_ITEM_SIZE) {
            throw new IndexOutOfBoundsException("itemSize has to be smaller then " + GuiConstants.GUI_MAX_ITEM_SIZE);
        }
        final int SKIPPED_ITEMS = (itemNr / GuiConstants.GUI_ROW_SIZE) * GuiConstants.GUI_ROW_SIZE;
        itemSize--;
        itemSize %= GuiConstants.GUI_ROW_SIZE;
        itemNr %= GuiConstants.GUI_ROW_SIZE;

        if (itemSize < itemNr) {
            throw new IndexOutOfBoundsException("itemNr does not have to be larger than itemSize");
        }
        
        if(itemSize == 0) {
            return SKIPPED_ITEMS + 4;
        }
        if(itemSize == 1) {
            if(itemNr == 0) return SKIPPED_ITEMS + 2;
            if(itemNr == 1) return SKIPPED_ITEMS + 6;
        }
        if(itemSize == 2) {
            if(itemNr == 0) return SKIPPED_ITEMS;
            if(itemNr == 1) return SKIPPED_ITEMS + 4;
            if(itemNr == 2) return SKIPPED_ITEMS + 8;
        }
        if(itemSize == 3) {
            if(itemNr == 0) return SKIPPED_ITEMS;
            if(itemNr == 1) return SKIPPED_ITEMS + 2;
            if(itemNr == 2) return SKIPPED_ITEMS + 6;
            if(itemNr == 3) return SKIPPED_ITEMS + 8;
        }
        if(itemSize == 4) {
            if(itemNr == 0) return SKIPPED_ITEMS;
            if(itemNr == 1) return SKIPPED_ITEMS + 2;
            if(itemNr == 2) return SKIPPED_ITEMS + 4;
            if(itemNr == 3) return SKIPPED_ITEMS + 6;
            if(itemNr == 4) return SKIPPED_ITEMS + 8;
        }
        if(itemSize == 5) {
            if(itemNr == 0) return SKIPPED_ITEMS;
            if(itemNr == 1) return SKIPPED_ITEMS + 1;
            if(itemNr == 2) return SKIPPED_ITEMS + 3;
            if(itemNr == 3) return SKIPPED_ITEMS + 5;
            if(itemNr == 4) return SKIPPED_ITEMS + 7;
            if(itemNr == 5) return SKIPPED_ITEMS + 8;
        }
        if(itemSize == 6) {
            if(itemNr == 0) return SKIPPED_ITEMS;
            if(itemNr == 1) return SKIPPED_ITEMS + 1;
            if(itemNr == 2) return SKIPPED_ITEMS + 3;
            if(itemNr == 3) return SKIPPED_ITEMS + 4;
            if(itemNr == 4) return SKIPPED_ITEMS + 5;
            if(itemNr == 5) return SKIPPED_ITEMS + 7;
            if(itemNr == 6) return SKIPPED_ITEMS + 8;
        }
        if(itemSize == 7) {
            if(itemNr == 0) return SKIPPED_ITEMS;
            if(itemNr == 1) return SKIPPED_ITEMS + 1;
            if (itemNr == 2) return SKIPPED_ITEMS + 2;
            if(itemNr == 3) return SKIPPED_ITEMS + 3;
            if(itemNr == 4) return SKIPPED_ITEMS + 5;
            if(itemNr == 5) return SKIPPED_ITEMS + 6;
            if(itemNr == 6) return SKIPPED_ITEMS + 7;
            if(itemNr == 7) return SKIPPED_ITEMS + 8;
        }
        //max items has to be 8
        return SKIPPED_ITEMS + itemNr;
    }

    /**
     * Checks if a list of clickActions has only one item and executes the clickActions
     * of this item
     *
     * @param player The player that should execute the item-clickActions
     * @param items the list with clickItems
     * @return true, if only one item is in the list and the clickActions of this item have been executed
     */
    public static boolean executeIfOnlyItem(Player player, List<ClickItem> items) {
        if(items.size() == 1) {
            items.get(0).getClickActions().forEach(a -> {
                try {
                    a.execute(player);
                } catch (InputException e) {
                    e.sendMessages(Messages.PREFIX);
                }
            });
            return true;
        }
        return false;
    }
}
