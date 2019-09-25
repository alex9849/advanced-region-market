package net.alex9849.inter;

import com.sk89q.worldedit.WorldEdit;
import net.alex9849.arm.exceptions.SchematicException;
import org.bukkit.World;

public abstract class WorldEditInterface {

    public abstract void createSchematic(WGRegion region, World bukkitworld, WorldEdit we);

    public abstract void resetBlocks(WGRegion region, World bukkitworld, WorldEdit we) throws SchematicException;

}
