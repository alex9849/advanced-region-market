package net.alex9849.arm.inactivityexpiration;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerInactivityGroupMapper {
    private static HashMap<World, HashMap<UUID, InactivityExpirationGroup>> bestResetAfterMap = new HashMap<>();
    private static HashMap<World, HashMap<UUID, InactivityExpirationGroup>> bestTakeoverAfterMap = new HashMap<>();
    private static long lastUpdated = 0;

    public static InactivityExpirationGroup getBestResetAfterMs(World world, UUID uuid) {
        return getBestAfterMsFromMap(world, uuid, bestResetAfterMap);
    }

    public static InactivityExpirationGroup getBestTakeoverAfterMs(World world, UUID uuid) {
        return getBestAfterMsFromMap(world, uuid, bestTakeoverAfterMap);
    }

    private static InactivityExpirationGroup getBestAfterMsFromMap(World world, UUID uuid, HashMap<World, HashMap<UUID, InactivityExpirationGroup>> map) {
        HashMap<UUID, InactivityExpirationGroup> uuidMap = map.get(world);
        if (uuidMap == null) {
            return InactivityExpirationGroup.NOT_CALCULATED;
        }
        InactivityExpirationGroup ieGroup = uuidMap.get(uuid);
        if (ieGroup == null) ieGroup = InactivityExpirationGroup.NOT_CALCULATED;

        return ieGroup;
    }

    public static void updateMapAscync() {
        Thread updateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<World, HashMap<UUID, InactivityExpirationGroup>> newBestResetAfterMap = new HashMap<>();
                HashMap<World, HashMap<UUID, InactivityExpirationGroup>> newBestTakeoverAfterMap = new HashMap<>();

                for (Region region : AdvancedRegionMarket.getInstance().getRegionManager()) {
                    World regionWorld = region.getRegionworld();
                    List<UUID> owners = region.getRegion().getOwners();
                    if (owners.size() < 1) {
                        continue;
                    }
                    UUID owner = owners.get(0);
                    if (newBestResetAfterMap.get(regionWorld) == null) {
                        newBestResetAfterMap.put(regionWorld, new HashMap<>());
                        newBestTakeoverAfterMap.put(regionWorld, new HashMap<>());
                    }
                    HashMap<UUID, InactivityExpirationGroup> resetUuidMap = newBestResetAfterMap.get(regionWorld);
                    HashMap<UUID, InactivityExpirationGroup> takeoverUuidMap = newBestTakeoverAfterMap.get(regionWorld);
                    if (resetUuidMap.get(owner) == null) {
                        OfflinePlayer oPlayerOwner = Bukkit.getOfflinePlayer(owner);
                        resetUuidMap.put(owner, InactivityExpirationGroup.getBestResetAfterMs(oPlayerOwner, regionWorld));
                        takeoverUuidMap.put(owner, InactivityExpirationGroup.getBestTakeOverAfterMs(oPlayerOwner, regionWorld));
                    }
                }
                bestResetAfterMap = newBestResetAfterMap;
                bestTakeoverAfterMap = newBestTakeoverAfterMap;
                lastUpdated = System.currentTimeMillis();
            }
        });
        updateThread.start();
    }

    public static void reset() {
        bestResetAfterMap = new HashMap<>();
        bestTakeoverAfterMap = new HashMap<>();
        lastUpdated = 0;
    }
}
