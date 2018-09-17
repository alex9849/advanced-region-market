package net.alex9849.arm.minifeatures;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Teleporter {

    public static boolean teleport(CommandSender sender, String regionString) throws InputException {
        if (!sender.hasPermission(Permission.ADMIN_TP) && !sender.hasPermission(Permission.MEMBER_TP)) {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
        if (!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;
        Region region = Region.searchRegionbyNameAndWorld(regionString, player.getWorld().getName());

        if (region == null) {
            throw new InputException(sender, Messages.REGION_DOES_NOT_EXIST);
        }

        if(!AdvancedRegionMarket.getWorldGuardInterface().hasMember(player, region.getRegion()) && !AdvancedRegionMarket.getWorldGuardInterface().hasOwner(player, region.getRegion())){
            if(!player.hasPermission(Permission.ADMIN_TP)){
                throw new InputException(sender, Messages.NOT_A_MEMBER_OR_OWNER);
            }
        }

        teleport(player, region);
        return true;
    }

    public static void teleport(Player player, Region region) throws InputException {

        if(region.getTeleportLocation() == null) {

            World world = Bukkit.getWorld(region.getRegionworld());
            if(world == null) {
                return;
            }
            int minX = region.getRegion().getMinimumPoint().getBlockX();
            int minY = region.getRegion().getMinimumPoint().getBlockY();
            int minZ = region.getRegion().getMinimumPoint().getBlockZ();
            int maxX = region.getRegion().getMaximumPoint().getBlockX();
            int maxY = region.getRegion().getMaximumPoint().getBlockY();
            int maxZ = region.getRegion().getMaximumPoint().getBlockZ();

            for (int x = maxX; x >= minX; x--) {
                for (int z = maxZ; z >= minZ; z--) {
                    for (int y = maxY; y >= minY; y--) {
                        Location loc = new Location(world, x, y, z);
                        if(isSaveTeleport(loc)) {
                            loc.add(0.5, 0, 0.5);
                            player.teleport(loc);
                            return;
                        }
                    }
                }
            }

            throw new InputException(player, Messages.TELEPORTER_NO_SAVE_LOCATION_FOUND);

        } else {
            player.teleport(region.getTeleportLocation());
        }
    }

    private static boolean isSaveTeleport(Location loc) {
        Location locP1 = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY() + 1, loc.getBlockZ());
        Location locM1 = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ());
        if(!(loc.getBlock().getType() == Material.AIR) || !(locP1.getBlock().getType() == Material.AIR)) {
            return false;
        }
        if((locM1.getBlock().getType() == Material.AIR) || (locM1.getBlock().getType() == Material.LAVA) || (locM1.getBlock().getType() == Material.MAGMA_BLOCK)) {
            return false;
        }
        return true;
    }

}
