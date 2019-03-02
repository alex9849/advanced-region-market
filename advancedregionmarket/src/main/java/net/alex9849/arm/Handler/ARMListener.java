package net.alex9849.arm.Handler;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.ArmSettings;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.Preseter.*;
import net.alex9849.arm.Preseter.presets.*;
import net.alex9849.arm.SubRegions.SubRegionCreator;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.exceptions.InputException;
import net.alex9849.arm.regions.*;
import net.alex9849.arm.gui.Gui;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.ContractPrice;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.arm.regions.price.RentPrice;
import net.alex9849.inter.WGRegion;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
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
            Boolean userResettable = true;
            EntityLimitGroup entityLimitGroup = EntityLimitGroup.DEFAULT;
            int allowedSubregions = 0;

            if(sign.getLine(0).equalsIgnoreCase("[ARM-Sell]")){
                SellPreset preset = (SellPreset) ActivePresetManager.getPreset(sign.getPlayer(), PresetType.SELLPRESET);
                if(preset != null) {
                    regionkind = preset.getRegionKind();
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
                SellRegion addRegion = new SellRegion(region, regionWorld, sellsign, price, false, autoReset, isHotel, doBlockReset, regionkind, null,1, userResettable, new ArrayList<Region>(), allowedSubregions, entityLimitGroup, new HashMap<>(), 0);
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

                RentPreset preset = (RentPreset) ActivePresetManager.getPreset(sign.getPlayer(), PresetType.RENTPRESET);
                if(preset != null) {
                    regionkind = preset.getRegionKind();
                    autoReset = preset.isAutoReset();
                    isHotel = preset.isHotel();
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

                RentRegion addRegion = new RentRegion(region, regionWorld, sellsign, price, false, autoReset, isHotel, doBlockReset, regionkind, null,
                        1, userResettable,1, new ArrayList<Region>(), allowedSubregions, entityLimitGroup, new HashMap<>(), 0);
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

                ContractPreset preset = (ContractPreset) ActivePresetManager.getPreset(sign.getPlayer(), PresetType.CONTRACTPRESET);
                if(preset != null) {
                    regionkind = preset.getRegionKind();
                    autoReset = preset.isAutoReset();
                    isHotel = preset.isHotel();
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

                ContractRegion addRegion = new ContractRegion(region, regionWorld, sellsign, price, false, autoReset, isHotel, doBlockReset, regionkind, null,
                        1, userResettable, 1, false, new ArrayList<Region>(), allowedSubregions, entityLimitGroup, new HashMap<>(), 0);
                addRegion.createSchematic();
                net.alex9849.arm.regions.RegionManager.addRegion(addRegion);
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
            if ((block.getBlock().getType() != Material.SIGN) && (block.getBlock().getType() != Material.WALL_SIGN)) {
                return;
            }
            Region region = net.alex9849.arm.regions.RegionManager.getRegion((Sign) block.getBlock().getState());
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
    public void interactEvent(PlayerInteractEvent event){
        try {
            this.buyregion(event);
            this.setSubregionMark(event);
            this.sellback(event);
            this.infoClick(event);
        } catch (InputException inputException) {
            inputException.sendMessages(Messages.PREFIX);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void addBuiltBlock(BlockPlaceEvent event){
        if(event.isCancelled()) {
            return;
        }
        List<Region> locRegions = net.alex9849.arm.regions.RegionManager.getRegionsByLocation(event.getBlock().getLocation());

        for(Region region : locRegions) {
            if(region.isHotel()) {
                if(region.isSold()) {
                    region.addBuiltBlock(event.getBlock().getLocation());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void breakBlock(BlockBreakEvent event) {
        if(event.isCancelled()) {
            return;
        }
        this.breakblockCheckHotel(event);
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
    public void checkAllowEntitySpawn(EntitySpawnEvent event) {
        if(event.isCancelled()) {
            return;
        }
        if(event.getEntityType() == EntityType.PLAYER) {
            return;
        }

        List<Region> regions = RegionManager.getRegionsByLocation(event.getLocation());

        for(Region region : regions) {
            if(region.getEntityLimitGroup().isLimitReached(region, event.getEntityType(), region.getExtraEntityAmount(event.getEntityType()))) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void vehicleSpawnEvent(VehicleCreateEvent event) {
        if(event.isCancelled()) {
            return;
        }
        List<Region> regions = RegionManager.getRegionsByLocation(event.getVehicle().getLocation());

        for(Region region : regions) {
            if(region.getEntityLimitGroup().isLimitReached(region, event.getVehicle().getType(), region.getExtraEntityAmount(event.getVehicle().getType()))) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void setLastLoginAndOpenOvertake(PlayerJoinEvent event) {
        if(ArmSettings.isEnableAutoReset() || ArmSettings.isEnableTakeOver()){
            try{
                ResultSet rs = ArmSettings.getStmt().executeQuery("SELECT * FROM `" + ArmSettings.getSqlPrefix() + "lastlogin` WHERE `uuid` = '" + event.getPlayer().getUniqueId().toString() + "'");

                if(rs.next()){
                    ArmSettings.getStmt().executeUpdate("UPDATE `" + ArmSettings.getSqlPrefix() + "lastlogin` SET `lastlogin` = CURRENT_TIMESTAMP WHERE `uuid` = '" + event.getPlayer().getUniqueId().toString() + "'");
                } else {
                    ArmSettings.getStmt().executeUpdate("INSERT INTO `" + ArmSettings.getSqlPrefix() + "lastlogin` (`uuid`, `lastlogin`) VALUES ('" + event.getPlayer().getUniqueId().toString() + "', CURRENT_TIMESTAMP)");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(ArmSettings.isEnableTakeOver()){
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
        ActivePresetManager.deletePreset(event.getPlayer());
        SubRegionCreator.removeSubRegioncreator(event.getPlayer());
    }

    public static void doOvertakeCheck(Player player) {
        GregorianCalendar comparedate = new GregorianCalendar();
        comparedate.add(Calendar.DAY_OF_MONTH, (-1 * ArmSettings.getTakeoverAfter()));
        Date convertdate = new Date();
        convertdate.setTime(comparedate.getTimeInMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String compareTime = sdf.format(convertdate);


        try {
            ResultSet rs = ArmSettings.getStmt().executeQuery("SELECT * FROM `" + ArmSettings.getSqlPrefix() + "lastlogin` WHERE `lastlogin` < '" + compareTime + "'");

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
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if(event.getPlayer().isSneaking()) {
            return;
        }
        if (event.getClickedBlock().getType() == Material.SIGN || event.getClickedBlock().getType() == Material.WALL_SIGN) {
            Sign sign = (Sign) event.getClickedBlock().getState();

            Region region = net.alex9849.arm.regions.RegionManager.getRegion(sign);
            if(region == null) {
                return;
            }
            region.buy(event.getPlayer());
        }
    }

    private void sellback(PlayerInteractEvent event) throws InputException {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if(!event.getPlayer().isSneaking()) {
            return;
        }
        if (event.getClickedBlock().getType() == Material.SIGN || event.getClickedBlock().getType() == Material.WALL_SIGN) {
            Sign sign = (Sign) event.getClickedBlock().getState();

            Region region = net.alex9849.arm.regions.RegionManager.getRegion(sign);
            if(region == null) {
                return;
            }

            if(!event.getPlayer().hasPermission(Permission.MEMBER_SELLBACK)) {
                return;
            }

            if(!region.getRegion().hasOwner(event.getPlayer().getUniqueId())) {
                throw new InputException(event.getPlayer(), Messages.REGION_NOT_OWN);
            }

            String confirmQuestion = region.getConvertedMessage(Messages.SELLBACK_WARNING);
            event.getPlayer().sendMessage(Messages.PREFIX + confirmQuestion);
            Gui.openSellWarning(event.getPlayer(), region, null);
        }
    }

    private void infoClick(PlayerInteractEvent event) throws InputException {
        if(event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        if (event.getClickedBlock().getType() == Material.SIGN || event.getClickedBlock().getType() == Material.WALL_SIGN) {
            Sign sign = (Sign) event.getClickedBlock().getState();

            Region region = net.alex9849.arm.regions.RegionManager.getRegion(sign);
            if(region == null) {
                return;
            }

            if(!event.getPlayer().hasPermission(Permission.MEMBER_INFO)) {
                return;
            }

            region.regionInfo(event.getPlayer());
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
                                if((subRegionCreator.getParentRegion().getRegion().getId().equals(wgRegion.getId())) && (subRegionCreator.getParentRegion().getRegionworld().getName().equals(event.getClickedBlock().getWorld().getName()))) {
                                    if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                                        subRegionCreator.setPos2(event.getClickedBlock().getLocation());
                                        event.setCancelled(true);
                                        player.sendMessage(Messages.SECOND_POSITION_SET);
                                    } else if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
                                        subRegionCreator.setPos1(event.getClickedBlock().getLocation());
                                        event.setCancelled(true);
                                        player.sendMessage(Messages.FIRST_POSITION_SET);
                                    }
                                    return;
                                } else {
                                    Region region = net.alex9849.arm.regions.RegionManager.getRegion(wgRegion);
                                    if(region == null) {
                                        throw new InputException(player, Messages.REGION_NOT_REGISTRED);
                                    }
                                    if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                                        player.sendMessage(Messages.MARK_IN_OTHER_REGION_REMOVING);
                                        subRegionCreator = new SubRegionCreator(region, player);
                                        subRegionCreator.setPos2(event.getClickedBlock().getLocation());
                                        event.setCancelled(true);
                                        player.sendMessage(Messages.SECOND_POSITION_SET);
                                    } else if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
                                        player.sendMessage(Messages.MARK_IN_OTHER_REGION_REMOVING);
                                        subRegionCreator = new SubRegionCreator(region, player);
                                        subRegionCreator.setPos1(event.getClickedBlock().getLocation());
                                        event.setCancelled(true);
                                        player.sendMessage(Messages.FIRST_POSITION_SET);
                                    }
                                    return;
                                }
                            } else {
                                Region region = net.alex9849.arm.regions.RegionManager.getRegion(wgRegion);
                                if(region == null) {
                                    throw new InputException(player, Messages.REGION_NOT_REGISTRED);
                                }
                                if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                                    subRegionCreator = new SubRegionCreator(region, player);
                                    subRegionCreator.setPos2(event.getClickedBlock().getLocation());
                                    event.setCancelled(true);
                                    player.sendMessage(Messages.SECOND_POSITION_SET);
                                } else if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
                                    subRegionCreator = new SubRegionCreator(region, player);
                                    subRegionCreator.setPos1(event.getClickedBlock().getLocation());
                                    event.setCancelled(true);
                                    player.sendMessage(Messages.FIRST_POSITION_SET);
                                }
                                return;
                            }
                        }
                    }
                    throw new InputException(event.getPlayer(), Messages.REGION_NOT_OWN);
                }
            }
        }
    }

    private void breakblockCheckHotel(BlockBreakEvent event) {
        if(event.isCancelled()) {
            return;
        }
        if(event.getPlayer().hasPermission(Permission.ADMIN_BUILDEVERYWHERE)){
            return;
        }
        try {
            List<Region> locRegions = net.alex9849.arm.regions.RegionManager.getRegionsByLocation(event.getBlock().getLocation());

            for(Region region : locRegions) {
                if(region.isHotel()) {
                    if(!region.allowBlockBreak(event.getBlock().getLocation())) {
                        event.setCancelled(true);
                        throw new InputException(event.getPlayer(), Messages.REGION_ERROR_CAN_NOT_BUILD_HERE);
                    }
                }
            }
        } catch (InputException inputException) {
            inputException.sendMessages(Messages.PREFIX);
        }
    }
}
