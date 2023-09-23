package net.alex9849.arm.adapters;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.*;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorldGuard7 extends WorldGuardInterface {

    public RegionManager getRegionManager(World world) {
        return WorldGuard.getInstance().getPlatform().getRegionContainer().get(new BukkitWorld(world));
    }

    public WG7Region getRegion(World world, String regionID) {
        RegionManager regionManager = this.getRegionManager(world);
        if (regionManager == null) {
            return null;
        }

        ProtectedRegion region = regionManager.getRegion(regionID);
        if (region == null) {
            return null;
        }

        return new WG7Region(region);
    }

    public boolean canBuild(Player player, Location location) {
        ApplicableRegionSet regSet = WorldGuard.getInstance().getPlatform().getRegionContainer().get(new BukkitWorld(location.getWorld())).getApplicableRegions(BlockVector3.at(location.getX(), location.getY(), location.getZ()));
        ArrayList<ProtectedRegion> regList = new ArrayList(regSet.getRegions());
        for (int i = 0; i < regList.size(); i++) {
            if (regList.get(i).getOwners().contains(player.getUniqueId()) || regList.get(i).getMembers().contains(player.getUniqueId())) {
                return true;
            }
        }
        return false;

    }

    @Override
    public WGRegion createRegion(String regionID, Location pos1, Location pos2) {
        ProtectedRegion protectedRegion = new ProtectedCuboidRegion(regionID,
                BlockVector3.at(pos1.getBlockX(), pos1.getBlockY(), pos1.getBlockZ()),
                BlockVector3.at(pos2.getBlockX(), pos2.getBlockY(), pos2.getBlockZ()));
        return new WG7Region(protectedRegion);
    }

    @Override
    public List<WGRegion> getApplicableRegions(World world, Location loc) {
        List<ProtectedRegion> protectedRegions = new ArrayList<ProtectedRegion>(this.getRegionManager(world)
                .getApplicableRegions(BlockVector3.at(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())).getRegions());
        List<WGRegion> wg7Regions = new ArrayList<WGRegion>();
        for (ProtectedRegion pRegion : protectedRegions) {
            wg7Regions.add(new WG7Region(pRegion));
        }
        return wg7Regions;
    }

    @Override
    public void addToRegionManager(WGRegion region, World world) {
        WG7Region wg6Region = (WG7Region) region;
        getRegionManager(world).addRegion(wg6Region.getRegion());
    }

    @Override
    public void removeFromRegionManager(WGRegion region, World world) {
        WG7Region wg7Region = (WG7Region) region;
        getRegionManager(world).removeRegion(wg7Region.getRegion().getId());
    }

    public Flag fuzzyMatchFlag(String id) {
        return Flags.fuzzyMatchFlag(WorldGuard.getInstance().getFlagRegistry(), id);
    }

    public <V> V parseFlagInput(Flag<V> flag, String input) throws InvalidFlagFormat {
        return flag.parseInput(FlagContext.create().setInput(input).build());
    }

    @Override
    public RegionGroup parseFlagInput(RegionGroupFlag flag, String input) throws InvalidFlagFormat {
        return flag.parseInput(FlagContext.create().setInput(input).build());
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
