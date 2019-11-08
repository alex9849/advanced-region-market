package net.alex9849.arm.handler.listener;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.FeatureDisabledException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.presets.ActivePresetManager;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.ContractPrice;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.arm.regions.price.RentPrice;
import net.alex9849.arm.util.MaterialFinder;
import net.alex9849.inter.WGRegion;
import net.alex9849.signs.SignData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.SignChangeEvent;

import java.util.ArrayList;
import java.util.List;

public class SignModifyListener implements Listener {
    private static final String SELLPRICE_LINE_REGEX = "[0-9]+";
    private static final String RENTPRICE_LINE_REGEX = "[0-9]+(;|:)[0-9]+(s|m|h|d)(;|:)[0-9]+(s|m|h|d)";
    private static final String CONTRACTPRICE_LINE_REGEX = "[0-9]+(;|:)[0-9]+(s|m|h|d)";

    private static Price parseSellPrice(String priceLine, CommandSender sender) throws InputException {
        AutoPrice autoPrice = AutoPrice.getAutoprice(priceLine);
        Price price = null;
        if (autoPrice != null) {
            price = new Price(autoPrice);
        } else {
            if (!priceLine.matches(SELLPRICE_LINE_REGEX)) {
                throw new InputException(sender, ChatColor.DARK_RED + "Please write a positive number as price or an AutoPrice at line 4");
            }
            price = new Price(Integer.parseInt(priceLine));
        }
        if (price.getPrice() < 0) {
            throw new InputException(sender, ChatColor.DARK_RED + "Price must be positive!");
        }
        return price;
    }

    private static ContractPrice parseContractPrice(String priceLine, CommandSender sender) throws InputException {
        AutoPrice autoPrice = AutoPrice.getAutoprice(priceLine);
        ContractPrice price = null;
        if (autoPrice != null) {
            price = new ContractPrice(autoPrice);
        } else {
            if (!priceLine.matches(CONTRACTPRICE_LINE_REGEX)) {
                throw new InputException(sender, "Please use d for days, h for hours, m for minutes and s for seconds!\n" +
                        Messages.PREFIX + "Please write you price in line 4 in the following pattern:\n" +
                        "<Price>;<Extendtime (ex.: 5d)>");
            }
            String[] priceSegments = priceLine.split("(;|:)");
            int moneyAmount = Integer.parseInt(priceSegments[0]);
            long extendTime = ContractPrice.stringToTime(priceSegments[1]);

            price = new ContractPrice(moneyAmount, extendTime);
        }
        if (price.getPrice() < 0) {
            throw new InputException(sender, ChatColor.DARK_RED + "Price must be positive!");
        }
        return price;
    }

