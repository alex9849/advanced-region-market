package net.alex9849.arm.handler.listener;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.entitylimit.EntityLimit;
import net.alex9849.arm.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;

import java.util.List;

public class EntitySpawnListener implements Listener {

    @EventHandler
    public void entitySpawnEvent(EntitySpawnEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getEntityType() == EntityType.PLAYER) {
            return;
        }

        if (AdvancedRegionMarket.getInstance().getRegionManager() == null) {
            return;
        }
        EntityLimit.LimitableEntityType limitableEntityType = EntityLimit.toLimitableEntityType(event.getEntityType());
        if (limitableEntityType == null) {
            return;
        }

        List<Region> regions = AdvancedRegionMarket.getInstance().getRegionManager().getRegionsByLocation(event.getLocation());

        for (Region region : regions) {
            if (region.getEntityLimitGroup().isLimitReached(region, event.getEntityType(), region.getExtraTotalEntitys())) {
                event.setCancelled(true);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Location playerLoc = player.getLocation();
                    if (region.getRegion().contains(playerLoc.getBlockX(), playerLoc.getBlockY(), playerLoc.getBlockZ())) {
                        player.getPlayer().sendMessage(Messages.PREFIX + region.getConvertedMessage(Messages.ENTITYLIMITGROUP_COULD_NOT_SPAWN_ENTITY));
                    }
                }
            }
        }
    }

    @EventHandler
    public void vehicleSpawnEvent(VehicleCreateEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (AdvancedRegionMarket.getInstance().getRegionManager() == null) {
            return;
        }

        EntityLimit.LimitableEntityType limitableEntityType = EntityLimit.toLimitableEntityType(event.getVehicle().getType());
        if (limitableEntityType == null) {
            return;
        }

        List<Region> regions = AdvancedRegionMarket.getInstance().getRegionManager().getRegionsByLocation(event.getVehicle().getLocation());

        for (Region region : regions) {
            if (region.getEntityLimitGroup().isLimitReached(region, event.getVehicle().getType(), region.getExtraTotalEntitys())) {
                event.setCancelled(true);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Location playerLoc = player.getLocation();
                    if (region.getRegion().contains(playerLoc.getBlockX(), playerLoc.getBlockY(), playerLoc.getBlockZ())) {
                        player.getPlayer().sendMessage(Messages.PREFIX + region.getConvertedMessage(Messages.ENTITYLIMITGROUP_COULD_NOT_SPAWN_ENTITY));
                    }
                }
            }
        }
    }

}
