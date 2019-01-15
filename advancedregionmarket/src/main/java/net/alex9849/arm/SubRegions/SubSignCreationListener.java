package net.alex9849.arm.SubRegions;

import net.alex9849.arm.ArmSettings;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.exceptions.LogicalException;
import net.alex9849.arm.regions.*;
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
                    //Change message
                    throw new InputException(event.getPlayer(), "Use a number as price in line 4");
                }
                List<Sign> signList = new ArrayList<>();
                signList.add((Sign) event.getBlock().getState());
                SellRegion sellRegion = new SellRegion(this.subRegionCreator.getSubRegion(), this.subRegionCreator.getParentRegion().getRegionworld(), signList, price, false, ArmSettings.isSubregionAutoReset(), false, ArmSettings.isSubregionBlockReset(), RegionKind.SUBREGION, null, 1, ArmSettings.isAllowSubRegionUserReset(), new ArrayList<Region>(), 0);
                this.subRegionCreator.saveWorldGuardRegion();
                event.setCancelled(true);
                this.subRegionCreator.getParentRegion().addSubRegion(sellRegion);
                sellRegion.createSchematic();
                this.subRegionCreator.remove();
                //TODO change message
                event.getPlayer().sendMessage(Messages.PREFIX + "Region created and saved");
            } else if(event.getLine(0).equalsIgnoreCase("[Sub-Rent]")) {
                if(!event.getPlayer().hasPermission(Permission.SUBREGION_CREATE_RENT)) {
                    throw new InputException(event.getPlayer(), Messages.NO_PERMISSION);
                }
                double price = 0;
                long maxRentTime = 0;
                long extendPerClick = 0;

                try{
                    String[] priceline = event.getLine(3).split("(;|:)", 3);
                    String pricestring = priceline[0];
                    String extendPerClickString = priceline[1];
                    String maxRentTimeString = priceline[2];
                    extendPerClick = RentRegion.stringToTime(extendPerClickString);
                    maxRentTime = RentRegion.stringToTime(maxRentTimeString);
                    price = Integer.parseInt(pricestring);
                } catch (ArrayIndexOutOfBoundsException e) {
                    event.getPlayer().sendMessage(Messages.PREFIX + "Please write your price in line 3 in the following pattern:");
                    event.getPlayer().sendMessage("<Price>;<Extend per Click (ex.: 5d)>;<Max rent Time (ex.: 10d)>");
                    return;
                } catch (IllegalArgumentException e) {
                    event.getPlayer().sendMessage(Messages.PREFIX + "Please use d for days, h for hours, m for minutes and s for seconds");
                    event.getPlayer().sendMessage(Messages.PREFIX + "Please write you price in line 3 in the following pattern:");
                    event.getPlayer().sendMessage("<Price>;<Extend per Click (ex.: 5d)>;<Max rent Time (ex.: 10d)>");
                    return;
                }

                List<Sign> signList = new ArrayList<>();
                signList.add((Sign) event.getBlock().getState());
                RentRegion rentRegion = new RentRegion(this.subRegionCreator.getSubRegion(), this.subRegionCreator.getParentRegion().getRegionworld(), signList, price, false, ArmSettings.isSubregionAutoReset(), false, ArmSettings.isSubregionBlockReset(), RegionKind.SUBREGION, null, 1, ArmSettings.isAllowSubRegionUserReset(), 0, maxRentTime, extendPerClick, new ArrayList<Region>(), 0);
                this.subRegionCreator.saveWorldGuardRegion();
                event.setCancelled(true);
                this.subRegionCreator.getParentRegion().addSubRegion(rentRegion);
                rentRegion.createSchematic();
                this.subRegionCreator.remove();
                //TODO change message
                event.getPlayer().sendMessage(Messages.PREFIX + "Region created and saved");
            } else if(event.getLine(0).equalsIgnoreCase("[Sub-Contract]")) {
                if(!event.getPlayer().hasPermission(Permission.SUBREGION_CREATE_CONTRACT)) {
                    throw new InputException(event.getPlayer(), Messages.NO_PERMISSION);
                }

                double price = 0;
                long extendtime = 0;

                try{
                    String[] priceline = event.getLine(3).split("(;|:)", 2);
                    String pricestring = priceline[0];
                    String extendtimeString = priceline[1];
                    extendtime = RentRegion.stringToTime(extendtimeString);
                    price = Integer.parseInt(pricestring);
                } catch (ArrayIndexOutOfBoundsException e) {
                    event.getPlayer().sendMessage(Messages.PREFIX + "Please write your price in line 3 in the following pattern:");
                    event.getPlayer().sendMessage("<Price>;<Extendtime (ex.: 5d)>");
                    return;
                } catch (IllegalArgumentException e) {
                    event.getPlayer().sendMessage(Messages.PREFIX + "Please use d for days, h for hours, m for minutes and s for seconds");
                    event.getPlayer().sendMessage(Messages.PREFIX + "Please write you price in line 3 in the following pattern:");
                    event.getPlayer().sendMessage("<Price>;<Extendtime (ex.: 5d)>");
                    return;
                }
                List<Sign> signList = new ArrayList<>();
                signList.add((Sign) event.getBlock().getState());
                ContractRegion contractRegion = new ContractRegion(this.subRegionCreator.getSubRegion(), this.subRegionCreator.getParentRegion().getRegionworld(), signList, price, false, ArmSettings.isSubregionAutoReset(), false, ArmSettings.isSubregionBlockReset(), RegionKind.SUBREGION, null, 1, ArmSettings.isAllowSubRegionUserReset(), extendtime, 0, false, new ArrayList<Region>(), 0);
                this.subRegionCreator.saveWorldGuardRegion();
                event.setCancelled(true);
                this.subRegionCreator.getParentRegion().addSubRegion(contractRegion);
                contractRegion.createSchematic();
                this.subRegionCreator.remove();
                //TODO change message
                event.getPlayer().sendMessage(Messages.PREFIX + "Region created and saved");
            }
        } catch (InputException e) {
            e.sendMessages();
        } catch (LogicalException e) {
            e.sendMessage();
            e.printStackTrace();
        }
    }

}
