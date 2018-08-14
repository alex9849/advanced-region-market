package inter;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public abstract class WorldGuardInterface {
    protected WorldGuardPlugin worldGuard;

    public WorldGuardInterface(WorldGuardPlugin wg) {
        this.worldGuard = wg;
    }

    public abstract RegionManager getRegionManager(World world);

    public abstract void addMember(UUID uuid, ProtectedRegion wgRegion);

    public void addMember(OfflinePlayer player, ProtectedRegion wgRegion){
        this.addMember(player.getUniqueId(), wgRegion);
    }

    public abstract boolean hasMember(UUID uuid, ProtectedRegion wgRegion);

    public boolean hasMember(OfflinePlayer player, ProtectedRegion wgRegion){
        return this.hasMember(player.getUniqueId(), wgRegion);
    }

    public abstract void deleteMembers(ProtectedRegion wgRegion);

    public void removeMember(OfflinePlayer player, ProtectedRegion wgRegion){
        this.removeMember(player.getUniqueId(), wgRegion);
    }

    public abstract void removeMember(UUID uuid, ProtectedRegion wgRegion);

    public abstract ArrayList<UUID> getMembers(ProtectedRegion wgRegion);

    public abstract void setOwner(OfflinePlayer player, ProtectedRegion wgRegion);

    public boolean hasOwner(OfflinePlayer player, ProtectedRegion wgRegion){
        return this.hasOwner(player.getUniqueId(), wgRegion);
    }

    public abstract boolean hasOwner(UUID uuid, ProtectedRegion wgRegion);

    public abstract void deleteOwners(ProtectedRegion wgRegion);

    public abstract void removeOwner(UUID uuid, ProtectedRegion wgRegion);

    public void removeOwner(OfflinePlayer player, ProtectedRegion wgRegion){
        this.removeOwner(player.getUniqueId(), wgRegion);
    }

    public abstract ArrayList<UUID> getOwners(ProtectedRegion wgRegion);

    public abstract boolean canBuild(Player player, Location location);

}
