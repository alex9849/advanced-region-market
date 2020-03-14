package net.alex9849.arm.subregions;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.ArmSettings;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.FeatureDisabledException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.regions.ContractRegion;
import net.alex9849.arm.regions.RentRegion;
import net.alex9849.arm.regions.SellRegion;
import net.alex9849.arm.regions.price.ContractPrice;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.arm.regions.price.RentPrice;
import net.alex9849.signs.SignData;
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
            if (event.getPlayer().getUniqueId() != this.player.getUniqueId()) {
                return;
            }
            ArmSettings pluginsSettings = AdvancedRegionMarket.getInstance().getPluginSettings();
            if (event.getLine(0).equalsIgnoreCase("[Sub-Sell]")) {
                if (!event.getPlayer().hasPermission(Permission.SUBREGION_CREATE_SELL)) {
                    throw new InputException(event.getPlayer(), Messages.NO_PERMISSION);
                }

                double price = 0;

                try {
                    price = Integer.parseInt(event.getLine(3));
                } catch (IllegalArgumentException e) {
                    //TODO Change message
                    throw new InputException(event.getPlayer(), "Use a number as price in line 4");
                }
                List<SignData> signList = new ArrayList<>();
                SignData signData = AdvancedRegionMarket.getInstance().getSignDataFactory().generateSignData(event.getBlock().getLocation());
                if (signData == null) {
                    throw new InputException(event.getPlayer(), "Could not import sign!");
                }
                signList.add(signData);
                Price subregPrice = new Price(price);
                SellRegion sellRegion = new SellRegion(this.subRegionCreator.getSubRegion(), signList, subregPrice,
                        false, this.subRegionCreator.getParentRegion());
                this.subRegionCreator.saveWorldGuardRegion();
                event.setCancelled(true);
                sellRegion.updateSigns();
                sellRegion.createSchematic();
                try {
                    sellRegion.applyFlagGroup(FlagGroup.ResetMode.COMPLETE, false);
                } catch (FeatureDisabledException e) {
                    //Ignore
                }
                this.subRegionCreator.remove();
                event.getPlayer().sendMessage(Messages.PREFIX + Messages.REGION_CREATED_AND_SAVED);
            } else if (event.getLine(0).equalsIgnoreCase("[Sub-Rent]")) {
                if (!event.getPlayer().hasPermission(Permission.SUBREGION_CREATE_RENT)) {
                    throw new InputException(event.getPlayer(), Messages.NO_PERMISSION);
                }
                double price = 0;
                long maxRentTime = 0;
                long extendPerClick = 0;

                try {
                    String[] priceline = event.getLine(3).split("(;|:)", 3);
                    String pricestring = priceline[0];
                    String extendPerClickString = priceline[1];
                    String maxRentTimeString = priceline[2];
                    extendPerClick = RentPrice.stringToTime(extendPerClickString);
                    maxRentTime = RentPrice.stringToTime(maxRentTimeString);
                    price = Integer.parseInt(pricestring);
                } catch (ArrayIndexOutOfBoundsException e) {
                    event.getPlayer().sendMessage(Messages.PREFIX + "Please write your price in line 4 in the following pattern:");
                    event.getPlayer().sendMessage("<Price>;<Extend per Click (ex.: 5d)>;<Max rent Time (ex.: 10d)>");
                    return;
                } catch (IllegalArgumentException e) {
                    event.getPlayer().sendMessage(Messages.PREFIX + "Please use d for days, h for hours, m for minutes and s for seconds");
                    event.getPlayer().sendMessage(Messages.PREFIX + "Please write you price in line 4 in the following pattern:");
                    event.getPlayer().sendMessage("<Price>;<Extend per Click (ex.: 5d)>;<Max rent Time (ex.: 10d)>");
                    return;
                }

                List<SignData> signList = new ArrayList<>();
                SignData signData = AdvancedRegionMarket.getInstance().getSignDataFactory().generateSignData(event.getBlock().getLocation());
                if (signData == null) {
                    throw new InputException(event.getPlayer(), "Could not import sign!");
                }
                signList.add(signData);
                RentPrice rentPrice = new RentPrice(price, extendPerClick, maxRentTime);
                RentRegion rentRegion = new RentRegion(this.subRegionCreator.getSubRegion(), signList, rentPrice,
                        false, this.subRegionCreator.getParentRegion());
                this.subRegionCreator.saveWorldGuardRegion();
                event.setCancelled(true);
                rentRegion.updateSigns();
                rentRegion.createSchematic();
                try {
                    rentRegion.applyFlagGroup(FlagGroup.ResetMode.COMPLETE, false);
                } catch (FeatureDisabledException e) {
                    //Ignore
                }
                this.subRegionCreator.remove();
                event.getPlayer().sendMessage(Messages.PREFIX + Messages.REGION_CREATED_AND_SAVED);
            } else if (event.getLine(0).equalsIgnoreCase("[Sub-Contract]")) {
                if (!event.getPlayer().hasPermission(Permission.SUBREGION_CREATE_CONTRACT)) {
                    throw new InputException(event.getPlayer(), Messages.NO_PERMISSION);
                }

                double price = 0;
                long extendtime = 0;

                try {
                    String[] priceline = event.getLine(3).split("(;|:)", 2);
                    String pricestring = priceline[0];
                    String extendtimeString = priceline[1];
                    extendtime = RentPrice.stringToTime(extendtimeString);
                    price = Integer.parseInt(pricestring);
                } catch (ArrayIndexOutOfBoundsException e) {
                    event.getPlayer().sendMessage(Messages.PREFIX + "Please write your price in line 4 in the following pattern:");
                    event.getPlayer().sendMessage("<Price>;<Extendtime (ex.: 5d)>");
                    return;
                } catch (IllegalArgumentException e) {
                    event.getPlayer().sendMessage(Messages.PREFIX + "Please use d for days, h for hours, m for minutes and s for seconds");
                    event.getPlayer().sendMessage(Messages.PREFIX + "Please write you price in line 4 in the following pattern:");
                    event.getPlayer().sendMessage("<Price>;<Extendtime (ex.: 5d)>");
                    return;
                }
                List<SignData> signList = new ArrayList<>();
                SignData signData = AdvancedRegionMarket.getInstance().getSignDataFactory().generateSignData(event.getBlock().getLocation());
                if (signData == null) {
                    throw new InputException(event.getPlayer(), "Could not import sign!");
                }
                signList.add(signData);
                ContractPrice contractPrice = new ContractPrice(price, extendtime);
                ContractRegion contractRegion = new ContractRegion(this.subRegionCreator.getSubRegion(), signList,
                        contractPrice, false, this.subRegionCreator.getParentRegion());
                this.subRegionCreator.saveWorldGuardRegion();
                event.setCancelled(true);
                contractRegion.updateSigns();
                contractRegion.createSchematic();
                try {
                    contractRegion.applyFlagGroup(FlagGroup.ResetMode.COMPLETE, false);
                } catch (FeatureDisabledException e) {
                    //Ignore
                }
                this.subRegionCreator.remove();
                event.getPlayer().sendMessage(Messages.PREFIX + Messages.REGION_CREATED_AND_SAVED);
            }
        } catch (InputException e) {
            e.sendMessages(Messages.PREFIX);
        }
    }

}
