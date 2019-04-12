package net.alex9849.adapters;

import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import net.alex9849.exceptions.SchematicNotFoundException;
import net.alex9849.inter.WGRegion;
import net.alex9849.inter.WorldEditInterface;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;

public class WorldEdit7FaWe extends WorldEditInterface {

    public void createSchematic(WGRegion region, org.bukkit.World bukkitworld, com.sk89q.worldedit.WorldEdit we) {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File rawschematicdic = new File(pluginfolder + "/schematics/" + bukkitworld.getName() + "/" + region.getId());
        File schematicdic = new File(rawschematicdic + "." + BuiltInClipboardFormat.SPONGE_SCHEMATIC.getPrimaryFileExtension());
        File schematicfolder = new File(pluginfolder + "/schematics/" + bukkitworld.getName());

        for (ClipboardFormat format : BuiltInClipboardFormat.values()) {
            for (String extension : format.getFileExtensions()) {
                if (new File(rawschematicdic.getAbsolutePath() + "." + extension).exists()) {
                    File delfile = new File(rawschematicdic.getAbsolutePath() + "." + extension);
                    delfile.delete();
                }
            }
        }

        schematicfolder.mkdirs();
        BlockVector3 minPoint = BlockVector3.at(region.getMinPoint().getBlockX(), region.getMinPoint().getBlockY(), region.getMinPoint().getBlockZ());
        BlockVector3 maxPoint = BlockVector3.at(region.getMaxPoint().getBlockX(), region.getMaxPoint().getBlockY(), region.getMaxPoint().getBlockZ());

        com.sk89q.worldedit.world.World world = new BukkitWorld(bukkitworld);
        CuboidRegion reg = new CuboidRegion(world, minPoint, maxPoint);
        Schematic schem = new Schematic(reg);
        try {
            schematicdic.createNewFile();
            schem.save(schematicdic, BuiltInClipboardFormat.SPONGE_SCHEMATIC);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void resetBlocks(WGRegion region, org.bukkit.World bukkitworld, com.sk89q.worldedit.WorldEdit we) throws IOException {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File rawschematicdic = new File(pluginfolder + "/schematics/" + bukkitworld.getName() + "/" + region.getId());
        File file = null;

        for (ClipboardFormat format : BuiltInClipboardFormat.values()) {
            for (String extension : format.getFileExtensions()) {
                if (new File(rawschematicdic.getAbsolutePath() + "." + extension).exists()) {
                    file = new File(rawschematicdic.getAbsolutePath() + "." + extension);
                }
            }
        }

        if(file == null) {
            throw new SchematicNotFoundException(region);
        }

        World weWorld = new BukkitWorld(bukkitworld);
        BlockVector3 minPoint = BlockVector3.at(region.getMinPoint().getBlockX(), region.getMinPoint().getBlockY(), region.getMinPoint().getBlockZ());
        if(BuiltInClipboardFormat.SPONGE_SCHEMATIC.isFormat(file)) {
            BuiltInClipboardFormat.SPONGE_SCHEMATIC.load(file).paste(weWorld, minPoint);
        } else {
            ClipboardFormats.findByFile(file).load(file).paste(weWorld, minPoint);
        }
    }
}
