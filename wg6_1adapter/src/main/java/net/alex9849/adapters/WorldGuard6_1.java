package net.alex9849.adapters;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.*;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.alex9849.inter.WGRegion;
import net.alex9849.inter.WorldGuardInterface;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldGuard6_1 extends WorldGuardInterface {
    private HashMap<ProtectedRegion, WG6Region> createdRegions = new HashMap<ProtectedRegion, WG6Region>();

    @Override
    public RegionManager getRegionManager(World world) {
        return WorldGuardPlugin.inst().getRegionManager(world);
    }

    public WG6Region getRegion(World world, String regionID) {
        RegionManager regionManager = this.getRegionManager(world);
        if (regionManager == null) {
            return null;
        }

        ProtectedRegion region = regionManager.getRegion(regionID);
        if (region == null) {
            return null;
        }

        return getUniqueRegion(region);
    }

    public boolean canBuild(Player player, Location location) {
        return WorldGuardPlugin.inst().canBuild(player, location);
    }

    @Override
    public WGRegion createRegion(String regionID, Location pos1, Location pos2) {
        ProtectedRegion protectedRegion = new ProtectedCuboidRegion(regionID, new BlockVector(pos1.getBlockX(), pos1.getBlockY(), pos1.getBlockZ()), new BlockVector(pos2.getBlockX(), pos2.getBlockY(), pos2.getBlockZ()));
        WG6Region returnRegion = getUniqueRegion(protectedRegion);
        return returnRegion;
    }

    @Override
    public List<WGRegion> getApplicableRegions(World world, Location loc) {
        List<ProtectedRegion> protectedRegions = new ArrayList<ProtectedRegion>(WorldGuardPlugin.inst().getRegionManager(world).getApplicableRegions(loc).getRegions());
        List<WGRegion> wg6Regions = new ArrayList<WGRegion>();
        for (ProtectedRegion pRegion : protectedRegions) {
            wg6Regions.add(getUniqueRegion(pRegion));
        }
        return wg6Regions;
    }

    @Override
    public void addToRegionManager(WGRegion region, World world) {
        WG6Region wg6Region = (WG6Region) region;
        getRegionManager(world).addRegion(wg6Region.getRegion());
    }

    @Override
    public void removeFromRegionManager(WGRegion region, World world) {
        WG6Region wg6Region = (WG6Region) region;
        getRegionManager(world).removeRegion(wg6Region.getRegion().getId());
    }

    private WG6Region getUniqueRegion(ProtectedRegion protectedRegion) {
        if (protectedRegion == null) {
            return null;
        }
        if (createdRegions.containsKey(protectedRegion)) {
            return createdRegions.get(protectedRegion);
        }
        WG6Region wg7Region = new WG6Region(protectedRegion);
        createdRegions.put(protectedRegion, wg7Region);
        return wg7Region;
    }

    public Flag fuzzyMatchFlag(String id) {
        return DefaultFlag.fuzzyMatchFlag(id);
    }

    public <V> V parseFlagInput(Flag<V> flag, String input) throws InvalidFlagFormat {
        return flag.parseInput(WorldGuardPlugin.inst(), null, input);
    }

    @Override
    public RegionGroup parseFlagInput(RegionGroupFlag flag, String input) throws InvalidFlagFormat {
        return flag.parseInput(WorldGuardPlugin.inst(), null, input);
    }

    @Override
    public List<String> tabCompleteRegions(String regionName, World world) {
        Map<String, ProtectedRegion> regions = this.getRegionManager(world).getRegions();
        List<String> regionIds = new ArrayList<>();
        for(Map.Entry<String, ProtectedRegion> entry : regions.entrySet()) {
            if(entry.getKey().toLowerCase().startsWith(regionName.toLowerCase())) {
                regionIds.add(entry.getValue().getId());
            }
        }
        return regionIds;
    }

}
