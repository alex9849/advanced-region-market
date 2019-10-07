package net.alex9849.arm.minifeatures.teleporter;

import net.alex9849.arm.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class TeleporterListener implements Listener {
    private Player player;
    private Integer teleportTask;
    private double tolerance = 0.5;

    protected TeleporterListener(Player player, int teleportTask) {
        this.player = player;
        this.teleportTask = teleportTask;
    }

    protected TeleporterListener(Player player) {
        this.player = player;
    }

    @EventHandler
    private void playerMove(PlayerMoveEvent event) {
        if (event.getPlayer().getUniqueId() == player.getUniqueId()) {
            if (event.getFrom().getX() == event.getTo().getX()) {
                if (event.getFrom().getY() == event.getTo().getY()) {
                    if (event.getFrom().getZ() == event.getTo().getZ()) {
                        return;
                    }
                }
            }

            this.tolerance = this.tolerance - Math.abs(event.getFrom().getX() - event.getTo().getX());
            this.tolerance = this.tolerance - Math.abs(event.getFrom().getY() - event.getTo().getY());
            this.tolerance = this.tolerance - Math.abs(event.getFrom().getZ() - event.getTo().getZ());

            if (this.tolerance < 0) {
                if (teleportTask != null) {
                    this.player.sendMessage(Messages.PREFIX + Messages.TELEPORTER_TELEPORTATION_ABORDED);
                    Bukkit.getScheduler().cancelTask(this.teleportTask);
                    PlayerMoveEvent.getHandlerList().unregister(this);
                    PlayerQuitEvent.getHandlerList().unregister(this);
                }
            }
        }
    }

    @EventHandler
    private void playerLeave(PlayerQuitEvent event) {
        if (event.getPlayer().getUniqueId() == this.player.getUniqueId()) {
            Bukkit.getScheduler().cancelTask(this.teleportTask);
            PlayerMoveEvent.getHandlerList().unregister(this);
            PlayerQuitEvent.getHandlerList().unregister(this);

        }
    }

    protected void setTeleportTaskID(int taskID) {
        this.teleportTask = taskID;
    }
}
