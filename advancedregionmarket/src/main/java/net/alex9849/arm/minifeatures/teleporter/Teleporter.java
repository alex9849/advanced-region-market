package net.alex9849.arm.minifeatures.teleporter;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.NoSaveLocationException;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.util.MaterialFinder;
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

    public static void teleport(Player player, Region region, String message, Boolean useCountdown) throws NoSaveLocationException {
        Location tpLoc = getTeleportLocation(region);
        if(tpLoc == null) {
            throw new NoSaveLocationException(region.replaceVariables(Messages.TELEPORTER_NO_SAVE_LOCATION_FOUND));
        }
        teleport(player, tpLoc, message, useCountdown);
    }

    public static void teleport(Player player, Location location, String message, boolean useCountdown) {
        int timer = 0;
        if (useCountdown) {
            timer = 20 * AdvancedRegionMarket.getInstance().getConfig().getInt("Other.TeleporterTimer");
        }
        scheduleTeleport(player, location, message, timer);
        return;
    }

    public static void teleport(Player player, Region region) throws NoSaveLocationException {
        teleport(player, region, "", true);
    }

    public static void teleport(Player player, Location location) {
        teleport(player, location, "", true);
    }

    public static boolean teleport(Player player, SignData sign) {
        Vector normalizedBlockFaceDirection = new Vector(sign.getBlockFace().getModX(), 0, sign.getBlockFace().getModZ());
        if (normalizedBlockFaceDirection.getBlockX() != 0 || normalizedBlockFaceDirection.getBlockY() != 0
                || normalizedBlockFaceDirection.getBlockZ() != 0) {
            normalizedBlockFaceDirection.normalize();
        }
        Location teleportXZLoc = sign.getLocation().clone().add(normalizedBlockFaceDirection);

        Location teleportLoc = teleportXZLoc;
        boolean locationFound = false;

        for (int y = teleportXZLoc.getBlockY(); ((y > 1) && (y > (teleportXZLoc.getBlockY() - 10)) && !locationFound); y--) {
            teleportLoc = new Location(teleportXZLoc.getWorld(), teleportXZLoc.getBlockX(), y, teleportXZLoc.getBlockZ());
            if (isSaveTeleport(teleportLoc)) {
                locationFound = true;
            }
        }
        for (int y = teleportXZLoc.getBlockY(); ((y < 255) && (y < (teleportXZLoc.getBlockY() + 10)) && !locationFound); y++) {
            teleportLoc = new Location(teleportXZLoc.getWorld(), teleportXZLoc.getBlockX(), y, teleportXZLoc.getBlockZ());
            if (isSaveTeleport(teleportLoc)) {
                locationFound = true;
            }
        }
        if(!locationFound) {
            return false;
        }
        Vector lookingDirection = new Vector(sign.getLocation().getX() - teleportLoc.getX(),
                sign.getLocation().getY() - teleportLoc.getY() - 1,
                sign.getLocation().getZ() - teleportLoc.getZ());
        teleportLoc.setDirection(lookingDirection);
        teleport(player, teleportLoc);
        return true;
    }

    public static Location getTeleportLocation(Region region) {
        if (region.getPlayerTeleportLocation() != null && isSaveTeleport(region.getPlayerTeleportLocation())) {
            return region.getPlayerTeleportLocation();
        }
        if (region.getTeleportLocation() != null && isSaveTeleport(region.getTeleportLocation())) {
            return region.getTeleportLocation();
        }
        World world = region.getRegionworld();
        if (world == null) {
            return null;
        }
        Vector min = region.getRegion().getMinPoint();
        Vector max = region.getRegion().getMaxPoint();
        int maxX = max.getBlockX();
        int maxY = max.getBlockY();
        int maxZ = max.getBlockZ();
        int minX = min.getBlockX();
        int minY = min.getBlockY();
        int minZ = min.getBlockZ();
        int xAxis = maxX - (maxX - minX) / 2;
        int zAxis = maxZ - (maxZ - minZ) / 2;
        Vector halfBlockVector = new Vector(0.5, 0, 0.5);
        int radius = 1;
        boolean abort = false;

        for (int y = maxY; y >= minY; y--) {
            Location loc = new Location(world, xAxis, y, zAxis);
            if (isSaveTeleport(loc)) {
                return loc.add(halfBlockVector);
            }
        }

        while (!abort) {
            boolean inRegionOneTime = false;


            int movedX = 0;
            do {
                movedX++;
                xAxis++;
                if (!region.getRegion().contains(xAxis, maxY, zAxis)) {
                    continue;
                }
                inRegionOneTime = true;
                for (int y = maxY; y >= minY; y--) {
                    Location loc = new Location(world, xAxis, y, zAxis);
                    if (isSaveTeleport(loc)) {
                        return loc.add(halfBlockVector);
                    }
                }
            } while (movedX < radius);

            int movedZ = 0;
            do {
                movedZ++;
                zAxis++;
                if (!region.getRegion().contains(xAxis, maxY, zAxis)) {
                    continue;
                }
                inRegionOneTime = true;
                for (int y = maxY; y >= minY; y--) {
                    Location loc = new Location(world, xAxis, y, zAxis);
                    if (isSaveTeleport(loc)) {
                        return loc.add(halfBlockVector);
                    }
                }
            } while (movedZ < radius);


            radius++;
            movedX = 0;
            do {
                movedX++;
                xAxis--;
                if (!region.getRegion().contains(xAxis, maxY, zAxis)) {
                    continue;
                }
                inRegionOneTime = true;
                for (int y = maxY; y >= minY; y--) {
                    Location loc = new Location(world, xAxis, y, zAxis);
                    if (isSaveTeleport(loc)) {
                        return loc.add(halfBlockVector);
                    }
                }
            } while (movedX < radius);

            movedZ = 0;
            do {
                movedZ++;
                zAxis--;
                if (!region.getRegion().contains(xAxis, maxY, zAxis)) {
                    continue;
                }
                inRegionOneTime = true;
                for (int y = maxY; y >= minY; y--) {
                    Location loc = new Location(world, xAxis, y, zAxis);
                    if (isSaveTeleport(loc)) {
                        return loc.add(halfBlockVector);
                    }
                }
            } while (movedZ < radius);

            radius++;
            abort = !inRegionOneTime;
        }
        return null;
    }

    public static boolean isSaveTeleport(Location loc) {
        Location locP1 = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY() + 1, loc.getBlockZ());
        Location locM1 = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ());
        if (!((locP1.getBlock().getType() == Material.AIR) || MaterialFinder.getSignMaterials().contains(locP1.getBlock().getType()) || (loc.getBlock().getType() == MaterialFinder.getWallTorch()))) {
            return false;
        }
        if (!((loc.getBlock().getType() == Material.AIR) || MaterialFinder.getSignMaterials().contains(loc.getBlock().getType()) || (loc.getBlock().getType() == MaterialFinder.getWallTorch()))) {
            return false;
        }
        return (locM1.getBlock().getType() != Material.AIR) && (locM1.getBlock().getType() != Material.LAVA) && (locM1.getBlock().getType() != MaterialFinder.getMagmaBlock())
                && !MaterialFinder.getSignMaterials().contains(locM1.getBlock().getType()) && (locM1.getBlock().getType() != MaterialFinder.getWallTorch());
    }

    public static void scheduleTeleport(Player player, Location loc, String message, int ticks) {
        if ((ticks == 0) || player.hasPermission(Permission.ADMIN_BYPASS_TELEPORTER_COOLDOWN)) {
            player.teleport(loc);
            if (!message.equals("") && message != null) {
                player.sendMessage(message);
            }
            return;
        } else {
            String preTPmessage = Messages.TELEPORTER_DONT_MOVE.replace("%time%", (ticks / 20) + "");
            player.sendMessage(Messages.PREFIX + preTPmessage);

            TeleporterListener listener = new TeleporterListener(player);

            int taskID = Bukkit.getScheduler().runTaskLater(AdvancedRegionMarket.getInstance(), new Runnable() {
                @Override
                public void run() {
                    player.teleport(loc);
                    if (!message.equals("")) {
                        player.sendMessage(message);
                    }
                    PlayerMoveEvent.getHandlerList().unregister(listener);
                    PlayerQuitEvent.getHandlerList().unregister(listener);
                }
            }, ticks).getTaskId();

            listener.setTeleportTaskID(taskID);

            Bukkit.getServer().getPluginManager().registerEvents(listener, AdvancedRegionMarket.getInstance());

            return;

        }

    }

}
