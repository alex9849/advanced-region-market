package net.alex9849.adapters;

import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.registry.WorldData;
import net.alex9849.arm.exceptions.SchematicNotFoundException;
import net.alex9849.inter.WGRegion;
import net.alex9849.inter.WorldEditInterface;
import org.bukkit.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class WorldEdit6FaWe extends WorldEditInterface {


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
        try {
            schematicPath.createNewFile();
            CuboidRegion reg = new CuboidRegion(world, minPoint, maxPoint);
            Schematic schem = new Schematic(reg);
            schem.save(schematicPath, ClipboardFormat.SCHEMATIC);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        WorldData worldData = world.getWorldData();
        BlockVector minPoint = new BlockVector(region.getMinPoint().getBlockX(), region.getMinPoint().getBlockY(), region.getMinPoint().getBlockZ());
        Clipboard clipboard;
        try {
            clipboard = ClipboardFormat.findByFile(schematicPath).getReader(new FileInputStream(schematicPath)).read(worldData);
            Extent source = clipboard;
            Extent destination = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, Integer.MAX_VALUE);
            ForwardExtentCopy copy = new ForwardExtentCopy(source, clipboard.getRegion(), clipboard.getOrigin(), destination, minPoint);
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
