package net.alex9849.inter;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.RegionGroupFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class WorldGuardInterface {

    public abstract RegionManager getRegionManager(World world, WorldGuardPlugin worldGuardPlugin);

    public abstract WGRegion getRegion(World world, WorldGuardPlugin worldGuardPlugin, String regionID);

    public abstract boolean canBuild(Player player, Location location, WorldGuardPlugin worldGuardPlugin);

    public abstract WGRegion createRegion(String regionID, Location pos1, Location pos2, WorldGuardPlugin worldGuardPlugin);

    public abstract List<WGRegion> getApplicableRegions(World world, Location loc, WorldGuardPlugin worldGuardPlugin);

    public abstract void addToRegionManager(WGRegion region, World world, WorldGuardPlugin worldGuardPlugin);

    public abstract void removeFromRegionManager(WGRegion region, World world, WorldGuardPlugin worldGuardPlugin);

    public abstract <V> V parseFlagInput(Flag<V> flag , String input) throws InvalidFlagFormat;

    public abstract RegionGroup parseFlagInput(RegionGroupFlag flag , String input) throws InvalidFlagFormat;

    public abstract Flag fuzzyMatchFlag(String id);
}
