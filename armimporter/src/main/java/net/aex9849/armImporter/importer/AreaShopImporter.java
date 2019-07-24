package net.aex9849.armImporter.importer;

import me.wiefferink.areashop.AreaShop;
import me.wiefferink.areashop.features.signs.RegionSign;
import me.wiefferink.areashop.regions.BuyRegion;
import me.wiefferink.areashop.regions.GeneralRegion;
import me.wiefferink.areashop.regions.RegionGroup;
import me.wiefferink.areashop.regions.RentRegion;
import me.wiefferink.areashop.shaded.interactivemessenger.processing.Message;
import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.arm.regions.price.RentPrice;
import net.alex9849.arm.util.MaterialFinder;
import net.alex9849.inter.WGRegion;
import net.alex9849.signs.SignAttachment;
import net.alex9849.signs.SignData;
import net.alex9849.signs.SignDataFactory;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.FileUtil;

import java.io.File;
import java.util.*;

public class AreaShopImporter {

    public static void importRegionKinds(boolean overwriteExisting) {
        Collection<RegionGroup> groups =  AreaShop.getInstance().getFileManager().getGroups();
        for(RegionGroup group : groups) {
            String regionKindName = group.getName();
            RegionKind existingArmKind = AdvancedRegionMarket.getRegionKindManager().getRegionKind(regionKindName);
            RegionKind armRegionKind = new RegionKind(regionKindName, MaterialFinder.getRedBed(), new ArrayList<String>(), regionKindName, true, true, 0.5);
            if(existingArmKind != null) {
                if(overwriteExisting) {
                    AdvancedRegionMarket.getRegionKindManager().remove(existingArmKind);
                    AdvancedRegionMarket.getRegionKindManager().add(armRegionKind, true);
                } else {
                    Bukkit.getLogger().info("Skipped regionkind " + regionKindName + "! A regionkind with this name already exists!");
                }
            } else {
                AdvancedRegionMarket.getRegionKindManager().add(armRegionKind, true);
            }
        }
    }

    public static void importRegions(boolean overwriteExisting, int delayBetweenSchematicImport) {
        Collection<GeneralRegion> regions = AreaShop.getInstance().getFileManager().getRegions();
        ScheamticImporter scheamticImporter = new ScheamticImporter();

        for(GeneralRegion asRegion : regions) {
            World regionWorld = asRegion.getWorld();

            WGRegion wgRegion = AdvancedRegionMarket.getWorldGuardInterface().getRegion(regionWorld, AdvancedRegionMarket.getWorldGuard(), asRegion.getRegion().getId());

            UUID regionOwner = asRegion.getOwner();

            if(regionOwner != null) {
                OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(regionOwner);
                if(oPlayer != null) {
                    wgRegion.setOwner(oPlayer);
                }
            }


            if(wgRegion != null) {
                List<RegionSign> asRegionSings = asRegion.getSignsFeature().getSigns();
                List<SignData> armSignDataList = new ArrayList<SignData>();
                SignDataFactory signDataFactory = AdvancedRegionMarket.getSignDataFactory();

                for(RegionSign asRegionSign : asRegionSings) {
                    SignAttachment signAttachment;
                    if(asRegionSign.getMaterial().toString().contains("WALL_SIGN") || asRegionSign.getMaterial().toString().equals("WALL_SIGN")) {
                        signAttachment = SignAttachment.WALL_SIGN;
                    } else {
                        signAttachment = SignAttachment.GROUND_SIGN;
                    }
                    armSignDataList.add(signDataFactory.generateSignData(asRegionSign.getLocation(), signAttachment, asRegionSign.getFacing()));
                }

                RegionKind regionKind = null;

                List<RegionGroup> groups = new ArrayList<RegionGroup>(asRegion.getGroups());

                for(RegionGroup group : groups) {
                    if(regionKind == null) {
                        regionKind = AdvancedRegionMarket.getRegionKindManager().getRegionKind(group.getName());
                    }
                }

                if(regionKind == null) {
                    regionKind = RegionKind.DEFAULT;
                }

                Region armRegion = null;
                boolean isSold = !asRegion.isAvailable();


                if(asRegion instanceof BuyRegion) {
                    BuyRegion buyRegion = (BuyRegion) asRegion;
                    double price = buyRegion.getPrice();

                    Price armPrice = new Price(price);

                    armRegion = new net.alex9849.arm.regions.SellRegion(wgRegion, regionWorld, armSignDataList, armPrice, isSold, true, false,
                            true, regionKind, FlagGroup.DEFAULT, null, 0, true, new ArrayList<Region>(), 0,
                            EntityLimitGroup.DEFAULT, new HashMap<EntityType, Integer>(), 0);

                } else if(asRegion instanceof RentRegion) {
                    RentRegion rentRegion = (RentRegion) asRegion;
                    double price = rentRegion.getPrice();
                    long extendTime = rentRegion.getDuration();
                    long maxRentTime = rentRegion.getMaxRentTime();
                    long payedTill = rentRegion.getRentedUntil();

                    RentPrice armPrice = new RentPrice(price, extendTime, maxRentTime);

                    armRegion = new net.alex9849.arm.regions.RentRegion(wgRegion, regionWorld, armSignDataList, armPrice, isSold, true, false,
                            true, regionKind, FlagGroup.DEFAULT, null, 0, true, payedTill, new ArrayList<Region>(), 0,
                            EntityLimitGroup.DEFAULT, new HashMap<EntityType, Integer>(), 0);
                }

                if(armRegion != null) {
                    Region armExistingRegion = AdvancedRegionMarket.getRegionManager().getRegion(wgRegion);

                    if(armExistingRegion != null) {
                        if(overwriteExisting) {
                            AdvancedRegionMarket.getRegionManager().remove(armExistingRegion);
                            AdvancedRegionMarket.getRegionManager().add(armRegion, true);
                            scheamticImporter.scheduleSchematic(armRegion, getSchematicPath(asRegion));
                        } else {
                            Bukkit.getLogger().info("Skipped regionkind " + armRegion.getRegion().getId() + "! A regionkind with this name already exists!");
                        }
                    } else {
                        AdvancedRegionMarket.getRegionManager().add(armRegion, true);
                        scheamticImporter.scheduleSchematic(armRegion, getSchematicPath(asRegion));
                    }

                }

            }

        }
        scheamticImporter.run(delayBetweenSchematicImport);

    }

