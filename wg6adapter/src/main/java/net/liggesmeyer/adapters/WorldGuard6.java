package net.liggesmeyer.adapters;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.liggesmeyer.inter.WorldGuardInterface;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class WorldGuard6 extends WorldGuardInterface {

    public WorldGuard6(WorldGuardPlugin wg) {
        super(wg);
    }

    @Override
    public RegionManager getRegionManager(World world) {
        return this.worldGuard.getRegionManager(world);
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

    public boolean canBuild(Player player, Location location){
        return this.worldGuard.canBuild(player, location);
    }


}
