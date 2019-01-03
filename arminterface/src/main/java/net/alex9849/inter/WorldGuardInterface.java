package net.alex9849.inter;

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

    public abstract RegionManager getRegionManager(World world, WorldGuardPlugin worldGuardPlugin);

    public abstract WGRegion getRegion(World world, WorldGuardPlugin worldGuardPlugin, String regionID);

    public abstract boolean canBuild(Player player, Location location, WorldGuardPlugin worldGuardPlugin);

    public abstract WGRegion createRegion(WGRegion parentRegion, Location pos1, Location pos2);
}
