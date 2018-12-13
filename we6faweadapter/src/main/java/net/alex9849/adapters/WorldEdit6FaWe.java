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
import net.alex9849.inter.WGRegion;
import net.alex9849.inter.WorldEditInterface;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class WorldEdit6FaWe extends WorldEditInterface {
    public void createSchematic(WGRegion region, String worldname, WorldEdit we) {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File file = new File(pluginfolder + "/schematics/" + worldname + "/" + region.getId() + ".schematic");
        File schematicfolder = new File(pluginfolder + "/schematics/" + worldname);
        if(file.exists()){
            file.delete();
        }

        schematicfolder.mkdirs();

        BlockVector minPoint = new BlockVector(region.getMinPoint().getBlockX(), region.getMinPoint().getBlockY(), region.getMinPoint().getBlockZ());
        BlockVector maxPoint = new BlockVector(region.getMaxPoint().getBlockX(), region.getMaxPoint().getBlockY(), region.getMaxPoint().getBlockZ());

        com.sk89q.worldedit.world.World world = new BukkitWorld(Bukkit.getWorld(worldname));
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

    public void resetBlocks(WGRegion region, String worldname, WorldEdit we) {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File file = new File(pluginfolder + "/schematics/" + worldname + "/" + region.getId() + ".schematic");

        com.sk89q.worldedit.world.World world = new BukkitWorld(Bukkit.getWorld(worldname));
        WorldData worldData = world.getWorldData();
        BlockVector minPoint = new BlockVector(region.getMinPoint().getBlockX(), region.getMinPoint().getBlockY(), region.getMinPoint().getBlockZ());
        Clipboard clipboard;
        try {
            clipboard = ClipboardFormat.SCHEMATIC.getReader(new FileInputStream(file)).read(worldData);
            Extent source = clipboard;
            Extent destination = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, Integer.MAX_VALUE);
            ForwardExtentCopy copy = new ForwardExtentCopy(source, clipboard.getRegion(), clipboard.getOrigin(), destination, minPoint);

            Operations.completeLegacy(copy);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (WorldEditException e) {
            e.printStackTrace();
        }
    }
}
