package net.alex9849.arm.Handler;

import com.sk89q.worldguard.protection.managers.RegionManager;
import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.Preseter.*;
import net.alex9849.arm.SubRegions.SubRegionCreator;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regions.*;
import net.alex9849.arm.gui.Gui;
import net.alex9849.inter.WGRegion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ARMListener implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void addSign(SignChangeEvent sign) {
        if(sign.isCancelled()) {
            return;
        }

        try {
            RegionKind regionkind = RegionKind.DEFAULT;
            Boolean autoReset = true;
            Boolean isHotel = false;
            Boolean doBlockReset = true;

            if(sign.getLine(0).equalsIgnoreCase("[ARM-Sell]")){
                SellPreset preset = (SellPreset) Preset.getPreset(PresetType.SELLPRESET, sign.getPlayer());
                if(preset != null) {
                    regionkind = preset.getRegionKind();
                    autoReset = preset.isAutoReset();
                    isHotel = preset.isHotel();
                    doBlockReset = preset.isDoBlockReset();
                    sign.getPlayer().sendMessage(Messages.PREFIX + "Applying preset...");
                }

                if(!sign.getPlayer().hasPermission(Permission.ADMIN_CREATE_SELL)){
                    throw new InputException(sign.getPlayer(), Messages.NO_PERMISSION);
                }

                String worldname = sign.getLine(1);
                String regionname = sign.getLine(2);

                if (sign.getLine(1).equals("")){
                    worldname = sign.getBlock().getLocation().getWorld().getName();
                } else {
                    if (Bukkit.getWorld(worldname) == null) {
                        throw new InputException(sign.getPlayer(), Messages.WORLD_DOES_NOT_EXIST);
                    }
                }

                if (AdvancedRegionMarket.getWorldGuardInterface().getRegionManager(Bukkit.getWorld(worldname), AdvancedRegionMarket.getWorldGuard()).getRegion(regionname) == null) {
                    throw new InputException(sign.getPlayer(), Messages.REGION_DOES_NOT_EXIST);
                }
                WGRegion region = AdvancedRegionMarket.getWorldGuardInterface().getRegion(Bukkit.getWorld(worldname), AdvancedRegionMarket.getWorldGuard(), regionname);
                Double price = null;

                if(sign.getLine(3).equals("")){
                    if(preset != null){
                        if(preset.hasPrice()){
                            price = preset.getPrice();
                        }
                    }
                }

                if(price == null) {
                    try{
                        price = Region.calculatePrice(region, sign.getLine(3));
                    } catch (IllegalArgumentException e){
                        throw new InputException(sign.getPlayer(), Messages.PLEASE_USE_A_NUMBER_AS_PRICE + " or an AutoPrice");
                    }
                }

                if(price < 0) {
                    throw new InputException(sign.getPlayer(), ChatColor.DARK_RED + "Price must be positive!");
                }

                Region searchregion = net.alex9849.arm.regions.RegionManager.searchRegionbyNameAndWorld(regionname, worldname);
                if(searchregion != null) {
                    if(!(searchregion instanceof SellRegion)) {
                        throw new InputException(sign.getPlayer(), "Region already registered as a non-sellregion");
                    }
                    searchregion.addSign(sign.getBlock().getLocation());
                    sign.setCancelled(true);
                    sign.getPlayer().sendMessage(Messages.PREFIX + Messages.SIGN_ADDED_TO_REGION);
                    return;
                }


                LinkedList<Sign> sellsign = new LinkedList<Sign>();
                sellsign.add((Sign) sign.getBlock().getState());
                SellRegion addRegion = new SellRegion(region, worldname, sellsign, price, false, autoReset, isHotel, doBlockReset, regionkind, null,1, new ArrayList<Region>(), false, false);
                addRegion.createSchematic();
                net.alex9849.arm.regions.RegionManager.addRegion(addRegion);
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

                RentPreset preset = (RentPreset) Preset.getPreset(PresetType.RENTPRESET, sign.getPlayer());
                if(preset != null) {
                    regionkind = preset.getRegionKind();
                    autoReset = preset.isAutoReset();
                    isHotel = preset.isHotel();
                    doBlockReset = preset.isDoBlockReset();
                    sign.getPlayer().sendMessage(Messages.PREFIX + "Applying preset...");
                }

                String worldname = sign.getLine(1);
                String regionname = sign.getLine(2);

                if (sign.getLine(1).equals("")){
                    worldname = sign.getBlock().getLocation().getWorld().getName();
                } else {
                    if (Bukkit.getWorld(worldname) == null) {
                        throw new InputException(sign.getPlayer(), Messages.WORLD_DOES_NOT_EXIST);
                    }
                }
                if (AdvancedRegionMarket.getWorldGuardInterface().getRegionManager(Bukkit.getWorld(worldname), AdvancedRegionMarket.getWorldGuard()).getRegion(regionname) == null) {
                    throw new InputException(sign.getPlayer(), Messages.REGION_DOES_NOT_EXIST);
                }
                WGRegion region = AdvancedRegionMarket.getWorldGuardInterface().getRegion(Bukkit.getWorld(worldname), AdvancedRegionMarket.getWorldGuard(), regionname);

                double price = 0;
                long extendPerClick = 0;
                long maxRentTime = 0;
                Boolean priceready = false;

                if(sign.getLine(3).equals("")) {
                    if(preset != null) {
                        if(preset.hasPrice() && preset.hasExtendPerClick() && preset.hasMaxRentTime()) {
                            price = preset.getPrice();
                            extendPerClick = preset.getExtendPerClick();
                            maxRentTime = preset.getMaxRentTime();
                            priceready = true;
                        } else {
                            throw new InputException(sign.getPlayer(), ChatColor.RED + "Your preset needs to have an option at Price, MaxRentTime and ExtendPerClick to take affect!");
                        }
                    }
                }

                if(!priceready) {
                    try{
                        String[] priceline = sign.getLine(3).split(";", 3);
                        String pricestring = priceline[0];
                        String extendPerClickString = priceline[1];
                        String maxRentTimeString = priceline[2];
                        extendPerClick = RentRegion.stringToTime(extendPerClickString);
                        maxRentTime = RentRegion.stringToTime(maxRentTimeString);
                        price = Integer.parseInt(pricestring);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        sign.getPlayer().sendMessage(Messages.PREFIX + "Please write your price in line 3 in the following pattern:");
                        sign.getPlayer().sendMessage("<Price>;<Extend per Click (ex.: 5d)>;<Max rent Time (ex.: 10d)>");
                        return;
                    } catch (IllegalArgumentException e) {
                        sign.getPlayer().sendMessage(Messages.PREFIX + "Please use d for days, h for hours, m for minutes and s for seconds");
                        sign.getPlayer().sendMessage(Messages.PREFIX + "Please write you price in line 3 in the following pattern:");
                        sign.getPlayer().sendMessage("<Price>;<Extend per Click (ex.: 5d)>;<Max rent Time (ex.: 10d)>");
                        return;
                    }
                }

                Region searchregion = net.alex9849.arm.regions.RegionManager.searchRegionbyNameAndWorld(regionname, worldname);
                if(searchregion != null) {
                    if(!(searchregion instanceof RentRegion)) {
                        throw new InputException(sign.getPlayer(), "Region already registered as a non-rentregion");
                    }
                    searchregion.addSign(sign.getBlock().getLocation());
                    sign.setCancelled(true);
                    sign.getPlayer().sendMessage(Messages.PREFIX + Messages.SIGN_ADDED_TO_REGION);
                    return;
                }

                LinkedList<Sign> sellsign = new LinkedList<Sign>();
                sellsign.add((Sign) sign.getBlock().getState());

                RentRegion addRegion = new RentRegion(region, worldname, sellsign, price, false, autoReset, isHotel, doBlockReset, regionkind, null,
                        1,1, maxRentTime, extendPerClick, new ArrayList<Region>(), false, false);
                addRegion.createSchematic();
                net.alex9849.arm.regions.RegionManager.addRegion(addRegion);

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

                ContractPreset preset = (ContractPreset) Preset.getPreset(PresetType.CONTRACTPRESET, sign.getPlayer());
                if(preset != null) {
                    regionkind = preset.getRegionKind();
                    autoReset = preset.isAutoReset();
                    isHotel = preset.isHotel();
                    doBlockReset = preset.isDoBlockReset();
                    sign.getPlayer().sendMessage(Messages.PREFIX + "Applying preset...");
                }

                String worldname = sign.getLine(1);
                String regionname = sign.getLine(2);

                if (sign.getLine(1).equals("")){
                    worldname = sign.getBlock().getLocation().getWorld().getName();
                } else {
                    if (Bukkit.getWorld(worldname) == null) {
                        throw new InputException(sign.getPlayer(), Messages.WORLD_DOES_NOT_EXIST);
                    }
                }
                if (AdvancedRegionMarket.getWorldGuardInterface().getRegionManager(Bukkit.getWorld(worldname), AdvancedRegionMarket.getWorldGuard()).getRegion(regionname) == null) {
                    throw new InputException(sign.getPlayer(), Messages.REGION_DOES_NOT_EXIST);
                }
                WGRegion region = AdvancedRegionMarket.getWorldGuardInterface().getRegion(Bukkit.getWorld(worldname), AdvancedRegionMarket.getWorldGuard(), regionname);

                double price = 0;
                long extendtime = 0;
                Boolean priceready = false;

                if(sign.getLine(3).equals("")) {
                    if(preset != null) {
                        if(preset.hasPrice() && preset.hasExtend()) {
                            price = preset.getPrice();
                            extendtime = preset.getExtend();
                            priceready = true;
                        } else {
                            throw new InputException(sign.getPlayer(), ChatColor.RED + "Your preset needs to have an option at Price and Extend to take affect!");
                        }
                    }
                }

                if(!priceready) {
                    try{
                        String[] priceline = sign.getLine(3).split(";", 2);
                        String pricestring = priceline[0];
                        String extendtimeString = priceline[1];
                        extendtime = RentRegion.stringToTime(extendtimeString);
                        price = Integer.parseInt(pricestring);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        sign.getPlayer().sendMessage(Messages.PREFIX + "Please write your price in line 3 in the following pattern:");
                        sign.getPlayer().sendMessage("<Price>;<Extendtime (ex.: 5d)>");
                        return;
                    } catch (IllegalArgumentException e) {
                        sign.getPlayer().sendMessage(Messages.PREFIX + "Please use d for days, h for hours, m for minutes and s for seconds");
                        sign.getPlayer().sendMessage(Messages.PREFIX + "Please write you price in line 3 in the following pattern:");
                        sign.getPlayer().sendMessage("<Price>;<Extendtime (ex.: 5d)>");
                        return;
                    }
                }


                Region searchregion = net.alex9849.arm.regions.RegionManager.searchRegionbyNameAndWorld(regionname, worldname);
                if(searchregion != null) {
                    if(!(searchregion instanceof ContractRegion)) {
                        throw new InputException(sign.getPlayer(), "Region already registered as a non-contractregion");
                    }
                    searchregion.addSign(sign.getBlock().getLocation());
                    sign.setCancelled(true);
                    sign.getPlayer().sendMessage(Messages.PREFIX + Messages.SIGN_ADDED_TO_REGION);
                    return;
                }
                LinkedList<Sign> sellsign = new LinkedList<Sign>();
                sellsign.add((Sign) sign.getBlock().getState());

                ContractRegion addRegion = new ContractRegion(region, worldname, sellsign, price, false, autoReset, isHotel, doBlockReset, regionkind, null,
                        1, extendtime, 1, false, new ArrayList<Region>(), false, false);
                addRegion.createSchematic();
                net.alex9849.arm.regions.RegionManager.addRegion(addRegion);
                sign.getPlayer().sendMessage(Messages.PREFIX + Messages.REGION_ADDED_TO_ARM);
                sign.setCancelled(true);

                if(preset != null) {
                    preset.executeSavedCommands(sign.getPlayer(), addRegion);
                }

            }
        } catch (InputException inputException) {
            inputException.sendMessages();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void removeSign(BlockBreakEvent block) {
        if(block.isCancelled()){
            return;
        }

        try {
            if ((block.getBlock().getType() != Material.SIGN) && (block.getBlock().getType() != Material.WALL_SIGN)) {
                return;
            }

            if(net.alex9849.arm.regions.RegionManager.getRegion((Sign) block.getBlock().getState()) == null){
                return;
            }

            if(!block.getPlayer().hasPermission(Permission.ADMIN_REMOVE_SIGN)) {
                block.setCancelled(true);
                throw new InputException(block.getPlayer(), Messages.NO_PERMISSION);
            }
            double loc_x = block.getBlock().getLocation().getX();
            double loc_y = block.getBlock().getLocation().getY();
            double loc_z = block.getBlock().getLocation().getZ();
            Location loc = new Location(block.getBlock().getWorld(), loc_x, loc_y, loc_z);

            net.alex9849.arm.regions.RegionManager.getRegion((Sign) block.getBlock().getState()).removeSign(loc, block.getPlayer());
        } catch (InputException inputException) {
            inputException.sendMessages();
        }
    }

    @EventHandler
    public void interactEvent(PlayerInteractEvent event){
        try {
            this.buyregion(event);
            this.setSubregionMark(event);
        } catch (InputException inputException) {
            inputException.sendMessages();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void addBuiltBlock(BlockPlaceEvent event){
        if(event.isCancelled()) {
            return;
        }

        if(AdvancedRegionMarket.getWorldGuardInterface().canBuild(event.getPlayer(), event.getBlock().getLocation(), AdvancedRegionMarket.getWorldGuard())) {
            List<Region> playersRegions = net.alex9849.arm.regions.RegionManager.getRegionsByOwnerOrMember(event.getPlayer().getUniqueId());
            int x = event.getBlock().getLocation().getBlockX();
            int y = event.getBlock().getLocation().getBlockY();
            int z = event.getBlock().getLocation().getBlockZ();

            for(Region region : playersRegions) {
                if(region.getRegion().contains(x, y, z)) {
                    if(region.getRegionworld().equals(event.getBlock().getWorld().getName())) {
                        if(region.isHotel()) {
                            if(region.isSold()) {
                                region.addBuiltBlock(event.getBlock().getLocation());
                            }
                        }
                    }
                }
            }

        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void breakBlock(BlockBreakEvent event) {
        if(event.isCancelled()) {
            return;
        }
        try {
            if(event.getPlayer().hasPermission(Permission.ADMIN_BUILDEVERYWHERE)){
                return;
            }
            if(AdvancedRegionMarket.getWorldGuardInterface().canBuild(event.getPlayer(), event.getBlock().getLocation(), AdvancedRegionMarket.getWorldGuard())){

                List<Region> playersRegions = net.alex9849.arm.regions.RegionManager.getRegionsByOwnerOrMember(event.getPlayer().getUniqueId());
                int x = event.getBlock().getLocation().getBlockX();
                int y = event.getBlock().getLocation().getBlockY();
                int z = event.getBlock().getLocation().getBlockZ();

                for(Region region : playersRegions) {
                    if(region.getRegion().contains(x, y, z)) {
                        if(region.getRegionworld().equals(event.getBlock().getWorld().getName())) {
                            if(region.isHotel()) {
                                if(!region.allowBlockBreak(event.getBlock().getLocation())) {
                                    event.setCancelled(true);
                                    throw new InputException(event.getPlayer(), Messages.REGION_ERROR_CAN_NOT_BUILD_HERE);
                                }
                            }
                        }
                    }
                }
            }
        } catch (InputException inputException) {
            inputException.sendMessages();
        }
    }

    @EventHandler
    public void protectSignPhysics(BlockPhysicsEvent sign) {
        if (sign.getBlock().getType() == Material.SIGN || sign.getBlock().getType() == Material.WALL_SIGN){
            if(net.alex9849.arm.regions.RegionManager.getRegion((Sign) sign.getBlock().getState()) != null){
                sign.setCancelled(true);
                return;
            }
        }
    }


    @EventHandler
    public void setLastLoginAndOpenOvertake(PlayerJoinEvent event) {
        if(AdvancedRegionMarket.getEnableAutoReset() || AdvancedRegionMarket.getEnableTakeOver()){
            try{
                ResultSet rs = AdvancedRegionMarket.getStmt().executeQuery("SELECT * FROM `" + AdvancedRegionMarket.getSqlPrefix() + "lastlogin` WHERE `uuid` = '" + event.getPlayer().getUniqueId().toString() + "'");

                if(rs.next()){
                    AdvancedRegionMarket.getStmt().executeUpdate("UPDATE `" + AdvancedRegionMarket.getSqlPrefix() + "lastlogin` SET `lastlogin` = CURRENT_TIMESTAMP WHERE `uuid` = '" + event.getPlayer().getUniqueId().toString() + "'");
                } else {
                    AdvancedRegionMarket.getStmt().executeUpdate("INSERT INTO `" + AdvancedRegionMarket.getSqlPrefix() + "lastlogin` (`uuid`, `lastlogin`) VALUES ('" + event.getPlayer().getUniqueId().toString() + "', CURRENT_TIMESTAMP)");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(AdvancedRegionMarket.getEnableTakeOver()){
            Plugin plugin = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket");
            Player player = event.getPlayer();
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    ARMListener.doOvertakeCheck(player);
                }
            }, 40L);
        }

        if(RentRegion.isSendExpirationWarning()) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket");
            Player player = event.getPlayer();
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    RentRegion.sendExpirationWarnings(player);
                }
            }, 40L);
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        SellPreset.removePreset(event.getPlayer());
        RentPreset.removePreset(event.getPlayer());
        ContractPreset.removePreset(event.getPlayer());
        SubRegionCreator.removeSubRegioncreator(event.getPlayer());
    }

    public static void doOvertakeCheck(Player player) {
        GregorianCalendar comparedate = new GregorianCalendar();
        comparedate.add(Calendar.DAY_OF_MONTH, (-1 * AdvancedRegionMarket.getTakeoverAfter()));
        Date convertdate = new Date();
        convertdate.setTime(comparedate.getTimeInMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String compareTime = sdf.format(convertdate);


        try {
            ResultSet rs = AdvancedRegionMarket.getStmt().executeQuery("SELECT * FROM `" + AdvancedRegionMarket.getSqlPrefix() + "lastlogin` WHERE `lastlogin` < '" + compareTime + "'");

            List<Region> overtake = new LinkedList<>();
            while (rs.next()){
                List<Region> regions = net.alex9849.arm.regions.RegionManager.getRegionsByOwner(UUID.fromString(rs.getString("uuid")));

                for(int i = 0; i < regions.size(); i++){
                    if(regions.get(i).getAutoreset()){
                        if(regions.get(i).getRegion().hasMember(player.getUniqueId())){
                            overtake.add(regions.get(i));
                        }
                    }
                }
            }
            if(overtake.size() != 0){
                Gui.openOvertakeGUI(player, overtake);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void buyregion(PlayerInteractEvent event) throws InputException {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.SIGN || event.getClickedBlock().getType() == Material.WALL_SIGN) {
                Sign sign = (Sign) event.getClickedBlock().getState();

                Region region = net.alex9849.arm.regions.RegionManager.getRegion(sign);
                if(region == null) {
                    return;
                }
                region.buy(event.getPlayer());
            }
        }
    }

    private void setSubregionMark(PlayerInteractEvent event) throws InputException {
        Player player = event.getPlayer();
        if(Permission.hasAnySubregionCreatePermission(player)) {
            if(event.getItem() == null) {
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
                                if((subRegionCreator.getParentRegion().getRegion().getId().equals(wgRegion.getId())) && (subRegionCreator.getParentRegion().getRegionworld().equalsIgnoreCase(event.getClickedBlock().getWorld().getName()))) {
                                    if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                                        subRegionCreator.setPos2(event.getClickedBlock().getLocation());
                                        player.sendMessage("Second position set!");
                                    } else if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
                                        subRegionCreator.setPos1(event.getClickedBlock().getLocation());
                                        player.sendMessage("First position set!");
                                    }
                                    return;
                                } else {
                                    Region region = net.alex9849.arm.regions.RegionManager.getRegion(wgRegion, event.getClickedBlock().getWorld());
                                    if(region == null) {
                                        throw new InputException(player, "Region not registred");
                                    }
                                    if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                                        player.sendMessage("Mark in other Region. Removing old mark");
                                        subRegionCreator = new SubRegionCreator(region, player);
                                        subRegionCreator.setPos2(event.getClickedBlock().getLocation());
                                        player.sendMessage("Second position set!");
                                    } else if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
                                        player.sendMessage("Mark in other Region. Removing old mark");
                                        subRegionCreator = new SubRegionCreator(region, player);
                                        subRegionCreator.setPos1(event.getClickedBlock().getLocation());
                                        player.sendMessage("First position set!");
                                    }
                                    return;
                                }
                            } else {
                                Region region = net.alex9849.arm.regions.RegionManager.getRegion(wgRegion, event.getClickedBlock().getWorld());
                                if(region == null) {
                                    throw new InputException(player, "Region not registred");
                                }
                                if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                                    subRegionCreator = new SubRegionCreator(region, player);
                                    subRegionCreator.setPos2(event.getClickedBlock().getLocation());
                                    player.sendMessage("Second position set!");
                                } else if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
                                    subRegionCreator = new SubRegionCreator(region, player);
                                    subRegionCreator.setPos1(event.getClickedBlock().getLocation());
                                    player.sendMessage("First position set!");
                                }
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

}
