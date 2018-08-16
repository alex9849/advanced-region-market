package net.liggesmeyer.adapters;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.util.io.Closer;
import com.sk89q.worldedit.world.registry.WorldData;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.liggesmeyer.inter.WorldEditInterface;
import org.bukkit.Bukkit;

import java.io.*;

public class WorldEdit6 extends WorldEditInterface {

    @Override
    public void createSchematic(ProtectedRegion region, String worldname, WorldEdit we) {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File schematicdic = new File(pluginfolder + "/schematics/" + worldname + "/" + region.getId() + ".schematic");
        File schematicfolder = new File(pluginfolder + "/schematics/" + worldname);
        if(schematicdic.exists()){
            schematicdic.delete();
        }

        schematicfolder.mkdirs();

        if(schematicdic.exists()) {
            schematicdic.delete();
        }
        com.sk89q.worldedit.world.World world = new BukkitWorld(Bukkit.getWorld(worldname));
        WorldData worldData = world.getWorldData();
        EditSession editSession = we.getEditSessionFactory().getEditSession(world, Integer.MAX_VALUE);
        CuboidRegion reg = new CuboidRegion(world, region.getMinimumPoint(), region.getMaximumPoint());
        BlockArrayClipboard clip = new BlockArrayClipboard(reg);
        clip.setOrigin(region.getMinimumPoint());
        ForwardExtentCopy copy = new ForwardExtentCopy(editSession, new CuboidRegion(world, region.getMinimumPoint(), region.getMaximumPoint()), clip, region.getMinimumPoint());
        try {
            Operations.completeLegacy(copy);
        } catch(MaxChangedBlocksException e) {
            e.printStackTrace();
        }
        try {
            Closer closer = Closer.create();
            schematicdic.createNewFile();
            FileOutputStream fileOutputStream = closer.register(new FileOutputStream(schematicdic));
            BufferedOutputStream bufferedOutputStream = closer.register(new BufferedOutputStream(fileOutputStream));
            ClipboardWriter writer = closer.register(ClipboardFormat.SCHEMATIC.getWriter(bufferedOutputStream));
            writer.write(clip, worldData);
            closer.close();
        } catch(IOException e) {
            e.printStackTrace();
            return;
        }
        return;
    }

    @Override
    public void resetBlocks(ProtectedRegion region, String worldname, WorldEdit we) {

        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File file = new File(pluginfolder + "/schematics/" + worldname + "/" + region.getId() + ".schematic");

        com.sk89q.worldedit.world.World world = new BukkitWorld(Bukkit.getWorld(worldname));
        WorldData worldData = world.getWorldData();
        Clipboard clipboard;
        try {
            clipboard = ClipboardFormat.SCHEMATIC.getReader(new FileInputStream(file)).read(worldData);
            Extent source = clipboard;
            Extent destination = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, Integer.MAX_VALUE);
            ForwardExtentCopy copy = new ForwardExtentCopy(source, clipboard.getRegion(), clipboard.getOrigin(), destination, region.getMinimumPoint());

            Operations.completeLegacy(copy);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WorldEditException e) {
            e.printStackTrace();
        }

    }
}
