package net.alex9849.arm.handler.listener;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.exceptions.*;
import net.alex9849.arm.regions.Region;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class SignClickListener implements Listener {

    @EventHandler
    public void interactEvent(PlayerInteractEvent event) {
        if ((event.getAction() != Action.LEFT_CLICK_BLOCK) && (event.getAction() != Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        AdvancedRegionMarket plugin = AdvancedRegionMarket.getInstance();
        if (!plugin.getMaterialFinder().getSignMaterials().contains(event.getClickedBlock().getType())) {
            return;
        }

        if (plugin.getRegionManager() == null) {
            return;
        }

        Sign sign = (Sign) event.getClickedBlock().getState();

        Region region = plugin.getRegionManager().getRegion(sign);

        if (region == null) {
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getPlayer().isSneaking()) {
                this.handleSignCmd(region, AdvancedRegionMarket.getInstance().getPluginSettings().getSignRightClickSneakCommand(), event);
            } else {
                this.handleSignCmd(region, AdvancedRegionMarket.getInstance().getPluginSettings().getSignRightClickNotSneakCommand(), event);
            }
        } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            event.setCancelled(true);
            if (event.getPlayer().isSneaking()) {
                this.handleSignCmd(region, AdvancedRegionMarket.getInstance().getPluginSettings().getSignLeftClickSneakCommand(), event);
            } else {
                this.handleSignCmd(region, AdvancedRegionMarket.getInstance().getPluginSettings().getSignLeftClickNotSneakCommand(), event);
            }
        }
    }

    private void handleSignCmd(Region region, String cmd, PlayerInteractEvent event) {
        if (cmd.equalsIgnoreCase("")) {
            return;
        }
        if (cmd.equalsIgnoreCase("buyaction")) {
            try {
                region.signClickAction(event.getPlayer());
            } catch (NoPermissionException | OutOfLimitExeption | NotEnoughMoneyException
                    | AlreadySoldException | NotSoldException | RegionNotOwnException
                    | ProtectionOfContinuanceException e) {
                if (e.hasMessage()) {
                    event.getPlayer().sendMessage(Messages.PREFIX + e.getMessage());
                }
            }
        } else {
            cmd = region.replaceVariables(cmd);
            event.getPlayer().performCommand(cmd);
        }
    }

}
