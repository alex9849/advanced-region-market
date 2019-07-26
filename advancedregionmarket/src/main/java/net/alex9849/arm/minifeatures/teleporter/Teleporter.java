package net.alex9849.arm.minifeatures.teleporter;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.util.MaterialFinder;
import net.alex9849.exceptions.InputException;
import net.alex9849.signs.SignData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

public class Teleporter {

    public static void teleport(Player player, Region region, String message, Boolean useCountdown) throws InputException {

        if(region.getTeleportLocation() == null) {

            World world = region.getRegionworld();
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
                            teleport(player, loc, message, useCountdown);
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

    public static void teleport(Player player, Location location, String message, boolean useCountdown) {
        location.add(0.5, 0, 0.5);
        int timer = 0;
        if(useCountdown) {
            timer = 20 * AdvancedRegionMarket.getARM().getConfig().getInt("Other.TeleporterTimer");
        }
        scheduleTeleport(player, location, message, timer);
        return;
    }

    public static void teleport(Player player, Region region) throws InputException {
        teleport(player, region, "", true);
    }

    public static void teleport(Player player, Location location) {
        teleport(player, location, "", true);
    }

    public static boolean teleport(Player player, SignData sign) throws InputException {
        for(int y = sign.getLocation().getBlockY(); ((y > 1) && (y > (sign.getLocation().getBlockY() - 10))); y--) {
            Location newLoc = new Location(sign.getLocation().getWorld(), sign.getLocation().getBlockX(), y, sign.getLocation().getBlockZ());
            if(isSaveTeleport(newLoc)) {
                teleport(player, newLoc);
                return true;
            }
        }
        for(int y = sign.getLocation().getBlockY(); ((y < 255) && (y < (sign.getLocation().getBlockY() + 10))); y++) {
            Location newLoc = new Location(sign.getLocation().getWorld(), sign.getLocation().getBlockX(), y, sign.getLocation().getBlockZ());
            if(isSaveTeleport(newLoc)) {
                teleport(player, newLoc);
                return true;
            }
        }
        return false;
    }

    private static boolean isSaveTeleport(Location loc) {
        Location locP1 = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY() + 1, loc.getBlockZ());
        Location locM1 = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ());
        if(!((locP1.getBlock().getType() == Material.AIR) || MaterialFinder.getSignMaterials().contains(locP1.getBlock().getType()) || (loc.getBlock().getType() == MaterialFinder.getWallTorch()))) {
            return false;
        }
        if(!((loc.getBlock().getType() == Material.AIR) || MaterialFinder.getSignMaterials().contains(loc.getBlock().getType()) || (loc.getBlock().getType() == MaterialFinder.getWallTorch()))) {
            return false;
        }
        if((locM1.getBlock().getType() == Material.AIR) || (locM1.getBlock().getType() == Material.LAVA) || (locM1.getBlock().getType() == MaterialFinder.getMagmaBlock())
                || MaterialFinder.getSignMaterials().contains(locM1.getBlock().getType()) || (locM1.getBlock().getType() == MaterialFinder.getWallTorch())) {
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
