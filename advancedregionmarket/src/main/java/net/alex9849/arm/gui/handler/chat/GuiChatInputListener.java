package net.alex9849.arm.gui.handler.chat;

import net.alex9849.arm.Messages;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.gui.ClickAction;
import net.alex9849.arm.gui.handler.GuiInputAction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GuiChatInputListener implements Listener {
    GuiInputAction guiInputAction;
    ClickAction backAction;
    private Player player;

    public GuiChatInputListener(Player player, GuiInputAction guiInputAction, ClickAction backAction) {
        this.player = player;
        this.guiInputAction = guiInputAction;
        this.backAction = backAction;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleChat(PlayerChatEvent event) throws InputException {
        if (event.getPlayer().getUniqueId() != this.player.getUniqueId()) {
            return;
        }
        event.setCancelled(true);

        if (event.getMessage().equalsIgnoreCase("cancel")) {
            this.backAction.execute(player);
            return;
        } else {
            this.guiInputAction.runAction(event.getMessage());
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
        PlayerChatEvent.getHandlerList().unregister(this);
        PlayerQuitEvent.getHandlerList().unregister(this);
    }
}
