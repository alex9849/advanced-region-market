package net.alex9849.adapters;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.util.io.Closer;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.alex9849.inter.WorldEditInterface;
import org.bukkit.Bukkit;

import java.io.*;
import java.util.logging.Level;

public class WorldEdit7 extends WorldEditInterface {

    @Override
    public void createSchematic(ProtectedRegion region, String worldname, WorldEdit we) {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File rawschematicdic = new File(pluginfolder + "/schematics/" + worldname + "/" + region.getId());
        File schematicdic = new File(rawschematicdic + "." + BuiltInClipboardFormat.SPONGE_SCHEMATIC.getPrimaryFileExtension());
        File schematicfolder = new File(pluginfolder + "/schematics/" + worldname);

        for (BuiltInClipboardFormat format : BuiltInClipboardFormat.values()) {
            for (String extension : format.getFileExtensions()) {
                if (new File(rawschematicdic.getAbsolutePath() + "." + extension).exists()) {
                    File delfile = new File(rawschematicdic.getAbsolutePath() + "." + extension);
                    delfile.delete();
                }
            }
        }

        schematicfolder.mkdirs();

        com.sk89q.worldedit.world.World world = new BukkitWorld(Bukkit.getWorld(worldname));
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
            ClipboardWriter writer = closer.register(BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(bufferedOutputStream));
            writer.write(clip);
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
        File rawschematicdic = new File(pluginfolder + "/schematics/" + worldname + "/" + region.getId());
        File file = null;

        for (BuiltInClipboardFormat format : BuiltInClipboardFormat.values()) {
            for (String extension : format.getFileExtensions()) {
                if (new File(rawschematicdic.getAbsolutePath() + "." + extension).exists()) {
                    file = new File(rawschematicdic.getAbsolutePath() + "." + extension);
                }
            }
        }

        com.sk89q.worldedit.world.World world = new BukkitWorld(Bukkit.getWorld(worldname));
        Clipboard clipboard;
        try {
            Closer closer = Closer.create();
            FileInputStream fileInputStream = closer.register(new FileInputStream(file));
            BufferedInputStream bufferedInputStream = closer.register(new BufferedInputStream(fileInputStream));
            ClipboardFormat clipboardFormat = ClipboardFormats.findByFile(file);
            ClipboardReader reader = clipboardFormat.getReader(bufferedInputStream);
            clipboard = reader.read();
            Extent source = clipboard;
            Extent destination = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, Integer.MAX_VALUE);
            ForwardExtentCopy copy = new ForwardExtentCopy(source, clipboard.getRegion(), clipboard.getOrigin(), destination, region.getMinimumPoint());

            Operations.completeLegacy(copy);
            closer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WorldEditException e) {
            e.printStackTrace();
        }
    }
}
