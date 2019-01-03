package net.alex9849.adapters;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.alex9849.inter.WGRegion;
import net.alex9849.inter.WorldGuardInterface;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class WorldGuard6 extends WorldGuardInterface {

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

        return new WG6Region(region);
    }

    public boolean canBuild(Player player, Location location, WorldGuardPlugin worldGuardPlugin){
        return worldGuardPlugin.canBuild(player, location);
    }

    @Override
    public WGRegion createRegion(WGRegion parentRegion, Location pos1, Location pos2) {
        return null;
    }

}
