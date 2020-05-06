package net.alex9849.adapters;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.Mask2D;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.session.PasteBuilder;
import com.sk89q.worldedit.util.io.Closer;
import net.alex9849.arm.exceptions.SchematicNotFoundException;
import net.alex9849.inter.WGRegion;
import net.alex9849.inter.WorldEditInterface;
import org.bukkit.World;

import javax.annotation.Nullable;
import java.io.*;

public class WorldEdit7 extends WorldEditInterface {

    @Override
    public void createSchematic(WGRegion region, World bukkitworld, File saveFolder, String fileNameWithoutEnding) {

        File schematicWithoutEnding = new File(saveFolder + "/" + fileNameWithoutEnding);
        File schematicPath = new File(schematicWithoutEnding + "." + BuiltInClipboardFormat.SPONGE_SCHEMATIC.getPrimaryFileExtension());

        for (BuiltInClipboardFormat format : BuiltInClipboardFormat.values()) {
            for (String extension : format.getFileExtensions()) {
                if (new File(schematicWithoutEnding.getAbsolutePath() + "." + extension).exists()) {
                    File delfile = new File(schematicWithoutEnding.getAbsolutePath() + "." + extension);
                    delfile.delete();
                }
            }
        }

        saveFolder.mkdirs();

        com.sk89q.worldedit.world.World world = new BukkitWorld(bukkitworld);
        BlockVector3 minPoint = BlockVector3.at(region.getMinPoint().getBlockX(), region.getMinPoint().getBlockY(), region.getMinPoint().getBlockZ());
        BlockVector3 maxPoint = BlockVector3.at(region.getMaxPoint().getBlockX(), region.getMaxPoint().getBlockY(), region.getMaxPoint().getBlockZ());

        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, Integer.MAX_VALUE);
        CuboidRegion reg = new CuboidRegion(world, minPoint, maxPoint);
        BlockArrayClipboard clip = new BlockArrayClipboard(reg);
        clip.setOrigin(minPoint);
        ForwardExtentCopy copy = new ForwardExtentCopy(editSession, new CuboidRegion(world, minPoint, maxPoint), clip, minPoint);
        copy.setCopyingEntities(true);
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
            ClipboardWriter writer = closer.register(BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(bufferedOutputStream));
            writer.write(clip);
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

        for (BuiltInClipboardFormat format : BuiltInClipboardFormat.values()) {
            for (String extension : format.getFileExtensions()) {
                if (new File(schematicPathWithoutFileEnding.getAbsolutePath() + "." + extension).exists()) {
                    schematicPath = new File(schematicPathWithoutFileEnding.getAbsolutePath() + "." + extension);
                }
            }
        }

        if (schematicPath == null) {
            throw new SchematicNotFoundException(region);
        }

        com.sk89q.worldedit.world.World world = new BukkitWorld(bukkitworld);
        BlockVector3 minPoint = BlockVector3.at(region.getMinPoint().getBlockX(), region.getMinPoint().getBlockY(), region.getMinPoint().getBlockZ());
        Clipboard clipboard;
        try {
            Closer closer = Closer.create();
            FileInputStream fileInputStream = closer.register(new FileInputStream(schematicPath));
            BufferedInputStream bufferedInputStream = closer.register(new BufferedInputStream(fileInputStream));
            ClipboardFormat clipboardFormat = ClipboardFormats.findByFile(schematicPath);
            ClipboardReader reader = clipboardFormat.getReader(bufferedInputStream);
            clipboard = reader.read();
            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1);
            PasteBuilder pasteBuilder = new ClipboardHolder(clipboard).createPaste(editSession).to(minPoint);
            if(!region.isCuboid()) {
                pasteBuilder.maskSource(new Mask() {
                    @Override
                    public boolean test(BlockVector3 v) {
                        return region.contains(v.getX(), v.getY(), v.getZ());
                    }

                    @Nullable
                    @Override
                    public Mask2D toMask2D() {
                        return null;
                    }
                });
            }
            Operation operation = pasteBuilder.build();
            Operations.complete(operation);
            editSession.flushSession();
            closer.close();
        } catch (WorldEditException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new SchematicNotFoundException(region);
        }
    }
}
