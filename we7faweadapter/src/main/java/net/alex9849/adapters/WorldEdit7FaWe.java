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
