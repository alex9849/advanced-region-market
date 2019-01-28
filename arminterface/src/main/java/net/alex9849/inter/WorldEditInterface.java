package net.alex9849.inter;

import com.sk89q.worldedit.WorldEdit;
import org.bukkit.World;

import java.io.IOException;

public abstract class WorldEditInterface {

    public abstract void createSchematic(WGRegion region, World bukkitworld, WorldEdit we);

    public abstract void resetBlocks(WGRegion region, World bukkitworld, WorldEdit we) throws IOException;

}
