package net.alex9849.arm.SubRegions;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.exceptions.LogicalException;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.RegionKind;
import net.alex9849.arm.regions.SellRegion;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.util.ArrayList;
import java.util.List;

public class SubSignCreationListener implements Listener {
    Player player;
    SubRegionCreator subRegionCreator;

    SubSignCreationListener(Player player, SubRegionCreator subRegionCreator) {
        this.player = player;
        this.subRegionCreator = subRegionCreator;
    }

    public void unregister() {
        SignChangeEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void signChangeEvent(SignChangeEvent event) {
        try {
            if(event.getPlayer().getUniqueId() != this.player.getUniqueId()) {
                return;
            }
            if(event.getLine(0).equalsIgnoreCase("[Sub-Sell]")) {
                if(!event.getPlayer().hasPermission(Permission.SUBREGION_CREATE_SELL)) {
                    throw new InputException(event.getPlayer(), Messages.NO_PERMISSION);
                }

                double price = 0;

                try {
                    price = Integer.parseInt(event.getLine(3));
                } catch (IllegalArgumentException e) {
                    throw new InputException(event.getPlayer(), "Use a number as price in line 4");
                }
                List<Sign> signList = new ArrayList<>();
                signList.add((Sign) event.getBlock().getState());
                SellRegion sellRegion = new SellRegion(this.subRegionCreator.getSubRegion(), this.subRegionCreator.getParentRegion().getRegionworld(), signList, price, false, false, false, false, RegionKind.DEFAULT, null, 1, false, new ArrayList<Region>(), false, true);
                this.subRegionCreator.saveWorldGuardRegion();
                event.setCancelled(true);
                this.subRegionCreator.getParentRegion().addSubRegion(sellRegion);
                sellRegion.setParentRegion(this.subRegionCreator.getParentRegion());
                sellRegion.createSchematic();
                this.subRegionCreator.remove();
                event.getPlayer().sendMessage(Messages.PREFIX + "Region created and saved");
            } else if(event.getLine(0).equalsIgnoreCase("[ARM-Rent]")) {
                if(!event.getPlayer().hasPermission(Permission.SUBREGION_CREATE_RENT)) {
                    throw new InputException(event.getPlayer(), Messages.NO_PERMISSION);
                }
            } else if(event.getLine(0).equalsIgnoreCase("[ARM-Contract]")) {
                if(!event.getPlayer().hasPermission(Permission.SUBREGION_CREATE_CONTRACT)) {
                    throw new InputException(event.getPlayer(), Messages.NO_PERMISSION);
                }
            }
        } catch (InputException e) {
            e.sendMessages();
        } catch (LogicalException e) {
            e.sendMessage();
            e.printStackTrace();
        }
    }

}
