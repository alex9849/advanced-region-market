package net.alex9849.arm.gui.chathandler;

import net.alex9849.arm.Messages;
import net.alex9849.exceptions.InputException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GuiChatInputListener implements Listener {
    private Player player;
    GuiInputAction guiInputAction;

    public GuiChatInputListener(Player player, GuiInputAction guiInputAction) {
        this.player = player;
        this.guiInputAction = guiInputAction;
    }

    @EventHandler
    public void handleChat(PlayerChatEvent event) {
        if(event.getPlayer().getUniqueId() != this.player.getUniqueId()) {
            return;
        }
        event.setCancelled(true);

        try {
            this.guiInputAction.runAction(event.getMessage());
        } catch (InputException e) {
            e.sendMessages(Messages.PREFIX);
        }
        this.unregister();
    }

    @EventHandler
    public void playerQuitEvent(PlayerQuitEvent event) {
        if(!(event.getPlayer().getUniqueId() == this.player.getUniqueId())) {
            return;
        }
        this.unregister();
    }

    public void unregister() {
        PlayerChatEvent.getHandlerList().unregister(this);
        PlayerQuitEvent.getHandlerList().unregister(this);
    }
}
