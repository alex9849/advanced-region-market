package net.alex9849.adapters;

import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.registry.WorldData;
import net.alex9849.exceptions.ArmInternalException;
import net.alex9849.inter.WGRegion;
import net.alex9849.inter.WorldEditInterface;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class WorldEdit6FaWe extends WorldEditInterface {
    public void createSchematic(WGRegion region, World bukkitworld, WorldEdit we) {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File file = new File(pluginfolder + "/schematics/" + bukkitworld.getName() + "/" + region.getId() + ".schematic");
        File schematicfolder = new File(pluginfolder + "/schematics/" + bukkitworld.getName());
        if(file.exists()){
            file.delete();
        }

        schematicfolder.mkdirs();

        BlockVector minPoint = new BlockVector(region.getMinPoint().getBlockX(), region.getMinPoint().getBlockY(), region.getMinPoint().getBlockZ());
        BlockVector maxPoint = new BlockVector(region.getMaxPoint().getBlockX(), region.getMaxPoint().getBlockY(), region.getMaxPoint().getBlockZ());

        com.sk89q.worldedit.world.World world = new BukkitWorld(bukkitworld);
        WorldData worldData = world.getWorldData();
        try {
            file.createNewFile();
            CuboidRegion reg = new CuboidRegion(world, minPoint, maxPoint);
            Schematic schem = new Schematic(reg);
            schem.save(file, ClipboardFormat.SCHEMATIC);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetBlocks(WGRegion region, World bukkitworld, WorldEdit we) throws IOException, ArmInternalException {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File file = new File(pluginfolder + "/schematics/" + bukkitworld.getName() + "/" + region.getId() + ".schematic");

        if(file == null) {
            throw new ArmInternalException("Could not find schematic file! Does it has been created?");
        }

        com.sk89q.worldedit.world.World world = new BukkitWorld(bukkitworld);
        WorldData worldData = world.getWorldData();
        BlockVector minPoint = new BlockVector(region.getMinPoint().getBlockX(), region.getMinPoint().getBlockY(), region.getMinPoint().getBlockZ());
        Clipboard clipboard;
        try {
            clipboard = ClipboardFormat.SCHEMATIC.getReader(new FileInputStream(file)).read(worldData);
            Extent source = clipboard;
            Extent destination = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, Integer.MAX_VALUE);
            ForwardExtentCopy copy = new ForwardExtentCopy(source, clipboard.getRegion(), clipboard.getOrigin(), destination, minPoint);
            copy.setRemovingEntities(false);

            Operations.completeLegacy(copy);

        } catch (WorldEditException e) {
            e.printStackTrace();
        }
    }
}
