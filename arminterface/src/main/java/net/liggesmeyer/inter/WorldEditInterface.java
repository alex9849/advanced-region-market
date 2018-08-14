package net.liggesmeyer.inter;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Player;

import java.io.File;

public abstract class WorldEditInterface {

    public abstract void createSchematic(ProtectedRegion region, File filepath);

    public abstract void resetBlocks(ProtectedRegion region, File filepath, Player player);

    public void resetBlocks(ProtectedRegion region, File filepath) {
        this.resetBlocks(region, filepath, null);
    }
}
