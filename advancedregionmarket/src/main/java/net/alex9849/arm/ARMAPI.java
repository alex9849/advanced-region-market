package net.alex9849.arm;

import net.alex9849.arm.regions.Region;
import org.bukkit.World;

import java.util.List;
import java.util.UUID;

public class ARMAPI {

    public List<Region> getRegionList() {
        return Region.getRegionList();
    }

    public List<Region> getRegionsByOwner(UUID uuid) {
        return Region.getRegionsByOwner(uuid);
    }

    public List<Region> getRegionsByMember(UUID uuid) {
        return Region.getRegionsByMember(uuid);
    }

    public Region getRegionbyIDAndWorld(World world, String id){
        return this.getRegionbyIDAndWorld(id, world.getName());
    }

    public Region getRegionbyIDAndWorld(String world, String id){
        return Region.searchRegionbyNameAndWorld(id, world);
    }


}