    public static void createBackup(File output) {
        File armFolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        try {
            Zipper.zipDir(armFolder, output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static File getSchematicPath(GeneralRegion asRegion) {
        ConfigurationSection profileSection = asRegion.getConfigurationSectionSetting("general.schematicProfile", "schematicProfiles");
        if(profileSection == null) {
            return null;
        }

        String restore = profileSection.getString(GeneralRegion.RegionEvent.CREATED.getValue() + ".save");

        // Restore the region if needed
        if(restore != null && !restore.equalsIgnoreCase("")) {
            restore = Message.fromString(restore).replacements(asRegion).getSingle();
            File schemFile = new File(AreaShop.getInstance().getFileManager().getSchematicFolder() + "/" + restore + ".schem");
            if(!schemFile.exists()) {
                schemFile = new File(AreaShop.getInstance().getFileManager().getSchematicFolder() + "/" + restore + ".schematic");
            }
            if(!schemFile.exists()) {
                return null;
            }
            return schemFile;
        }
        return null;

    }

    static class ScheamticImporter {
        Map<Region, File> regionFileMap = new HashMap<Region, File>();

        public void scheduleSchematic(Region armRegion, File areaShopSchemPath) {
            this.regionFileMap.put(armRegion, areaShopSchemPath);
        }

        public void run(int delayInTicks) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("ARMImporter");
            final File armSchemFolder = new File(AdvancedRegionMarket.getARM().getDataFolder() + "/schematics");
            List<Region> regions = new ArrayList<Region>(this.regionFileMap.keySet());

            final int regionsSize = regions.size();

            for(int i = 0; i < regions.size(); i++) {
                final int finalI = i;
                final Region finalRegion = regions.get(i);
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    public void run() {
                        Region region = finalRegion;
                        File areaShopRegionSchemDic = regionFileMap.get(region);
                        File armWorldSchemDic = new File(armSchemFolder + "/" + region.getRegionworld().getName());
                        File armRegionSchemDic = new File(armWorldSchemDic + "/" + region.getRegion().getId() + ".schem");

                        if(!armWorldSchemDic.exists()) {
                            armWorldSchemDic.mkdirs();
                        }

                        if(areaShopRegionSchemDic != null) {
                            if(areaShopRegionSchemDic.exists()) {
                                FileUtil.copy(areaShopRegionSchemDic, armRegionSchemDic);
                            } else {
                                region.createSchematic();
                            }
                        } else {
                            region.createSchematic();
                        }

                        Bukkit.getLogger().info("Imported schematic for region " + region.getRegion().getId() + "! (" + (finalI + 1) + "/" + regionsSize + ")");
                    }
                }, i * delayInTicks);

            }
        }

    }
}
