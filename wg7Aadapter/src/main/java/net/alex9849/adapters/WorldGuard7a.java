package net.alex9849.adapters;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.alex9849.inter.WorldGuardInterface;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class WorldGuard7a extends WorldGuardInterface {

    public RegionManager getRegionManager(World world, WorldGuardPlugin worldGuardPlugin) {
        return WorldGuard.getInstance().getPlatform().getRegionContainer().get(new BukkitWorld(world));
    }

    public WG7aRegion getRegion(World world, WorldGuardPlugin worldGuardPlugin, String regionID) {
        RegionManager regionManager = this.getRegionManager(world, worldGuardPlugin);
        if(regionManager == null) {
            return null;
        }

        ProtectedRegion region = regionManager.getRegion(regionID);
        if(region == null) {
            return null;
        }

        return new WG7aRegion(region);
    }

    @Override
    public void addMember(UUID uuid, ProtectedRegion wgRegion) {
        wgRegion.getMembers().addPlayer(uuid);
    }

    @Override
    public boolean hasMember(UUID uuid, ProtectedRegion wgRegion) {
        return wgRegion.getMembers().contains(uuid);
    }

    public void deleteMembers(ProtectedRegion wgRegion) {
        DefaultDomain defaultDomain = new DefaultDomain();
        wgRegion.setMembers(defaultDomain);
    }

    @Override
    public void removeMember(UUID uuid, ProtectedRegion wgRegion) {
        wgRegion.getMembers().removePlayer(uuid);
    }

    @Override
    public ArrayList<UUID> getMembers(ProtectedRegion wgRegion) {
        return new ArrayList<UUID>(wgRegion.getMembers().getUniqueIds());
    }

    @Override
    public void setOwner(OfflinePlayer player, ProtectedRegion wgRegion) {
        DefaultDomain newOwner = new DefaultDomain();
        newOwner.addPlayer(player.getUniqueId());
        wgRegion.setOwners(newOwner);
    }

    @Override
    public boolean hasOwner(UUID uuid, ProtectedRegion wgRegion) {
        return wgRegion.getOwners().contains(uuid);
    }

    public void deleteOwners(ProtectedRegion wgRegion) {
        DefaultDomain defaultDomain = new DefaultDomain();
        wgRegion.setOwners(defaultDomain);
    }

    @Override
    public void removeOwner(UUID uuid, ProtectedRegion wgRegion) {
        wgRegion.getOwners().removePlayer(uuid);
    }

    @Override
    public ArrayList<UUID> getOwners(ProtectedRegion wgRegion) {
        return new ArrayList<UUID>(wgRegion.getOwners().getUniqueIds());
    }

    public boolean canBuild(Player player, Location location, WorldGuardPlugin worldGuardPlugin){

        /*
        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(location);

        return query.testState(loc, WorldGuardPlugin.inst().wrapPlayer(player), Flags.BUILD);
        */

        ApplicableRegionSet regSet = WorldGuard.getInstance().getPlatform().getRegionContainer().get(new BukkitWorld(location.getWorld())).getApplicableRegions(new BlockVector(location.getX(), location.getY(), location.getZ()));
        ArrayList<ProtectedRegion> regList = new ArrayList(regSet.getRegions());
        for(int i = 0; i < regList.size(); i++) {
            if(regList.get(i).getOwners().contains(player.getUniqueId()) || regList.get(i).getMembers().contains(player.getUniqueId())) {
                return true;
            }
        }
        return false;

    }

    public int getMaxX(ProtectedRegion region) {
        return region.getMaximumPoint().getBlockX();
    }

    public int getMaxY(ProtectedRegion region) {
        return region.getMaximumPoint().getBlockY();
    }

    public int getMaxZ(ProtectedRegion region) {
        return region.getMaximumPoint().getBlockZ();
    }

    public int getMinX(ProtectedRegion region) {
        return region.getMinimumPoint().getBlockX();
    }

    public int getMinY(ProtectedRegion region) {
        return region.getMinimumPoint().getBlockY();
    }

    public int getMinZ(ProtectedRegion region) {
        return region.getMinimumPoint().getBlockZ();
    }

}
