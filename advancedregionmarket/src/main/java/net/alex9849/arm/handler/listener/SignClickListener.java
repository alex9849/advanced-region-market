package net.alex9849.arm.handler.listener;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.exceptions.*;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.util.MaterialFinder;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
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
        if(event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        if (!MaterialFinder.getSignMaterials().contains(event.getClickedBlock().getType())) {
            return;
        }

        if (AdvancedRegionMarket.getInstance().getRegionManager() == null) {
            return;
        }

        Sign sign = (Sign) event.getClickedBlock().getState();

        Region region = AdvancedRegionMarket.getInstance().getRegionManager().getRegion(sign);

        if (region == null) {
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getPlayer().isSneaking()) {
                this.handleSignCmd(region, AdvancedRegionMarket.getInstance().getPluginSettings().getSignRightClickSneakCommand(), event.getPlayer());
            } else {
                this.handleSignCmd(region, AdvancedRegionMarket.getInstance().getPluginSettings().getSignRightClickNotSneakCommand(), event.getPlayer());
            }
        } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (event.getPlayer().isSneaking()) {
                this.handleSignCmd(region, AdvancedRegionMarket.getInstance().getPluginSettings().getSignLeftClickSneakCommand(), event.getPlayer());
            } else {
                this.handleSignCmd(region, AdvancedRegionMarket.getInstance().getPluginSettings().getSignLeftClickNotSneakCommand(), event.getPlayer());
            }
        }
    }

    private void handleSignCmd(Region region, String cmd, Player player) {
        if (cmd.equalsIgnoreCase("")) {
            return;
        }
        if (cmd.equalsIgnoreCase("buyaction")) {
            try {
                region.signClickAction(player);
            } catch (NoPermissionException | OutOfLimitExeption | NotEnoughMoneyException | AlreadySoldException
                    | NotSoldException | RegionNotOwnException e) {
                if (e.hasMessage()) player.sendMessage(Messages.PREFIX + e.getMessage());
            }

            return;
        } else if (cmd.equalsIgnoreCase("")) {
            return;
        } else {
            cmd = region.replaceVariables(cmd);
            player.performCommand(cmd);
        }
    }

}