    private static RentPrice parseRentPrice(String priceLine, CommandSender sender) throws InputException {
        AutoPrice autoPrice = AutoPrice.getAutoprice(priceLine);
        RentPrice price = null;
        if (autoPrice != null) {
            price = new RentPrice(autoPrice);
        } else {
            if (!priceLine.matches(RENTPRICE_LINE_REGEX)) {
                throw new InputException(sender, "Please use d for days, h for hours, m for minutes and s for seconds!\n" +
                        Messages.PREFIX + "Please write you price in line 4 in the following pattern:\n" +
                        "<Price>;<Extend per Click (ex.: 5d)>;<Max rent Time (ex.: 10d)>");
            }
            String[] priceSegments = priceLine.split("(;|:)");
            int moneyAmount = Integer.parseInt(priceSegments[0]);
            long extendPerClick = RentPrice.stringToTime(priceSegments[1]);
            long maxRentTime = RentPrice.stringToTime(priceSegments[2]);

            price = new RentPrice(moneyAmount, extendPerClick, maxRentTime);
        }
        if (price.getPrice() < 0) {
            throw new InputException(sender, ChatColor.DARK_RED + "Price must be positive!");
        }
        return price;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void addSign(SignChangeEvent sign) {
        if (AdvancedRegionMarket.getInstance().getRegionManager() == null) {
            return;
        }

        try {
            //Pick the right preset, parse price and check permissions
            PresetType presetType;
            Price price = null;
            if (sign.getLine(0).equalsIgnoreCase("[ARM-Sell]")) {
                presetType = PresetType.SELLPRESET;
                if (!sign.getPlayer().hasPermission(Permission.ADMIN_CREATE_CONTRACT))
                    throw new InputException(sign.getPlayer(), Messages.NO_PERMISSION);
                if (!sign.getLine(3).equalsIgnoreCase("")) {
                    price = parseSellPrice(sign.getLine(3), sign.getPlayer());
                }
            } else if (sign.getLine(0).equalsIgnoreCase("[ARM-Rent]")) {
                presetType = PresetType.RENTPRESET;
                if (!sign.getPlayer().hasPermission(Permission.ADMIN_CREATE_CONTRACT))
                    throw new InputException(sign.getPlayer(), Messages.NO_PERMISSION);
                if (!sign.getLine(3).equalsIgnoreCase("")) {
                    price = this.parseRentPrice(sign.getLine(3), sign.getPlayer());
                }
            } else if (sign.getLine(0).equalsIgnoreCase("[ARM-Contract]")) {
                presetType = PresetType.CONTRACTPRESET;
                if (!sign.getPlayer().hasPermission(Permission.ADMIN_CREATE_CONTRACT))
                    throw new InputException(sign.getPlayer(), Messages.NO_PERMISSION);
                if (!sign.getLine(3).equalsIgnoreCase("")) {
                    price = this.parseContractPrice(sign.getLine(3), sign.getPlayer());
                }
            } else {
                return;
            }

            Preset preset = ActivePresetManager.getPreset(sign.getPlayer(), presetType);
            if (preset == null) {
                preset = presetType.create();
            }

            //Get world
            String regionWorldName = sign.getLine(1);
            if (regionWorldName.equalsIgnoreCase("")) {
                regionWorldName = sign.getBlock().getWorld().getName();
            }
            World regionWorld = Bukkit.getWorld(regionWorldName);
            if (regionWorld == null) {
                throw new InputException(sign.getPlayer(), Messages.WORLD_DOES_NOT_EXIST);
            }

            //Get region
            WGRegion wgRegion = AdvancedRegionMarket.getInstance().getWorldGuardInterface().getRegion(regionWorld, AdvancedRegionMarket.getInstance().getWorldGuard(), sign.getLine(2));
            if (wgRegion == null) {
                throw new InputException(sign.getPlayer(), Messages.REGION_DOES_NOT_EXIST);
            }

            //Generate signdata
            SignData signData = AdvancedRegionMarket.getInstance().getSignDataFactory().generateSignData(sign.getBlock().getLocation());
            if (signData == null) {
                throw new InputException(sign.getPlayer(), ChatColor.DARK_RED + "Could not import sign!");
            }

            Region existingArmRegion = AdvancedRegionMarket.getInstance().getRegionManager().getRegion(wgRegion);
            if (existingArmRegion != null) {
                existingArmRegion.addSign(signData);
                sign.setCancelled(true);
                sign.getPlayer().sendMessage(Messages.PREFIX + Messages.SIGN_ADDED_TO_REGION);
                return;
            } else {
                List<SignData> signDataList = new ArrayList<>();
                signDataList.add(signData);
                Region newArmRegion = preset.generateRegion(wgRegion, regionWorld, signDataList);
                if (price == null && preset.canPriceLineBeLetEmpty()) {
                    sign.getPlayer().sendMessage(Messages.PREFIX + "Price not defined! Using default Autoprice!");
                } else {
                    newArmRegion.setPrice(price);
                }
                newArmRegion.createSchematic();
                try {
                    newArmRegion.applyFlagGroup(FlagGroup.ResetMode.COMPLETE, false);
                } catch (FeatureDisabledException e) {
                    //Ignore
                }
                AdvancedRegionMarket.getInstance().getRegionManager().add(newArmRegion);
                sign.setCancelled(true);
                sign.getPlayer().sendMessage(Messages.PREFIX + Messages.REGION_ADDED_TO_ARM);
                return;
            }


        } catch (InputException inputException) {
            inputException.sendMessages(Messages.PREFIX);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void removeSign(BlockBreakEvent block) {
        if (block.isCancelled()) {
            return;
        }

        try {
            if (!MaterialFinder.getSignMaterials().contains(block.getBlock().getType())) {
                return;
            }
            Region region = AdvancedRegionMarket.getInstance().getRegionManager().getRegion((Sign) block.getBlock().getState());
            if (region == null) {
                return;
            }

            if (!(block.getPlayer().hasPermission(Permission.ADMIN_REMOVE_SIGN) || ((block.getPlayer().hasPermission(Permission.SUBREGION_DELETE_SOLD) ||
                    block.getPlayer().hasPermission(Permission.SUBREGION_DELETE_AVAILABLE)) && region.isSubregion()))) {
                block.setCancelled(true);
                throw new InputException(block.getPlayer(), Messages.NO_PERMISSION);
            }
            double loc_x = block.getBlock().getLocation().getX();
            double loc_y = block.getBlock().getLocation().getY();
            double loc_z = block.getBlock().getLocation().getZ();
            Location loc = new Location(block.getBlock().getWorld(), loc_x, loc_y, loc_z);

            if (block.getPlayer().hasPermission(Permission.ADMIN_REMOVE_SIGN)) {
                this.removeSignAndSendMessages(region, loc, block.getPlayer());
                return;
            }

            if (region.isSubregion() && (block.getPlayer().hasPermission(Permission.SUBREGION_DELETE_AVAILABLE) || block.getPlayer().hasPermission(Permission.SUBREGION_DELETE_SOLD))) {
                if (region.getParentRegion() != null) {
                    if (region.getParentRegion().getRegion().hasOwner(block.getPlayer().getUniqueId())) {
                        if (region.isSold()) {
                            if (block.getPlayer().hasPermission(Permission.SUBREGION_DELETE_SOLD)) {
                                this.removeSignAndSendMessages(region, loc, block.getPlayer());
                                return;
                            } else {
                                block.setCancelled(true);
                                throw new InputException(block.getPlayer(), Messages.NOT_ALLOWED_TO_REMOVE_SUB_REGION_SOLD);
                            }
                        } else {
                            if (block.getPlayer().hasPermission(Permission.SUBREGION_DELETE_AVAILABLE)) {
                                this.removeSignAndSendMessages(region, loc, block.getPlayer());
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

    private void removeSignAndSendMessages(Region region, Location signLoc, Player player) {
        region.removeSign(signLoc);
        String message = Messages.SIGN_REMOVED_FROM_REGION.replace("%remaining%", region.getNumberOfSigns() + "");
        player.sendMessage(Messages.PREFIX + message);
        if(region.getNumberOfSigns() == 0) {
            if(region.isSubregion()) {
                region.delete(AdvancedRegionMarket.getInstance().getRegionManager());
            }
            AdvancedRegionMarket.getInstance().getRegionManager().remove(region);
            player.sendMessage(Messages.PREFIX + Messages.REGION_REMOVED_FROM_ARM);
        }
    }

    @EventHandler
    public void protectSignPhysics(BlockPhysicsEvent sign) {
        if (MaterialFinder.getSignMaterials().contains(sign.getBlock().getType())) {
            if (AdvancedRegionMarket.getInstance().getRegionManager().getRegion((Sign) sign.getBlock().getState()) != null) {
                sign.setCancelled(true);
                return;
            }
        }
    }
}
