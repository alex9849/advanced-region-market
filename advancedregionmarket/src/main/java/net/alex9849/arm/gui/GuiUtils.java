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
        GuiInventory gui = new GuiInventory((items.size() / GuiConstants.GUI_ROW_SIZE)
                * GuiConstants.GUI_ROW_SIZE + GuiConstants.GUI_ROW_SIZE, name);
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

    public static GuiInventory placeFillItems(GuiInventory inv) {
        if (GuiConstants.getFillItem() != Material.AIR) {
            for (int i = 0; i < inv.getInventory().getSize(); i++) {
                if (inv.getIcon(i) == null) {
                    ClickItem fillIcon = new ClickItem(GuiConstants.getFillItem())
                            .setName(" ");
                    inv.addIcon(fillIcon, i);
                }
            }
            return inv;
        }
        return inv;
    }

    public static void placeClickItems(GuiInventory inv, List<ClickItem> clickItems) {
        placeClickItems(inv, clickItems, 0);
    }

    public static void placeClickItems(GuiInventory inv, List<ClickItem> clickItems, int rowOffset) {
        final int skipItems = rowOffset * GuiConstants.GUI_ROW_SIZE;
        if(skipItems + clickItems.size() >= inv.getSize()) {
            throw new IndexOutOfBoundsException("Not enough Space in GuiInventory! InvSize = " + inv.getSize() +
                    " ClickItemsSize = " + clickItems.size() + " RowOffset = " + rowOffset);
        }
        int itemNr = 0;
        while (itemNr < clickItems.size()) {
            inv.addIcon(clickItems.get(itemNr), skipItems + getDisturbedItemPosition(++itemNr, clickItems.size()));
        }
    }

    private static int getDisturbedItemPosition(int itemNr, int maxItems) {
        if(maxItems > GuiConstants.GUI_MAX_ITEM_SIZE) {
            throw new IndexOutOfBoundsException("maxItems has to be smaller then " + GuiConstants.GUI_MAX_ITEM_SIZE);
        }
        
        final int SKIPPED_ITEMS = (itemNr / GuiConstants.GUI_ROW_SIZE) * GuiConstants.GUI_ROW_SIZE;
        maxItems %= GuiConstants.GUI_ROW_SIZE;
        itemNr %= GuiConstants.GUI_ROW_SIZE;

        if (maxItems < itemNr) {
            throw new IndexOutOfBoundsException("itemNr does not have to be larger than maxItems");
        }
        
        if(maxItems == 1) {
            return SKIPPED_ITEMS + 4;
        }
        if(maxItems == 2) {
            if(itemNr == 1) return SKIPPED_ITEMS + 2;
            if(itemNr == 2) return SKIPPED_ITEMS + 6;
        }
        if(maxItems == 3) {
            if(itemNr == 1) return SKIPPED_ITEMS;
            if(itemNr == 2) return SKIPPED_ITEMS + 4;
            if(itemNr == 3) return SKIPPED_ITEMS + 8;
        }
        if(maxItems == 4) {
            if(itemNr == 1) return SKIPPED_ITEMS;
            if(itemNr == 2) return SKIPPED_ITEMS + 2;
            if(itemNr == 3) return SKIPPED_ITEMS + 6;
            if(itemNr == 4) return SKIPPED_ITEMS + 8;
        }
        if(maxItems == 5) {
            if(itemNr == 1) return SKIPPED_ITEMS;
            if(itemNr == 2) return SKIPPED_ITEMS + 2;
            if(itemNr == 3) return SKIPPED_ITEMS + 4;
            if(itemNr == 4) return SKIPPED_ITEMS + 6;
            if(itemNr == 5) return SKIPPED_ITEMS + 8;
        }
        if(maxItems == 6) {
            if(itemNr == 1) return SKIPPED_ITEMS;
            if(itemNr == 2) return SKIPPED_ITEMS + 1;
            if(itemNr == 3) return SKIPPED_ITEMS + 3;
            if(itemNr == 4) return SKIPPED_ITEMS + 5;
            if(itemNr == 5) return SKIPPED_ITEMS + 7;
            if(itemNr == 6) return SKIPPED_ITEMS + 8;
        }
        if(maxItems == 7) {
            if(itemNr == 1) return SKIPPED_ITEMS;
            if(itemNr == 2) return SKIPPED_ITEMS + 1;
            if(itemNr == 3) return SKIPPED_ITEMS + 3;
            if(itemNr == 4) return SKIPPED_ITEMS + 4;
            if(itemNr == 5) return SKIPPED_ITEMS + 5;
            if(itemNr == 6) return SKIPPED_ITEMS + 7;
            if(itemNr == 7) return SKIPPED_ITEMS + 8;
        }
        if(maxItems == 8) {
            if(itemNr == 1) return SKIPPED_ITEMS;
            if(itemNr == 2) return SKIPPED_ITEMS + 1;
            if (itemNr == 3) return SKIPPED_ITEMS + 2;
            if(itemNr == 4) return SKIPPED_ITEMS + 3;
            if(itemNr == 5) return SKIPPED_ITEMS + 5;
            if(itemNr == 6) return SKIPPED_ITEMS + 6;
            if(itemNr == 7) return SKIPPED_ITEMS + 7;
            if(itemNr == 8) return SKIPPED_ITEMS + 8;
        }
        
        //max items has to be 9
        return SKIPPED_ITEMS + itemNr;
    }

    public static void executeIfOnlyItem(Player player, List<ClickItem> items) {
        if(items.size() == 1) {
            items.get(0).getClickActions().forEach(a -> {
                try {
                    a.execute(player);
                } catch (InputException e) {
                    e.sendMessages(Messages.PREFIX);
                }
            });
        }
    }
}
