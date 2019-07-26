package net.alex9849.arm.handler.listener;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.subregions.SubRegionCreator;
import net.alex9849.exceptions.InputException;
import net.alex9849.inter.WGRegion;
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
            inputException.sendMessages(Messages.PREFIX);
        }
    }

    private void setSubregionMark(PlayerInteractEvent event) throws InputException {
        Player player = event.getPlayer();
        if(Permission.hasAnySubregionCreatePermission(player)) {
            if(event.getItem() == null) {
                return;
            }

            if(AdvancedRegionMarket.getRegionManager() == null) {
                return;
            }

            //TODO change me --> see ToolCommand
            if((event.getItem().getType() == Material.FEATHER) && ((event.getAction() == Action.RIGHT_CLICK_BLOCK) || (event.getAction() == Action.LEFT_CLICK_BLOCK))) {
                if(event.getItem().getItemMeta().getDisplayName().equals("Subregion Tool")) {
                    List<WGRegion> applicableRegion = AdvancedRegionMarket.getWorldGuardInterface().getApplicableRegions(event.getClickedBlock().getWorld(), event.getClickedBlock().getLocation(), AdvancedRegionMarket.getWorldGuard());
                    for(WGRegion wgRegion : applicableRegion) {
                        if(wgRegion.hasOwner(player.getUniqueId())) {
                            SubRegionCreator subRegionCreator = SubRegionCreator.getSubRegioncreator(player);
                            if(subRegionCreator != null) {
                                if((subRegionCreator.getParentRegion().getRegion().getId().equals(wgRegion.getId())) && (subRegionCreator.getParentRegion().getRegionworld().getName().equals(event.getClickedBlock().getWorld().getName()))) {
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
                                } else {
                                    Region region = AdvancedRegionMarket.getRegionManager().getRegion(wgRegion);
                                    if(region == null) {
                                        throw new InputException(player, Messages.REGION_NOT_REGISTRED);
                                    }
                                    if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                                        player.sendMessage(Messages.MARK_IN_OTHER_REGION_REMOVING);
                                        subRegionCreator = new SubRegionCreator(region, player);
                                        subRegionCreator.setPos2(event.getClickedBlock().getLocation());
                                        event.setCancelled(true);
                                        player.sendMessage(Messages.SECOND_POSITION_SET);
                                    } else if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
                                        player.sendMessage(Messages.MARK_IN_OTHER_REGION_REMOVING);
                                        subRegionCreator = new SubRegionCreator(region, player);
                                        subRegionCreator.setPos1(event.getClickedBlock().getLocation());
                                        event.setCancelled(true);
                                        player.sendMessage(Messages.FIRST_POSITION_SET);
                                    }
                                    return;
                                }
                            } else {
                                Region region = AdvancedRegionMarket.getRegionManager().getRegion(wgRegion);
                                if(region == null) {
                                    throw new InputException(player, Messages.REGION_NOT_REGISTRED);
                                }
                                if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                                    subRegionCreator = new SubRegionCreator(region, player);
                                    subRegionCreator.setPos2(event.getClickedBlock().getLocation());
                                    event.setCancelled(true);
                                    player.sendMessage(Messages.SECOND_POSITION_SET);
                                } else if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
                                    subRegionCreator = new SubRegionCreator(region, player);
                                    subRegionCreator.setPos1(event.getClickedBlock().getLocation());
                                    event.setCancelled(true);
                                    player.sendMessage(Messages.FIRST_POSITION_SET);
                                }
                                return;
                            }
                        }
                    }
                    throw new InputException(event.getPlayer(), Messages.REGION_NOT_OWN);
                }
            }
        }
    }


}
