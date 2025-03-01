package net.alex9849.arm.regions;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.adapters.WGRegion;
import net.alex9849.arm.adapters.signs.SignAttachment;
import net.alex9849.arm.adapters.signs.SignData;
import net.alex9849.arm.adapters.signs.SignDataFactory;
import net.alex9849.arm.adapters.util.YamlFileManager;
import net.alex9849.arm.entitylimit.EntityLimit;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.events.AddRegionEvent;
import net.alex9849.arm.events.RemoveRegionEvent;
import net.alex9849.arm.exceptions.FeatureDisabledException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.exceptions.NoSaveLocationException;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.minifeatures.teleporter.Teleporter;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.ContractPrice;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.arm.regions.price.RentPrice;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

public class RegionManager extends YamlFileManager<Region> {

    private boolean tabCompleteRegions = false;
    private final HashMap<World, HashMap<DummyChunk, List<Region>>> worldChunkRegionMap;
    private UpdateScheduler updateScheduler;

    public RegionManager(File savepath, int updateTicks) {
        super(savepath);
        this.worldChunkRegionMap = new HashMap<>();

        for (Region region : this) {
            this.addToWorldChunkMap(region);
        }

        this.updateScheduler = this.new UpdateScheduler(updateTicks);
    }


    /*#############################################
    ######## Parsing and add/remove stuff #########
    #############################################*/

    @Override
    public boolean add(Region region) {
        return this.add(region, false);
    }

