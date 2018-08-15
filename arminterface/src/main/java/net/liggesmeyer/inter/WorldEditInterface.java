package net.liggesmeyer.inter;

import org.bukkit.World;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Player;

import java.io.File;

public abstract class WorldEditInterface {

    public abstract void createSchematic(ProtectedRegion region, File schematicpath, File schematicdic, World world);

    public abstract void resetBlocks(ProtectedRegion region, File schematicpath, World world);
}
