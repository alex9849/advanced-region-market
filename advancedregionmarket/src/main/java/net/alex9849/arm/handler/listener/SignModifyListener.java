package net.alex9849.arm.handler.listener;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.presets.ActivePresetManager;
import net.alex9849.arm.presets.presets.ContractPreset;
import net.alex9849.arm.presets.presets.PresetType;
import net.alex9849.arm.presets.presets.RentPreset;
import net.alex9849.arm.presets.presets.SellPreset;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.ContractRegion;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.RentRegion;
import net.alex9849.arm.regions.SellRegion;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.ContractPrice;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.arm.regions.price.RentPrice;
import net.alex9849.arm.util.MaterialFinder;
import net.alex9849.exceptions.InputException;
import net.alex9849.inter.WGRegion;
import net.alex9849.signs.SignData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.SignChangeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SignModifyListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void addSign(SignChangeEvent sign) {
        if(sign.isCancelled()) {
            return;
        }

        if(AdvancedRegionMarket.getRegionManager() == null) {
            return;
        }

        try {
            RegionKind regionkind = RegionKind.DEFAULT;
            FlagGroup flagGroup = FlagGroup.DEFAULT;
            Boolean autoReset = true;
            Boolean isHotel = false;
            Boolean doBlockReset = true;
            Boolean userResettable = true;
            EntityLimitGroup entityLimitGroup = EntityLimitGroup.DEFAULT;
            int allowedSubregions = 0;

            if(sign.getLine(0).equalsIgnoreCase("[ARM-Sell]")){
                SellPreset preset = (SellPreset) ActivePresetManager.getPreset(sign.getPlayer(), PresetType.SELLPRESET);
                if(preset != null) {
                    regionkind = preset.getRegionKind();
                    flagGroup = preset.getFlagGroup();
                    autoReset = preset.isAutoReset();
                    isHotel = preset.isHotel();
                    doBlockReset = preset.isDoBlockReset();
                    userResettable = preset.isUserResettable();
                    allowedSubregions = preset.getAllowedSubregions();
                    entityLimitGroup = preset.getEntityLimitGroup();
                    sign.getPlayer().sendMessage(Messages.PREFIX + "Applying preset...");
                }

                if(!sign.getPlayer().hasPermission(Permission.ADMIN_CREATE_SELL)){
                    throw new InputException(sign.getPlayer(), Messages.NO_PERMISSION);
                }

                String worldname = sign.getLine(1);
                String regionname = sign.getLine(2);
                World regionWorld = null;

                if (sign.getLine(1).equals("")){
                    worldname = sign.getBlock().getLocation().getWorld().getName();
                    regionWorld = sign.getBlock().getLocation().getWorld();
                } else {
                    regionWorld = Bukkit.getWorld(worldname);
                    if (regionWorld == null) {
                        throw new InputException(sign.getPlayer(), Messages.WORLD_DOES_NOT_EXIST);
                    }
                }

                if (AdvancedRegionMarket.getWorldGuardInterface().getRegionManager(regionWorld, AdvancedRegionMarket.getWorldGuard()).getRegion(regionname) == null) {
                    throw new InputException(sign.getPlayer(), Messages.REGION_DOES_NOT_EXIST);
                }
                WGRegion region = AdvancedRegionMarket.getWorldGuardInterface().getRegion(regionWorld, AdvancedRegionMarket.getWorldGuard(), regionname);
                Price price = null;

                if(sign.getLine(3).equals("")){
                    if(preset != null){
                        if(preset.hasPrice()){
                            price = new Price(preset.getPrice());
                        }
                        if(preset.hasAutoPrice()) {
                            price = new Price(preset.getAutoPrice());
                        }
                    }
                }

                if(price == null) {
                    AutoPrice autoPrice = AutoPrice.getAutoprice(sign.getLine(3));
                    if(autoPrice == null) {
                        try {
                            price = new Price(Double.parseDouble(sign.getLine(3)));
                        } catch (IllegalArgumentException e){
                            throw new InputException(sign.getPlayer(), Messages.PLEASE_USE_A_NUMBER_AS_PRICE + " or an AutoPrice");
                        }
                    } else {
                        price = new Price(autoPrice);
                    }
                }


                if(price.getPrice() < 0) {
                    throw new InputException(sign.getPlayer(), ChatColor.DARK_RED + "Price must be positive!");
                }

                SignData signData = AdvancedRegionMarket.getSignDataFactory().generateSignData(sign.getBlock().getLocation());
                if(signData == null) {
                    throw new InputException(sign.getPlayer(), "Could not import sign!");
                }

                Region searchregion = AdvancedRegionMarket.getRegionManager().getRegionByNameAndWorld(regionname, worldname);
                if(searchregion != null) {
                    if(!(searchregion instanceof SellRegion)) {
                        throw new InputException(sign.getPlayer(), "Region already registered as a non-sellregion");
                    }
                    searchregion.addSign(signData);
                    searchregion.queueSave();
                    sign.setCancelled(true);
                    sign.getPlayer().sendMessage(Messages.PREFIX + Messages.SIGN_ADDED_TO_REGION);
                    return;
                }

                List<SignData> sellsign = new ArrayList<>();
                sellsign.add(signData);
                SellRegion addRegion = new SellRegion(region, regionWorld, sellsign, price, false, autoReset, isHotel, doBlockReset, regionkind, flagGroup,null,1, userResettable, new ArrayList<Region>(), allowedSubregions, entityLimitGroup, new HashMap<>(), 0);
                addRegion.createSchematic();
                addRegion.applyFlagGroup(FlagGroup.ResetMode.COMPLETE);
                AdvancedRegionMarket.getRegionManager().add(addRegion);
                sign.getPlayer().sendMessage(Messages.PREFIX + Messages.REGION_ADDED_TO_ARM);
                sign.setCancelled(true);

                if(preset != null) {
                    preset.executeSavedCommands(sign.getPlayer(), addRegion);
                }

            }


            if(sign.getLine(0).equalsIgnoreCase("[ARM-Rent]")){
                if(!sign.getPlayer().hasPermission(Permission.ADMIN_CREATE_RENT)){
                    throw new InputException(sign.getPlayer(), Messages.NO_PERMISSION);
                }

                RentPreset preset = (RentPreset) ActivePresetManager.getPreset(sign.getPlayer(), PresetType.RENTPRESET);
                if(preset != null) {
                    regionkind = preset.getRegionKind();
                    autoReset = preset.isAutoReset();
                    isHotel = preset.isHotel();
                    flagGroup = preset.getFlagGroup();
                    doBlockReset = preset.isDoBlockReset();
                    userResettable = preset.isUserResettable();
                    entityLimitGroup = preset.getEntityLimitGroup();
                    allowedSubregions = preset.getAllowedSubregions();
                    sign.getPlayer().sendMessage(Messages.PREFIX + "Applying preset...");
                }

                String worldname = sign.getLine(1);
                String regionname = sign.getLine(2);

                World regionWorld = null;

                if (sign.getLine(1).equals("")){
                    worldname = sign.getBlock().getLocation().getWorld().getName();
                    regionWorld = sign.getBlock().getLocation().getWorld();
                } else {
                    regionWorld = Bukkit.getWorld(worldname);
                    if (regionWorld == null) {
                        throw new InputException(sign.getPlayer(), Messages.WORLD_DOES_NOT_EXIST);
                    }
                }
                if (AdvancedRegionMarket.getWorldGuardInterface().getRegionManager(regionWorld, AdvancedRegionMarket.getWorldGuard()).getRegion(regionname) == null) {
                    throw new InputException(sign.getPlayer(), Messages.REGION_DOES_NOT_EXIST);
                }
                WGRegion region = AdvancedRegionMarket.getWorldGuardInterface().getRegion(regionWorld, AdvancedRegionMarket.getWorldGuard(), regionname);

                RentPrice price = new RentPrice(0, 0,0);
                long extendPerClick = 0;
                long maxRentTime = 0;
                Boolean priceready = false;

                if(sign.getLine(3).equals("")) {
                    if(preset != null) {
                        if(preset.hasPrice() && preset.hasExtendPerClick() && preset.hasMaxRentTime()) {
                            price = new RentPrice(preset.getPrice(), preset.getExtendPerClick(), preset.getMaxRentTime());
                            priceready = true;
                        } else if(preset.hasAutoPrice()) {
                            price = new RentPrice(preset.getAutoPrice());
                            priceready = true;
                        } else {
                            sign.getPlayer().sendMessage(ChatColor.RED + "Your preset needs to have an option at Price, MaxRentTime and ExtendPerClick or an AutoPrice to take affect!");
                        }
                    }
                }

                if(!priceready) {
                    try{
                        if(AutoPrice.getAutoprice(sign.getLine(3)) != null) {
                            price = new RentPrice(AutoPrice.getAutoprice(sign.getLine(3)));
                        } else {
                            String[] priceline = sign.getLine(3).split("(;|:)", 3);
                            String pricestring = priceline[0];
                            String extendPerClickString = priceline[1];
                            String maxRentTimeString = priceline[2];
                            extendPerClick = RentPrice.stringToTime(extendPerClickString);
                            maxRentTime = RentPrice.stringToTime(maxRentTimeString);
                            double doublePrice = Double.parseDouble(pricestring);
                            price = new RentPrice(doublePrice, extendPerClick, maxRentTime);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        sign.getPlayer().sendMessage(Messages.PREFIX + "Please write your price in line 4 in the following pattern:");
                        sign.getPlayer().sendMessage("<Price>;<Extend per Click (ex.: 5d)>;<Max rent Time (ex.: 10d)>");
                        return;
                    } catch (IllegalArgumentException e) {
                        sign.getPlayer().sendMessage(Messages.PREFIX + "Please use d for days, h for hours, m for minutes and s for seconds");
                        sign.getPlayer().sendMessage(Messages.PREFIX + "Please write you price in line 4 in the following pattern:");
                        sign.getPlayer().sendMessage("<Price>;<Extend per Click (ex.: 5d)>;<Max rent Time (ex.: 10d)>");
                        return;
                    }
                }

                SignData signData = AdvancedRegionMarket.getSignDataFactory().generateSignData(sign.getBlock().getLocation());
                if(signData == null) {
                    throw new InputException(sign.getPlayer(), "Could not import sign!");
                }

                Region searchregion = AdvancedRegionMarket.getRegionManager().getRegionByNameAndWorld(regionname, worldname);
                if(searchregion != null) {
                    if(!(searchregion instanceof RentRegion)) {
                        throw new InputException(sign.getPlayer(), "Region already registered as a non-rentregion");
                    }
                    searchregion.addSign(signData);
                    searchregion.queueSave();
                    sign.setCancelled(true);
                    sign.getPlayer().sendMessage(Messages.PREFIX + Messages.SIGN_ADDED_TO_REGION);
                    return;
                }

                List<SignData> sellsign = new ArrayList<>();
                sellsign.add(signData);

                RentRegion addRegion = new RentRegion(region, regionWorld, sellsign, price, false, autoReset, isHotel, doBlockReset, regionkind, flagGroup,null,
                        1, userResettable,1, new ArrayList<Region>(), allowedSubregions, entityLimitGroup, new HashMap<>(), 0);
                addRegion.createSchematic();
                addRegion.applyFlagGroup(FlagGroup.ResetMode.COMPLETE);
                AdvancedRegionMarket.getRegionManager().add(addRegion);

                sign.getPlayer().sendMessage(Messages.PREFIX + Messages.REGION_ADDED_TO_ARM);
                sign.setCancelled(true);

                if(preset != null) {
                    preset.executeSavedCommands(sign.getPlayer(), addRegion);
                }

            }
            if(sign.getLine(0).equalsIgnoreCase("[ARM-Contract]")) {
                if(!sign.getPlayer().hasPermission(Permission.ADMIN_CREATE_CONTRACT)){
                    throw new InputException(sign.getPlayer(), Messages.NO_PERMISSION);
                }

                ContractPreset preset = (ContractPreset) ActivePresetManager.getPreset(sign.getPlayer(), PresetType.CONTRACTPRESET);
                if(preset != null) {
                    regionkind = preset.getRegionKind();
                    autoReset = preset.isAutoReset();
                    isHotel = preset.isHotel();
                    doBlockReset = preset.isDoBlockReset();
                    userResettable = preset.isUserResettable();
                    flagGroup = preset.getFlagGroup();
                    entityLimitGroup = preset.getEntityLimitGroup();
                    allowedSubregions = preset.getAllowedSubregions();
                    sign.getPlayer().sendMessage(Messages.PREFIX + "Applying preset...");
                }

                String worldname = sign.getLine(1);
                String regionname = sign.getLine(2);
                World regionWorld = null;

                if (sign.getLine(1).equals("")){
                    worldname = sign.getBlock().getLocation().getWorld().getName();
                    regionWorld = sign.getBlock().getLocation().getWorld();
                } else {
                    regionWorld = Bukkit.getWorld(worldname);
                    if (regionWorld == null) {
                        throw new InputException(sign.getPlayer(), Messages.WORLD_DOES_NOT_EXIST);
                    }
                }
                if (AdvancedRegionMarket.getWorldGuardInterface().getRegionManager(regionWorld, AdvancedRegionMarket.getWorldGuard()).getRegion(regionname) == null) {
                    throw new InputException(sign.getPlayer(), Messages.REGION_DOES_NOT_EXIST);
                }
                WGRegion region = AdvancedRegionMarket.getWorldGuardInterface().getRegion(regionWorld, AdvancedRegionMarket.getWorldGuard(), regionname);

                ContractPrice price = new ContractPrice(0, 0);
                long extendtime = 0;
                Boolean priceready = false;

                if(sign.getLine(3).equals("")) {
                    if(preset != null) {
                        if(preset.hasPrice() && preset.hasExtend()) {
                            price = new ContractPrice(preset.getPrice(), preset.getExtend());
                            priceready = true;
                        } else if (preset.hasAutoPrice()) {
                            price = new ContractPrice(preset.getAutoPrice());
                            priceready = true;
                        } else {
                            sign.getPlayer().sendMessage(ChatColor.RED + "Your preset needs to have an option at Price and Extend or AutoPrice to take affect!");
                        }
                    }
                }

                if(!priceready) {
                    try{
                        if(AutoPrice.getAutoprice(sign.getLine(3)) != null) {
                            price = new ContractPrice(AutoPrice.getAutoprice(sign.getLine(3)));
                        } else {
                            String[] priceline = sign.getLine(3).split("(;|:)", 2);
                            String pricestring = priceline[0];
                            String extendtimeString = priceline[1];
                            extendtime = RentPrice.stringToTime(extendtimeString);
                            double doublePrice = Double.parseDouble(pricestring);
                            price = new ContractPrice(doublePrice, extendtime);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        sign.getPlayer().sendMessage(Messages.PREFIX + "Please write your price in line 4 in the following pattern:");
                        sign.getPlayer().sendMessage("<Price>;<Extendtime (ex.: 5d)>");
                        return;
                    } catch (IllegalArgumentException e) {
                        sign.getPlayer().sendMessage(Messages.PREFIX + "Please use d for days, h for hours, m for minutes and s for seconds");
                        sign.getPlayer().sendMessage(Messages.PREFIX + "Please write you price in line 4 in the following pattern:");
                        sign.getPlayer().sendMessage("<Price>;<Extendtime (ex.: 5d)>");
                        return;
                    }
                }

                SignData signData = AdvancedRegionMarket.getSignDataFactory().generateSignData(sign.getBlock().getLocation());
                if(signData == null) {
                    throw new InputException(sign.getPlayer(), "Could not import sign!");
                }

                Region searchregion = AdvancedRegionMarket.getRegionManager().getRegionByNameAndWorld(regionname, worldname);
                if(searchregion != null) {
                    if(!(searchregion instanceof ContractRegion)) {
                        throw new InputException(sign.getPlayer(), "Region already registered as a non-contractregion");
                    }
                    searchregion.addSign(signData);
                    searchregion.queueSave();
                    sign.setCancelled(true);
                    sign.getPlayer().sendMessage(Messages.PREFIX + Messages.SIGN_ADDED_TO_REGION);
                    return;
                }
                List<SignData> sellsign = new ArrayList<>();
                sellsign.add(signData);

                ContractRegion addRegion = new ContractRegion(region, regionWorld, sellsign, price, false, autoReset, isHotel, doBlockReset, regionkind, flagGroup,null,
                        1, userResettable, 1, false, new ArrayList<Region>(), allowedSubregions, entityLimitGroup, new HashMap<>(), 0);
                addRegion.createSchematic();
                addRegion.applyFlagGroup(FlagGroup.ResetMode.COMPLETE);
                AdvancedRegionMarket.getRegionManager().add(addRegion);
                sign.getPlayer().sendMessage(Messages.PREFIX + Messages.REGION_ADDED_TO_ARM);
                sign.setCancelled(true);

                if(preset != null) {
                    preset.executeSavedCommands(sign.getPlayer(), addRegion);
                }

            }
        } catch (InputException inputException) {
            inputException.sendMessages(Messages.PREFIX);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void removeSign(BlockBreakEvent block) {
        if(block.isCancelled()){
            return;
        }

        try {
            if (!MaterialFinder.getSignMaterials().contains(block.getBlock().getType())) {
                return;
            }
            Region region = AdvancedRegionMarket.getRegionManager().getRegion((Sign) block.getBlock().getState());
            if(region == null){
                return;
            }

            if(!(block.getPlayer().hasPermission(Permission.ADMIN_REMOVE_SIGN) || ((block.getPlayer().hasPermission(Permission.SUBREGION_DELETE_SOLD) ||
                    block.getPlayer().hasPermission(Permission.SUBREGION_DELETE_AVAILABLE)) && region.isSubregion()))) {
                block.setCancelled(true);
                throw new InputException(block.getPlayer(), Messages.NO_PERMISSION);
            }
            double loc_x = block.getBlock().getLocation().getX();
            double loc_y = block.getBlock().getLocation().getY();
            double loc_z = block.getBlock().getLocation().getZ();
            Location loc = new Location(block.getBlock().getWorld(), loc_x, loc_y, loc_z);

            if(block.getPlayer().hasPermission(Permission.ADMIN_REMOVE_SIGN)) {
                block.setCancelled(!region.removeSign(loc, block.getPlayer()));
                return;
            }

            if(region.isSubregion() && (block.getPlayer().hasPermission(Permission.SUBREGION_DELETE_AVAILABLE) || block.getPlayer().hasPermission(Permission.SUBREGION_DELETE_SOLD))) {
                if(region.getParentRegion() != null) {
                    if(region.getParentRegion().getRegion().hasOwner(block.getPlayer().getUniqueId())) {
                        if(region.isSold()) {
                            if(block.getPlayer().hasPermission(Permission.SUBREGION_DELETE_SOLD)) {
                                block.setCancelled(!region.removeSign(loc, block.getPlayer()));
                                return;
                            } else {
                                block.setCancelled(true);
                                throw new InputException(block.getPlayer(), Messages.NOT_ALLOWED_TO_REMOVE_SUB_REGION_SOLD);
                            }
                        } else {
                            if(block.getPlayer().hasPermission(Permission.SUBREGION_DELETE_AVAILABLE)) {
                                block.setCancelled(!region.removeSign(loc, block.getPlayer()));
                                return;
                            } else {
                                block.setCancelled(true);
                                throw new InputException(block.getPlayer(), Messages.NOT_ALLOWED_TO_REMOVE_SUB_REGION_AVAILABLE);
                            }
                        }
                    } else {
                        block.setCancelled(true);
                        throw new InputException(block.getPlayer(), Messages.PARENT_REGION_NOT_OWN);
                    }
                }
            }
        } catch (InputException inputException) {
            inputException.sendMessages(Messages.PREFIX);
        }
    }

    @EventHandler
    public void protectSignPhysics(BlockPhysicsEvent sign) {
        if (MaterialFinder.getSignMaterials().contains(sign.getBlock().getType())){
            if(AdvancedRegionMarket.getRegionManager().getRegion((Sign) sign.getBlock().getState()) != null){
                sign.setCancelled(true);
                return;
            }
        }
    }

}
