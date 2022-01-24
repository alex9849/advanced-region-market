package net.alex9849.arm.gui.handler.inventory;

import net.alex9849.arm.Messages;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.flaggroups.PresetContent;
import net.alex9849.arm.gui.handler.GuiInputAction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GuiInventoryInputListener implements Listener {

    private ItemStack previousItem = new ItemStack(Material.BOOK) {
        {
            ItemMeta meta = getItemMeta();
            meta.setDisplayName(Messages.GUI_PREV_PAGE);
            setItemMeta(meta);
        }
    };

    private ItemStack nextItem = new ItemStack(Material.BOOK) {
        {
            ItemMeta meta = getItemMeta();
            meta.setDisplayName(Messages.GUI_NEXT_PAGE);
            setItemMeta(meta);
        }
    };

    GuiInputAction guiInputAction;
    private final Player player;
    private final List<PresetContent> presetContents = new ArrayList<>();
    private final Inventory inventory;
    private int page;

    public GuiInventoryInputListener(Player player, GuiInputAction guiInputAction, List<PresetContent> presetContents) {
        this.player = player;
        this.guiInputAction = guiInputAction;
        inventory = Bukkit.createInventory(null, 54, Messages.GUI_FLAGSELECTOR_NAME);
        player.openInventory(inventory);
        for (PresetContent presetContent : presetContents) {
            String permission = presetContent.getPermission();
            if (permission == null || player.hasPermission(permission)) {
                this.presetContents.add(presetContent);
            }
        }
        setPage(1);
    }

    private void setPage(int page) {
        this.page = page;
        int length = Math.min(presetContents.size() - ((page-1) * 45), 45);
        inventory.clear();
        for(int i = 0; i < length; i++) {
            PresetContent presetContent = presetContents.get((page-1)*45 + i);
            inventory.setItem(i, new ItemStack(Material.PAPER) {
                {
                    ItemMeta meta = getItemMeta();
                    if (presetContent.getDescription().size() >= 1) {
                        meta.setDisplayName(presetContent.getDescription().get(0));
                        if (presetContent.getDescription().size() >= 2) {
                            List<String> lore = presetContent.getDescription().subList(1, presetContent.getDescription().size());
                            meta.setLore(lore);
                        }
                    } else {
                        meta.setDisplayName(ChatColor.RESET + presetContent.getSettings());
                    }
                    setItemMeta(meta);
                }
            });
        }
        if (page > 1) {
            inventory.setItem(47, previousItem);
        }
        if (presetContents.size() > page * 45) {
            inventory.setItem(51, nextItem);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleClick(InventoryClickEvent event) {
        if (event.getWhoClicked().getUniqueId() != this.player.getUniqueId()) {
            return;
        }
        event.setCancelled(true);

        if (event.getClick() != ClickType.LEFT) {
            return;
        }
        if (event.getClickedInventory() != event.getInventory()) {
            return;
        }
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }

        if (event.getRawSlot() == 47) {
            setPage(page-1);
            return;
        } else if (event.getRawSlot() == 51) {
            setPage(page+1);
            return;
        } else {
            try {
                this.guiInputAction.runAction(presetContents.get(((page-1)*45) + event.getRawSlot()).getSettings());
            } catch (InputException e) {
                e.sendMessages(Messages.PREFIX);
            }
        }

        this.unregister();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleDrag(InventoryDragEvent event) {
        if (event.getWhoClicked().getUniqueId() != this.player.getUniqueId()) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleClose(InventoryCloseEvent event) {
        if (event.getInventory() != this.inventory) {
            return;
        }
        this.unregister();
    }

    @EventHandler
    public void playerQuitEvent(PlayerQuitEvent event) {
        if (!(event.getPlayer().getUniqueId() == this.player.getUniqueId())) {
            return;
        }
        this.unregister();
    }

    public void unregister() {
        InventoryCloseEvent.getHandlerList().unregister(this);
        InventoryDragEvent.getHandlerList().unregister(this);
        InventoryClickEvent.getHandlerList().unregister(this);
        PlayerQuitEvent.getHandlerList().unregister(this);
    }
}
