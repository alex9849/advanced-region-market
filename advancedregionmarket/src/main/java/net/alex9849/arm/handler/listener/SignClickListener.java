package net.alex9849.arm.handler.listener;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.ArmSettings;
import net.alex9849.arm.Messages;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.util.MaterialFinder;
import net.alex9849.exceptions.InputException;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignClickListener implements Listener {

    @EventHandler
    public void interactEvent(PlayerInteractEvent event){
        try {

            if((event.getAction() != Action.LEFT_CLICK_BLOCK) && (event.getAction() != Action.RIGHT_CLICK_BLOCK)) {
                return;
            }

            if (!MaterialFinder.getSignMaterials().contains(event.getClickedBlock().getType())) {
                return;
            }

            if(AdvancedRegionMarket.getRegionManager() == null) {
                return;
            }

            Sign sign = (Sign) event.getClickedBlock().getState();

            Region region = AdvancedRegionMarket.getRegionManager().getRegion(sign);
            if(region == null) {
                return;
            }

            if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if(event.getPlayer().isSneaking()) {
                    this.handleSignCmd(region, ArmSettings.getSignRightClickSneakCommand(), event.getPlayer());
                } else {
                    this.handleSignCmd(region, ArmSettings.getSignRightClickNotSneakCommand(), event.getPlayer());
                }
            } else if (event.getAction() == Action.LEFT_CLICK_BLOCK){
                if(event.getPlayer().isSneaking()) {
                    this.handleSignCmd(region, ArmSettings.getSignLeftClickSneakCommand(), event.getPlayer());
                } else {
                    this.handleSignCmd(region, ArmSettings.getSignLeftClickNotSneakCommand(), event.getPlayer());
                }
            }

        } catch (InputException inputException) {
            inputException.sendMessages(Messages.PREFIX);
        }
    }

    private void handleSignCmd(Region region, String cmd, Player player) throws InputException {
        if(cmd.equalsIgnoreCase("")) {
            return;
        }
        if(cmd.equalsIgnoreCase("buyaction")) {
            region.buy(player);
            return;
        } else if (cmd.equalsIgnoreCase("")) {
            return;
        } else {
            cmd = region.getConvertedMessage(cmd);
            player.performCommand(cmd);
        }
    }

}
