package Interfaces;

import net.liggesmeyer.arm.regions.Region;
import org.bukkit.entity.Player;

public abstract class WorldEditInterface {

    public abstract void createSchematic(Region region);

    public abstract void resetBlocks(Region region, Player player);

    public void resetBlocks(Region region) {
        this.resetBlocks(region, null);
    }
}
