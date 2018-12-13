package net.alex9849.inter;

import com.sk89q.worldedit.WorldEdit;

public abstract class WorldEditInterface {

    public abstract void createSchematic(WGRegion region, String worldname, WorldEdit we);

    public abstract void resetBlocks(WGRegion region, String worldname, WorldEdit we);

}
