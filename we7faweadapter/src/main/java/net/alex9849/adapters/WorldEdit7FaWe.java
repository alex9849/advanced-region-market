package net.alex9849.adapters;

import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import net.alex9849.inter.WorldEditInterface;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class WorldEdit7FaWe extends WorldEditInterface {

    public void createSchematic(com.sk89q.worldguard.protection.regions.ProtectedRegion region, String worldname, com.sk89q.worldedit.WorldEdit we) {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File rawschematicdic = new File(pluginfolder + "/schematics/" + worldname + "/" + region.getId());
        File schematicdic = new File(rawschematicdic + "." + ClipboardFormat.SPONGE_SCHEMATIC.getPrimaryFileExtension());
        File schematicfolder = new File(pluginfolder + "/schematics/" + worldname);

        for (ClipboardFormat format : ClipboardFormat.values()) {
            for (String extension : format.getFileExtensions()) {
                if (new File(rawschematicdic.getAbsolutePath() + "." + extension).exists()) {
                    File delfile = new File(rawschematicdic.getAbsolutePath() + "." + extension);
                    delfile.delete();
                }
            }
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
        File rawschematicdic = new File(pluginfolder + "/schematics/" + worldname + "/" + region.getId());
        File file = null;

        for (ClipboardFormat format : ClipboardFormat.values()) {
            for (String extension : format.getFileExtensions()) {
                if (new File(rawschematicdic.getAbsolutePath() + "." + extension).exists()) {
                    file = new File(rawschematicdic.getAbsolutePath() + "." + extension);
                }
            }
        }
        World weWorld = new BukkitWorld(Bukkit.getWorld(worldname));
        try {
            if(ClipboardFormat.SPONGE_SCHEMATIC.isFormat(file)) {
                ClipboardFormat.SPONGE_SCHEMATIC.load(file).paste(weWorld, region.getMinimumPoint());
            } else if(ClipboardFormat.SCHEMATIC.isFormat(file)) {
                ClipboardFormat.SCHEMATIC.load(file).paste(weWorld, region.getMinimumPoint());
            } else {
                ClipboardFormats.findByFile(file).load(file).paste(weWorld, region.getMinimumPoint());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
