package net.alex9849.adapters;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionType;
import net.alex9849.inter.WGRegion;
import net.alex9849.inter.WorldGuardInterface;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorldGuard6 extends WorldGuardInterface {
    private static List<WG6Region> createdRegions = new ArrayList<WG6Region>();

    @Override
    public RegionManager getRegionManager(World world, WorldGuardPlugin worldGuardPlugin) {
        return worldGuardPlugin.getRegionManager(world);
    }

    public WG6Region getRegion(World world, WorldGuardPlugin worldGuardPlugin, String regionID) {
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
        return worldGuardPlugin.canBuild(player, location);
    }

    @Override
    public WGRegion createRegion(String regionID, Location pos1, Location pos2, WorldGuardPlugin worldGuardPlugin) {
        ProtectedRegion protectedRegion = new ProtectedCuboidRegion(regionID, new BlockVector(pos1.getBlockX(), pos1.getBlockY(), pos1.getBlockZ()), new BlockVector(pos2.getBlockX(), pos2.getBlockY(), pos2.getBlockZ()));
        WG6Region returnRegion = getUniqueRegion(protectedRegion);
        return returnRegion;
    }

    @Override
    public List<WGRegion> getApplicableRegions(World world, Location loc, WorldGuardPlugin worldGuardPlugin) {
        List<ProtectedRegion> protectedRegions = new ArrayList<ProtectedRegion>(worldGuardPlugin.getRegionManager(world).getApplicableRegions(loc).getRegions());
        List<WGRegion> wg6Regions = new ArrayList<WGRegion>();
        for(ProtectedRegion pRegion : protectedRegions) {
            wg6Regions.add(getUniqueRegion(pRegion));
        }
        return wg6Regions;
    }

    @Override
    public void addToRegionManager(WGRegion region, World world, WorldGuardPlugin worldGuardPlugin) {
        WG6Region wg6Region = (WG6Region) region;
        getRegionManager(world, worldGuardPlugin).addRegion(wg6Region.getRegion());
    }

    @Override
    public void removeFromRegionManager(WGRegion region, World world, WorldGuardPlugin worldGuardPlugin) {
        WG6Region wg6Region = (WG6Region) region;
        getRegionManager(world, worldGuardPlugin).removeRegion(wg6Region.getRegion().getId());
    }

    private WG6Region getUniqueRegion(ProtectedRegion protectedRegion) {
        for(WG6Region wgRegion : createdRegions) {
            if(wgRegion.getRegion() == protectedRegion) {
                return wgRegion;
            }
        }
        WG6Region wg6Region = new WG6Region(protectedRegion);
        return wg6Region;
    }

}
