package net.alex9849.adapters;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.util.io.Closer;
import net.alex9849.exceptions.SchematicNotFoundException;
import net.alex9849.inter.WGRegion;
import net.alex9849.inter.WorldEditInterface;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.*;

public class WorldEdit7 extends WorldEditInterface {

    @Override
    public void createSchematic(WGRegion region, World bukkitworld, WorldEdit we) {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File rawschematicdic = new File(pluginfolder + "/schematics/" + bukkitworld.getName() + "/" + region.getId());
        File schematicdic = new File(rawschematicdic + "." + BuiltInClipboardFormat.SPONGE_SCHEMATIC.getPrimaryFileExtension());
        File schematicfolder = new File(pluginfolder + "/schematics/" + bukkitworld.getName());

        for (BuiltInClipboardFormat format : BuiltInClipboardFormat.values()) {
            for (String extension : format.getFileExtensions()) {
                if (new File(rawschematicdic.getAbsolutePath() + "." + extension).exists()) {
                    File delfile = new File(rawschematicdic.getAbsolutePath() + "." + extension);
                    delfile.delete();
                }
            }
        }

        schematicfolder.mkdirs();

        com.sk89q.worldedit.world.World world = new BukkitWorld(bukkitworld);
        BlockVector3 minPoint = BlockVector3.at(region.getMinPoint().getBlockX(), region.getMinPoint().getBlockY(), region.getMinPoint().getBlockZ());
        BlockVector3 maxPoint = BlockVector3.at(region.getMaxPoint().getBlockX(), region.getMaxPoint().getBlockY(), region.getMaxPoint().getBlockZ());

        EditSession editSession = we.getEditSessionFactory().getEditSession(world, Integer.MAX_VALUE);
        CuboidRegion reg = new CuboidRegion(world, minPoint, maxPoint);
        BlockArrayClipboard clip = new BlockArrayClipboard(reg);
        clip.setOrigin(minPoint);
        ForwardExtentCopy copy = new ForwardExtentCopy(editSession, new CuboidRegion(world, minPoint, maxPoint), clip, minPoint);
        copy.setCopyingEntities(true);
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
    public void resetBlocks(WGRegion region, World bukkitworld, WorldEdit we) throws IOException {

        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File rawschematicdic = new File(pluginfolder + "/schematics/" + bukkitworld.getName() + "/" + region.getId());
        File file = null;

        for (BuiltInClipboardFormat format : BuiltInClipboardFormat.values()) {
            for (String extension : format.getFileExtensions()) {
                if (new File(rawschematicdic.getAbsolutePath() + "." + extension).exists()) {
                    file = new File(rawschematicdic.getAbsolutePath() + "." + extension);
                }
            }
        }

        if(file == null) {
            throw new SchematicNotFoundException(region);
        }

        com.sk89q.worldedit.world.World world = new BukkitWorld(bukkitworld);
        BlockVector3 minPoint = BlockVector3.at(region.getMinPoint().getBlockX(), region.getMinPoint().getBlockY(), region.getMinPoint().getBlockZ());
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
            ForwardExtentCopy copy = new ForwardExtentCopy(source, clipboard.getRegion(), clipboard.getOrigin(), destination, minPoint);

            Operations.completeLegacy(copy);
            ((EditSession) destination).flushSession();
            closer.close();
        } catch (SchematicNotFoundException e) {
            throw e;
        } catch (Exception e) {
            Bukkit.getLogger().info("Could not load schematic " + file.getAbsolutePath() + " please check your WorldEdit version or regenerate the schematic file!");
            e.printStackTrace();
        }
    }
}
