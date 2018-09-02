package net.alex9849.adapters;

import com.sk89q.worldedit.Vector;
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

public class WorldGuard7 extends WorldGuardInterface {

    public RegionManager getRegionManager(World world, WorldGuardPlugin worldGuardPlugin) {
        return WorldGuard.getInstance().getPlatform().getRegionContainer().get(new BukkitWorld(world));
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
        ApplicableRegionSet regSet = WorldGuard.getInstance().getPlatform().getRegionContainer().get(new BukkitWorld(location.getWorld())).getApplicableRegions(new Vector(location.getX(), location.getY(), location.getZ()));
        ArrayList<ProtectedRegion> regList = new ArrayList(regSet.getRegions());
        for(int i = 0; i < regList.size(); i++) {
            if(regList.get(i).getOwners().contains(player.getUniqueId()) || regList.get(i).getMembers().contains(player.getUniqueId())) {
                return true;
            }
        }
        return false;
    }
}