    @Override
    public boolean add(Region region, boolean unsafe) {
        if(region.isSubregion()) {
            return false;
        }
        AddRegionEvent addRegionEvent = new AddRegionEvent(region);
        Bukkit.getServer().getPluginManager().callEvent(addRegionEvent);
        if (addRegionEvent.isCancelled()) {
            return false;
        }
        if (super.add(region, unsafe)) {
            this.addToWorldChunkMap(region);
            this.updateScheduler.rearrangeUpdateQuenue();
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Region region) {
        RemoveRegionEvent removeRegionEvent = new RemoveRegionEvent(region);
        Bukkit.getServer().getPluginManager().callEvent(removeRegionEvent);
        if (removeRegionEvent.isCancelled()) {
            return false;
        }

        if (super.remove(region)) {
            this.removeFromWorldChunkMap(region);
            this.updateScheduler.rearrangeUpdateQuenue();
            return true;
        }
        return false;
    }

    @Override
    protected List<Region> loadSavedObjects(YamlConfiguration yamlConfiguration) {
        List<Region> loadedRegions = new ArrayList<>();
        boolean fileupdated = false;
        yamlConfiguration.options().copyDefaults(true);

        if (yamlConfiguration.get("Regions") != null) {
            ConfigurationSection mainSection = yamlConfiguration.getConfigurationSection("Regions");
            List<String> worlds = new ArrayList<String>(mainSection.getKeys(false));
            if (worlds != null) {
                for (String worldString : worlds) {
                    World regionWorld = Bukkit.getWorld(worldString);
                    if (regionWorld != null) {
                        if (mainSection.get(worldString) != null) {
                            ConfigurationSection worldSection = mainSection.getConfigurationSection(worldString);
                            List<String> regions = new ArrayList<String>(worldSection.getKeys(false));
                            if (regions != null) {
                                for (String regionname : regions) {
                                    ConfigurationSection regionSection = worldSection.getConfigurationSection(regionname);
                                    WGRegion wgRegion = AdvancedRegionMarket.getInstance().getWorldGuardInterface().getRegion(regionWorld, regionname);

                                    if (wgRegion != null) {
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

        if (fileupdated) {
            this.saveFile();
        }

        yamlConfiguration.options().copyDefaults(false);

        return loadedRegions;
    }

    @Override
    protected void saveObjectToYamlObject(Region region, YamlConfiguration yamlConfiguration) {
        yamlConfiguration.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId(), region.toConfigurationSection());
    }

    @Override
    protected void writeStaticSettings(YamlConfiguration yamlConfiguration) {
    }

    @Override
    public boolean staticSaveQuenued() {
        return false;
    }

    private static Region parseRegion(ConfigurationSection regionSection, World regionWorld, WGRegion wgRegion) {
        boolean sold = regionSection.getBoolean("sold");
        String kind = regionSection.getString("kind");
        String flagGroupString = regionSection.getString("flagGroup");
        String autoPriceString = regionSection.getString("autoprice");
        boolean inactivityReset = regionSection.getBoolean("inactivityReset");
        String regiontype = regionSection.getString("regiontype");
        String entityLimitGroupString = regionSection.getString("entityLimitGroup");
        String landlordString = regionSection.getString("landlord");
        boolean isHotel = regionSection.getBoolean("isHotel");
        boolean autorestore = regionSection.getBoolean("autorestore");
        long lastreset = regionSection.getLong("lastreset");
        long lastLogin = regionSection.getLong("lastLogin");
        int maxMembers = regionSection.getInt("maxMembers");
        int paybackPercentage = regionSection.getInt("paybackPercentage");
        String teleportLocString = regionSection.getString("teleportLoc");
        String playerTeleportLocString = regionSection.getString("playerTeleportLoc");
        int allowedSubregions = regionSection.getInt("allowedSubregions");
        int boughtExtraTotalEntitys = regionSection.getInt("boughtExtraTotalEntitys");
        boolean isProtectionOfContinuance = regionSection.getBoolean("isprotectionofcontinuance");
        List<String> boughtExtraEntitys = regionSection.getStringList("boughtExtraEntitys");
        boolean userrestorable = regionSection.getBoolean("userrestorable");
        Location teleportLoc = parseTpLocation(teleportLocString);
        Location playerTeleportLoc = parseTpLocation(playerTeleportLocString);
        RegionKind regionKind = AdvancedRegionMarket.getInstance().getRegionKindManager().getRegionKind(kind);
        FlagGroup flagGroup = AdvancedRegionMarket.getInstance().getFlagGroupManager().getFlagGroup(flagGroupString);
        UUID landlord = null;
        if(landlordString != null) {
            landlord = UUID.fromString(landlordString);
        }
        if (flagGroup == null) {
            flagGroup = FlagGroup.DEFAULT;
        }
        if (regionKind == null) {
            regionKind = RegionKind.DEFAULT;
        }
        EntityLimitGroup entityLimitGroup = AdvancedRegionMarket.getInstance().getEntityLimitGroupManager().getEntityLimitGroup(entityLimitGroupString);
        if (entityLimitGroup == null) {
            entityLimitGroup = EntityLimitGroup.DEFAULT;
        }
        List<SignData> regionsigns = parseRegionsSigns(regionSection);
        Region region;

        if (regiontype.equalsIgnoreCase("rentregion")) {
            RentPrice rentPrice;
            if (autoPriceString != null) {
                if (AutoPrice.getAutoprice(autoPriceString) != null) {
                    rentPrice = new RentPrice(AutoPrice.getAutoprice(autoPriceString));
                } else {
                    AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Could not find Autoprice '"
                            + autoPriceString + "' for region + '" + wgRegion.getId() + "'! Using Default Autoprice!");
                    rentPrice = new RentPrice(AutoPrice.DEFAULT);
                }
            } else {
                double price = regionSection.getDouble("price");
                long maxExtendTime = regionSection.getLong("maxExtendTime");
                long extendTime = regionSection.getLong("extendTime");
                try {
                    rentPrice = new RentPrice(price, extendTime, maxExtendTime);
                } catch (IllegalArgumentException e) {
                    AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "'RentPrice for region '"
                            + wgRegion.getId() + "' has an extendTime or maxExtendTime smaller than 1 second! Replacing it with "
                            + "if an extendTime and maxExtendTime of one second!");
                    rentPrice = new RentPrice(price, 1000, 1000);
                }
            }
            long payedtill = regionSection.getLong("payedTill");
            RentRegion rentRegion = new RentRegion(wgRegion, regionWorld, regionsigns, rentPrice, sold);
            rentRegion.setPayedTill(payedtill);
            region = rentRegion;

        } else if (regiontype.equalsIgnoreCase("contractregion")) {

            ContractPrice contractPrice;
            if (autoPriceString != null) {
                if (AutoPrice.getAutoprice(autoPriceString) != null) {
                    contractPrice = new ContractPrice(AutoPrice.getAutoprice(autoPriceString));
                } else {
                    AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Could not find Autoprice '"
                            + autoPriceString + "' for region + '" + wgRegion.getId() + "'! Using Default Autoprice!");
                    contractPrice = new ContractPrice(AutoPrice.DEFAULT);
                }
            } else {
                double price = regionSection.getDouble("price");
                long extendTime = regionSection.getLong("extendTime");
                try {
                    contractPrice = new ContractPrice(price, extendTime);
                } catch (IllegalArgumentException e) {
                    AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "ContractPrice for region '"
                            + wgRegion.getId() + "' has an extendTime smaller than 1 second! Replacing it with "
                            + "if an extendTime of one second!");
                    contractPrice = new ContractPrice(price, 1000);
                }
            }
            long payedtill = regionSection.getLong("payedTill");
            Boolean terminated = regionSection.getBoolean("terminated");
            ContractRegion contractRegion = new ContractRegion(wgRegion, regionWorld, regionsigns, contractPrice, sold);
            contractRegion.setTerminated(terminated);
            contractRegion.setPayedTill(payedtill);
            region = contractRegion;
        } else {
            Price sellPrice;
            if (autoPriceString != null) {
                if (AutoPrice.getAutoprice(autoPriceString) != null) {
                    sellPrice = new Price(AutoPrice.getAutoprice(autoPriceString));
                } else {
                    AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Could not find Autoprice '"
                            + autoPriceString + "' for region + '" + wgRegion.getId() + "'! Using Default Autoprice!");
                    sellPrice = new Price(AutoPrice.DEFAULT);
                }
            } else {
                double price = regionSection.getDouble("price");
                sellPrice = new Price(price);
            }
            region = new SellRegion(wgRegion, regionWorld, regionsigns, sellPrice, sold);
        }

        if (regionSection.getConfigurationSection("subregions") != null) {
            List<String> subregionsection = new ArrayList<>(regionSection.getConfigurationSection("subregions").getKeys(false));
            for (String subregionName : subregionsection) {
                WGRegion subWGRegion = AdvancedRegionMarket.getInstance().getWorldGuardInterface().getRegion(regionWorld, subregionName);
                if (subWGRegion != null) {
                    parseSubRegion(regionSection.getConfigurationSection("subregions." + subregionName), subWGRegion, region);
                }
            }
        }

        region.setRegionKind(regionKind);
        region.setFlagGroup(flagGroup);
        region.setInactivityReset(inactivityReset);
        region.setEntityLimitGroup(entityLimitGroup);
        region.setHotel(isHotel);
        region.setAutoRestore(autorestore);
        region.setLastReset(lastreset);
        region.setLastLogin(lastLogin);
        region.setMaxMembers(maxMembers);
        region.setPaybackPercentage(paybackPercentage);
        region.setAllowedSubregions(allowedSubregions);
        region.setExtraTotalEntitys(boughtExtraTotalEntitys);
        region.setLandlord(landlord);
        region.setProtectionOfContinuance(isProtectionOfContinuance);
        for(Map.Entry<EntityLimit.LimitableEntityType, Integer> entry : parseBoughtExtraEntitys(boughtExtraEntitys).entrySet()) {
            region.setExtraEntityAmount(entry.getKey(), entry.getValue());
        }
        region.setUserRestorable(userrestorable);
        region.setTeleportLocation(teleportLoc);
        region.setPlayerTeleportLocation(playerTeleportLoc);
        region.setSaved();

        try {
            region.applyFlagGroup(FlagGroup.ResetMode.NON_EDITABLE, false);
        } catch (FeatureDisabledException e) {
            //Ignore
        }

        for (Region subRegion : region.getSubregions()) {
            try {
                region.applyFlagGroup(FlagGroup.ResetMode.NON_EDITABLE, false);
            } catch (FeatureDisabledException e) {
                //Ignore
            }

            WGRegion parentRegion = region.getRegion();
            WGRegion subWGRegion = subRegion.getRegion();

            if (AdvancedRegionMarket.getInstance().getPluginSettings().isAllowParentRegionOwnersBuildOnSubregions()) {
                if (subWGRegion.getParent() == null || subWGRegion.getParent().unwrap() != parentRegion.unwrap()) {
                    subWGRegion.setParent(parentRegion);
                }
            } else {
                if ((parentRegion.getPriority() + 1) != subWGRegion.getPriority())
                    subWGRegion.setPriority(parentRegion.getPriority() + 1);
            }

        }

        return region;
    }

    private static Region parseSubRegion(ConfigurationSection section, WGRegion wgSubregion, Region parentRegion) {
        double subregPrice = section.getDouble("price");
        boolean subregIsSold = section.getBoolean("sold");
        boolean subregIsHotel = section.getBoolean("isHotel");
        String subregionRegiontype = section.getString("regiontype");
        long sublastreset = section.getLong("lastreset");
        long sublastLogin = section.getLong("lastLogin");
        List<SignData> subregionsigns = parseRegionsSigns(section);
        Region region;

        if (subregionRegiontype.equalsIgnoreCase("rentregion")) {
            long subregpayedtill = section.getLong("payedTill");
            long subregmaxExtendTime = section.getLong("maxExtendTime");
            long subregextendTime = section.getLong("extendTime");
            RentPrice subPrice = new RentPrice(subregPrice, subregextendTime, subregmaxExtendTime);
            RentRegion rentRegion = new RentRegion(wgSubregion, subregionsigns, subPrice, subregIsSold, parentRegion);
            rentRegion.setPayedTill(subregpayedtill);
            region = rentRegion;

        } else if (subregionRegiontype.equalsIgnoreCase("contractregion")) {
            long subregpayedtill = section.getLong("payedTill");
            long subregextendTime = section.getLong("extendTime");
            Boolean subregterminated = section.getBoolean("terminated");
            ContractPrice subPrice = new ContractPrice(subregPrice, subregextendTime);
            ContractRegion contractRegion = new ContractRegion(wgSubregion, subregionsigns, subPrice, subregIsSold, parentRegion);
            contractRegion.setPayedTill(subregpayedtill);
            contractRegion.setTerminated(subregterminated);
            region = contractRegion;

        } else {
            Price subPrice = new Price(subregPrice);
            region = new SellRegion(wgSubregion, subregionsigns, subPrice, subregIsSold, parentRegion);
        }
        region.setHotel(subregIsHotel);
        region.setLastReset(sublastreset);
        region.setLastLogin(sublastLogin);
        return region;
    }

    private static List<SignData> parseRegionsSigns(ConfigurationSection section) {
        List<String> regionsignsloc = section.getStringList("signs");
        List<SignData> regionsigns = new ArrayList<>();
        for (int j = 0; j < regionsignsloc.size(); j++) {
            String[] locsplit = regionsignsloc.get(j).split(";");
            World world = Bukkit.getWorld(locsplit[0]);

            if (world != null) {
                Double x = Double.parseDouble(locsplit[1]);
                Double y = Double.parseDouble(locsplit[2]);
                Double z = Double.parseDouble(locsplit[3]);
                Location loc = new Location(world, x, y, z);

                SignAttachment signAttachment;
                try {
                    signAttachment = SignAttachment.valueOf(locsplit[4]);
                } catch (IllegalArgumentException e) {
                    signAttachment = SignAttachment.GROUND;
                }

                BlockFace facing;

                try {
                    facing = BlockFace.valueOf(locsplit[5]);
                } catch (IllegalArgumentException e) {
                    facing = BlockFace.NORTH;
                }

                SignDataFactory signDataFactory = AdvancedRegionMarket.getInstance().getSignDataFactory();
                SignData signData = signDataFactory.generateSignData(loc, signAttachment, facing);

                regionsigns.add(signData);
            }
        }
        return regionsigns;
    }

    private static HashMap<EntityLimit.LimitableEntityType, Integer> parseBoughtExtraEntitys(List<String> stringList) {
        HashMap<EntityLimit.LimitableEntityType, Integer> boughtExtraEntitys = new HashMap<>();
        for (String element : stringList) {
            if (element.matches("[^;\n ]+: [0-9]+")) {
                String[] extraparts = element.split(": ");
                int extraAmount = Integer.parseInt(extraparts[1]);

                EntityLimit.LimitableEntityType limitableEntityType = EntityLimit.getLimitableEntityType(extraparts[0]);
                if (limitableEntityType == null) {
                    Bukkit.getServer().getLogger().log(Level.INFO, "Could not parse EntitysType " + extraparts[0] + " at boughtExtraEntitys. Ignoring it...");
                    continue;
                }
                boughtExtraEntitys.put(limitableEntityType, extraAmount);
            }
        }
        return boughtExtraEntitys;
    }

    private static Location parseTpLocation(String teleportLocString) {
        Location teleportLoc = null;
        if (teleportLocString != null) {
            String[] teleportLocarr = teleportLocString.split(";");
            World teleportLocWorld = Bukkit.getWorld(teleportLocarr[0]);
            double teleportLocBlockX = Double.parseDouble(teleportLocarr[1]);
            double teleportLocBlockY = Double.parseDouble(teleportLocarr[2]);
            double teleportLocBlockZ = Double.parseDouble(teleportLocarr[3]);
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

        fileupdated |= addDefault(section, "sold", false);
        fileupdated |= addDefault(section, "kind", "default");
        fileupdated |= addDefault(section, "inactivityReset", true);
        fileupdated |= addDefault(section, "lastreset", 1);
        fileupdated |= addDefault(section, "isHotel", false);
        fileupdated |= addDefault(section, "paybackPercentage", 50);
        fileupdated |= addDefault(section, "entityLimitGroup", "default");
        fileupdated |= addDefault(section, "autorestore", true);
        fileupdated |= addDefault(section, "allowedSubregions", 0);
        fileupdated |= addDefault(section, "lastLogin", new GregorianCalendar().getTimeInMillis());
        fileupdated |= addDefault(section, "userrestorable", true);
        fileupdated |= addDefault(section, "maxMembers", -1);
        fileupdated |= addDefault(section, "boughtExtraTotalEntitys", 0);
        fileupdated |= addDefault(section, "isprotectionofcontinuance", false);
        fileupdated |= addDefault(section, "boughtExtraEntitys", new ArrayList<String>());
        fileupdated |= addDefault(section, "regiontype", "sellregion");
        fileupdated |= addDefault(section, "flagGroup", "default");
        if (section.getString("regiontype").equalsIgnoreCase("rentregion")) {
            fileupdated |= addDefault(section, "payedTill", 0);
            fileupdated |= addDefault(section, "maxExtendTime", 1000);
            fileupdated |= addDefault(section, "extendTime", 1000);
        }
        if (section.getString("regiontype").equalsIgnoreCase("contractregion")) {
            fileupdated |= addDefault(section, "payedTill", 0);
            fileupdated |= addDefault(section, "extendTime", 1000);
            fileupdated |= addDefault(section, "terminated", false);
        }
        if (section.get("subregions") != null) {
            List<String> subregions = new ArrayList<String>(section.getConfigurationSection("subregions").getKeys(false));
            if (subregions != null) {
                for (String subregionID : subregions) {
                    fileupdated |= addDefault(section, "subregions." + subregionID + ".price", 0);
                    fileupdated |= addDefault(section, "subregions." + subregionID + ".sold", false);
                    fileupdated |= addDefault(section, "subregions." + subregionID + ".isHotel", false);
                    fileupdated |= addDefault(section, "subregions." + subregionID + ".lastreset", 1);
                    fileupdated |= addDefault(section, "subregions." + subregionID + ".regiontype", "sellregion");
                    if (section.getString("subregions." + subregionID + ".regiontype").equalsIgnoreCase("contractregion")) {
                        fileupdated |= addDefault(section, "subregions." + subregionID + ".payedTill", 0);
                        fileupdated |= addDefault(section, "subregions." + subregionID + ".extendTime", 1000);
                        fileupdated |= addDefault(section, "subregions." + subregionID + ".terminated", false);
                    }
                    if (section.getString("subregions." + subregionID + ".regiontype").equalsIgnoreCase("rentregion")) {
                        fileupdated |= addDefault(section, "subregions." + subregionID + ".payedTill", 0);
                        fileupdated |= addDefault(section, "subregions." + subregionID + ".maxExtendTime", 1000);
                        fileupdated |= addDefault(section, "subregions." + subregionID + ".extendTime", 1000);
                    }
                }
            }
        }

        return fileupdated;
    }


    /*##########################
    ##### Search Methods #######
    ##########################*/

    public boolean checkIfSignExists(Sign sign) {
        for (Region region : this) {
            if (region.hasSign(sign)) {
                return true;
            }
            for (Region subregion : region.getSubregions()) {
                if (subregion.hasSign(sign)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Region getRegion(Sign sign) {
        for (Region region : this) {
            if (region.hasSign(sign)) {
                return region;
            }
            for (Region subregion : region.getSubregions()) {
                if (subregion.hasSign(sign)) {
                    return subregion;
                }
            }
        }
        return null;
    }

    public Region getRegion(WGRegion wgRegion) {
        for (Region region : this) {
            if (region.getRegion().unwrap() == wgRegion.unwrap()) {
                return region;
            }
            for (Region subregion : region.getSubregions()) {
                if (subregion.getRegion().unwrap() == wgRegion.unwrap()) {
                    return subregion;
                }
            }
        }
        return null;
    }

    public Region getRegionByNameAndWorld(String regionId, String world) {
        for (Region region : this) {
            if (region.getRegionworld().getName().equalsIgnoreCase(world)) {
                if (region.getRegion().getId().equalsIgnoreCase(regionId)) {
                    return region;
                }
                for (Region subregion : region.getSubregions()) {
                    if (subregion.getRegion().getId().equalsIgnoreCase(regionId)) {
                        return subregion;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Selectes a region by using the players position or the regionID (regionName)
     *
     * @param player     the player
     * @param regionName The Name of the region. Use null or "" if you want to use the players position instead
     * @return A region (is never null)
     * @throws InputException If there are more then 1 or 0 regions at the players position or if the region with the ID regionName does not exist
     */
    public Region getRegionAtPositionOrNameCommand(Player player, String regionName) throws InputException {
        Region selectedRegion;
        if (regionName == null || regionName.equalsIgnoreCase("")) {
            List<Region> selectedRegions = this.getRegionsByLocation(player.getLocation());
            if (selectedRegions.size() == 0) {
                throw new InputException(player, Messages.NO_REGION_AT_PLAYERS_POSITION);
            }
            if (selectedRegions.size() > 1) {
                String regions = "";
                for (Region sRegion : selectedRegions) {
                    regions = regions + sRegion.getRegion().getId() + " ";
                }
                throw new InputException(player, Messages.REGION_SELECTED_MULTIPLE_REGIONS + regions);
            }
            selectedRegion = selectedRegions.get(0);
        } else {
            selectedRegion = this.getRegionbyNameAndWorldCommands(regionName, player.getWorld().getName());
            if (selectedRegion == null) {
                throw new InputException(player, Messages.REGION_DOES_NOT_EXIST);
            }
        }
        return selectedRegion;
    }

    public Region getRegionbyNameAndWorldCommands(String regionId, String world) {
        Region mayReturn = null;
        for (Region region : this) {
            if (region.getRegionworld().getName().equalsIgnoreCase(world)) {
                if (region.getRegion().getId().equalsIgnoreCase(regionId)) {
                    return region;
                }
                for (Region subregion : region.getSubregions()) {
                    if (subregion.getRegion().getId().equalsIgnoreCase(regionId)) {
                        return subregion;
                    }
                }
            } else {
                if (region.getRegion().getId().equalsIgnoreCase(regionId)) {
                    mayReturn = region;
                }
                for (Region subregion : region.getSubregions()) {
                    if (subregion.getRegion().getId().equalsIgnoreCase(regionId)) {
                        mayReturn = subregion;
                    }
                }
            }
        }
        return mayReturn;
    }

    public List<Region> getRegionsByLocation(Location location) {
        List<Region> regions = new ArrayList<>();

        HashMap<DummyChunk, List<Region>> chunkRegionList = this.getWorldChunkRegionMap().get(location.getWorld());
        if (chunkRegionList == null) {
            return regions;
        }

        DummyChunk locationChunk = DummyChunk.getUniqueDummyChunk(location.getBlockX() >> 4, location.getBlockZ() >> 4);
        List<Region> regionsInChunk = chunkRegionList.get(locationChunk);
        if (regionsInChunk == null) {
            return regions;
        }

        for (Region region : regionsInChunk) {
            if (region.getRegion().contains(location.getBlockX(), location.getBlockY(), location.getBlockZ())) {
                if (region.getRegionworld().getName().equals(location.getWorld().getName())) {
                    regions.add(region);
                }
                for (Region subregion : region.getSubregions()) {
                    if (subregion.getRegion().contains(location.getBlockX(), location.getBlockY(), location.getBlockZ())) {
                        regions.add(subregion);
                    }
                }
            }
        }
        return regions;
    }

    public List<Region> getRegionsByRegionKind(RegionKind regionKind) {
        List<Region> regions = new ArrayList<>();

        for (Region region : this) {
            if (region.getRegionKind() == regionKind) {
                regions.add(region);
                for (Region subregion : region.getSubregions()) {
                    if (subregion.getRegionKind() == regionKind) {
                        regions.add(subregion);
                    }
                }
            }
        }
        return regions;
    }

    public List<Region> getRegionsBySelltype(SellType sellType) {
        List<Region> regions = new ArrayList<>();
        for (Region region : this) {
            if (region.getSellType() == sellType) {
                regions.add(region);
                for (Region subregion : region.getSubregions()) {
                    if (subregion.getSellType() == sellType) {
                        regions.add(subregion);
                    }
                }
            }
        }
        return regions;
    }

    public List<Region> getBuyableRegions(RegionKind regionKind) {
        List<Region> regions = new ArrayList<>();

        for (Region region : this) {
            if (region.getRegionKind() == regionKind) {
                if (region.isBuyable()) {
                    regions.add(region);
                }
            }
            for (Region subregion : region.getSubregions()) {
                if (subregion.getRegionKind() == regionKind) {
                    if (subregion.isBuyable()) {
                        regions.add(subregion);
                    }
                }
            }
        }
        return regions;
    }

    public List<Region> getRegionsByMember(UUID uuid) {
        List<Region> returnme = new ArrayList<>();
        for (Region region : this) {
            for (Region subregion : region.getSubregions()) {
                if (subregion.getRegion().hasMember(uuid)) {
                    returnme.add(subregion);
                }
            }
            if (region.getRegion().hasMember(uuid)) {
                returnme.add(region);
            }
        }
        return returnme;
    }

    public List<Region> getRegionsByOwner(UUID uuid) {
        List<Region> returnme = new ArrayList<>();
        for (Region region : this) {
            for (Region subregion : region.getSubregions()) {
                if (subregion.getRegion().hasOwner(uuid)) {
                    returnme.add(subregion);
                }
            }
            if (region.getRegion().hasOwner(uuid)) {
                returnme.add(region);
            }
        }
        return returnme;
    }

    public boolean containsRegion(Region region) {

        for (Region mangerRegion : this) {
            if (mangerRegion == region) {
                return true;
            }
        }
        return false;
    }

    public List<String> completeTabRegions(Player player, String arg, PlayerRegionRelationship playerRegionRelationship, boolean inculdeNormalRegions, boolean includeSubregions) {
        List<String> returnme = new ArrayList<>();

        if (this.tabCompleteRegions) {
            for (Region region : this) {
                if (inculdeNormalRegions) {
                    if (region.getRegion().getId().toLowerCase().startsWith(arg)) {
                        if (playerRegionRelationship == PlayerRegionRelationship.OWNER) {
                            if (region.getRegion().hasOwner(player.getUniqueId())) {
                                returnme.add(region.getRegion().getId());
                            }
                        } else if (playerRegionRelationship == PlayerRegionRelationship.MEMBER) {
                            if (region.getRegion().hasMember(player.getUniqueId())) {
                                returnme.add(region.getRegion().getId());
                            }
                        } else if (playerRegionRelationship == PlayerRegionRelationship.MEMBER_OR_OWNER) {
                            if (region.getRegion().hasMember(player.getUniqueId()) || region.getRegion().hasOwner(player.getUniqueId())) {
                                returnme.add(region.getRegion().getId());
                            }
                        } else if (playerRegionRelationship == PlayerRegionRelationship.ALL) {
                            returnme.add(region.getRegion().getId());
                        } else if (playerRegionRelationship == PlayerRegionRelationship.AVAILABLE) {
                            if (!region.isSold()) {
                                returnme.add(region.getRegion().getId());
                            }
                        }
                    }
                }
                if (includeSubregions) {
                    for (Region subregion : region.getSubregions()) {
                        if (subregion.getRegion().getId().toLowerCase().startsWith(arg)) {
                            if (playerRegionRelationship == PlayerRegionRelationship.OWNER) {
                                if (subregion.getRegion().hasOwner(player.getUniqueId())) {
                                    returnme.add(subregion.getRegion().getId());
                                }
                            } else if (playerRegionRelationship == PlayerRegionRelationship.MEMBER) {
                                if (subregion.getRegion().hasMember(player.getUniqueId())) {
                                    returnme.add(subregion.getRegion().getId());
                                }
                            } else if (playerRegionRelationship == PlayerRegionRelationship.MEMBER_OR_OWNER) {
                                if (subregion.getRegion().hasMember(player.getUniqueId()) || subregion.getRegion().hasOwner(player.getUniqueId())) {
                                    returnme.add(subregion.getRegion().getId());
                                }
                            } else if (playerRegionRelationship == PlayerRegionRelationship.ALL) {
                                returnme.add(subregion.getRegion().getId());
                            } else if (playerRegionRelationship == PlayerRegionRelationship.AVAILABLE) {
                                if (!subregion.isSold()) {
                                    returnme.add(subregion.getRegion().getId());
                                }
                            } else if (playerRegionRelationship == PlayerRegionRelationship.PARENTREGION_OWNER) {
                                if (subregion.getParentRegion().getRegion().hasOwner(player.getUniqueId())) {
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


    /*#############################
    ##### Other methods ###########
    #############################*/

    public void setTabCompleteRegions(boolean tabCompleteRegions) {
        this.tabCompleteRegions = tabCompleteRegions;
    }

    public Region teleportToBuyableRegion(RegionKind type, Player player) throws InputException {
        for (Region region : this) {

            if ((region.isBuyable()) && (region.getRegionKind() == type)) {
                try {
                    String message = region.replaceVariables(Messages.REGION_TELEPORT_MESSAGE);
                    Teleporter.teleport(player, region, Messages.PREFIX + message, true);
                } catch (NoSaveLocationException e) {
                    continue;
                }
                return region;
            }
        }
        throw new InputException(player, Messages.NO_FREE_REGION_WITH_THIS_KIND);
    }


    /*###########################
    ####### Inner classes #######
    ############################*/

    private void addToWorldChunkMap(Region region) {
        HashMap<DummyChunk, List<Region>> chunkRegionMap = this.getWorldChunkRegionMap().get(region.getRegionworld());
        if (chunkRegionMap == null) {
            chunkRegionMap = new HashMap<>();
            this.getWorldChunkRegionMap().put(region.getRegionworld(), chunkRegionMap);
        }
        Set<DummyChunk> regionChunks = DummyChunk.getChunks(region);

        for (DummyChunk chunk : regionChunks) {
            List<Region> chunkRegions = chunkRegionMap.get(chunk);
            if (chunkRegions == null) {
                chunkRegions = new ArrayList<>();
                chunkRegionMap.put(chunk, chunkRegions);
            }
            chunkRegions.add(region);
        }
    }

    private void removeFromWorldChunkMap(Region region) {
        HashMap<DummyChunk, List<Region>> chunkRegionMap = this.getWorldChunkRegionMap().get(region.getRegionworld());
        if (chunkRegionMap != null) {
            Set<DummyChunk> regionChunks = DummyChunk.getChunks(region);
            for (DummyChunk chunk : regionChunks) {
                List<Region> regionList = chunkRegionMap.get(chunk);
                if (regionList != null) {
                    regionList.remove(region);
                    if (regionList.isEmpty()) {
                        chunkRegionMap.remove(chunk);
                        if (chunkRegionMap.isEmpty()) {
                            this.getWorldChunkRegionMap().remove(region.getRegionworld());
                        }
                    }
                }
            }
        }
    }

    private HashMap<World, HashMap<DummyChunk, List<Region>>> getWorldChunkRegionMap() {
        return this.worldChunkRegionMap;
    }

    public void doTick() {
        this.updateScheduler.updateNextGroup();
    }

    private static class DummyChunk {
        private static HashMap<Long, DummyChunk> dummyChunks = new HashMap<>();
        private int x;
        private int z;

        private DummyChunk(int x, int z) {
            this.x = x;
            this.z = z;
        }

        static DummyChunk getUniqueDummyChunk(int x, int y) {
            DummyChunk newChunk = new DummyChunk(x, y);
            DummyChunk chunk = dummyChunks.get(newChunk.coordianteHash());
            if (chunk == null) {
                chunk = newChunk;
                dummyChunks.put(chunk.coordianteHash(), chunk);
            }
            return chunk;
        }

        static Set<DummyChunk> getChunks(Region region) {
            Set<DummyChunk> chunkSet = new HashSet<>();
            int maxX = region.getRegion().getMaxPoint().getBlockX();
            int maxZ = region.getRegion().getMaxPoint().getBlockZ();

            for (int x = region.getRegion().getMinPoint().getBlockX(); x <= maxX + 16; x += 16) {
                for (int z = region.getRegion().getMinPoint().getBlockZ(); z <= maxZ + 16; z += 16) {
                    chunkSet.add(getUniqueDummyChunk(x >> 4, z >> 4));
                }
            }
            return chunkSet;
        }

        private long coordianteHash() {
            return this.x * 10000000 + this.z;
        }
    }

    private class UpdateScheduler {
        private int nextToUpdate = 0;
        private List<Region>[] updateQuenue;

        UpdateScheduler(int ticks) {
            this.updateQuenue = new List[ticks];
            for (int i = 0; i < this.updateQuenue.length; i++) {
                this.updateQuenue[i] = new ArrayList<>();
            }
            this.rearrangeUpdateQuenue();
        }

        void rearrangeUpdateQuenue() {
            List<Region>[] newUpdateQuenue = new List[this.updateQuenue.length];
            for (int i = 0; i < newUpdateQuenue.length; i++) {
                newUpdateQuenue[i] = new ArrayList<>();
            }

            //Map regionSigns to chunks
            HashMap<DummyChunk, List<Region>> signMap = new HashMap<>();
            for (Region region : RegionManager.this) {
                for (SignData signData : region.getSellSigns()) {
                    Location sLoc = signData.getLocation();
                    DummyChunk chunk = DummyChunk.getUniqueDummyChunk(sLoc.getBlockX() >> 4, sLoc.getBlockZ() >> 4);
                    List<Region> regions = signMap.get(chunk);
                    if (regions == null) {
                        regions = new ArrayList<>();
                        signMap.put(chunk, regions);
                    }
                    regions.add(region);
                }
                if(region.getSellSigns().isEmpty()){
                    Vector sLoc = region.getRegion().getMaxPoint();
                    DummyChunk chunk = DummyChunk.getUniqueDummyChunk(sLoc.getBlockX() >> 4, sLoc.getBlockZ() >> 4);
                    List<Region> regions = signMap.get(chunk);
                    if (regions == null) {
                        regions = new ArrayList<>();
                        signMap.put(chunk, regions);
                    }
                    regions.add(region);
                }
            }
            HashSet<Region> scheduledRegions = new HashSet<>();

            //Try to distribute regions from the same chuck to different update-ticks as good as possible
            int index = 0;
            for (List<Region> regionChunk : signMap.values()) {
                for (Region region : regionChunk) {
                    if (!scheduledRegions.contains(region)) {
                        scheduledRegions.add(region);
                        newUpdateQuenue[index].add(region);
                        index++;
                        index %= newUpdateQuenue.length;
                    }
                }
            }

            this.updateQuenue = newUpdateQuenue;
        }

        void updateNextGroup() {
            List<Region> toUpdate = this.updateQuenue[this.nextToUpdate++];
            this.nextToUpdate %= this.updateQuenue.length;

            for (Region region : toUpdate) {
                region.updateRegion();
                for (Region subregion : region.getSubregions()) {
                    subregion.updateRegion();
                }
            }

        }
    }


}
