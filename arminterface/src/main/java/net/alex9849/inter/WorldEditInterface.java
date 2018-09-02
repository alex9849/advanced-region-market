package net.alex9849.inter;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public abstract class WorldEditInterface {

    public abstract void createSchematic(ProtectedRegion region, String worldname, WorldEdit we);

    public abstract void resetBlocks(ProtectedRegion region, String worldname, WorldEdit we);

}
