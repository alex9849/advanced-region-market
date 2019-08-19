package net.alex9849.arm.regions;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.ArmSettings;
import net.alex9849.arm.Messages;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.events.AddRegionEvent;
import net.alex9849.arm.events.RemoveRegionEvent;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.minifeatures.teleporter.Teleporter;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.ContractPrice;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.arm.regions.price.RentPrice;
import net.alex9849.arm.util.YamlFileManager;
import net.alex9849.exceptions.InputException;
import net.alex9849.exceptions.SchematicNotFoundException;
import net.alex9849.inter.WGRegion;
import net.alex9849.signs.SignAttachment;
import net.alex9849.signs.SignData;
import net.alex9849.signs.SignDataFactory;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

public class RegionManager extends YamlFileManager<Region> {

    private HashMap<World, HashMap<Chunk, List<Region>>> worldChunkRegionMap;

    public RegionManager(File savepath) {
        super(savepath);
    }

    @Override
    public boolean add(Region region) {
        return this.add(region, false);
    }

    @Override
    public boolean add(Region region, boolean unsafe) {
        AddRegionEvent addRegionEvent = new AddRegionEvent(region);
        Bukkit.getServer().getPluginManager().callEvent(addRegionEvent);
        if(addRegionEvent.isCancelled()) {
            return false;
        }
        if(super.add(region, unsafe)) {
            this.addToWorldChunkMap(region);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Region region) {
        RemoveRegionEvent removeRegionEvent = new RemoveRegionEvent(region);
        Bukkit.getServer().getPluginManager().callEvent(removeRegionEvent);
        if(removeRegionEvent.isCancelled()) {
            return false;
        }

        if(super.remove(region)) {
            this.removeFromWorldChunkMap(region);
            return true;
        }
        return false;
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

        for(Region region : loadedRegions) {
            this.addToWorldChunkMap(region);
        }

        return loadedRegions;
    }

    @Override
    public boolean staticSaveQuenued() {
        return false;
    }

    private void addToWorldChunkMap(Region region) {
        HashMap<Chunk, List<Region>> chunkRegionMap = this.getWorldChunkRegionMap().get(region.getRegionworld());
        if(chunkRegionMap == null) {
            chunkRegionMap = new HashMap<>();
            this.getWorldChunkRegionMap().put(region.getRegionworld(), chunkRegionMap);
        }
        Set<Chunk> regionChunks = region.getChunks();

        for (Chunk chunk : regionChunks) {
            List<Region> chunkRegions = chunkRegionMap.get(chunk);
            if(chunkRegions == null) {
                chunkRegions = new ArrayList<>();
                chunkRegionMap.put(chunk, chunkRegions);
            }
            chunkRegions.add(region);
        }
    }

    private void removeFromWorldChunkMap(Region region) {
        HashMap<Chunk, List<Region>> chunkRegionMap = this.getWorldChunkRegionMap().get(region.getRegionworld());
        if(chunkRegionMap != null) {
            Set<Chunk> regionChunks = region.getChunks();
            for(Chunk chunk : regionChunks) {
                List<Region> regionList = chunkRegionMap.get(chunk);
                if(regionList != null) {
                    regionList.remove(region);
                    if(regionList.isEmpty()) {
                        chunkRegionMap.remove(chunk);
                        if(chunkRegionMap.isEmpty()) {
                            this.getWorldChunkRegionMap().remove(region.getRegionworld());
                        }
                    }
                }
            }
        }
    }

    private HashMap<World, HashMap<Chunk, List<Region>>> getWorldChunkRegionMap() {
        if(this.worldChunkRegionMap == null) {
            this.worldChunkRegionMap = new HashMap<>();
        }
        return this.worldChunkRegionMap;
    }

    private static Region parseRegion(ConfigurationSection regionSection, World regionWorld, WGRegion wgRegion) {
        boolean sold = regionSection.getBoolean("sold");
        String kind = regionSection.getString("kind");
        String flagGroupString = regionSection.getString("flagGroup");
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
        FlagGroup flagGroup = AdvancedRegionMarket.getFlagGroupManager().getFlagGroup(flagGroupString);
        if(flagGroup == null) {
            flagGroup = FlagGroup.DEFAULT;
        }
        if(regionKind == null) {
            regionKind = RegionKind.DEFAULT;
        }
        EntityLimitGroup entityLimitGroup = AdvancedRegionMarket.getEntityLimitGroupManager().getEntityLimitGroup(entityLimitGroupString);
        if(entityLimitGroup == null) {
            entityLimitGroup = EntityLimitGroup.DEFAULT;
        }
        HashMap<EntityType, Integer> extraEntitysMap = parseBoughtExtraEntitys(boughtExtraEntitys);
        List<SignData> regionsigns = parseRegionsSigns(regionSection);

        List<Region> subregions = new ArrayList<>();
        if (regionSection.getConfigurationSection("subregions") != null) {
            List<String> subregionsection = new ArrayList<>(regionSection.getConfigurationSection("subregions").getKeys(false));
            if (subregionsection != null) {
                for (String subregionName : subregionsection) {
                    WGRegion subWGRegion = AdvancedRegionMarket.getWorldGuardInterface().getRegion(regionWorld, AdvancedRegionMarket.getWorldGuard(), subregionName);
                    if(subWGRegion != null) {
                        Region armSubRegion = parseSubRegion(regionSection.getConfigurationSection("subregions." + subregionName), regionWorld, subWGRegion);
                        subregions.add(armSubRegion);
                    }
                }
            }
        }

        Region region;

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
            region = new RentRegion(wgRegion, regionWorld, regionsigns, rentPrice, sold, autoreset, allowonlynewblocks, doBlockReset, regionKind, flagGroup, teleportLoc,
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
            region = new ContractRegion(wgRegion, regionWorld, regionsigns, contractPrice, sold, autoreset, allowonlynewblocks, doBlockReset, regionKind, flagGroup, teleportLoc,
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
            region = new SellRegion(wgRegion, regionWorld, regionsigns, sellPrice, sold, autoreset, allowonlynewblocks, doBlockReset, regionKind, flagGroup, teleportLoc, lastreset,
                    isUserResettable, subregions, allowedSubregions, entityLimitGroup, extraEntitysMap, boughtExtraTotalEntitys);

        }

        region.applyFlagGroup(FlagGroup.ResetMode.NON_EDITABLE);

        for(Region subRegion : region.getSubregions()) {
            region.applyFlagGroup(FlagGroup.ResetMode.NON_EDITABLE);

            WGRegion parentRegion = region.getRegion();
            WGRegion subWGRegion = subRegion.getRegion();

            if (ArmSettings.isAllowParentRegionOwnersBuildOnSubregions()) {
                if (subWGRegion.getParent() == null || !subWGRegion.getParent().equals(parentRegion)) {
                    subWGRegion.setParent(parentRegion);
                }
            } else {
                if ((parentRegion.getPriority() + 1) != subWGRegion.getPriority())
                    subWGRegion.setPriority(parentRegion.getPriority() + 1);
            }

        }

        return region;
    }

    private static Region parseSubRegion(ConfigurationSection section, World regionWorld, WGRegion subregion) {
        double subregPrice = section.getDouble("price");
        boolean subregIsSold = section.getBoolean("sold");
        boolean subregIsHotel = section.getBoolean("isHotel");
        String subregionRegiontype = section.getString("regiontype");
        long sublastreset = section.getLong("lastreset");
        List<SignData> subregionsigns = parseRegionsSigns(section);

        if (subregionRegiontype.equalsIgnoreCase("rentregion")) {
            long subregpayedtill = section.getLong("payedTill");
            long subregmaxRentTime = section.getLong("maxRentTime");
            long subregrentExtendPerClick = section.getLong("rentExtendPerClick");
            RentPrice subPrice = new RentPrice(subregPrice, subregrentExtendPerClick, subregmaxRentTime);
            return new RentRegion(subregion, regionWorld, subregionsigns, subPrice, subregIsSold, ArmSettings.isSubregionAutoReset(), subregIsHotel, ArmSettings.isSubregionBlockReset(), RegionKind.SUBREGION, FlagGroup.SUBREGION, null,
                    sublastreset, ArmSettings.isAllowSubRegionUserReset(), subregpayedtill, new ArrayList<Region>(), 0, EntityLimitGroup.SUBREGION, new HashMap<>(), 0);

        }  else if (subregionRegiontype.equalsIgnoreCase("contractregion")) {
            long subregpayedtill = section.getLong("payedTill");
            long subregextendTime = section.getLong("extendTime");
            Boolean subregterminated = section.getBoolean("terminated");
            ContractPrice subPrice = new ContractPrice(subregPrice, subregextendTime);
            return new ContractRegion(subregion, regionWorld, subregionsigns, subPrice, subregIsSold, ArmSettings.isSubregionAutoReset(), subregIsHotel, ArmSettings.isSubregionBlockReset(), RegionKind.SUBREGION, FlagGroup.SUBREGION, null,
                    sublastreset, ArmSettings.isAllowSubRegionUserReset(), subregpayedtill, subregterminated, new ArrayList<Region>(), 0, EntityLimitGroup.SUBREGION, new HashMap<>(), 0);

        } else {
            Price subPrice = new Price(subregPrice);
            return new SellRegion(subregion, regionWorld, subregionsigns, subPrice, subregIsSold, ArmSettings.isSubregionAutoReset(), subregIsHotel, ArmSettings.isSubregionBlockReset(), RegionKind.SUBREGION, FlagGroup.SUBREGION, null,
                    sublastreset, ArmSettings.isAllowSubRegionUserReset(), new ArrayList<Region>(), 0, EntityLimitGroup.SUBREGION, new HashMap<>(), 0);
        }
    }

    private static List<SignData> parseRegionsSigns(ConfigurationSection section) {
        List<String> regionsignsloc = section.getStringList("signs");
        List<SignData> regionsigns = new ArrayList<>();
        for(int j = 0; j < regionsignsloc.size(); j++) {
            String[] locsplit = regionsignsloc.get(j).split(";");
            World world = Bukkit.getWorld(locsplit[0]);

            if(world != null) {
                Double x = Double.parseDouble(locsplit[1]);
                Double yy = Double.parseDouble(locsplit[2]);
                Double z = Double.parseDouble(locsplit[3]);
                Location loc = new Location(world, x, yy, z);
                //Location locminone = new Location(world, x, yy - 1, z);

                //boolean isWallSign = false;

                SignAttachment signAttachment = SignAttachment.GROUND_SIGN;
                if(locsplit[4].equalsIgnoreCase("WALL")) {
                    signAttachment = SignAttachment.WALL_SIGN;
                }

                BlockFace facing;

                try {
                    facing = BlockFace.valueOf(locsplit[5]);
                } catch (IllegalArgumentException e) {
                    facing = BlockFace.NORTH;
                }

                /*
                if (!MaterialFinder.getSignMaterials().contains(loc.getBlock().getType())){
                    if(!isWallSign){
                        if(locminone.getBlock().getType() == Material.AIR || locminone.getBlock().getType() == Material.LAVA || locminone.getBlock().getType() == Material.WATER
                                || locminone.getBlock().getType() == Material.LAVA || locminone.getBlock().getType() == Material.WATER) {
                            locminone.getBlock().setType(Material.STONE);
                        }
                    }

                    if(isWallSign) {
                        loc.getBlock().setType(MaterialFinder.getWallSign(), false);
                    } else {
                        loc.getBlock().setType(MaterialFinder.getSign(), false);
                    }
                }
                */

                SignDataFactory signDataFactory = AdvancedRegionMarket.getSignDataFactory();
                SignData signData = signDataFactory.generateSignData(loc, signAttachment, facing);

                regionsigns.add(signData);
            }
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
                    Bukkit.getServer().getLogger().log(Level.INFO, "Could not parse EntitysType " + extraparts[0] + " at boughtExtraEntitys. Ignoring it...");
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
        fileupdated |= this.addDefault(section,"flagGroup", "default");
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
        yamlConfiguration.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId(), region.toConfigurationSection());
    }

    @Override
    public void writeStaticSettings(YamlConfiguration yamlConfiguration) {

    }

    public List<Region> getRegionsByMember(UUID uuid) {
        List<Region> returnme = new ArrayList<>();
        for (Region region : this){
            for(Region subregion : region.getSubregions()) {
                if(subregion.getRegion().hasMember(uuid)){
                    returnme.add(subregion);
                }
            }
            if(region.getRegion().hasMember(uuid)) {
                returnme.add(region);
            }
        }
        return returnme;
    }

    public List<Region> getRegionsByOwner(UUID uuid) {
        List<Region> returnme = new ArrayList<>();
        for (Region region : this){
            for(Region subregion : region.getSubregions()) {
                if(subregion.getRegion().hasOwner(uuid)){
                    returnme.add(subregion);
                }
            }
            if(region.getRegion().hasOwner(uuid)) {
                returnme.add(region);
            }
        }
        return returnme;
    }

    public List<Region> getRegionsByOwnerOrMember(UUID uuid) {
        List<Region> returnme = new ArrayList<>();
        for (Region region : this){
            for(Region subregion : region.getSubregions()) {
                if(subregion.getRegion().hasOwner(uuid) || subregion.getRegion().hasMember(uuid)){
                    returnme.add(subregion);
                }
            }
            if(region.getRegion().hasOwner(uuid) || region.getRegion().hasMember(uuid)) {
                returnme.add(region);
            }
        }
        return returnme;
    }

    public boolean autoResetRegionsFromOwner(UUID uuid){
        List<Region> regions = this.getRegionsByOwner(uuid);
        for(Region region : regions){
            if(region.getAutoreset()){
                region.unsell();
                if(region.isDoBlockReset()) {
                    try {
                        region.resetBlocks();
                    } catch (SchematicNotFoundException e) {
                        Bukkit.getLogger().log(Level.WARNING, "Could not find schematic file for region " + region.getRegion().getId() + "in world " + region.getRegionworld().getName());
                    }
                }
            }
        }
        return true;
    }

    public void teleportToFreeRegion(RegionKind type, Player player) throws InputException {
        for (Region region : this){

            if ((region.isSold() == false) && (region.getRegionKind() == type)){
                WGRegion regionTP = region.getRegion();
                String message = region.getConvertedMessage(Messages.REGION_TELEPORT_MESSAGE);
                Teleporter.teleport(player, region, Messages.PREFIX + message, true);
                return;
            }
        }
        throw new InputException(player, Messages.NO_FREE_REGION_WITH_THIS_KIND);
    }

    public boolean checkIfSignExists(Sign sign) {
        for(Region region : this){
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

    public Region getRegion(Sign sign) {
        for(Region region : this) {
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

    public Region getRegion(WGRegion wgRegion) {
        for(Region region : this) {
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

    public Region getRegionByNameAndWorld(String name, String world){
        for(Region region : this) {
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

    public Region getRegionbyNameAndWorldCommands(String name, String world) {
        Region mayReturn = null;
        for(Region region : this) {
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

    public List<Region> getRegionsByLocation(Location location) {
        List<Region> regions = new ArrayList<>();

        HashMap<Chunk, List<Region>> chunkRegionList = this.getWorldChunkRegionMap().get(location.getWorld());
        if(chunkRegionList == null) {
            return regions;
        }

        Chunk locationChunk = location.getChunk();
        List<Region> regionsInChunk = chunkRegionList.get(locationChunk);
        if(regionsInChunk == null) {
            return regions;
        }

        for(Region region : regionsInChunk) {
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

    public List<Region> getRegionsByRegionKind(RegionKind regionKind) {
        List<Region> regions = new ArrayList<>();

        for(Region region : this) {
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

    public List<Region> getRegionsBySelltype(SellType sellType) {
        List<Region> regions = new ArrayList<>();
        for(Region region : this) {
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

    public  List<Region> getFreeRegions(RegionKind regionKind) {
        List<Region> regions = new ArrayList<>();

        for(Region region : this) {
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

    public boolean containsRegion(Region region) {

        for(Region mangerRegion : this) {
            if(mangerRegion == region) {
                return true;
            }
        }
        return false;
    }

    public List<String> completeTabRegions(Player player, String arg, PlayerRegionRelationship playerRegionRelationship, boolean inculdeNormalRegions, boolean includeSubregions) {
        List<String> returnme = new ArrayList<>();

        if(Region.completeTabRegions) {
            for(Region region : this) {
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

    public void updateRegions(){
        for(Region region : this) {
            region.updateRegion();
            for(Region subregion : region.getSubregions()) {
                subregion.updateRegion();
            }
        }
    }

    /**
     * Selectes a region by using the players position or the regionID (regionName)
     * @param player the player
     * @param regionName The Name of the region. Use null or "" if you want to use the players position instead
     * @return A region (is never null)
     * @throws InputException If there are more then 1 or 0 regions at the players position or if the region with the ID regionName does not exist
     */
    public Region getRegionAtPositionOrNameCommand(Player player, String regionName) throws InputException {
        Region selectedRegion;
        if(regionName == null || regionName.equalsIgnoreCase("")) {
            List<Region> selectedRegions = this.getRegionsByLocation(player.getLocation());
            if(selectedRegions.size() == 0) {
                throw new InputException(player, Messages.NO_REGION_AT_PLAYERS_POSITION);
            }
            if(selectedRegions.size() > 1) {
                String regions = "";
                for(Region sRegion : selectedRegions) {
                    regions = regions + sRegion.getRegion().getId() + " ";
                }
                throw new InputException(player, Messages.REGION_SELECTED_MULTIPLE_REGIONS + regions);
            }
            selectedRegion = selectedRegions.get(0);
        } else {
            selectedRegion = this.getRegionbyNameAndWorldCommands(regionName, player.getWorld().getName());
            if(selectedRegion == null) {
                throw new InputException(player, Messages.REGION_DOES_NOT_EXIST);
            }
        }
        return selectedRegion;
    }


}
