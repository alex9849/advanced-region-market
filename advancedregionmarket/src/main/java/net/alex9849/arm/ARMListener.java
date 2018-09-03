package net.alex9849.arm;

import net.alex9849.arm.Preseter.ContractPreset;
import net.alex9849.arm.Preseter.Preset;
import net.alex9849.arm.Preseter.RentPreset;
import net.alex9849.arm.Preseter.SellPreset;
import net.alex9849.arm.regions.*;
import net.alex9849.arm.regions.*;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.alex9849.arm.gui.Gui;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
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


    @EventHandler
    public void addSign(SignChangeEvent sign) {
        if(sign.isCancelled()) {
            return;
        }

        RegionKind regionkind = RegionKind.DEFAULT;
        Boolean autoReset = true;
        Boolean isHotel = false;
        Boolean doBlockReset = true;

        if(sign.getLine(0).equalsIgnoreCase("[ARM-Sell]")){

            if(SellPreset.hasPreset(sign.getPlayer())) {
                Preset preset = SellPreset.getPreset(sign.getPlayer());
                regionkind = preset.getRegionKind();
                autoReset = preset.isAutoReset();
                isHotel = preset.isHotel();
                doBlockReset = preset.isDoBlockReset();
                sign.getPlayer().sendMessage(Messages.PREFIX + "Applying preset...");
            }

            if(!sign.getPlayer().hasPermission(Permission.ADMIN_CREATE_SELL)){
                sign.getPlayer().sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
                return;
            }

            String worldname = sign.getLine(1);
            String regionname = sign.getLine(2);

            if (sign.getLine(1).equals("")){
                worldname = sign.getBlock().getLocation().getWorld().getName();
            } else {
                if (Bukkit.getWorld(worldname) == null) {
                    sign.getPlayer().sendMessage(Messages.PREFIX + Messages.WORLD_DOES_NOT_EXIST);
                    return;
                }
            }

            if (Main.getWorldGuardInterface().getRegionManager(Bukkit.getWorld(worldname), Main.getWorldGuard()).getRegion(regionname) == null) {
                sign.getPlayer().sendMessage(Messages.PREFIX + Messages.REGION_DOES_NOT_EXIST);
                return;
            }
            ProtectedRegion region = Main.getWorldGuardInterface().getRegionManager(Bukkit.getWorld(worldname), Main.getWorldGuard()).getRegion(regionname);
            Double price = null;

            if(sign.getLine(3).equals("")){
                if(SellPreset.hasPreset(sign.getPlayer())){
                    if(SellPreset.getPreset(sign.getPlayer()).hasPrice()){
                        price = SellPreset.getPreset(sign.getPlayer()).getPrice();
                    }
                }
            }

            if(price == null) {
                try{
                    price = Region.calculatePrice(region, sign.getLine(3));
                } catch (IllegalArgumentException e){
                    sign.getPlayer().sendMessage(Messages.PREFIX + Messages.PLEASE_USE_A_NUMBER_AS_PRICE + " or an AutoPrice");
                    return;
                }
            }

            if(price < 0) {
                sign.getPlayer().sendMessage(Messages.PREFIX + ChatColor.DARK_RED + "Price must be positive!");
                return;
            }

            Region searchregion = Region.searchRegionbyNameAndWorld(regionname, worldname);
            if(searchregion != null) {
                if(!(searchregion instanceof SellRegion)) {
                    sign.getPlayer().sendMessage(Messages.PREFIX + "Region already registered as a non-sellregion");
                    return;
                }
                searchregion.addSign(sign.getBlock().getLocation());
                sign.setCancelled(true);
                sign.getPlayer().sendMessage(Messages.PREFIX + Messages.SIGN_ADDED_TO_REGION);
                return;
            }


            LinkedList<Sign> sellsign = new LinkedList<Sign>();
            sellsign.add((Sign) sign.getBlock().getState());
            Region.getRegionList().add(new SellRegion(region, worldname, sellsign, price, false, autoReset, isHotel, doBlockReset, regionkind, null,1,true));
            sign.getPlayer().sendMessage(Messages.PREFIX + Messages.REGION_ADDED_TO_ARM);
            sign.setCancelled(true);
        }


        if(sign.getLine(0).equalsIgnoreCase("[ARM-Rent]")){
            if(!sign.getPlayer().hasPermission(Permission.ADMIN_CREATE_RENT)){
                sign.getPlayer().sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
                return;
            }

            if(RentPreset.hasPreset(sign.getPlayer())) {
                Preset preset = RentPreset.getPreset(sign.getPlayer());
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
                    sign.getPlayer().sendMessage(Messages.PREFIX + Messages.WORLD_DOES_NOT_EXIST);
                    return;
                }
            }
            if (Main.getWorldGuardInterface().getRegionManager(Bukkit.getWorld(worldname), Main.getWorldGuard()).getRegion(regionname) == null) {
                sign.getPlayer().sendMessage(Messages.PREFIX + Messages.REGION_DOES_NOT_EXIST);
                return;
            }
            ProtectedRegion region = Main.getWorldGuardInterface().getRegionManager(Bukkit.getWorld(worldname), Main.getWorldGuard()).getRegion(regionname);

            double price = 0;
            long extendPerClick = 0;
            long maxRentTime = 0;
            Boolean priceready = false;

            if(sign.getLine(3).equals("")) {
                if(RentPreset.hasPreset(sign.getPlayer())) {
                    RentPreset preset = RentPreset.getPreset(sign.getPlayer());
                    if(preset.hasPrice() && preset.hasExtendPerClick() && preset.hasMaxRentTime()) {
                        price = preset.getPrice();
                        extendPerClick = preset.getExtendPerClick();
                        maxRentTime = preset.getMaxRentTime();
                        priceready = true;
                    } else {
                        sign.getPlayer().sendMessage(Messages.PREFIX + ChatColor.RED + "Your preset needs to have an option at Price, MaxRentTime and ExtendPerClick to take affect!");
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

            Region searchregion = Region.searchRegionbyNameAndWorld(regionname, worldname);
            if(searchregion != null) {
                if(!(searchregion instanceof RentRegion)) {
                    sign.getPlayer().sendMessage(Messages.PREFIX + "Region already registered as a non-rentregion");
                    return;
                }
                searchregion.addSign(sign.getBlock().getLocation());
                sign.setCancelled(true);
                sign.getPlayer().sendMessage(Messages.PREFIX + Messages.SIGN_ADDED_TO_REGION);
                return;
            }

            LinkedList<Sign> sellsign = new LinkedList<Sign>();
            sellsign.add((Sign) sign.getBlock().getState());
            Region.getRegionList().add(new RentRegion(region, worldname, sellsign, price, false, autoReset, isHotel, doBlockReset, regionkind, null,
                    1,1, maxRentTime, extendPerClick, true));
            sign.getPlayer().sendMessage(Messages.PREFIX + Messages.REGION_ADDED_TO_ARM);
            sign.setCancelled(true);
        }
        if(sign.getLine(0).equalsIgnoreCase("[ARM-Contract]")) {
            if(!sign.getPlayer().hasPermission(Permission.ADMIN_CREATE_RENT)){
                sign.getPlayer().sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
                return;
            }

            if(ContractPreset.hasPreset(sign.getPlayer())) {
                Preset preset = ContractPreset.getPreset(sign.getPlayer());
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
                    sign.getPlayer().sendMessage(Messages.PREFIX + Messages.WORLD_DOES_NOT_EXIST);
                    return;
                }
            }
            if (Main.getWorldGuardInterface().getRegionManager(Bukkit.getWorld(worldname), Main.getWorldGuard()).getRegion(regionname) == null) {
                sign.getPlayer().sendMessage(Messages.PREFIX + Messages.REGION_DOES_NOT_EXIST);
                return;
            }
            ProtectedRegion region = Main.getWorldGuardInterface().getRegionManager(Bukkit.getWorld(worldname), Main.getWorldGuard()).getRegion(regionname);

            double price = 0;
            long extendtime = 0;
            Boolean priceready = false;

            if(sign.getLine(3).equals("")) {
                if(ContractPreset.hasPreset(sign.getPlayer())) {
                    ContractPreset preset = ContractPreset.getPreset(sign.getPlayer());
                    if(preset.hasPrice() && preset.hasExtend()) {
                        price = preset.getPrice();
                        extendtime = preset.getExtend();
                        priceready = true;
                    } else {
                        sign.getPlayer().sendMessage(Messages.PREFIX + ChatColor.RED + "Your preset needs to have an option at Price and Extend to take affect!");
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


            Region searchregion = Region.searchRegionbyNameAndWorld(regionname, worldname);
            if(searchregion != null) {
                if(!(searchregion instanceof ContractRegion)) {
                    sign.getPlayer().sendMessage(Messages.PREFIX + "Region already registered as a non-contractregion");
                    return;
                }
                searchregion.addSign(sign.getBlock().getLocation());
                sign.setCancelled(true);
                sign.getPlayer().sendMessage(Messages.PREFIX + Messages.SIGN_ADDED_TO_REGION);
                return;
            }
            LinkedList<Sign> sellsign = new LinkedList<Sign>();
            sellsign.add((Sign) sign.getBlock().getState());
            Region.getRegionList().add(new ContractRegion(region, worldname, sellsign, price, false, autoReset, isHotel, doBlockReset, regionkind, null,
                    1, extendtime, 1, false, true));
            sign.getPlayer().sendMessage(Messages.PREFIX + Messages.REGION_ADDED_TO_ARM);
            sign.setCancelled(true);
        }
    }

    @EventHandler
    public void removeSign(BlockBreakEvent block) {
        if(block.isCancelled()){
            return;
        }

        if ((block.getBlock().getType() != Material.SIGN) && (block.getBlock().getType() != Material.WALL_SIGN)) {
            return;
        }

        if(!Region.checkIfSignExists((Sign) block.getBlock().getState())){
            return;
        }

        if(!block.getPlayer().hasPermission(Permission.ADMIN_REMOVE)) {
            block.getPlayer().sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
            block.setCancelled(true);
            return;
        }
        double loc_x = block.getBlock().getLocation().getX();
        double loc_y = block.getBlock().getLocation().getY();
        double loc_z = block.getBlock().getLocation().getZ();
        Location loc = new Location(block.getBlock().getWorld(), loc_x, loc_y, loc_z);

        for (int i = 0; i < Region.getRegionList().size(); i++) {
            if(Region.getRegionList().get(i).removeSign(loc, block.getPlayer())){
                return;
            }
        }
    }

    @EventHandler
    public void buyRegion(PlayerInteractEvent event){
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.SIGN || event.getClickedBlock().getType() == Material.WALL_SIGN) {
                Sign sign = (Sign) event.getClickedBlock().getState();

                for(int i = 0; i < Region.getRegionList().size(); i++){
                    if(Region.getRegionList().get(i).hasSign(sign)){
                        Region.getRegionList().get(i).buy(event.getPlayer());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void addBuiltBlock(BlockPlaceEvent event){
        if(event.isCancelled()) {
            return;
        }
        for(int i = 0; i < Region.getRegionList().size(); i++){
            if(Main.getWorldGuardInterface().canBuild(event.getPlayer(), event.getBlock().getLocation(), Main.getWorldGuard())){
                int x = event.getBlock().getLocation().getBlockX();
                int y = event.getBlock().getLocation().getBlockY();
                int z = event.getBlock().getLocation().getBlockZ();
                if (Region.getRegionList().get(i).getRegion().contains(x, y, z) && Region.getRegionList().get(i).getRegionworld().equals(event.getPlayer().getLocation().getWorld().getName())) {
                    if(Region.getRegionList().get(i).isHotel()){
                        if(Region.getRegionList().get(i).isSold()){
                            if(Main.getWorldGuardInterface().canBuild(event.getPlayer(), event.getBlock().getLocation(), Main.getWorldGuard())) {
                                Region.getRegionList().get(i).addBuiltBlock(event.getBlock().getLocation());
                            }
                        }
                    }
                    return;
                }
            }
        }

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void breakBlock(BlockBreakEvent event) {
        if(event.isCancelled()) {
            return;
        }
        if(!event.getPlayer().hasPermission(Permission.ADMIN_BUILDEVERYWHERE)){
            if(Main.getWorldGuardInterface().canBuild(event.getPlayer(), event.getBlock().getLocation(), Main.getWorldGuard())){
                for(int i = 0; i < Region.getRegionList().size(); i++) {
                    if(Region.getRegionList().get(i).isHotel()){
                        int x = event.getBlock().getLocation().getBlockX();
                        int y = event.getBlock().getLocation().getBlockY();
                        int z = event.getBlock().getLocation().getBlockZ();
                        if(Region.getRegionList().get(i).getRegion().contains(x, y, z) && Region.getRegionList().get(i).getRegionworld().equals(event.getPlayer().getLocation().getWorld().getName())) {
                            if(Region.getRegionList().get(i).allowBlockBreak(event.getBlock().getLocation())){
                                return;
                            } else {
                                event.setCancelled(true);
                                event.getPlayer().sendMessage(Messages.PREFIX + Messages.REGION_ERROR_CAN_NOT_BUILD_HERE);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void protectSignPhysics(BlockPhysicsEvent sign) {
        if (sign.getBlock().getType() == Material.SIGN || sign.getBlock().getType() == Material.WALL_SIGN){
            for (int i = 0; i < Region.getRegionList().size() ; i++){
                if(Region.getRegionList().get(i).hasSign((Sign) sign.getBlock().getState())){
                    sign.setCancelled(true);
                    return;
                }
            }
        }
    }



    @EventHandler
    public void setLastLoginAndOpenOvertake(PlayerJoinEvent event) {
        if(Main.getEnableAutoReset() || Main.getEnableTakeOver()){
            try{
                ResultSet rs = Main.getStmt().executeQuery("SELECT * FROM `" + Main.getSqlPrefix() + "lastlogin` WHERE `uuid` = '" + event.getPlayer().getUniqueId().toString() + "'");

                if(rs.next()){
                    Main.getStmt().executeUpdate("UPDATE `" + Main.getSqlPrefix() + "lastlogin` SET `lastlogin` = CURRENT_TIMESTAMP WHERE `uuid` = '" + event.getPlayer().getUniqueId().toString() + "'");
                } else {
                    Main.getStmt().executeUpdate("INSERT INTO `" + Main.getSqlPrefix() + "lastlogin` (`uuid`, `lastlogin`) VALUES ('" + event.getPlayer().getUniqueId().toString() + "', CURRENT_TIMESTAMP)");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(Main.getEnableTakeOver()){
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
        if(SellPreset.hasPreset(event.getPlayer())){
            SellPreset.removePreset(event.getPlayer());
        }
        if(RentPreset.hasPreset(event.getPlayer())) {
            RentPreset.removePreset(event.getPlayer());
        }
    }

    public static void doOvertakeCheck(Player player) {
        GregorianCalendar comparedate = new GregorianCalendar();
        comparedate.add(Calendar.DAY_OF_MONTH, (-1 * Main.getTakeoverAfter()));
        Date convertdate = new Date();
        convertdate.setTime(comparedate.getTimeInMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String compareTime = sdf.format(convertdate);


        try {
            ResultSet rs = Main.getStmt().executeQuery("SELECT * FROM `" + Main.getSqlPrefix() + "lastlogin` WHERE `lastlogin` < '" + compareTime + "'");

            List<Region> overtake = new LinkedList<>();
            while (rs.next()){
                List<Region> regions = Region.getRegionsByOwner(UUID.fromString(rs.getString("uuid")));

                for(int i = 0; i < regions.size(); i++){
                    if(regions.get(i).getAutoreset()){
                        if(Main.getWorldGuardInterface().hasMember(player, regions.get(i).getRegion())){
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

}
