package Handlers;

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
import net.liggesmeyer.inter.WorldEditInterface;
import net.liggesmeyer.arm.Main;
import net.liggesmeyer.arm.Messages;
import net.liggesmeyer.arm.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.*;

public class WorldEdit6 extends WorldEditInterface {
    @Override
    public void createSchematic(Region region) {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File schematicdic = new File(pluginfolder + "/schematics/" + region.getRegionworld() + "/" + region.getRegion().getId() + ".schematic");
        File schematicfolder = new File(pluginfolder + "/schematics/" + region.getRegionworld());
        if(schematicdic.exists()){
            schematicdic.delete();
        }

        Vector max = region.getRegion().getMaximumPoint();
        Vector min = region.getRegion().getMinimumPoint();

        schematicfolder.mkdirs();

        if(Main.isFaWeInstalled()) {
            CuboidRegion copyregion = new CuboidRegion(new BukkitWorld(Bukkit.getWorld(region.getRegionworld())), min, max);
            Schematic schematic = new Schematic(copyregion);
            try {
                schematic.save(schematicdic, ClipboardFormat.SCHEMATIC);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            if(schematicdic.exists()) {
                schematicdic.delete();
            }
            com.sk89q.worldedit.world.World world = new BukkitWorld(Bukkit.getWorld(region.getRegionworld()));
            WorldData worldData = world.getWorldData();
            EditSession editSession = Main.getWorldedit().getWorldEdit().getEditSessionFactory().getEditSession(world, Integer.MAX_VALUE);
            CuboidRegion reg = new CuboidRegion(world, region.getRegion().getMinimumPoint(), region.getRegion().getMaximumPoint());
            BlockArrayClipboard clip = new BlockArrayClipboard(reg);
            clip.setOrigin(region.getRegion().getMinimumPoint());
            ForwardExtentCopy copy = new ForwardExtentCopy(editSession, new CuboidRegion(world, region.getRegion().getMinimumPoint(), region.getRegion().getMaximumPoint()), clip, region.getRegion().getMinimumPoint());
            try {
                Operations.completeLegacy(copy);
            } catch(MaxChangedBlocksException e) {
                e.printStackTrace();
            }
            try(Closer closer = Closer.create()) {
                schematicdic.createNewFile();
                FileOutputStream fileOutputStream = closer.register(new FileOutputStream(schematicdic));
                BufferedOutputStream bufferedOutputStream = closer.register(new BufferedOutputStream(fileOutputStream));
                ClipboardWriter writer = closer.register(ClipboardFormat.SCHEMATIC.getWriter(bufferedOutputStream));
                writer.write(clip, worldData);
            } catch(IOException e) {
                e.printStackTrace();
                return;
            }
            return;
        }
    }

    public void resetBlocks(Region region, Player player) {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File file = new File(pluginfolder + "/schematics/" + region.getRegionworld() + "/" + region.getRegion().getId() + ".schematic");


        if(Main.isFaWeInstalled()) {

            com.sk89q.worldedit.world.World weWorld = new BukkitWorld(Bukkit.getWorld(region.getRegionworld()));
            WorldData worldData = weWorld.getWorldData();
            Clipboard clipboard;
            try {
                clipboard = ClipboardFormat.SCHEMATIC.getReader(new FileInputStream(file)).read(worldData);
                Schematic schem = new Schematic(clipboard);
                schem.paste(weWorld, region.getRegion().getMinimumPoint());
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {

            com.sk89q.worldedit.world.World world = new BukkitWorld(Bukkit.getWorld(region.getRegionworld()));
            WorldData worldData = world.getWorldData();
            Clipboard clipboard;
            try {
                clipboard = ClipboardFormat.SCHEMATIC.getReader(new FileInputStream(file)).read(worldData);
                Extent source = clipboard;
                Extent destination = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, Integer.MAX_VALUE);
                ForwardExtentCopy copy = new ForwardExtentCopy(source, clipboard.getRegion(), clipboard.getOrigin(), destination, region.getRegion().getMinimumPoint());

                Operations.completeLegacy(copy);
            } catch (IOException | WorldEditException e) {
                e.printStackTrace();
            }

        }

        if(player != null) {
            player.sendMessage(Messages.PREFIX + Messages.RESET_COMPLETE);
        }
    }
}
