package net.alex9849.inter;

import net.alex9849.arm.exceptions.SchematicNotFoundException;
import org.bukkit.World;

import java.io.File;

public abstract class WorldEditInterface {

    public abstract void createSchematic(WGRegion region, World bukkitworld, File saveFolder, String fileNameWithoutEnding);

    public abstract void restoreSchematic(WGRegion region, World bukkitworld, File schematicPathWithoutFileEnding) throws SchematicNotFoundException;

}
