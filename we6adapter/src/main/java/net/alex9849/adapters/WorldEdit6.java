package net.alex9849.adapters;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
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
import net.alex9849.exceptions.SchematicNotFoundException;
import net.alex9849.inter.WGRegion;
import net.alex9849.inter.WorldEditInterface;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.*;

public class WorldEdit6 extends WorldEditInterface {

    @Override
    public void createSchematic(WGRegion region, World bukkitworld, WorldEdit we) {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File schematicdic = new File(pluginfolder + "/schematics/" + bukkitworld.getName() + "/" + region.getId() + ".schematic");
        File schematicfolder = new File(pluginfolder + "/schematics/" + bukkitworld.getName());
        if(schematicdic.exists()){
            schematicdic.delete();
        }

        schematicfolder.mkdirs();

        BlockVector minPoint = new BlockVector(region.getMinPoint().getBlockX(), region.getMinPoint().getBlockY(), region.getMinPoint().getBlockZ());
        BlockVector maxPoint = new BlockVector(region.getMaxPoint().getBlockX(), region.getMaxPoint().getBlockY(), region.getMaxPoint().getBlockZ());

        com.sk89q.worldedit.world.World world = new BukkitWorld(bukkitworld);
        WorldData worldData = world.getWorldData();
        EditSession editSession = we.getEditSessionFactory().getEditSession(world, Integer.MAX_VALUE);
        CuboidRegion reg = new CuboidRegion(world, minPoint, maxPoint);
        BlockArrayClipboard clip = new BlockArrayClipboard(reg);
        clip.setOrigin(minPoint);
        ForwardExtentCopy copy = new ForwardExtentCopy(editSession, new CuboidRegion(world, minPoint, maxPoint), clip, minPoint);
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
    public void resetBlocks(WGRegion region, World bukkitworld, WorldEdit we) throws IOException {

        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File rawschematicdic = new File(pluginfolder + "/schematics/" + bukkitworld.getName() + "/" + region.getId());
        File file = null;

        for (ClipboardFormat format : ClipboardFormat.values()) {
            for (String extension : format.getAliases()) {
                if (new File(rawschematicdic.getAbsolutePath() + "." + extension).exists()) {
                    file = new File(rawschematicdic.getAbsolutePath() + "." + extension);
                }
            }
        }

        if(file == null) {
            throw new SchematicNotFoundException(region);
        }

        com.sk89q.worldedit.world.World world = new BukkitWorld(bukkitworld);
        BlockVector minPoint = new BlockVector(region.getMinPoint().getBlockX(), region.getMinPoint().getBlockY(), region.getMinPoint().getBlockZ());
        WorldData worldData = world.getWorldData();
        Clipboard clipboard;
        try {
            clipboard = ClipboardFormat.findByFile(file).getReader(new FileInputStream(file)).read(worldData);
            Extent source = clipboard;
            Extent destination = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, Integer.MAX_VALUE);
            ForwardExtentCopy copy = new ForwardExtentCopy(source, clipboard.getRegion(), clipboard.getOrigin(), destination, minPoint);
            copy.setRemovingEntities(false);

            Operations.completeLegacy(copy);
            ((EditSession) destination).flushQueue();
        } catch (SchematicNotFoundException e) {
            throw e;
        } catch (Exception e) {
            Bukkit.getLogger().info("Could not load schematic " + file.getAbsolutePath() + " please check your WorldEdit version or regenerate the schematic file!");
            e.printStackTrace();
        }

    }
}
