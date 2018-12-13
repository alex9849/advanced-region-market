package net.alex9849.arm.minifeatures.teleporter;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regions.Region;
import net.alex9849.inter.WGRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

public class Teleporter {

    public static void teleport(Player player, Region region, String message, Boolean useCountdown) throws InputException {

        if(region.getTeleportLocation() == null) {

            World world = Bukkit.getWorld(region.getRegionworld());
            if(world == null) {
                return;
            }
            Vector min = region.getRegion().getMinPoint();
            Vector max = region.getRegion().getMaxPoint();
            int maxX = max.getBlockX();
            int maxY = max.getBlockY();
            int maxZ = max.getBlockZ();
            int minX = min.getBlockX();
            int minY = min.getBlockY();
            int minZ = min.getBlockZ();

            for (int x = maxX; x >= minX; x--) {
                for (int z = maxZ; z >= minZ; z--) {
                    for (int y = maxY; y >= minY; y--) {
                        Location loc = new Location(world, x, y, z);
                        if(isSaveTeleport(loc)) {
                            loc.add(0.5, 0, 0.5);
                            int timer = 0;
                            if(useCountdown) {
                                timer = 20 * AdvancedRegionMarket.getARM().getConfig().getInt("Other.TeleporterTimer");
                            }
                            scheduleTeleport(player, loc, message, timer);
                            return;
                        }
                    }
                }
            }

            throw new InputException(player, Messages.TELEPORTER_NO_SAVE_LOCATION_FOUND);

        } else {
            player.teleport(region.getTeleportLocation());
        }
    }

    public static void teleport(Player player, Region region) throws InputException {
        teleport(player, region, "", true);
    }

    private static boolean isSaveTeleport(Location loc) {
        Location locP1 = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY() + 1, loc.getBlockZ());
        Location locM1 = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ());
        if(!(loc.getBlock().getType() == Material.AIR) || !(locP1.getBlock().getType() == Material.AIR)) {
            return false;
        }
        if((locM1.getBlock().getType() == Material.AIR) || (locM1.getBlock().getType() == Material.LAVA) || (locM1.getBlock().getType() == Material.MAGMA_BLOCK)) {
            return false;
        }
        return true;
    }

    public static void scheduleTeleport(Player player, Location loc, String message, int ticks) {
        if((ticks == 0) || player.hasPermission(Permission.ADMIN_BYPASS_TELEPORTER_COOLDOWN)) {
            player.teleport(loc);
            if(!message.equals("") && message != null) {
                player.sendMessage(message);
            }
            return;
        } else {
            String preTPmessage = Messages.TELEPORTER_DONT_MOVE.replace("%time%", (ticks/20) + "");
            player.sendMessage(Messages.PREFIX + preTPmessage);

            TeleporterListener listener = new TeleporterListener(player);

            int taskID = Bukkit.getScheduler().runTaskLater(AdvancedRegionMarket.getARM(), new Runnable() {
                @Override
                public void run() {
                    player.teleport(loc);
                    if(!message.equals("")) {
                        player.sendMessage(message);
                    }
                    PlayerMoveEvent.getHandlerList().unregister(listener);
                    PlayerQuitEvent.getHandlerList().unregister(listener);
                }
            }, ticks).getTaskId();

            listener.setTeleportTaskID(taskID);

            Bukkit.getServer().getPluginManager().registerEvents(listener, AdvancedRegionMarket.getARM());

            return;

        }

    }
}
