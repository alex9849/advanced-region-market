package net.alex9849.arm.regions;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.ArmSettings;
import net.alex9849.arm.Messages;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.entitylimit.EntityLimitGroupManager;
import net.alex9849.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.minifeatures.teleporter.Teleporter;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.ContractPrice;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.arm.regions.price.RentPrice;
import net.alex9849.exceptions.SchematicNotFoundException;
import net.alex9849.inter.WGRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class RegionManager {
    private static YamlConfiguration regionsconf;
    private static List<Region> regionList = new ArrayList<>();

    public static void addRegion(Region region) {
        regionList.add(region);
        writeRegionToYamlObject(region);
        saveRegionsConf();
    }

    private static void writeRegionToYamlObject(Region region) {
        regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId(), null);
        regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".sold", region.isSold());
        if(region.getRegionKind() == RegionKind.DEFAULT) {
            regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".kind", "default");
        } else {
            regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".kind", region.getRegionKind().getName());
        }
        regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".autoreset", region.isAutoreset());
        regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".lastreset", region.getLastreset());
        regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".isHotel", region.isHotel);
        regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".entityLimitGroup", region.getEntityLimitGroup().getName());
        regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".doBlockReset", region.isDoBlockReset());
        regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".allowedSubregions", region.getAllowedSubregions());
        regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".isUserResettable", region.isUserResettable());

        for(Region subregion : region.getSubregions()) {
            regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".subregions." + subregion.getRegion().getId() + ".price", subregion.getPrice());
            regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".subregions." + subregion.getRegion().getId() + ".sold", subregion.isSold());
            regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".subregions." + subregion.getRegion().getId() + ".isHotel", subregion.isHotel());
            regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".subregions." + subregion.getRegion().getId() + ".lastreset", subregion.getLastreset());
            List<String> subregionsigns = new ArrayList<>();
            for(Sign sign : subregion.getSellSigns()) {
                Location signloc = sign.getLocation();
                subregionsigns.add(signloc.getWorld().getName() + ";" + signloc.getX() + ";" + signloc.getY() + ";" + signloc.getZ());
            }
            regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".subregions." + subregion.getRegion().getId() + ".signs", subregionsigns);
            if(subregion instanceof SellRegion) {
                regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".subregions." + subregion.getRegion().getId() + ".regiontype", "sellregion");
            } else if (subregion instanceof RentRegion) {
                RentRegion rentRegion = (RentRegion) subregion;
                regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".subregions." + subregion.getRegion().getId() + ".regiontype", "rentregion");
                regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".subregions." + subregion.getRegion().getId() + ".payedTill", rentRegion.getPayedTill());
                regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".subregions." + subregion.getRegion().getId() + ".maxRentTime", rentRegion.getMaxRentTime());
                regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".subregions." + subregion.getRegion().getId() + ".rentExtendPerClick", rentRegion.getRentExtendPerClick());
            } else if (subregion instanceof ContractRegion) {
                ContractRegion contractRegion = (ContractRegion) subregion;
                regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".subregions." + subregion.getRegion().getId() + ".regiontype", "contractregion");
                regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".subregions." + subregion.getRegion().getId() + ".payedTill", contractRegion.getPayedTill());
                regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".subregions." + subregion.getRegion().getId() + ".extendTime", contractRegion.getExtendTime());
                regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".subregions." + subregion.getRegion().getId() + ".terminated", contractRegion.isTerminated());
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
            if(region.getPriceObject().isAutoPrice()) {
                regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".autoprice", region.getPriceObject().getAutoPrice().getName());
                regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".price", null);
            } else {
                regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".price", region.getPrice());
            }
            regionsconf.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId() + ".regiontype", "sellregion");
        } else if (region instanceof RentRegion) {
            RentRegion rentRegion = (RentRegion) region;
            if(region.getPriceObject().isAutoPrice()) {
                regionsconf.set("Regions." + rentRegion.getRegionworld().getName() + "." + rentRegion.getRegion().getId() + ".autoprice", region.getPriceObject().getAutoPrice().getName());
                regionsconf.set("Regions." + rentRegion.getRegionworld().getName() + "." + rentRegion.getRegion().getId() + ".price", null);
                regionsconf.set("Regions." + rentRegion.getRegionworld().getName() + "." + rentRegion.getRegion().getId() + ".maxRentTime", null);
                regionsconf.set("Regions." + rentRegion.getRegionworld().getName() + "." + rentRegion.getRegion().getId() + ".rentExtendPerClick", null);
            } else {
                regionsconf.set("Regions." + rentRegion.getRegionworld().getName() + "." + rentRegion.getRegion().getId() + ".price", region.getPrice());
                regionsconf.set("Regions." + rentRegion.getRegionworld().getName() + "." + rentRegion.getRegion().getId() + ".maxRentTime", rentRegion.getMaxRentTime());
                regionsconf.set("Regions." + rentRegion.getRegionworld().getName() + "." + rentRegion.getRegion().getId() + ".rentExtendPerClick", rentRegion.getRentExtendPerClick());
            }
            regionsconf.set("Regions." + rentRegion.getRegionworld().getName() + "." + rentRegion.getRegion().getId() + ".regiontype", "rentregion");
            regionsconf.set("Regions." + rentRegion.getRegionworld().getName() + "." + rentRegion.getRegion().getId() + ".payedTill", rentRegion.getPayedTill());
        } else if (region instanceof ContractRegion) {
            ContractRegion contractRegion = (ContractRegion) region;
            if(region.getPriceObject().isAutoPrice()) {
                regionsconf.set("Regions." + contractRegion.getRegionworld().getName() + "." + contractRegion.getRegion().getId() + ".autoprice", region.getPriceObject().getAutoPrice().getName());
                regionsconf.set("Regions." + contractRegion.getRegionworld().getName() + "." + contractRegion.getRegion().getId() + ".price", null);
                regionsconf.set("Regions." + contractRegion.getRegionworld().getName() + "." + contractRegion.getRegion().getId() + ".extendTime", null);
            } else {
                regionsconf.set("Regions." + contractRegion.getRegionworld().getName() + "." + contractRegion.getRegion().getId() + ".price", region.getPrice());
                regionsconf.set("Regions." + contractRegion.getRegionworld().getName() + "." + contractRegion.getRegion().getId() + ".extendTime", contractRegion.getExtendTime());
            }
            regionsconf.set("Regions." + contractRegion.getRegionworld().getName() + "." + contractRegion.getRegion().getId() + ".regiontype", "contractregion");
            regionsconf.set("Regions." + contractRegion.getRegionworld().getName() + "." + contractRegion.getRegion().getId() + ".payedTill", contractRegion.getPayedTill());
            regionsconf.set("Regions." + contractRegion.getRegionworld().getName() + "." + contractRegion.getRegion().getId() + ".terminated", contractRegion.isTerminated());
        }
        region.setSaved();
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
        writeRegionsToConfig(true);
        return true;
    }

    public static void writeRegionsToConfig(boolean all) {
        boolean anythingdone = false;

        if(all) {
            regionsconf = new YamlConfiguration();
        }

        for(Region region : regionList) {
            if(region.needsSave() || all) {
                writeRegionToYamlObject(region);
                region.setSaved();
                anythingdone = true;
            }
        }
        if(anythingdone) {
            saveRegionsConf();
        }
    }

    private static List<Region> getRegionList() {
        return regionList;
    }

    public static void loadRegionsFromConfig() {
        List<Region> loadedRegions = new ArrayList<>();
        updateConfig();
        if(RegionManager.getRegionsConf().get("Regions") != null) {
            ConfigurationSection mainSection = RegionManager.getRegionsConf().getConfigurationSection("Regions");
            List<String> worlds = new ArrayList<String>(mainSection.getKeys(false));
            if(worlds != null) {
                for(String worldString : worlds) {
                    World regionWorld = Bukkit.getWorld(worldString);
                    if(regionWorld != null) {
                        if(mainSection.get(worldString) != null) {
                            ConfigurationSection worldSection = mainSection.getConfigurationSection(worldString);
                            List<String> regions = new ArrayList<String>(worldSection.getKeys(false));
                            if(regions != null) {
                                for(String regionname : regions){
                                    ConfigurationSection regionSection = worldSection.getConfigurationSection(regionname);
                                    WGRegion wgRegion = AdvancedRegionMarket.getWorldGuardInterface().getRegion(regionWorld, AdvancedRegionMarket.getWorldGuard(), regionname);

                                    if(wgRegion != null) {
                                        Region armRegion = parseRegion(regionSection, regionWorld, wgRegion);
                                        loadedRegions.add(armRegion);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        regionList = loadedRegions;
    }

    private static Region parseRegion(ConfigurationSection regionSection, World regionWorld, WGRegion wgRegion) {
        boolean sold = regionSection.getBoolean("sold");
        String kind = regionSection.getString("kind");
        String autoPriceString = regionSection.getString("autoprice");
        boolean autoreset = regionSection.getBoolean("autoreset");
        String regiontype = regionSection.getString("regiontype");
        String entityLimitGroupString = regionSection.getString("entityLimitGroup");
        boolean allowonlynewblocks = regionSection.getBoolean("isHotel");
        boolean doBlockReset = regionSection.getBoolean("doBlockReset");
        long lastreset = regionSection.getLong("lastreset");
        String teleportLocString = regionSection.getString("teleportLoc");
        int allowedSubregions = regionSection.getInt("allowedSubregions");
        boolean isUserResettable = regionSection.getBoolean("isUserResettable");
        Location teleportLoc = parseTpLocation(teleportLocString);
        RegionKind regionKind = RegionKind.getRegionKind(kind);
        if(regionKind == null) {
            regionKind = RegionKind.DEFAULT;
        }
        EntityLimitGroup entityLimitGroup = EntityLimitGroupManager.getEntityLimitGroup(entityLimitGroupString);
        if(entityLimitGroup == null) {
            entityLimitGroup = EntityLimitGroup.DEFAULT;
        }
        List<Sign> regionsigns = parseRegionsSigns(regionSection);

        List<Region> subregions = new ArrayList<>();
        if (regionSection.getConfigurationSection("subregions") != null) {
            List<String> subregionsection = new ArrayList<>(regionSection.getConfigurationSection("subregions").getKeys(false));
            if (subregionsection != null) {
                for (String subregionName : subregionsection) {
                    WGRegion subWGRegion = AdvancedRegionMarket.getWorldGuardInterface().getRegion(regionWorld, AdvancedRegionMarket.getWorldGuard(), subregionName);
                    if(subWGRegion != null) {
                        Region armSubRegion = parseSubRegion(regionSection.getConfigurationSection("subregions." + subregionName), regionWorld, subWGRegion, wgRegion);
                        subregions.add(armSubRegion);
                    }
                }
            }
        }

        if (regiontype.equalsIgnoreCase("rentregion")) {
            RentPrice rentPrice;
            if (autoPriceString != null) {
                if (AutoPrice.getAutoprice(autoPriceString) != null) {
                    rentPrice = new RentPrice(AutoPrice.getAutoprice(autoPriceString));
                } else {
                    rentPrice = new RentPrice(AutoPrice.DEFAULT);
                }
            } else {
                double price = regionSection.getDouble("price");
                long maxRentTime = regionSection.getLong("maxRentTime");
                long rentExtendPerClick = regionSection.getLong("rentExtendPerClick");
                rentPrice = new RentPrice(price, rentExtendPerClick, maxRentTime);
            }
            long payedtill = regionSection.getLong("payedTill");
            return new RentRegion(wgRegion, regionWorld, regionsigns, rentPrice, sold, autoreset, allowonlynewblocks, doBlockReset, regionKind, teleportLoc,
                    lastreset, isUserResettable, payedtill, subregions, allowedSubregions, entityLimitGroup);


        }  else if (regiontype.equalsIgnoreCase("contractregion")) {

            ContractPrice contractPrice;
            if (autoPriceString != null) {
                if (AutoPrice.getAutoprice(autoPriceString) != null) {
                    contractPrice = new ContractPrice(AutoPrice.getAutoprice(autoPriceString));
                } else {
                    contractPrice = new ContractPrice(AutoPrice.DEFAULT);
                }
            } else {
                double price = regionSection.getDouble("price");
                long extendTime = regionSection.getLong("extendTime");
                contractPrice = new ContractPrice(price, extendTime);
            }
            long payedtill = regionSection.getLong("payedTill");
            Boolean terminated = regionSection.getBoolean("terminated");
            return new ContractRegion(wgRegion, regionWorld, regionsigns, contractPrice, sold, autoreset, allowonlynewblocks, doBlockReset, regionKind, teleportLoc, lastreset, isUserResettable, payedtill, terminated, subregions, allowedSubregions, entityLimitGroup);
        } else {
            Price sellPrice;
            if (autoPriceString != null) {
                if (AutoPrice.getAutoprice(autoPriceString) != null) {
                    sellPrice = new Price(AutoPrice.getAutoprice(autoPriceString));
                } else {
                    sellPrice = new Price(AutoPrice.DEFAULT);
                }
            } else {
                double price = regionSection.getDouble("price");
                sellPrice = new Price(price);
            }
            return new SellRegion(wgRegion, regionWorld, regionsigns, sellPrice, sold, autoreset, allowonlynewblocks, doBlockReset, regionKind, teleportLoc, lastreset, isUserResettable, subregions, allowedSubregions, entityLimitGroup);

        }
    }

    private static Region parseSubRegion(ConfigurationSection section, World regionWorld, WGRegion subregion, WGRegion parentRegion) {
        double subregPrice = section.getDouble("price");
        boolean subregIsSold = section.getBoolean("sold");
        boolean subregIsHotel = section.getBoolean("isHotel");
        String subregionRegiontype = section.getString("regiontype");
        long sublastreset = section.getLong("lastreset");
        List<Sign> subregionsigns = parseRegionsSigns(section);

        if (ArmSettings.isAllowParentRegionOwnersBuildOnSubregions()) {
            if (subregion.getParent() == null) {
                subregion.setParent(parentRegion);
            } else {
                if (!subregion.getParent().equals(parentRegion)) {
                    subregion.setParent(parentRegion);
                }
            }
        } else {
            if ((parentRegion.getPriority() + 1) != subregion.getPriority())
                subregion.setPriority(parentRegion.getPriority() + 1);
        }

        if (subregionRegiontype.equalsIgnoreCase("rentregion")) {
            long subregpayedtill = section.getLong("payedTill");
            long subregmaxRentTime = section.getLong("maxRentTime");
            long subregrentExtendPerClick = section.getLong("rentExtendPerClick");
            RentPrice subPrice = new RentPrice(subregPrice, subregrentExtendPerClick, subregmaxRentTime);
            return new RentRegion(subregion, regionWorld, subregionsigns, subPrice, subregIsSold, ArmSettings.isSubregionAutoReset(), subregIsHotel, ArmSettings.isSubregionBlockReset(), RegionKind.SUBREGION, null,
                    sublastreset, ArmSettings.isAllowSubRegionUserReset(), subregpayedtill, new ArrayList<Region>(), 0, EntityLimitGroup.SUBREGION);

        }  else if (subregionRegiontype.equalsIgnoreCase("contractregion")) {
            long subregpayedtill = section.getLong("payedTill");
            long subregextendTime = section.getLong("extendTime");
            Boolean subregterminated = section.getBoolean("terminated");
            ContractPrice subPrice = new ContractPrice(subregPrice, subregextendTime);
            return new ContractRegion(subregion, regionWorld, subregionsigns, subPrice, subregIsSold, ArmSettings.isSubregionAutoReset(), subregIsHotel, ArmSettings.isSubregionBlockReset(), RegionKind.SUBREGION, null, sublastreset, ArmSettings.isAllowSubRegionUserReset(), subregpayedtill, subregterminated, new ArrayList<Region>(), 0, EntityLimitGroup.SUBREGION);

        } else {
            Price subPrice = new Price(subregPrice);
            return new SellRegion(subregion, regionWorld, subregionsigns, subPrice, subregIsSold, ArmSettings.isSubregionAutoReset(), subregIsHotel, ArmSettings.isSubregionBlockReset(), RegionKind.SUBREGION, null, sublastreset, ArmSettings.isAllowSubRegionUserReset(), new ArrayList<Region>(), 0, EntityLimitGroup.SUBREGION);
        }
    }

    private static List<Sign> parseRegionsSigns(ConfigurationSection section) {
        List<String> regionsignsloc = section.getStringList("signs");
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
        return regionsigns;
    }

    private static Location parseTpLocation(String teleportLocString) {
        Location teleportLoc = null;
        if (teleportLocString != null) {
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
        return teleportLoc;
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

    private static void updateConfig() {
        if(regionsconf.get("Regions") != null) {
            LinkedList<String> worlds = new LinkedList<String>(regionsconf.getConfigurationSection("Regions").getKeys(false));
            if(worlds != null) {
                for(int y = 0; y < worlds.size(); y++) {
                    if(regionsconf.get("Regions." + worlds.get(y)) != null) {
                        LinkedList<String> regions = new LinkedList<String>(regionsconf.getConfigurationSection("Regions." + worlds.get(y)).getKeys(false));
                        if(regions != null) {
                            for (int i = 0; i < regions.size(); i++) {
                                //regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".price", 0);
                                regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".sold", false);
                                regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".kind", "default");
                                regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".autoreset", true);
                                regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".lastreset", 1);
                                regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".isHotel", false);
                                regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".entityLimitGroup", "default");
                                regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".doBlockReset", true);
                                regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".allowedSubregions", 0);
                                regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".isUserResettable", true);
                                regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".regiontype", "sellregion");
                                if (regionsconf.getString("Regions." + worlds.get(y) + "." + regions.get(i) + ".regiontype").equalsIgnoreCase("rentregion")) {
                                    regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".payedTill", 1);
                                    //regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".maxRentTime", 1);
                                    //regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".rentExtendPerClick", 1);
                                }
                                if (regionsconf.getString("Regions." + worlds.get(y) + "." + regions.get(i) + ".regiontype").equalsIgnoreCase("contractregion")) {
                                    regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".payedTill", 1);
                                    //regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".extendTime", 1);
                                    regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".terminated", false);
                                }
                                if(regionsconf.get("Regions." + worlds.get(y)+ "." + regions.get(i) + ".subregions") != null) {
                                    LinkedList<String> subregions = new LinkedList<String>(regionsconf.getConfigurationSection("Regions." + worlds.get(y)+ "." + regions.get(i) + ".subregions").getKeys(false));
                                    if(subregions != null) {
                                        for (int j = 0; j < subregions.size(); j++) {
                                            regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".subregions." + subregions.get(j) + ".price", 0);
                                            regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".subregions." + subregions.get(j) + ".sold", false);
                                            regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".subregions." + subregions.get(j) + ".isHotel", false);
                                            regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".subregions." + subregions.get(j) + ".lastreset", 1);
                                            regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".subregions." + subregions.get(j) + ".regiontype", "sellregion");
                                            if (regionsconf.getString("Regions." + worlds.get(y) + "." + regions.get(i) + ".subregions." + subregions.get(j) + ".regiontype").equalsIgnoreCase("contractregion")) {
                                                regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".subregions." + subregions.get(j) + ".payedTill", 0);
                                                regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".subregions." + subregions.get(j) + ".extendTime", 0);
                                                regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".subregions." + subregions.get(j) + ".terminated", false);
                                            }
                                            if (regionsconf.getString("Regions." + worlds.get(y) + "." + regions.get(i) + ".subregions." + subregions.get(j) + ".regiontype").equalsIgnoreCase("rentregion")) {
                                                regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".subregions." + subregions.get(j) + ".payedTill", 0);
                                                regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".subregions." + subregions.get(j) + ".maxRentTime", 0);
                                                regionsconf.addDefault("Regions." + worlds.get(y) + "." + regions.get(i) + ".subregions." + subregions.get(j) + ".rentExtendPerClick", 0);
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
        regionsconf.options().copyDefaults(true);
        saveRegionsConf();
        regionsconf.options().copyDefaults(false);
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
        for(Region region : regionList) {
            region.updateRegion();
            for(Region subregion : region.getSubregions()) {
                subregion.updateRegion();
            }
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
                    try {
                        regions.get(i).resetBlocks();
                    } catch (SchematicNotFoundException e) {
                        Bukkit.getLogger().log(Level.WARNING, "Could not find schematic file for region " + regions.get(i).getRegion().getId() + "in world " + regions.get(i).getRegionworld().getName());
                    }
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

    public static Region getRegion(WGRegion wgRegion) {
        for(Region region : regionList) {
            if(region.getRegion().equals(wgRegion)) {
                return region;
            }
            for(Region subregion : region.getSubregions()) {
                if(subregion.getRegion().equals(wgRegion)) {
                    return subregion;
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

    public static List<Region> getRegionsByRegionKind(RegionKind regionKind) {
        List<Region> regions = new ArrayList<>();
        for(Region region : regionList) {
            if(region.getRegionKind() == regionKind) {
                regions.add(region);
                for(Region subregion : region.getSubregions()) {
                    if(subregion.getRegionKind() == regionKind) {
                        regions.add(subregion);
                    }
                }
            }
        }
        return regions;
    }

    public static List<Region> getRegionsBySelltype(SellType sellType) {
        List<Region> regions = new ArrayList<>();
        for(Region region : regionList) {
            if(region.getSellType() == sellType) {
                regions.add(region);
                for(Region subregion : region.getSubregions()) {
                    if(subregion.getSellType() == sellType) {
                        regions.add(subregion);
                    }
                }
            }
        }
        return regions;
    }

    public static List<Region> getFreeRegions(RegionKind regionKind) {
        List<Region> regions = new ArrayList<>();
        for(Region region : regionList) {
            if(region.getRegionKind() == regionKind) {
                if(!region.isSold()) {
                    regions.add(region);
                }
            }
            for(Region subregion : region.getSubregions()) {
                if(subregion.getRegionKind() == regionKind) {
                    if(!subregion.isSold()) {
                        regions.add(subregion);
                    }
                }
            }
        }
        return regions;
    }

    public static boolean containsRegion(Region region) {
        return RegionManager.regionList.contains(region);
    }

    public static List<Region> getAllRegions() {
        List<Region> regions = new ArrayList<>();
        for(Region region : regionList) {
            regions.add(region);
        }
        return regions;
    }

}
