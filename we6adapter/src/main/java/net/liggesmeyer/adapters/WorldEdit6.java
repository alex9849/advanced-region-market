package net.liggesmeyer.adapters;

import com.boydti.fawe.object.schematic.Schematic;
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
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.*;

public class WorldEdit6 extends WorldEditInterface {
    @Override
    public void createSchematic(ProtectedRegion region, File schematicpath, File schematicdic, World world) {

        if(schematicpath.exists()){
            schematicpath.delete();
        }

        Vector max = region.getMaximumPoint();
        Vector min = region.getMinimumPoint();

        schematicdic.mkdirs();

 /*       if(Main.isFaWeInstalled()) {
            CuboidRegion copyregion = new CuboidRegion(new BukkitWorld(world), min, max);
            Schematic schematic = new Schematic(copyregion);
            try {
                schematic.save(schematicpath, ClipboardFormat.SCHEMATIC);
            } catch (IOException e) {
                e.printStackTrace();
            }
 *///       } else {

            com.sk89q.worldedit.world.World weWorld = new BukkitWorld(world);
            WorldData worldData = weWorld.getWorldData();
            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, Integer.MAX_VALUE);
            CuboidRegion reg = new CuboidRegion(weWorld, region.getMinimumPoint(), region.getMaximumPoint());
            BlockArrayClipboard clip = new BlockArrayClipboard(reg);
            clip.setOrigin(region.getMinimumPoint());
            ForwardExtentCopy copy = new ForwardExtentCopy(editSession, new CuboidRegion(weWorld, region.getMinimumPoint(), region.getMaximumPoint()), clip, region.getMinimumPoint());
            try {
                Operations.completeLegacy(copy);
            } catch(MaxChangedBlocksException e) {
                e.printStackTrace();
            }
            try {
                Closer closer = Closer.create();
                schematicpath.createNewFile();
                FileOutputStream fileOutputStream = closer.register(new FileOutputStream(schematicpath));
                BufferedOutputStream bufferedOutputStream = closer.register(new BufferedOutputStream(fileOutputStream));
                ClipboardWriter writer = closer.register(ClipboardFormat.SCHEMATIC.getWriter(bufferedOutputStream));
                writer.write(clip, worldData);
                closer.close();
                writer.close();
            } catch(IOException e) {
                e.printStackTrace();
                return;
            }
            return;
    //    }
    }

    @Override
    public void resetBlocks(ProtectedRegion region, File schematicpath, World world) {

    /*    if(Main.isFaWeInstalled()) {

            com.sk89q.worldedit.world.World weWorld = new BukkitWorld(world);
            WorldData worldData = weWorld.getWorldData();
            Clipboard clipboard;
            try {
                clipboard = ClipboardFormat.SCHEMATIC.getReader(new FileInputStream(schematicpath)).read(worldData);
                Schematic schem = new Schematic(clipboard);
                schem.paste(weWorld, region.getMinimumPoint());
            } catch (IOException e) {
                e.printStackTrace();
            }

   *///     } else {

            com.sk89q.worldedit.world.World weWorld = new BukkitWorld(world);
            WorldData worldData = weWorld.getWorldData();
            Clipboard clipboard;
            try {
                clipboard = ClipboardFormat.SCHEMATIC.getReader(new FileInputStream(schematicpath)).read(worldData);
                Extent source = clipboard;
                Extent destination = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, Integer.MAX_VALUE);
                ForwardExtentCopy copy = new ForwardExtentCopy(source, clipboard.getRegion(), clipboard.getOrigin(), destination, region.getMinimumPoint());

                Operations.completeLegacy(copy);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (WorldEditException e) {
                e.printStackTrace();
            }

    //    }
    }
}
