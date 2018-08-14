package Handlers;

import Interfaces.WorldEditInterface;
import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.schematic.SchematicFormat;
import net.liggesmeyer.arm.Main;
import net.liggesmeyer.arm.Messages;
import net.liggesmeyer.arm.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class WorldEdit5 extends WorldEditInterface {
    @Override
    public void createSchematic(Region region) {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File schematicdic = new File(pluginfolder + "/schematics/" + region.getRegionworld() + "/" + region.getRegion().getId() + ".schematic");
        File schematicfolder = new File(pluginfolder + "/schematics/" + region.getRegionworld());

        if (schematicdic.exists()) {
            schematicdic.delete();
        }

        Vector max = region.getRegion().getMaximumPoint();
        Vector min = region.getRegion().getMinimumPoint();

        schematicfolder.mkdirs();

        if (Main.isFaWeInstalled()) {
            CuboidRegion copyregion = new CuboidRegion(new BukkitWorld(Bukkit.getWorld(region.getRegionworld())), min, max);
            Schematic schematic = new Schematic(copyregion);
            try {
                schematic.save(schematicdic, ClipboardFormat.SCHEMATIC);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            max = max.subtract(min);
            max = max.add(new Vector(1, 1, 1));

            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(Bukkit.getWorld(region.getRegionworld())), Integer.MAX_VALUE);

            CuboidClipboard clipboard = new CuboidClipboard(max, min);

            clipboard.copy(editSession);

            try {
                SchematicFormat.MCEDIT.save(clipboard, schematicdic);
                editSession.flushQueue();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DataException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void resetBlocks(Region region, Player player) {

        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File file = new File(pluginfolder + "/schematics/" + region.getRegionworld() + "/" + region.getRegion().getId() + ".schematic");

        int maxX = region.getRegion().getMaximumPoint().getBlockX();
        int minX = region.getRegion().getMinimumPoint().getBlockX();
        int maxY = region.getRegion().getMaximumPoint().getBlockY();
        int minY = region.getRegion().getMinimumPoint().getBlockY();
        int maxZ = region.getRegion().getMaximumPoint().getBlockZ();
        int minZ = region.getRegion().getMinimumPoint().getBlockZ();

        try {
            BukkitWorld bw = new BukkitWorld(Bukkit.getWorld(region.getRegionworld()));
            EditSession editSession = new EditSession(bw, Integer.MAX_VALUE);
            SchematicFormat.MCEDIT.load(file).paste(editSession, region.getRegion().getMinimumPoint(), false);
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DataException e) {
            e.printStackTrace();
        }
        if(player != null) {
            player.sendMessage(Messages.PREFIX + Messages.RESET_COMPLETE);
        }
    }
}
