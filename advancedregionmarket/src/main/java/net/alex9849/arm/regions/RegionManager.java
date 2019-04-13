package net.alex9849.arm.regions;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.ArmSettings;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.ContractPrice;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.arm.regions.price.RentPrice;
import net.alex9849.arm.util.YamlFileManager;
import net.alex9849.inter.WGRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class RegionManager extends YamlFileManager<Region> {

    public RegionManager(File savepath) {
        super(savepath);
    }

    @Override
    public List<Region> loadSavedObjects(YamlConfiguration yamlConfiguration) {
        List<Region> loadedRegions = new ArrayList<>();
        boolean fileupdated = false;
        yamlConfiguration.options().copyDefaults(true);

        if(yamlConfiguration.get("Regions") != null) {
            ConfigurationSection mainSection = yamlConfiguration.getConfigurationSection("Regions");
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
                                        fileupdated |= updateDefaults(regionSection);
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

        if(fileupdated) {
            this.saveFile();
        }

        yamlConfiguration.options().copyDefaults(false);

        return loadedRegions;
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
        int boughtExtraTotalEntitys = regionSection.getInt("boughtExtraTotalEntitys");
        List<String> boughtExtraEntitys = regionSection.getStringList("boughtExtraEntitys");
        boolean isUserResettable = regionSection.getBoolean("isUserResettable");
        Location teleportLoc = parseTpLocation(teleportLocString);
        RegionKind regionKind = AdvancedRegionMarket.getRegionKindManager().getRegionKind(kind);
        if(regionKind == null) {
            regionKind = RegionKind.DEFAULT;
        }
        EntityLimitGroup entityLimitGroup = AdvancedRegionMarket.getEntityLimitGroupManager().getEntityLimitGroup(entityLimitGroupString);
        if(entityLimitGroup == null) {
            entityLimitGroup = EntityLimitGroup.DEFAULT;
        }
        HashMap<EntityType, Integer> extraEntitysMap = parseBoughtExtraEntitys(boughtExtraEntitys);
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
                    lastreset, isUserResettable, payedtill, subregions, allowedSubregions, entityLimitGroup, extraEntitysMap, boughtExtraTotalEntitys);


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
            return new ContractRegion(wgRegion, regionWorld, regionsigns, contractPrice, sold, autoreset, allowonlynewblocks, doBlockReset, regionKind, teleportLoc,
                    lastreset, isUserResettable, payedtill, terminated, subregions, allowedSubregions, entityLimitGroup, extraEntitysMap, boughtExtraTotalEntitys);
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
            return new SellRegion(wgRegion, regionWorld, regionsigns, sellPrice, sold, autoreset, allowonlynewblocks, doBlockReset, regionKind, teleportLoc, lastreset,
                    isUserResettable, subregions, allowedSubregions, entityLimitGroup, extraEntitysMap, boughtExtraTotalEntitys);

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
                    sublastreset, ArmSettings.isAllowSubRegionUserReset(), subregpayedtill, new ArrayList<Region>(), 0, EntityLimitGroup.SUBREGION, new HashMap<>(), 0);

        }  else if (subregionRegiontype.equalsIgnoreCase("contractregion")) {
            long subregpayedtill = section.getLong("payedTill");
            long subregextendTime = section.getLong("extendTime");
            Boolean subregterminated = section.getBoolean("terminated");
            ContractPrice subPrice = new ContractPrice(subregPrice, subregextendTime);
            return new ContractRegion(subregion, regionWorld, subregionsigns, subPrice, subregIsSold, ArmSettings.isSubregionAutoReset(), subregIsHotel, ArmSettings.isSubregionBlockReset(), RegionKind.SUBREGION, null,
                    sublastreset, ArmSettings.isAllowSubRegionUserReset(), subregpayedtill, subregterminated, new ArrayList<Region>(), 0, EntityLimitGroup.SUBREGION, new HashMap<>(), 0);

        } else {
            Price subPrice = new Price(subregPrice);
            return new SellRegion(subregion, regionWorld, subregionsigns, subPrice, subregIsSold, ArmSettings.isSubregionAutoReset(), subregIsHotel, ArmSettings.isSubregionBlockReset(), RegionKind.SUBREGION, null,
                    sublastreset, ArmSettings.isAllowSubRegionUserReset(), new ArrayList<Region>(), 0, EntityLimitGroup.SUBREGION, new HashMap<>(), 0);
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

    private static HashMap<EntityType, Integer> parseBoughtExtraEntitys(List<String> stringList) {
        HashMap<EntityType, Integer> boughtExtraEntitys = new HashMap<>();
        for(String element : stringList) {
            if(element.matches("[^;\n ]+: [0-9]+")) {
                String[] extraparts = element.split(": ");
                int extraAmount = Integer.parseInt(extraparts[1]);
                try {
                    EntityType entityType = EntityType.valueOf(extraparts[0]);
                    boughtExtraEntitys.put(entityType, extraAmount);
                } catch (IllegalArgumentException e) {
                    Bukkit.getServer().getLogger().log(Level.INFO, "[ARM] Could not parse EntitysType " + extraparts[0] + " at boughtExtraEntitys. Ignoring it...");
                }

            }
        }
        return boughtExtraEntitys;
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

    private boolean updateDefaults(ConfigurationSection section) {
        boolean fileupdated = false;

        fileupdated |= this.addDefault(section,"sold", false);
        fileupdated |= this.addDefault(section,"kind", "default");
        fileupdated |= this.addDefault(section,"autoreset", true);
        fileupdated |= this.addDefault(section,"lastreset", 1);
        fileupdated |= this.addDefault(section,"isHotel", false);
        fileupdated |= this.addDefault(section,"entityLimitGroup", "default");
        fileupdated |= this.addDefault(section,"doBlockReset", true);
        fileupdated |= this.addDefault(section,"allowedSubregions", 0);
        fileupdated |= this.addDefault(section,"isUserResettable", true);
        fileupdated |= this.addDefault(section,"boughtExtraTotalEntitys", 0);
        fileupdated |= this.addDefault(section,"boughtExtraEntitys", new ArrayList<String>());
        fileupdated |= this.addDefault(section,"regiontype", "sellregion");
        if (section.getString("regiontype").equalsIgnoreCase("rentregion")) {
            fileupdated |= this.addDefault(section,"payedTill", 1);
        }
        if (section.getString("regiontype").equalsIgnoreCase("contractregion")) {
            fileupdated |= this.addDefault(section,"payedTill", 1);
            fileupdated |= this.addDefault(section,"terminated", false);
        }
        if(section.get("subregions") != null) {
            List<String> subregions = new ArrayList<String>(section.getConfigurationSection("subregions").getKeys(false));
            if(subregions != null) {
                for (String subregionID : subregions) {
                    fileupdated |= this.addDefault(section,"subregions." + subregionID + ".price", 0);
                    fileupdated |= this.addDefault(section,"subregions." + subregionID + ".sold", false);
                    fileupdated |= this.addDefault(section,"subregions." + subregionID + ".isHotel", false);
                    fileupdated |= this.addDefault(section,"subregions." + subregionID + ".lastreset", 1);
                    fileupdated |= this.addDefault(section,"subregions." + subregionID + ".regiontype", "sellregion");
                    if (section.getString("subregions." + subregionID + ".regiontype").equalsIgnoreCase("contractregion")) {
                        fileupdated |= this.addDefault(section,"subregions." + subregionID + ".payedTill", 0);
                        fileupdated |= this.addDefault(section,"subregions." + subregionID + ".extendTime", 0);
                        fileupdated |= this.addDefault(section,"subregions." + subregionID + ".terminated", false);
                    }
                    if (section.getString("subregions." + subregionID + ".regiontype").equalsIgnoreCase("rentregion")) {
                        fileupdated |= this.addDefault(section,"subregions." + subregionID + ".payedTill", 0);
                        fileupdated |= this.addDefault(section,"subregions." + subregionID + ".maxRentTime", 0);
                        fileupdated |= this.addDefault(section,"subregions." + subregionID + ".rentExtendPerClick", 0);
                    }
                }
            }
        }

        return fileupdated;
    }

    @Override
    public void saveObjectToYamlObject(Region region, YamlConfiguration yamlConfiguration) {
        yamlConfiguration.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId(), region.toConfigureationSection());
    }

    @Override
    public void writeStaticSettings(YamlConfiguration yamlConfiguration) {

    }
}
