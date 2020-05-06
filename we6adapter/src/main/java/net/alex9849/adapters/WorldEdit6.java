package net.alex9849.adapters;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.Mask2D;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.util.io.Closer;
import com.sk89q.worldedit.world.registry.WorldData;
import net.alex9849.arm.exceptions.SchematicNotFoundException;
import net.alex9849.inter.WGRegion;
import net.alex9849.inter.WorldEditInterface;
import org.bukkit.World;

import javax.annotation.Nullable;
import java.io.*;

public class WorldEdit6 extends WorldEditInterface {

    @Override
    public void createSchematic(WGRegion region, World bukkitworld, File saveFolder, String fileNameWithoutEnding) {

        File schematicWithoutEnding = new File(saveFolder + "/" + fileNameWithoutEnding);
        File schematicPath = new File(schematicWithoutEnding + ".schematic");

        for (ClipboardFormat format : ClipboardFormat.values()) {
            for (String extension : format.getAliases()) {
                if (new File(schematicWithoutEnding.getAbsolutePath() + "." + extension).exists()) {
                    File delfile = new File(schematicWithoutEnding.getAbsolutePath() + "." + extension);
                    delfile.delete();
                }
            }
        }

        saveFolder.mkdirs();

        BlockVector minPoint = new BlockVector(region.getMinPoint().getBlockX(), region.getMinPoint().getBlockY(), region.getMinPoint().getBlockZ());
        BlockVector maxPoint = new BlockVector(region.getMaxPoint().getBlockX(), region.getMaxPoint().getBlockY(), region.getMaxPoint().getBlockZ());

        com.sk89q.worldedit.world.World world = new BukkitWorld(bukkitworld);
        WorldData worldData = world.getWorldData();
        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, Integer.MAX_VALUE);
        CuboidRegion reg = new CuboidRegion(world, minPoint, maxPoint);
        BlockArrayClipboard clip = new BlockArrayClipboard(reg);
        clip.setOrigin(minPoint);
        ForwardExtentCopy copy = new ForwardExtentCopy(editSession, new CuboidRegion(world, minPoint, maxPoint), clip, minPoint);
        try {
            Operations.completeLegacy(copy);
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }
        try {
            Closer closer = Closer.create();
            schematicPath.createNewFile();
            FileOutputStream fileOutputStream = closer.register(new FileOutputStream(schematicPath));
            BufferedOutputStream bufferedOutputStream = closer.register(new BufferedOutputStream(fileOutputStream));
            ClipboardWriter writer = closer.register(ClipboardFormat.SCHEMATIC.getWriter(bufferedOutputStream));
            writer.write(clip, worldData);
            closer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        return;
    }

    @Override
    public void restoreSchematic(WGRegion region, World bukkitworld, File schematicPathWithoutFileEnding) throws SchematicNotFoundException {

        File schematicPath = null;

        for (ClipboardFormat format : ClipboardFormat.values()) {
            for (String extension : format.getAliases()) {
                if (new File(schematicPathWithoutFileEnding.getAbsolutePath() + "." + extension).exists()) {
                    schematicPath = new File(schematicPathWithoutFileEnding.getAbsolutePath() + "." + extension);
                }
            }
        }

        if (schematicPath == null) {
            throw new SchematicNotFoundException(region);
        }

        com.sk89q.worldedit.world.World world = new BukkitWorld(bukkitworld);
        BlockVector minPoint = new BlockVector(region.getMinPoint().getBlockX(), region.getMinPoint().getBlockY(), region.getMinPoint().getBlockZ());
        WorldData worldData = world.getWorldData();
        Clipboard clipboard;
        try {
            clipboard = ClipboardFormat.findByFile(schematicPath).getReader(new FileInputStream(schematicPath)).read(worldData);
            Extent source = clipboard;
            Extent destination = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, Integer.MAX_VALUE);
            ForwardExtentCopy copy = new ForwardExtentCopy(source, clipboard.getRegion(), clipboard.getOrigin(), destination, minPoint);
            if(!region.isCuboid()) {
                copy.setSourceMask(new Mask() {
                    @Override
                    public boolean test(Vector vector) {
                        return region.contains(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
                    }

                    @Nullable
                    @Override
                    public Mask2D toMask2D() {
                        return null;
                    }
                });
            }
            copy.setRemovingEntities(false);

            Operations.completeLegacy(copy);
            ((EditSession) destination).flushQueue();
        } catch (IOException e) {
            throw new SchematicNotFoundException(region);
        } catch (MaxChangedBlocksException e) {
            throw new SchematicNotFoundException(region);
        }

    }
}
