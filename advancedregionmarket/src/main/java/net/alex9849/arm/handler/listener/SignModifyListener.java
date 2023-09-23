package net.alex9849.arm.handler.listener;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.adapters.WGRegion;
import net.alex9849.arm.adapters.signs.SignData;
import net.alex9849.arm.exceptions.FeatureDisabledException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.presets.ActivePresetManager;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import net.alex9849.arm.regions.ContractRegion;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.RentRegion;
import net.alex9849.arm.regions.SellRegion;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.ContractPrice;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.arm.regions.price.RentPrice;
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
            price = new Price(Double.parseDouble(priceLine));
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
            double moneyAmount = Double.parseDouble(priceSegments[0]);
            long extendTime = ContractPrice.stringToTime(priceSegments[1]);

            price = new ContractPrice(moneyAmount, extendTime);
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
            double moneyAmount = Double.parseDouble(priceSegments[0]);
            long extendPerClick = RentPrice.stringToTime(priceSegments[1]);
            long maxExtendTime = RentPrice.stringToTime(priceSegments[2]);

            price = new RentPrice(moneyAmount, extendPerClick, maxExtendTime);
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
            if (sign.getLine(0).matches("(?i)\\[ARM\\-(Sell|s)\\]")) {
                presetType = PresetType.SELLPRESET;
                if (!sign.getPlayer().hasPermission(Permission.ADMIN_CREATE_SELL))
                    throw new InputException(sign.getPlayer(), Messages.NO_PERMISSION);
            } else if (sign.getLine(0).matches("(?i)\\[ARM\\-(Rent|r)\\]")) {
                presetType = PresetType.RENTPRESET;
                if (!sign.getPlayer().hasPermission(Permission.ADMIN_CREATE_RENT))
                    throw new InputException(sign.getPlayer(), Messages.NO_PERMISSION);
            } else if (sign.getLine(0).matches("(?i)\\[ARM\\-(Contract|c)\\]")) {
                presetType = PresetType.CONTRACTPRESET;
                if (!sign.getPlayer().hasPermission(Permission.ADMIN_CREATE_CONTRACT))
                    throw new InputException(sign.getPlayer(), Messages.NO_PERMISSION);
            } else {
                return;
            }
            String priceLine = sign.getLine(3);
            Price price = null;
            if (!priceLine.isEmpty()) {
                switch (presetType) {
                    case SELLPRESET:
                        price = parseSellPrice(priceLine, sign.getPlayer());
                        break;
                    case CONTRACTPRESET:
                        price = parseContractPrice(priceLine, sign.getPlayer());
                        break;
                    case RENTPRESET:
                        price = parseRentPrice(priceLine, sign.getPlayer());
                        break;
                    default:
                        throw new RuntimeException("This is a bug! SignModifyListener does not know how to set the " +
                                "Price in PresetType " + presetType + "!");
                }
            }

            Preset preset = ActivePresetManager.getPreset(sign.getPlayer(), presetType);
            if (preset == null) {
                preset = presetType.create();
            }

            //Get world
            String regionWorldName = sign.getLine(1);
            if (regionWorldName.isEmpty()) {
                regionWorldName = sign.getBlock().getWorld().getName();
            }
            World regionWorld = Bukkit.getWorld(regionWorldName);
            if (regionWorld == null) {
                throw new InputException(sign.getPlayer(), Messages.WORLD_DOES_NOT_EXIST);
            }

            //Get region
            WGRegion wgRegion = AdvancedRegionMarket.getInstance().getWorldGuardInterface().getRegion(regionWorld, sign.getLine(2));
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
                if (price != null) {
                    applyPrice(existingArmRegion, price, sign.getPlayer());
                    try {
                        existingArmRegion.applyFlagGroup(FlagGroup.ResetMode.COMPLETE, false);
                    } catch (FeatureDisabledException e) {
                        //Ignore
                    }
                }
                existingArmRegion.addSign(signData);
                sign.setCancelled(true);
                sign.getPlayer().sendMessage(Messages.PREFIX + Messages.SIGN_ADDED_TO_REGION);
            } else {
                List<SignData> signDataList = new ArrayList<>();
                signDataList.add(signData);
                Region newArmRegion = preset.generateRegion(wgRegion, regionWorld, sign.getPlayer(), false, signDataList);

                //Apply Price
                if (price != null) {
                    applyPrice(newArmRegion, price, sign.getPlayer());
                } else if (!preset.canPriceLineBeLetEmpty()) {
                    sign.getPlayer().sendMessage(Messages.PREFIX + "Price not defined! Using default Autoprice!");
                }
                newArmRegion.createSchematic();
                try {
                    newArmRegion.applyFlagGroup(FlagGroup.ResetMode.COMPLETE, false);
                } catch (FeatureDisabledException e) {
                    //Ignore
                }
                AdvancedRegionMarket.getInstance().getRegionManager().add(newArmRegion);
                newArmRegion.updateSigns();
                sign.setCancelled(true);
                preset.executeSetupCommands(sign.getPlayer(), newArmRegion);
                sign.getPlayer().sendMessage(Messages.PREFIX + Messages.REGION_AND_SIGN_ADDED_TO_ARM);
            }


        } catch (InputException inputException) {
            inputException.sendMessages(Messages.PREFIX);
        } catch (IllegalArgumentException e) {
            sign.getPlayer().sendMessage(Messages.PREFIX + e.getMessage());
        }
    }

    /**
     * Applies a price to a region and used the correct method.
     * Method doesn't perform any type checks!
     */
    private void applyPrice(Region region, Price price, Player player) throws InputException {
        if (region instanceof SellRegion) {
            ((SellRegion) region).setSellPrice(price);
        } else if (region instanceof ContractRegion) {
            if (!(price instanceof ContractPrice)) {
                throw new InputException(player, "Price format doesn't not applicable region type!");
            }
            ((ContractRegion) region).setContractPrice((ContractPrice) price);
        } else if (region instanceof RentRegion) {
            if (!(price instanceof RentPrice)) {
                throw new InputException(player, "Price format doesn't not applicable region type!");
            }
            ((RentRegion) region).setRentPrice((RentPrice) price);
        } else {
            throw new RuntimeException("This is a bug! SignModifyListener does not know how to set the " +
                    "Price in region-class " + region.getClass().getName() + "!");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void removeSign(BlockBreakEvent block) {
        if (block.isCancelled()) {
            return;
        }

        try {
            if (!AdvancedRegionMarket.getInstance().getMaterialFinder().getSignMaterials().contains(block.getBlock().getType())) {
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
                                throw new InputException(block.getPlayer(), Messages.NOT_ALLOWED_TO_REMOVE_SUBREGION_SOLD);
                            }
                        } else {
                            if (block.getPlayer().hasPermission(Permission.SUBREGION_DELETE_AVAILABLE)) {
                                this.removeSignAndSendMessages(region, loc, block.getPlayer());
                                return;
                            } else {
                                block.setCancelled(true);
                                throw new InputException(block.getPlayer(), Messages.NOT_ALLOWED_TO_REMOVE_SUBREGION_AVAILABLE);
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
            player.sendMessage(Messages.PREFIX + region.replaceVariables(Messages.REGION_ZERO_SIGNS_REACHED));
        }
    }

    @EventHandler
    public void protectSignPhysics(BlockPhysicsEvent sign) {
        AdvancedRegionMarket plugin = AdvancedRegionMarket.getInstance();
        if (plugin.getMaterialFinder().getSignMaterials().contains(sign.getBlock().getType())) {
            if (plugin.getRegionManager().getRegion((Sign) sign.getBlock().getState()) != null) {
                sign.setCancelled(true);
                return;
            }
        }
    }
}
