package net.alex9849.arm.gui;

import net.alex9849.arm.Messages;
import net.alex9849.arm.exceptions.InputException;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
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

    public static void openInfiniteGuiList(Player player, List<ClickItem> clickItems, int startIndex, String name,
                                           @Nullable ClickAction goBackAction, @Nullable ClickItem additionalOption) {

        final int NR_OF_OPTION_ITEMS = (additionalOption != null? 1:0) + (goBackAction != null? 1:0);
        final int MAX_ITEMS_PER_PAGE = GuiConstants.GUI_MAX_ITEM_SIZE - GuiConstants.GUI_ROW_SIZE;
        final boolean IS_MULTIPAGE_GUI = clickItems.size() > MAX_ITEMS_PER_PAGE;
        final boolean IS_SINGLE_ROW_GUI = !IS_MULTIPAGE_GUI && clickItems.size() + (NR_OF_OPTION_ITEMS == 0? 0:(NR_OF_OPTION_ITEMS + 1)) <= GuiConstants.GUI_ROW_SIZE;
        final int ITEMS_TO_DISPLAY_ON_PAGE = Math.max(0, Math.min(clickItems.size() - startIndex, MAX_ITEMS_PER_PAGE));
        final int ROWS_NEEDED_TO_DISPLAY_ITEMS = (ITEMS_TO_DISPLAY_ON_PAGE / GuiConstants.GUI_ROW_SIZE)
                + (ITEMS_TO_DISPLAY_ON_PAGE % GuiConstants.GUI_ROW_SIZE != 0? 1:0);

        int inventorySize;

        if(IS_SINGLE_ROW_GUI) {
            inventorySize = GuiConstants.GUI_ROW_SIZE;
        } else {
            inventorySize = ROWS_NEEDED_TO_DISPLAY_ITEMS * GuiConstants.GUI_ROW_SIZE
                    + ((NR_OF_OPTION_ITEMS == 0)? 0:GuiConstants.GUI_ROW_SIZE);

        }

        GuiInventory inv = new GuiInventory(inventorySize, name);

        for (int i = 0; i < ITEMS_TO_DISPLAY_ON_PAGE; i++) {
            inv.addIcon(clickItems.get(startIndex + i), i);
        }

        if (startIndex != 0 && IS_MULTIPAGE_GUI) {
            final int newStartIndex = Math.max(0, startIndex - MAX_ITEMS_PER_PAGE);
            ClickItem prevPageButton = new ClickItem(GuiConstants.getPrevPageItem())
                    .setName(Messages.GUI_PREV_PAGE)
                    .addClickAction(p -> openInfiniteGuiList(p, clickItems, newStartIndex, name, goBackAction, additionalOption));
            inv.addIcon(prevPageButton, inv.getSize() - GuiConstants.GUI_ROW_SIZE);
        }

        List<ClickItem> optionButtons = new ArrayList<>();
        if (goBackAction != null) {
            ClickItem goBackButton = new ClickItem(GuiConstants.getGoBackItem())
                    .setName(Messages.GUI_GO_BACK)
                    .addClickAction(goBackAction);
            optionButtons.add(goBackButton);
        }

        if(additionalOption != null) {
            optionButtons.add(additionalOption);
        }

        if(optionButtons.size() == 1) {
            if(IS_SINGLE_ROW_GUI) {
                inv.addIcon(optionButtons.get(0), inv.getSize() - 1);
            } else {
                inv.addIcon(optionButtons.get(0), inv.getSize() - 5);
            }
        } else if (optionButtons.size() == 2) {
            if(IS_SINGLE_ROW_GUI) {
                inv.addIcon(optionButtons.get(0), inv.getSize() - 1);
                inv.addIcon(optionButtons.get(1), inv.getSize() - 2);
            } else {
                inv.addIcon(optionButtons.get(0), inv.getSize() - 3);
                inv.addIcon(optionButtons.get(1), inv.getSize() - 7);
            }
        }

        if (startIndex + MAX_ITEMS_PER_PAGE < clickItems.size()) {
            final int newStartIndex = startIndex + MAX_ITEMS_PER_PAGE;
            ClickItem nextPageButton = new ClickItem(GuiConstants.getNextPageItem())
                    .setName(Messages.GUI_NEXT_PAGE)
                    .addClickAction(p -> openInfiniteGuiList(p, clickItems, newStartIndex, name, goBackAction, additionalOption));
            inv.addIcon(nextPageButton, inv.getSize() - 1);
        }

        GuiUtils.placeFillItems(inv);
        player.openInventory(inv.getInventory());
    }
}
