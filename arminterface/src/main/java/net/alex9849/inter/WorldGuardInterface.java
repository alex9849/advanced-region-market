package net.alex9849.inter;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.RegionGroupFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class WorldGuardInterface {

    public abstract RegionManager getRegionManager(World world);

    public abstract WGRegion getRegion(World world, String regionID);

    public abstract boolean canBuild(Player player, Location location);

    public abstract WGRegion createRegion(String regionID, Location pos1, Location pos2);

    public abstract List<WGRegion> getApplicableRegions(World world, Location loc);

    public abstract void addToRegionManager(WGRegion region, World world);

    public abstract void removeFromRegionManager(WGRegion region, World world);

    public abstract <V> V parseFlagInput(Flag<V> flag, String input) throws InvalidFlagFormat;

    public abstract RegionGroup parseFlagInput(RegionGroupFlag flag, String input) throws InvalidFlagFormat;

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

    public abstract Flag fuzzyMatchFlag(String id);
}
