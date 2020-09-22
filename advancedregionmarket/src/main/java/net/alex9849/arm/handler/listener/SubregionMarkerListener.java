package net.alex9849.arm.handler.listener;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.subregions.SubRegionCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;

public class SubregionMarkerListener implements Listener {

    @EventHandler
    public void interactEvent(PlayerInteractEvent event) {
        try {
            this.setSubregionMark(event);
        } catch (InputException inputException) {
            event.setCancelled(true);
            inputException.sendMessages(Messages.PREFIX);
        }
    }

    private void setSubregionMark(PlayerInteractEvent event) throws InputException {
        if(event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        Player player = event.getPlayer();
        if (event.getItem() == null) {
            return;
        }
        if (AdvancedRegionMarket.getInstance().getRegionManager() == null) {
            return;
        }
        if (!Permission.hasAnySubregionCreatePermission(player)) {
            return;
        }
        if ((event.getItem().getType() != Material.FEATHER) || ((event.getAction() != Action.RIGHT_CLICK_BLOCK) && (event.getAction() != Action.LEFT_CLICK_BLOCK))) {
            return;
        }
        
        if (!event.getItem().hasItemMeta() || 
            !event.getItem().getItemMeta().hasDisplayName() ||
            !event.getItem().getItemMeta().getDisplayName().equals("Subregion Tool")) {
            return;
        }

        List<Region> applicableRegions = AdvancedRegionMarket.getInstance().getRegionManager().getRegionsByLocation(event.getClickedBlock().getLocation());

        if (applicableRegions.size() == 0) {
            throw new InputException(event.getPlayer(), Messages.NO_REGION_AT_PLAYERS_POSITION);
        }

        for (Region region : applicableRegions) {
            if (!region.getRegion().hasOwner(player.getUniqueId()) && !player.hasPermission(Permission.ADMIN_SUBREGION_CREATE_ON_UNOWNED_REGIONS)) {
                continue;
            }
            if (region.isSubregion()) {
                continue;
            }

            SubRegionCreator subRegionCreator = SubRegionCreator.getSubRegioncreator(player);

            if (subRegionCreator == null) {
                subRegionCreator = new SubRegionCreator(region, player);

            } else if (subRegionCreator.getParentRegion() != region) {
                player.sendMessage(Messages.PREFIX + Messages.MARK_IN_OTHER_REGION_REMOVING);
                subRegionCreator = new SubRegionCreator(region, player);
            }

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                subRegionCreator.setPos2(event.getClickedBlock().getLocation());
                event.setCancelled(true);
                player.sendMessage(Messages.PREFIX + Messages.SECOND_POSITION_SET);
            } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                subRegionCreator.setPos1(event.getClickedBlock().getLocation());
                event.setCancelled(true);
                player.sendMessage(Messages.PREFIX + Messages.FIRST_POSITION_SET);
            }
            return;
        }
        throw new InputException(event.getPlayer(), Messages.REGION_NOT_OWN);
    }


}
