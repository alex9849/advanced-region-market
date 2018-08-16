package net.liggesmeyer.inter;

import com.sk89q.worldedit.WorldEdit;
import org.bukkit.World;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Player;

import java.io.File;

public abstract class WorldEditInterface {

    public abstract void createSchematic(ProtectedRegion region, String worldname, WorldEdit we);

    public abstract void resetBlocks(ProtectedRegion region, String worldname, WorldEdit we);
}
