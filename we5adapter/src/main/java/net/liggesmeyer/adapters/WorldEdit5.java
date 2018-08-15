package net.liggesmeyer.adapters;

import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.liggesmeyer.inter.WorldEditInterface;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class WorldEdit5 extends WorldEditInterface {
    @Override
    public void createSchematic(ProtectedRegion region, File schematicpath, File schematicdic, World world) {

        if (schematicpath.exists()) {
            schematicpath.delete();
        }

        Vector max = region.getMaximumPoint();
        Vector min = region.getMinimumPoint();

        schematicdic.mkdirs();

      /*  if (Main.isFaWeInstalled()) {
            CuboidRegion copyregion = new CuboidRegion(new BukkitWorld(world), min, max);
            Schematic schematic = new Schematic(copyregion);
            try {
                schematic.save(schematicpath, ClipboardFormat.SCHEMATIC);
            } catch (IOException e) {
                e.printStackTrace();
            }
  *///      } else {

            max = max.subtract(min);
            max = max.add(new Vector(1, 1, 1));

            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(world), Integer.MAX_VALUE);

            CuboidClipboard clipboard = new CuboidClipboard(max, min);

            clipboard.copy(editSession);

            try {
                SchematicFormat.MCEDIT.save(clipboard, schematicpath);
                editSession.flushQueue();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DataException e) {
                e.printStackTrace();
            }
  //      }
    }

    @Override
    public void resetBlocks(ProtectedRegion region, File schematicpath, World world) {

        try {
            BukkitWorld bw = new BukkitWorld(world);
            EditSession editSession = new EditSession(bw, Integer.MAX_VALUE);
            SchematicFormat.MCEDIT.load(schematicpath).paste(editSession, region.getMinimumPoint(), false);
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DataException e) {
            e.printStackTrace();
        }
    }
}
