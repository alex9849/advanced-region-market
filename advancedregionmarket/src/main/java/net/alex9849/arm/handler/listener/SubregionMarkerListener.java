package net.alex9849.arm.handler.listener;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.subregions.SubRegionCreator;
import net.alex9849.exceptions.InputException;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public class SubregionMarkerListener implements Listener {

    @EventHandler
    public void interactEvent(PlayerInteractEvent event){
        try {
            this.setSubregionMark(event);
        } catch (InputException inputException) {
            event.setCancelled(true);
            inputException.sendMessages(Messages.PREFIX);
        }
    }

    private void setSubregionMark(PlayerInteractEvent event) throws InputException {
        Player player = event.getPlayer();
        if(event.getItem() == null) {
            return;
        }
        if(AdvancedRegionMarket.getARM().getRegionManager() == null) {
            return;
        }
        if(!Permission.hasAnySubregionCreatePermission(player)) {
            return;
        }
        if((event.getItem().getType() != Material.FEATHER) || ((event.getAction() != Action.RIGHT_CLICK_BLOCK) && (event.getAction() != Action.LEFT_CLICK_BLOCK))) {
            return;
        }

        if(!event.getItem().getItemMeta().getDisplayName().equals("Subregion Tool")) {
            return;
        }

        List<Region> applicableRegions = AdvancedRegionMarket.getARM().getRegionManager().getRegionsByLocation(event.getClickedBlock().getLocation());

        if(applicableRegions.size() == 0) {
            player.sendMessage(Messages.NO_REGION_AT_PLAYERS_POSITION);
            return;
        }

        for(Region region : applicableRegions) {
            if(!region.getRegion().hasOwner(player.getUniqueId()) && !player.hasPermission(Permission.ADMIN_SUBREGION_CREATE_ON_UNOWNED_REGIONS)) {
                continue;
            }
            if(region.isSubregion()) {
                continue;
            }

            SubRegionCreator subRegionCreator = SubRegionCreator.getSubRegioncreator(player);

            if(subRegionCreator == null) {
                subRegionCreator = new SubRegionCreator(region, player);

            } else if(subRegionCreator.getParentRegion() != region) {
                player.sendMessage(Messages.MARK_IN_OTHER_REGION_REMOVING);
                subRegionCreator = new SubRegionCreator(region, player);
            }

            if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                subRegionCreator.setPos2(event.getClickedBlock().getLocation());
                event.setCancelled(true);
                player.sendMessage(Messages.SECOND_POSITION_SET);
            } else if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
                subRegionCreator.setPos1(event.getClickedBlock().getLocation());
                event.setCancelled(true);
                player.sendMessage(Messages.FIRST_POSITION_SET);
            }
            return;
        }
        throw new InputException(event.getPlayer(), Messages.REGION_NOT_OWN);
    }


}
