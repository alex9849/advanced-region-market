package net.alex9849.arm.regions;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.ArmSettings;
import net.alex9849.arm.Messages;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.minifeatures.teleporter.Teleporter;
import net.alex9849.inter.WGRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class RegionManager {
    private static YamlConfiguration regionsconf;
    private static List<Region> regionList = new ArrayList<>();

    public static void addRegion(Region region) {
        regionList.add(region);
        writeRegionToYamlObject(region);
        saveRegionsConf();
    }

    protected static void writeRegionToYamlObject(Region region) {
        regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId(), null);
        regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".price", region.getPrice());
        regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".sold", region.isSold());
        if(region.getRegionKind() == RegionKind.DEFAULT) {
            regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".kind", "default");
        } else {
            regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".kind", region.getRegionKind().getName());
        }
        regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".autoreset", region.isAutoreset());
        regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".lastreset", region.getLastreset());
        regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".isHotel", region.isHotel);
        regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".doBlockReset", region.isDoBlockReset());
        for(Region subregion : region.getSubregions()) {
            regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".subregions." + subregion.getRegion().getId() + ".price", subregion.getPrice());
            regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".subregions." + subregion.getRegion().getId() + ".sold", subregion.isSold());
            regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".subregions." + subregion.getRegion().getId() + ".isHotel", subregion.isHotel());
            List<String> subregionsigns = new ArrayList<>();
            for(Sign sign : subregion.getSellSigns()) {
                Location signloc = sign.getLocation();
                subregionsigns.add(signloc.getWorld().getName() + ";" + signloc.getX() + ";" + signloc.getY() + ";" + signloc.getZ());
            }
            regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".subregions." + subregion.getRegion().getId() + ".signs", subregionsigns);
            if(subregion instanceof SellRegion) {
                regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".subregions." + subregion.getRegion().getId() + ".regiontype", "sellregion");
            } else if (subregion instanceof RentRegion) {
                RentRegion rentRegion = (RentRegion) region;
                regionsconf.set("Regions." + rentRegion.getRegionworld().getName() + "." + rentRegion.getRegion().getId() + ".subregions." + subregion.getRegion().getId() + ".regiontype", "rentregion");
                regionsconf.set("Regions." + rentRegion.getRegionworld().getName() + "." + rentRegion.getRegion().getId() + ".subregions." + subregion.getRegion().getId() + ".payedTill", rentRegion.getPayedTill());
                regionsconf.set("Regions." + rentRegion.getRegionworld().getName() + "." + rentRegion.getRegion().getId() + ".subregions." + subregion.getRegion().getId() + ".maxRentTime", rentRegion.getMaxRentTime());
                regionsconf.set("Regions." + rentRegion.getRegionworld().getName() + "." + rentRegion.getRegion().getId() + ".subregions." + subregion.getRegion().getId() + ".rentExtendPerClick", rentRegion.getRentExtendPerClick());
            } else if (subregion instanceof ContractRegion) {
                ContractRegion contractRegion = (ContractRegion) region;
                regionsconf.set("Regions." + contractRegion.getRegionworld().getName() + "." + contractRegion.getRegion().getId() + ".subregions." + subregion.getRegion().getId() + ".regiontype", "contractregion");
                regionsconf.set("Regions." + contractRegion.getRegionworld().getName() + "." + contractRegion.getRegion().getId() + ".subregions." + subregion.getRegion().getId() + ".payedTill", contractRegion.getPayedTill());
                regionsconf.set("Regions." + contractRegion.getRegionworld().getName() + "." + contractRegion.getRegion().getId() + ".subregions." + subregion.getRegion().getId() + ".extendTime", contractRegion.getExtendTime());
                regionsconf.set("Regions." + contractRegion.getRegionworld().getName() + "." + contractRegion.getRegion().getId() + ".subregions." + subregion.getRegion().getId() + ".terminated", contractRegion.isTerminated());
            }
        }

        if(region.getTeleportLocation() != null) {
            String teleportloc = region.getTeleportLocation().getWorld().getName() + ";" + region.getTeleportLocation().getBlockX() + ";" +
                    region.getTeleportLocation().getBlockY() + ";" + region.getTeleportLocation().getBlockZ() + ";" +
                    region.getTeleportLocation().getPitch() + ";" + region.getTeleportLocation().getYaw();
            regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".teleportLoc", teleportloc);
        } else {
            regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".teleportLoc", null);
        }

        List<String> regionsigns = new ArrayList<>();
        for(Sign sign : region.getSellSigns()) {
            Location signloc = sign.getLocation();
            regionsigns.add(signloc.getWorld().getName() + ";" + signloc.getX() + ";" + signloc.getY() + ";" + signloc.getZ());
        }
        regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".signs", regionsigns);

        if(region instanceof SellRegion) {
            regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".regiontype", "sellregion");
        } else if (region instanceof RentRegion) {
            RentRegion rentRegion = (RentRegion) region;
            regionsconf.set("Regions." + rentRegion.getRegionworld().getName() + "." + rentRegion.getRegion().getId() + ".regiontype", "rentregion");
            regionsconf.set("Regions." + rentRegion.getRegionworld().getName() + "." + rentRegion.getRegion().getId() + ".payedTill", rentRegion.getPayedTill());
            regionsconf.set("Regions." + rentRegion.getRegionworld().getName() + "." + rentRegion.getRegion().getId() + ".maxRentTime", rentRegion.getMaxRentTime());
            regionsconf.set("Regions." + rentRegion.getRegionworld().getName() + "." + rentRegion.getRegion().getId() + ".rentExtendPerClick", rentRegion.getRentExtendPerClick());
        } else if (region instanceof ContractRegion) {
            ContractRegion contractRegion = (ContractRegion) region;
            regionsconf.set("Regions." + contractRegion.getRegionworld().getName() + "." + contractRegion.getRegion().getId() + ".regiontype", "contractregion");
            regionsconf.set("Regions." + contractRegion.getRegionworld().getName() + "." + contractRegion.getRegion().getId() + ".payedTill", contractRegion.getPayedTill());
            regionsconf.set("Regions." + contractRegion.getRegionworld().getName() + "." + contractRegion.getRegion().getId() + ".extendTime", contractRegion.getExtendTime());
            regionsconf.set("Regions." + contractRegion.getRegionworld().getName() + "." + contractRegion.getRegion().getId() + ".terminated", contractRegion.isTerminated());
        }
    }

    public static boolean removeRegion(Region region) {
        for(int i = 0; i < regionList.size(); i++) {
            if(regionList.get(i) == region) {
                regionList.remove(i);
                i--;
            } else {
                regionList.get(i).getSubregions().remove(region);
            }
        }
        writeRegionsToConfig();
        return true;
    }

    public static void writeRegionsToConfig() {
        regionsconf = new YamlConfiguration();
        for(Region region : regionList) {
            writeRegionToYamlObject(region);
        }
        saveRegionsConf();
    }

    private static List<Region> getRegionList() {
        return regionList;
    }

    public static void loadRegionsFromConfig() {
        if(RegionManager.getRegionsConf().get("Regions") != null) {
            LinkedList<String> worlds = new LinkedList<String>(RegionManager.getRegionsConf().getConfigurationSection("Regions").getKeys(false));
            if(worlds != null) {
                for(int y = 0; y < worlds.size(); y++) {
                    World regionWorld = Bukkit.getWorld(worlds.get(y));
                    if(regionWorld != null) {
                        if(RegionManager.getRegionsConf().get("Regions." + worlds.get(y)) != null) {
                            LinkedList<String> regions = new LinkedList<String>(RegionManager.getRegionsConf().getConfigurationSection("Regions." + worlds.get(y)).getKeys(false));
                            if(regions != null) {
                                for(int i = 0; i < regions.size(); i++){
                                    String regionworld = worlds.get(y);
                                    String regionname = regions.get(i);
                                    int price = RegionManager.getRegionsConf().getInt("Regions." + worlds.get(y) + "." + regions.get(i) + ".price");
                                    boolean sold = RegionManager.getRegionsConf().getBoolean("Regions." + worlds.get(y) + "." + regions.get(i) + ".sold");
                                    String kind = RegionManager.getRegionsConf().getString("Regions." + worlds.get(y) + "." + regions.get(i) + ".kind");
                                    boolean autoreset = RegionManager.getRegionsConf().getBoolean("Regions." + worlds.get(y) + "." + regions.get(i) + ".autoreset");
                                    String regiontype = RegionManager.getRegionsConf().getString("Regions." + worlds.get(y) + "." + regions.get(i) + ".regiontype");
                                    boolean allowonlynewblocks = RegionManager.getRegionsConf().getBoolean("Regions." + worlds.get(y) + "." + regions.get(i) + ".isHotel");
                                    boolean doBlockReset = RegionManager.getRegionsConf().getBoolean("Regions." + worlds.get(y) + "." + regions.get(i) + ".doBlockReset");
                                    long lastreset = RegionManager.getRegionsConf().getLong("Regions." + worlds.get(y) + "." + regions.get(i) + ".lastreset");
                                    String teleportLocString = RegionManager.getRegionsConf().getString("Regions." + worlds.get(y) + "." + regions.get(i) + ".teleportLoc");
                                    Location teleportLoc = null;
                                    if(teleportLocString != null) {
                                        String[] teleportLocarr = teleportLocString.split(";");
                                        World teleportLocWorld = Bukkit.getWorld(teleportLocarr[0]);
                                        int teleportLocBlockX = Integer.parseInt(teleportLocarr[1]);
                                        int teleportLocBlockY = Integer.parseInt(teleportLocarr[2]);
                                        int teleportLocBlockZ = Integer.parseInt(teleportLocarr[3]);
                                        float teleportLocPitch = Float.parseFloat(teleportLocarr[4]);
                                        float teleportLocYaw = Float.parseFloat(teleportLocarr[5]);
                                        teleportLoc = new Location(teleportLocWorld, teleportLocBlockX, teleportLocBlockY, teleportLocBlockZ);
                                        teleportLoc.setYaw(teleportLocYaw);
                                        teleportLoc.setPitch(teleportLocPitch);
                                    }
                                    RegionKind regionKind = RegionKind.DEFAULT;
                                    if(kind != null){
                                        RegionKind result = RegionKind.getRegionKind(kind);
                                        if(result != null){
                                            regionKind = result;
                                        }
                                    }
                                    WGRegion region = AdvancedRegionMarket.getWorldGuardInterface().getRegion(Bukkit.getWorld(regionworld), AdvancedRegionMarket.getWorldGuard(), regionname);

                                    if(region != null) {
                                        List<String> regionsignsloc = RegionManager.getRegionsConf().getStringList("Regions." + worlds.get(y) + "." + regions.get(i) + ".signs");
                                        List<Sign> regionsigns = new ArrayList<>();
                                        for(int j = 0; j < regionsignsloc.size(); j++) {
                                            String[] locsplit = regionsignsloc.get(j).split(";", 4);
                                            World world = Bukkit.getWorld(locsplit[0]);
                                            Double x = Double.parseDouble(locsplit[1]);
                                            Double yy = Double.parseDouble(locsplit[2]);
                                            Double z = Double.parseDouble(locsplit[3]);
                                            Location loc = new Location(world, x, yy, z);
                                            Location locminone = new Location(world, x, yy - 1, z);

                                            if ((loc.getBlock().getType() != Material.SIGN) && (loc.getBlock().getType() != Material.WALL_SIGN)){
                                                if(locminone.getBlock().getType() == Material.AIR || locminone.getBlock().getType() == Material.LAVA || locminone.getBlock().getType() == Material.WATER
                                                        || locminone.getBlock().getType() == Material.LAVA || locminone.getBlock().getType() == Material.WATER) {
                                                    locminone.getBlock().setType(Material.STONE);
                                                }
                                                loc.getBlock().setType(Material.SIGN);

                                            }

                                            regionsigns.add((Sign) loc.getBlock().getState());
                                        }
                                        List<Region> subregions = new ArrayList<>();
                                        if(RegionManager.getRegionsConf().getConfigurationSection("Regions." + worlds.get(y) + "." + regions.get(i) + ".subregions") != null) {
                                            LinkedList<String> subregionsection = new LinkedList<String>(RegionManager.getRegionsConf().getConfigurationSection("Regions." + worlds.get(y) + "." + regions.get(i) + ".subregions").getKeys(false));
                                            if(subregionsection != null) {
                                                for (String subregion : subregionsection) {
                                                    int subregPrice = RegionManager.getRegionsConf().getInt("Regions." + worlds.get(y) + "." + regions.get(i) + ".subregions." + subregion + ".price");
                                                    boolean subregIsSold = RegionManager.getRegionsConf().getBoolean("Regions." + worlds.get(y) + "." + regions.get(i) + ".subregions." + subregion + ".sold");
                                                    boolean subregIsHotel = RegionManager.getRegionsConf().getBoolean("Regions." + worlds.get(y) + "." + regions.get(i) + ".subregions." + subregion + ".isHotel");
                                                    String subregionRegiontype = RegionManager.getRegionsConf().getString("Regions." + worlds.get(y) + "." + regions.get(i) + ".subregions." + subregion + ".regiontype");

                                                    WGRegion subWGRegion = AdvancedRegionMarket.getWorldGuardInterface().getRegion(regionWorld, AdvancedRegionMarket.getWorldGuard(), subregion);

                                                    if(subWGRegion != null) {
                                                        List<String> subregionsignsloc = RegionManager.getRegionsConf().getStringList("Regions." + worlds.get(y) + "." + regions.get(i) + ".subregions." + subregion + ".signs");
                                                        List<Sign> subregionsigns = new ArrayList<>();
                                                        for(int j = 0; j < subregionsignsloc.size(); j++) {
                                                            String[] locsplit = subregionsignsloc.get(j).split(";", 4);
                                                            World world = Bukkit.getWorld(locsplit[0]);
                                                            Double x = Double.parseDouble(locsplit[1]);
                                                            Double yy = Double.parseDouble(locsplit[2]);
                                                            Double z = Double.parseDouble(locsplit[3]);
                                                            Location loc = new Location(world, x, yy, z);
                                                            Location locminone = new Location(world, x, yy - 1, z);

                                                            if ((loc.getBlock().getType() != Material.SIGN) && (loc.getBlock().getType() != Material.WALL_SIGN)){
                                                                if(locminone.getBlock().getType() == Material.AIR || locminone.getBlock().getType() == Material.LAVA || locminone.getBlock().getType() == Material.WATER
                                                                        || locminone.getBlock().getType() == Material.LAVA || locminone.getBlock().getType() == Material.WATER) {
                                                                    locminone.getBlock().setType(Material.STONE);
                                                                }
                                                                loc.getBlock().setType(Material.SIGN);

                                                            }

                                                            subregionsigns.add((Sign) loc.getBlock().getState());

                                                            if (subregionRegiontype.equalsIgnoreCase("rentregion")){
                                                                long subregpayedtill = RegionManager.getRegionsConf().getLong("Regions." + worlds.get(y) + "." + regions.get(i) + ".subregions." + subregion + ".payedTill");
                                                                long subregmaxRentTime = RegionManager.getRegionsConf().getLong("Regions." + worlds.get(y) + "." + regions.get(i) + ".subregions." + subregion + ".maxRentTime");
                                                                long subregrentExtendPerClick = RegionManager.getRegionsConf().getLong("Regions." + worlds.get(y) + "." + regions.get(i) + ".subregions." + subregion + ".rentExtendPerClick");
                                                                Region armregion = new RentRegion(subWGRegion, regionWorld, subregionsigns, subregPrice, subregIsSold, ArmSettings.isSubregionAutoReset(), subregIsHotel, ArmSettings.isSubregionBlockReset(), RegionKind.SUBREGION, null,
                                                                        1, ArmSettings.isAllowSubRegionUserReset(), subregpayedtill, subregmaxRentTime, subregrentExtendPerClick, new ArrayList<Region>(), false, true);
                                                                subregions.add(armregion);
                                                            } else if (subregionRegiontype.equalsIgnoreCase("sellregion")){
                                                                Region armregion = new SellRegion(subWGRegion, regionWorld, subregionsigns, subregPrice, subregIsSold, ArmSettings.isSubregionAutoReset(), subregIsHotel, ArmSettings.isSubregionBlockReset(), RegionKind.SUBREGION, null, 1, ArmSettings.isAllowSubRegionUserReset(), new ArrayList<Region>(), false, true);
                                                                subregions.add(armregion);
                                                            } else if (subregionRegiontype.equalsIgnoreCase("contractregion")) {
                                                                long subregpayedtill = RegionManager.getRegionsConf().getLong("Regions." + worlds.get(y) + "." + regions.get(i) + ".subregions." + subregion + ".payedTill");
                                                                long subregextendTime = RegionManager.getRegionsConf().getLong("Regions." + worlds.get(y) + "." + regions.get(i) + ".subregions." + subregion + ".extendTime");
                                                                Boolean subregterminated = RegionManager.getRegionsConf().getBoolean("Regions." + worlds.get(y) + "." + regions.get(i) + ".subregions." + subregion + ".terminated");
                                                                Region armregion = new ContractRegion(subWGRegion, regionWorld, subregionsigns, subregPrice, subregIsSold, ArmSettings.isSubregionAutoReset(), subregIsHotel, ArmSettings.isSubregionBlockReset(), RegionKind.SUBREGION, null, 1, ArmSettings.isAllowSubRegionUserReset(), subregextendTime, subregpayedtill, subregterminated, new ArrayList<Region>(), false, true);
                                                                subregions.add(armregion);
                                                            }



                                                        }
                                                    }


                                                }
                                            }
                                        }

                                        if (regiontype.equalsIgnoreCase("rentregion")){
                                            long payedtill = RegionManager.getRegionsConf().getLong("Regions." + worlds.get(y) + "." + regions.get(i) + ".payedTill");
                                            long maxRentTime = RegionManager.getRegionsConf().getLong("Regions." + worlds.get(y) + "." + regions.get(i) + ".maxRentTime");
                                            long rentExtendPerClick = RegionManager.getRegionsConf().getLong("Regions." + worlds.get(y) + "." + regions.get(i) + ".rentExtendPerClick");
                                            Region armregion = new RentRegion(region, regionWorld, regionsigns, price, sold, autoreset, allowonlynewblocks, doBlockReset, regionKind, teleportLoc,
                                                    lastreset, true, payedtill, maxRentTime, rentExtendPerClick, subregions, true, false);
                                            RegionManager.addRegion(armregion);
                                            for(Region subregion : subregions) {
                                                subregion.setParentRegion(armregion);
                                            }
                                        } else if (regiontype.equalsIgnoreCase("sellregion")){
                                            Region armregion = new SellRegion(region, regionWorld, regionsigns, price, sold, autoreset, allowonlynewblocks, doBlockReset, regionKind, teleportLoc, lastreset, true, subregions, true, false);
                                            RegionManager.addRegion(armregion);
                                            for(Region subregion : subregions) {
                                                subregion.setParentRegion(armregion);
                                            }
                                        } else if (regiontype.equalsIgnoreCase("contractregion")) {
                                            long payedtill = RegionManager.getRegionsConf().getLong("Regions." + worlds.get(y) + "." + regions.get(i) + ".payedTill");
                                            long extendTime = RegionManager.getRegionsConf().getLong("Regions." + worlds.get(y) + "." + regions.get(i) + ".extendTime");
                                            Boolean terminated = RegionManager.getRegionsConf().getBoolean("Regions." + worlds.get(y) + "." + regions.get(i) + ".terminated");
                                            Region armregion = new ContractRegion(region, regionWorld, regionsigns, price, sold, autoreset, allowonlynewblocks, doBlockReset, regionKind, teleportLoc, lastreset, true,extendTime, payedtill, terminated, subregions, true, false);
                                            RegionManager.addRegion(armregion);
                                            for(Region subregion : subregions) {
                                                subregion.setParentRegion(armregion);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void generatedefaultConfig(){
        Plugin plugin = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket");
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File regiondic = new File(pluginfolder + "/regions.yml");
        if(!regiondic.exists()){
            try {
                ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                InputStream stream = plugin.getResource("regions.yml");
                byte[] buffer = new byte[stream.available()];
                stream.read(buffer);
                OutputStream output = new FileOutputStream(regiondic);
                output.write(buffer);
                output.close();
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static YamlConfiguration getRegionsConf() {
        return RegionManager.regionsconf;
    }

    public static void setRegionsConf(){
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File regionsconfigdic = new File(pluginfolder + "/regions.yml");
        RegionManager.regionsconf = YamlConfiguration.loadConfiguration(regionsconfigdic);
    }

    public static void saveRegionsConf() {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File regionsconfigdic = new File(pluginfolder + "/regions.yml");
        try {
            RegionManager.regionsconf.save(regionsconfigdic);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void Reset() {
        regionList = new ArrayList<>();
        regionsconf = null;
    }

    public static void updateRegions(){
        for(int i = 0; i < RegionManager.getRegionList().size(); i++) {
            RegionManager.getRegionList().get(i).updateRegion();
        }
    }

    public static List<String> completeTabRegions(Player player, String arg, PlayerRegionRelationship playerRegionRelationship, boolean inculdeNormalRegions, boolean includeSubregions) {
        List<String> returnme = new ArrayList<>();

        if(Region.completeTabRegions) {

            for(Region region : RegionManager.getRegionList()) {
                if(inculdeNormalRegions) {
                    if(region.getRegion().getId().toLowerCase().startsWith(arg)) {
                        if(playerRegionRelationship == PlayerRegionRelationship.OWNER) {
                            if(region.getRegion().hasOwner(player.getUniqueId())) {
                                returnme.add(region.getRegion().getId());
                            }
                        } else if (playerRegionRelationship == PlayerRegionRelationship.MEMBER) {
                            if(region.getRegion().hasMember(player.getUniqueId())) {
                                returnme.add(region.getRegion().getId());
                            }
                        } else if (playerRegionRelationship == PlayerRegionRelationship.MEMBER_OR_OWNER) {
                            if(region.getRegion().hasMember(player.getUniqueId()) || region.getRegion().hasOwner(player.getUniqueId())) {
                                returnme.add(region.getRegion().getId());
                            }
                        } else if (playerRegionRelationship == PlayerRegionRelationship.ALL) {
                            returnme.add(region.getRegion().getId());
                        } else if (playerRegionRelationship == PlayerRegionRelationship.AVAILABLE) {
                            if(!region.isSold()) {
                                returnme.add(region.getRegion().getId());
                            }
                        }
                    }
                }
                if(includeSubregions) {
                    for(Region subregion : region.getSubregions()) {
                        if(subregion.getRegion().getId().toLowerCase().startsWith(arg)) {
                            if(playerRegionRelationship == PlayerRegionRelationship.OWNER) {
                                if(subregion.getRegion().hasOwner(player.getUniqueId())) {
                                    returnme.add(subregion.getRegion().getId());
                                }
                            } else if (playerRegionRelationship == PlayerRegionRelationship.MEMBER) {
                                if(subregion.getRegion().hasMember(player.getUniqueId())) {
                                    returnme.add(subregion.getRegion().getId());
                                }
                            } else if (playerRegionRelationship == PlayerRegionRelationship.MEMBER_OR_OWNER) {
                                if(subregion.getRegion().hasMember(player.getUniqueId()) || subregion.getRegion().hasOwner(player.getUniqueId())) {
                                    returnme.add(subregion.getRegion().getId());
                                }
                            } else if (playerRegionRelationship == PlayerRegionRelationship.ALL) {
                                returnme.add(subregion.getRegion().getId());
                            } else if (playerRegionRelationship == PlayerRegionRelationship.AVAILABLE) {
                                if(!subregion.isSold()) {
                                    returnme.add(subregion.getRegion().getId());
                                }
                            } else if (playerRegionRelationship == PlayerRegionRelationship.PARENTREGION_OWNER) {
                                if(subregion.getParentRegion().getRegion().hasOwner(player.getUniqueId())) {
                                    returnme.add(subregion.getRegion().getId());
                                }
                            }
                        }
                    }
                }
            }
        }

        return returnme;
    }

    public static List<Region> getRegionsByMember(UUID uuid) {
        List<Region> regions = new LinkedList<>();
        for (int i = 0; i < RegionManager.getRegionList().size(); i++){
            for(Region subregion : RegionManager.getRegionList().get(i).getSubregions()) {
                if(subregion.getRegion().hasMember(uuid)){
                    regions.add(subregion);
                }
            }
            if(RegionManager.getRegionList().get(i).getRegion().hasMember(uuid)) {
                regions.add(RegionManager.getRegionList().get(i));
            }
        }
        return regions;
    }

    public static List<Region> getRegionsByOwner(UUID uuid) {
        List<Region> regions = new LinkedList<>();
        for (int i = 0; i < RegionManager.getRegionList().size(); i++){
            for(Region subregion : RegionManager.getRegionList().get(i).getSubregions()) {
                if(subregion.getRegion().hasOwner(uuid)){
                    regions.add(subregion);
                }
            }
            if(RegionManager.getRegionList().get(i).getRegion().hasOwner(uuid)){
                regions.add(RegionManager.getRegionList().get(i));
            }
        }
        return regions;
    }

    public static List<Region> getRegionsByOwnerOrMember(UUID uuid) {
        List<Region> regions = new ArrayList<>();
        for(Region region : regionList) {
            for(Region subregion : region.getSubregions()) {
                if(subregion.getRegion().hasOwner(uuid) || subregion.getRegion().hasOwner(uuid)){
                    regions.add(subregion);
                }
            }
            if(region.getRegion().hasOwner(uuid) || region.getRegion().hasOwner(uuid)) {
                regions.add(region);
            }
        }
        return regions;
    }

    public static boolean autoResetRegionsFromOwner(UUID uuid){
        List<Region> regions = RegionManager.getRegionsByOwner(uuid);
        for(int i = 0; i < regions.size(); i++){
            if(regions.get(i).getAutoreset()){
                regions.get(i).unsell();
                if(regions.get(i).isDoBlockReset()) {
                    regions.get(i).resetBlocks();
                }
            }
        }
        return true;
    }

    public static void teleportToFreeRegion(RegionKind type, Player player) throws InputException {
        for (int i = 0; i < RegionManager.getRegionList().size(); i++){

            if ((RegionManager.getRegionList().get(i).isSold() == false) && (RegionManager.getRegionList().get(i).getRegionKind() == type)){
                WGRegion regionTP = RegionManager.getRegionList().get(i).getRegion();
                String message = Messages.REGION_TELEPORT_MESSAGE.replace("%regionid%", regionTP.getId());
                Teleporter.teleport(player, RegionManager.getRegionList().get(i), Messages.PREFIX + message, true);
                return;
            }
        }
        throw new InputException(player, Messages.NO_FREE_REGION_WITH_THIS_KIND);
    }

    public static boolean checkIfSignExists(Sign sign) {
        for(Region region : regionList){
            if(region.hasSign(sign)){
                return true;
            }
            for(Region subregion : region.getSubregions()) {
                if(subregion.hasSign(sign)){
                    return true;
                }
            }
        }
        return false;
    }

    public static Region getRegion(Sign sign) {
        for(Region region : regionList) {
            if(region.hasSign(sign)) {
                return region;
            }
            for(Region subregion : region.getSubregions()) {
                if(subregion.hasSign(sign)) {
                    return subregion;
                }
            }
        }
        return null;
    }

    public static Region getRegion(WGRegion wgRegion, World world) {
        for(Region region : regionList) {
            if(region.getRegionworld().getName().equals(world.getName())) {
                if(region.getRegion().getId().equals(wgRegion.getId())) {
                    return region;
                }
                for(Region subregion : region.getSubregions()) {
                    if(subregion.getRegion().getId().equals(wgRegion.getId())) {
                        return subregion;
                    }
                }
            }
        }
        return null;
    }

    public static Region searchRegionbyNameAndWorld(String name, String world){
        for(Region region : regionList) {
            if(region.getRegionworld().getName().equalsIgnoreCase(world)) {
                if(region.getRegion().getId().equalsIgnoreCase(name)) {
                    return region;
                }
                for(Region subregion : region.getSubregions()) {
                    if(subregion.getRegion().getId().equalsIgnoreCase(name)) {
                        return subregion;
                    }
                }
            }
        }
        return null;
    }

    public static Region getRegionbyNameAndWorldCommands(String name, String world) {
        Region mayReturn = null;
        for(Region region : regionList) {
            if(region.getRegionworld().getName().equalsIgnoreCase(world)) {
                if(region.getRegion().getId().equalsIgnoreCase(name)) {
                    return region;
                }
                for(Region subregion : region.getSubregions()) {
                    if(subregion.getRegion().getId().equalsIgnoreCase(name)) {
                        return subregion;
                    }
                }
            } else {
                if(region.getRegion().getId().equalsIgnoreCase(name)) {
                    mayReturn = region;
                }
                for(Region subregion : region.getSubregions()) {
                    if(subregion.getRegion().getId().equalsIgnoreCase(name)) {
                        mayReturn = subregion;
                    }
                }
            }
        }
        return mayReturn;
    }

    public static List<Region> getRegionsByLocation(Location location) {
        List<Region> regions = new ArrayList<>();
        for(Region region : regionList) {
            if(region.getRegion().contains(location.getBlockX(), location.getBlockY(), location.getBlockZ())) {
                if(region.getRegionworld().getName().equals(location.getWorld().getName())) {
                    regions.add(region);
                }
                for(Region subregion : region.getSubregions()) {
                    if(subregion.getRegion().contains(location.getBlockX(), location.getBlockY(), location.getBlockZ())) {
                        regions.add(subregion);
                    }
                }
            }
        }
        return regions;
    }

    public static List<Region> getRegions() {
        return regionList;
    }

}
