package net.alex9849.adapters;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
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
import java.util.HashSet;
import java.util.List;

public class WorldGuard7Beta01 extends WorldGuardInterface {
    private static HashMap<ProtectedRegion, WG7RegionBeta01> createdRegions = new HashMap<ProtectedRegion, WG7RegionBeta01>();

    public RegionManager getRegionManager(World world, WorldGuardPlugin worldGuardPlugin) {
        return WorldGuard.getInstance().getPlatform().getRegionContainer().get(new BukkitWorld(world));
    }

    public WG7RegionBeta01 getRegion(World world, WorldGuardPlugin worldGuardPlugin, String regionID) {
        RegionManager regionManager = this.getRegionManager(world, worldGuardPlugin);
        if(regionManager == null) {
            return null;
        }

        ProtectedRegion region = regionManager.getRegion(regionID);
        if(region == null) {
            return null;
        }

        return getUniqueRegion(region);
    }

    public boolean canBuild(Player player, Location location, WorldGuardPlugin worldGuardPlugin){

        ApplicableRegionSet regSet = WorldGuard.getInstance().getPlatform().getRegionContainer().get(new BukkitWorld(location.getWorld())).getApplicableRegions(new Vector(location.getX(), location.getY(), location.getZ()));
        ArrayList<ProtectedRegion> regList = new ArrayList(regSet.getRegions());
        for(int i = 0; i < regList.size(); i++) {
            if(regList.get(i).getOwners().contains(player.getUniqueId()) || regList.get(i).getMembers().contains(player.getUniqueId())) {
                return true;
            }
        }
        return false;

    }

    @Override
    public WGRegion createRegion(String regionID, Location pos1, Location pos2, WorldGuardPlugin worldGuardPlugin) {
        ProtectedRegion protectedRegion = new ProtectedCuboidRegion(regionID, new BlockVector(pos1.getBlockX(), pos1.getBlockY(), pos1.getBlockZ()), new BlockVector(pos2.getBlockX(), pos2.getBlockY(), pos2.getBlockZ()));
        WG7RegionBeta01 returnRegion = getUniqueRegion(protectedRegion);
        return returnRegion;
    }

    @Override
    public List<WGRegion> getApplicableRegions(World world, Location loc, WorldGuardPlugin worldGuardPlugin) {
        List<ProtectedRegion> protectedRegions = new ArrayList<ProtectedRegion>(this.getRegionManager(world, worldGuardPlugin).getApplicableRegions(new Vector(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())).getRegions());
        List<WGRegion> wg7Regions = new ArrayList<WGRegion>();
        for(ProtectedRegion pRegion : protectedRegions) {
            wg7Regions.add(getUniqueRegion(pRegion));
        }
        return wg7Regions;
    }

    @Override
    public void addToRegionManager(WGRegion region, World world, WorldGuardPlugin worldGuardPlugin) {
        WG7RegionBeta01 wg6Region = (WG7RegionBeta01) region;
        getRegionManager(world, worldGuardPlugin).addRegion(wg6Region.getRegion());
    }

    @Override
    public void removeFromRegionManager(WGRegion region, World world, WorldGuardPlugin worldGuardPlugin) {
        WG7RegionBeta01 wg7Region = (WG7RegionBeta01) region;
        getRegionManager(world, worldGuardPlugin).removeRegion(wg7Region.getRegion().getId());
    }

    private WG7RegionBeta01 getUniqueRegion(ProtectedRegion protectedRegion) {
        if(protectedRegion == null) {
            return null;
        }
        if(createdRegions.containsKey(protectedRegion)) {
            return createdRegions.get(protectedRegion);
        }
        WG7RegionBeta01 wg7Region = new WG7RegionBeta01(protectedRegion);
        createdRegions.put(protectedRegion, wg7Region);
        return wg7Region;
    }

    public Flag fuzzyMatchFlag(String id) {
        return Flags.fuzzyMatchFlag(WorldGuard.getInstance().getFlagRegistry(), id);
    }

    public <V> V parseFlagInput(Flag<V> flag , String input) throws InvalidFlagFormat {
        return flag.parseInput(FlagContext.create().setInput(input).build());
    }

    @Override
    public RegionGroup parseFlagInput(RegionGroupFlag flag, String input) throws InvalidFlagFormat {
        return flag.parseInput(FlagContext.create().setInput(input).build());
    }

}
