package net.liggesmeyer.adapters;

import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.util.io.Closer;
import com.sk89q.worldedit.world.World;
import net.liggesmeyer.inter.WorldEditInterface;
import org.bukkit.Bukkit;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class WorldEdit7FaWe extends WorldEditInterface {

    public void createSchematic(com.sk89q.worldguard.protection.regions.ProtectedRegion region, String worldname, com.sk89q.worldedit.WorldEdit we) {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File schematicdic = new File(pluginfolder + "/schematics/" + worldname + "/" + region.getId() + ".schematic");
        File schematicfolder = new File(pluginfolder + "/schematics/" + worldname);
        if(schematicdic.exists()){
            schematicdic.delete();
        }

        schematicfolder.mkdirs();

        com.sk89q.worldedit.world.World world = new BukkitWorld(Bukkit.getWorld(worldname));
        CuboidRegion reg = new CuboidRegion(world, region.getMinimumPoint(), region.getMaximumPoint());
        Schematic schem = new Schematic(reg);
        try {
            schematicdic.createNewFile();
            schem.save(schematicdic, ClipboardFormat.SPONGE_SCHEMATIC);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void resetBlocks(com.sk89q.worldguard.protection.regions.ProtectedRegion region, String worldname, com.sk89q.worldedit.WorldEdit we) {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File file = new File(pluginfolder + "/schematics/" + worldname + "/" + region.getId() + ".schematic");
        World weWorld = new BukkitWorld(Bukkit.getWorld(worldname));
        try {
            ClipboardFormats.findByFile(file).load(file).paste(weWorld, region.getMinimumPoint());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
