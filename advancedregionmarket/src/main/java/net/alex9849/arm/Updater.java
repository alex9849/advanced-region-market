package net.alex9849.arm;

import net.alex9849.arm.regions.Region;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class Updater {

    static void updateConfigs() {
        FileConfiguration pluginConfig = AdvancedRegionMarket.getInstance().getConfig();
        String[] versionPartsAsString = pluginConfig.getString("Version").split("\\.");
        int[] versionParts = Arrays.stream(versionPartsAsString).mapToInt(Integer::parseInt).toArray();
        Version lastVersion = new Version(versionParts);

        //convert legacy versionNumbers
        if(versionParts.length > 1) {
            int major = versionParts[0];
            int minor = versionParts[1];
            if(major < 3) {
                List<Integer> legacyVersionParts = new ArrayList<>();
                legacyVersionParts.add(major);
                for(char c : (minor + "").toCharArray()) {
                    legacyVersionParts.add(Character.getNumericValue(c));
                }
                lastVersion = new Version(legacyVersionParts.stream().mapToInt(x -> x).toArray());
            }
        }


        try {
            if (new Version(1, 1).biggerThan(lastVersion)) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.1...");
                updateTo1p1(pluginConfig);
            }
            if (new Version(1, 2).biggerThan(lastVersion)) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.2...");
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Warning!: ARM uses a new schematic format now! You have to update all region schematics with");
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "/arm updateschematic [REGION] or go back to ARM version 1.1");
                updateTo1p2(pluginConfig);
            }
            if (new Version(1, 2, 1).biggerThan(lastVersion)) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.21...");
                updateTo1p21(pluginConfig);
            }
            if (new Version(1, 3).biggerThan(lastVersion)) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.3...");
                updateTo1p3(pluginConfig);
            }
            if (new Version(1, 4).biggerThan(lastVersion)) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.4...");
                updateTo1p4(pluginConfig);
            }
            if (new Version(1, 4, 1).biggerThan(lastVersion)) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.4.1...");
                pluginConfig.set("Version", 1.41);
                AdvancedRegionMarket.getInstance().saveConfig();
            }
            if (new Version(1, 4, 4).biggerThan(lastVersion)) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.4.4...");
                updateTo1p44(pluginConfig);
            }
            if (new Version(1, 5).biggerThan(lastVersion)) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.5...");
                pluginConfig.set("Version", 1.5);
                pluginConfig.set("Reselling.Offers.OfferTimeOut", 30);
                AdvancedRegionMarket.getInstance().saveConfig();
            }
            if (new Version(1, 5, 2).biggerThan(lastVersion)) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.5.2...");
                updateTo1p52(pluginConfig);
            }
            if (new Version(1, 6).biggerThan(lastVersion)) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.6...");
                updateTo1p6(pluginConfig);
            }
            if (new Version(1, 7).biggerThan(lastVersion)) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.7...");
                pluginConfig.set("Other.RemoveEntitiesOnRegionBlockReset", true);
                pluginConfig.set("Version", 1.7);
                AdvancedRegionMarket.getInstance().saveConfig();
            }
            if (new Version(1, 7, 2).biggerThan(lastVersion)) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.7.2...");
                pluginConfig.set("SignClickActions.RightClickNotSneakCmd", "buyaction");
                pluginConfig.set("SignClickActions.RightClickSneakCmd", "arm sellback %regionid%");
                pluginConfig.set("SignClickActions.LeftClickNotSneakCmd", "arm info %regionid%");
                pluginConfig.set("SignClickActions.LeftClickSneakCmd", "arm info %regionid%");
                pluginConfig.set("Version", 1.72);
                AdvancedRegionMarket.getInstance().saveConfig();
            }
            if (new Version(1, 7, 5).biggerThan(lastVersion)) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.7.5...");
                updateTo1p75(pluginConfig);
            }
            if (new Version(1, 8).biggerThan(lastVersion)) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.8...");
                updateTo1p8(pluginConfig);
            }

            if (new Version(1, 8, 1).biggerThan(lastVersion)) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.8.1..");
                updateTo1p81(pluginConfig);
            }
            if (new Version(1, 8, 3).biggerThan(lastVersion)) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.8.3..");
                updateTo1p83(pluginConfig);
            }
            if (new Version(1, 9).biggerThan(lastVersion)) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.9..");
                updateTo1p9(pluginConfig);
            }
            if (new Version(1, 9, 2).biggerThan(lastVersion)) {
                updateTo1p92(pluginConfig);
            }
            if (new Version(1, 9, 5).biggerThan(lastVersion)) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.9.5..");
                updateTo1p95(pluginConfig);
            }
            if (new Version(1, 9, 7).biggerThan(lastVersion)) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.9.7..");
                updateTo1p97(pluginConfig);
            }
            if (new Version(2).biggerThan(lastVersion)) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 2.0.0..");
                updateTo2p00(pluginConfig);
            }
            if (new Version(2, 0, 8).biggerThan(lastVersion)) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 2.0.8..");
                updateTo2p08(pluginConfig);
            }
            if (new Version(2, 0, 9).biggerThan(lastVersion)) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 2.0.9..");
                updateTo2p09(pluginConfig);
            }
            if (new Version(2, 1, 2).biggerThan(lastVersion)) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 2.1.2..");
                updateTo2p12(pluginConfig);
            }
            if (new Version(2, 1, 4).biggerThan(lastVersion)) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 2.1.4..");
                updateTo2p14(pluginConfig);
            }
            if (new Version(3).biggerThan(lastVersion)) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 3.0...");
                updateTo3p0(pluginConfig);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updateTo1p1(FileConfiguration pluginConfig) {
        pluginConfig.set("GUI.RegionOwnerItem", Material.ENDER_CHEST.toString());
        pluginConfig.set("GUI.RegionMemberItem", Material.CHEST.toString());
        pluginConfig.set("GUI.RegionFinderItem", Material.COMPASS.toString());
        pluginConfig.set("GUI.GoBackItem", "WOOD_DOOR");
        pluginConfig.set("GUI.WarningYesItem", "MELON_BLOCK");
        pluginConfig.set("GUI.WarningNoItem", Material.REDSTONE_BLOCK.toString());
        pluginConfig.set("GUI.TPItem", Material.ENDER_PEARL.toString());
        pluginConfig.set("GUI.SellRegionItem", Material.DIAMOND.toString());
        pluginConfig.set("GUI.ResetItem", Material.TNT.toString());
        pluginConfig.set("GUI.ExtendItem", "WATCH");
        pluginConfig.set("GUI.InfoItem", Material.BOOK.toString());
        pluginConfig.set("GUI.PromoteMemberToOwnerItem", Material.LADDER.toString());
        pluginConfig.set("GUI.RemoveMemberItem", Material.LAVA_BUCKET.toString());
        pluginConfig.set("Version", 1.1);
        AdvancedRegionMarket.getInstance().saveConfig();
    }

    private static void updateTo1p2(FileConfiguration pluginConfig) throws IOException {
        pluginConfig.set("Version", 1.2);
        AdvancedRegionMarket.getInstance().saveConfig();

        File regionConfDic = new File(AdvancedRegionMarket.getInstance().getDataFolder() + "/regions.yml");
        YamlConfiguration regionConf = YamlConfiguration.loadConfiguration(regionConfDic);

        ArrayList<String> worlds = new ArrayList<String>(regionConf.getConfigurationSection("Regions").getKeys(false));
        if (worlds != null) {
            for (int y = 0; y < worlds.size(); y++) {
                ArrayList<String> regions = new ArrayList<String>(regionConf.getConfigurationSection("Regions." + worlds.get(y)).getKeys(false));
                if (regions != null) {
                    for (int i = 0; i < regions.size(); i++) {
                        regionConf.set("Regions." + worlds.get(y) + "." + regions.get(i) + ".doBlockReset", true);
                    }
                }
            }
        }
        regionConf.save(regionConfDic);
    }

    private static void updateTo1p21(FileConfiguration pluginConfig) {
        pluginConfig.set("Version", 1.21);
        AdvancedRegionMarket.getInstance().saveConfig();
    }

    private static void updateTo1p3(FileConfiguration pluginConfig) throws IOException {

        File regionConfDic = new File(AdvancedRegionMarket.getInstance().getDataFolder() + "/regions.yml");
        YamlConfiguration regionConf = YamlConfiguration.loadConfiguration(regionConfDic);

        pluginConfig.set("DefaultRegionKind.DisplayName", "Default");
        pluginConfig.set("DefaultRegionKind.Item", "RED_BED");
        List<String> defaultLore = new ArrayList<>();
        defaultLore.add("very default");
        pluginConfig.set("DefaultRegionKind.Lore", defaultLore);
        pluginConfig.set("DefaultRegionKind.DisplayInLimits", true);
        pluginConfig.set("DefaultRegionKind.DisplayInGUI", false);
        pluginConfig.set("Other.SendRentRegionExpirationWarning", true);
        pluginConfig.set("Other.RentRegionExpirationWarningTime", "2d");
        pluginConfig.set("Other.TeleportAfterContractRegionBought", true);
        pluginConfig.set("Other.SendContractRegionExtendMessage", true);
        pluginConfig.set("Other.SignAndResetUpdateInterval", 10);
        pluginConfig.set("Other.RemainingTimeFormat", "%countdown%");
        pluginConfig.set("Other.DateTimeFormat", "dd.MM.yyyy hh:mm");
        pluginConfig.set("Other.ShortCountdown", false);
        pluginConfig.set("Version", 1.3);
        AdvancedRegionMarket.getInstance().saveConfig();

        if (regionConf.get("Regions") != null) {
            List<String> worlds = new ArrayList<>(regionConf.getConfigurationSection("Regions").getKeys(false));
            if (worlds != null) {
                for (int y = 0; y < worlds.size(); y++) {
                    if (regionConf.get("Regions." + worlds.get(y)) != null) {
                        LinkedList<String> regions = new LinkedList<String>(regionConf.getConfigurationSection("Regions." + worlds.get(y)).getKeys(false));
                        if (regions != null) {
                            for (int i = 0; i < regions.size(); i++) {
                                if (regionConf.getBoolean("Regions." + worlds.get(y) + "." + regions.get(i) + ".rentregion")) {
                                    regionConf.set("Regions." + worlds.get(y) + "." + regions.get(i) + ".regiontype", "rentregion");
                                } else {
                                    regionConf.set("Regions." + worlds.get(y) + "." + regions.get(i) + ".regiontype", "sellregion");
                                }
                                regionConf.set("Regions." + worlds.get(y) + "." + regions.get(i) + ".rentregion", null);
                                regionConf.set("Regions." + worlds.get(y) + "." + regions.get(i) + ".world", null);
                            }
                        }
                    }
                }
            }
        }
        regionConf.save(regionConfDic);
    }

    private static void updateTo1p4(FileConfiguration pluginConfig) {
        pluginConfig.set("GUI.FillItem", "GRAY_STAINED_GLASS_PANE");
        pluginConfig.set("GUI.ContractItem", "WRITABLE_BOOK");
        pluginConfig.set("GUI.DisplayRegionOwnerButton", true);
        pluginConfig.set("GUI.DisplayRegionMemberButton", true);
        pluginConfig.set("GUI.DisplayRegionFinderButton", true);
        pluginConfig.set("Other.CompleteRegionsOnTabComplete", false);
        pluginConfig.set("Version", 1.4);
        if (pluginConfig.get("RegionKinds") != null) {
            LinkedList<String> regionkinds = new LinkedList<String>(pluginConfig.getConfigurationSection("RegionKinds").getKeys(false));
            if (regionkinds != null) {
                for (int y = 0; y < regionkinds.size(); y++) {
                    pluginConfig.set("RegionKinds." + regionkinds.get(y) + ".displayName", regionkinds.get(y));
                }
            }
        }
        AdvancedRegionMarket.getInstance().saveConfig();
    }

    private static void updateTo1p44(FileConfiguration pluginConfig) {
        pluginConfig.set("Other.TeleporterTimer", 0);
        pluginConfig.set("Other.TeleportAfterRegionBoughtCountdown", false);
        pluginConfig.set("Version", 1.44);
        AdvancedRegionMarket.getInstance().saveConfig();
    }

    private static void updateTo1p52(FileConfiguration pluginConfig) {
        double paybackPercentage = pluginConfig.getDouble("Other.paypackPercentage");
        pluginConfig.set("DefaultRegionKind.PaypackPercentage", paybackPercentage);

        if (pluginConfig.get("RegionKinds") != null) {
            LinkedList<String> regionkinds = new LinkedList<String>(pluginConfig.getConfigurationSection("RegionKinds").getKeys(false));
            if (regionkinds != null) {
                for (int y = 0; y < regionkinds.size(); y++) {
                    pluginConfig.set("RegionKinds." + regionkinds.get(y) + ".paypackPercentage", paybackPercentage);
                    pluginConfig.set("RegionKinds." + regionkinds.get(y) + ".displayInLimits", true);
                    pluginConfig.set("RegionKinds." + regionkinds.get(y) + ".displayInGUI", true);
                }
            }
        }

        pluginConfig.set("Version", 1.52);
        AdvancedRegionMarket.getInstance().saveConfig();
    }

    private static void updateTo1p6(FileConfiguration pluginConfig) {
        pluginConfig.set("SubregionRegionKind.DisplayName", "Subregion");
        pluginConfig.set("SubregionRegionKind.Item", "PLAYER_HEAD");
        List<String> subregionRegionKindLore = new ArrayList<>();
        subregionRegionKindLore.add("very subregion");
        pluginConfig.set("SubregionRegionKind.Lore", subregionRegionKindLore);
        pluginConfig.set("SubregionRegionKind.DisplayInLimits", true);
        pluginConfig.set("SubregionRegionKind.DisplayInGUI", false);
        pluginConfig.set("SubregionRegionKind.PaypackPercentage", 0);
        pluginConfig.set("Subregions.AllowSubRegionUserReset", false);
        pluginConfig.set("Subregions.SubregionBlockReset", false);
        pluginConfig.set("Subregions.SubregionAutoReset", true);
        pluginConfig.set("Subregions.deleteSubregionsOnParentRegionUnsell", false);
        pluginConfig.set("Subregions.deleteSubregionsOnParentRegionBlockReset", false);
        pluginConfig.set("Subregions.allowParentRegionOwnersBuildOnSubregions", true);
        pluginConfig.set("Other.RegionInfoParticleBorder", true);
        pluginConfig.set("GUI.SubRegionItem", "GRASS_BLOCK");
        pluginConfig.set("GUI.TeleportToSignItem", "SIGN");
        pluginConfig.set("GUI.TeleportToRegionItem", "GRASS_BLOCK");
        pluginConfig.set("GUI.DeleteItem", "BARRIER");
        pluginConfig.set("GUI.NextPageItem", "ARROW");
        pluginConfig.set("GUI.PrevPageItem", "ARROW");
        pluginConfig.set("GUI.HotelSettingItem", "RED_BED");
        pluginConfig.set("GUI.PrevPageItem", "ARROW");
        pluginConfig.set("GUI.HotelSettingItem", "RED_BED");
        pluginConfig.set("GUI.UnsellItem", "NAME_TAG");

        pluginConfig.set("AutoPrice", null);
        pluginConfig.set("AutoPrice.example1.price", 200);
        pluginConfig.set("AutoPrice.example1.extendTime", "5d");
        pluginConfig.set("AutoPrice.example1.maxRentTime", "60d");
        pluginConfig.set("AutoPrice.example1.autoPriceCalculation", "static");
        pluginConfig.set("AutoPrice.example2.price", 2);
        pluginConfig.set("AutoPrice.example2.extendTime", "12h");
        pluginConfig.set("AutoPrice.example2.maxRentTime", "7d");
        pluginConfig.set("AutoPrice.example2.autoPriceCalculation", "per_m2");
        pluginConfig.set("AutoPrice.example3.price", 0.05);
        pluginConfig.set("AutoPrice.example3.extendTime", "7d");
        pluginConfig.set("AutoPrice.example3.maxRentTime", "30d");
        pluginConfig.set("AutoPrice.example3.autoPriceCalculation", "per_m3");

        pluginConfig.set("Other.RegionInfoParticleBorder", true);
        pluginConfig.set("Other.AllowRegionfinderTeleportToBuySign", true);

        pluginConfig.set("DefaultAutoprice.price", 2.0);
        pluginConfig.set("DefaultAutoprice.extendTime", "1d");
        pluginConfig.set("DefaultAutoprice.maxRentTime", "7d");
        pluginConfig.set("DefaultAutoprice.autoPriceCalculation", "per_m2");

        pluginConfig.set("Version", 1.6);
        AdvancedRegionMarket.getInstance().saveConfig();
    }

    private static void updateTo1p75(FileConfiguration pluginConfig) throws IOException {
        File regionKindsConfDic = new File(AdvancedRegionMarket.getInstance().getDataFolder() + "/regionkinds.yml");
        YamlConfiguration regionKindsConf = YamlConfiguration.loadConfiguration(regionKindsConfDic);
        ConfigurationSection oldRegionKinds = pluginConfig.getConfigurationSection("RegionKinds");

        regionKindsConf.set("RegionKinds", oldRegionKinds);
        pluginConfig.set("RegionKinds", null);

        regionKindsConf.set("DefaultRegionKind.displayName", pluginConfig.getString("DefaultRegionKind.DisplayName"));
        regionKindsConf.set("DefaultRegionKind.item", pluginConfig.getString("DefaultRegionKind.Item"));
        regionKindsConf.set("DefaultRegionKind.lore", pluginConfig.getStringList("DefaultRegionKind.Lore"));
        regionKindsConf.set("DefaultRegionKind.displayInLimits", pluginConfig.getBoolean("DefaultRegionKind.DisplayInLimits"));
        regionKindsConf.set("DefaultRegionKind.displayInGUI", pluginConfig.getBoolean("DefaultRegionKind.DisplayInGUI"));
        regionKindsConf.set("DefaultRegionKind.paypackPercentage", pluginConfig.getDouble("DefaultRegionKind.PaypackPercentage"));
        pluginConfig.set("DefaultRegionKind", null);

        regionKindsConf.set("SubregionRegionKind.displayName", pluginConfig.getString("SubregionRegionKind.DisplayName"));
        regionKindsConf.set("SubregionRegionKind.item", pluginConfig.getString("SubregionRegionKind.Item"));
        regionKindsConf.set("SubregionRegionKind.lore", pluginConfig.getStringList("SubregionRegionKind.Lore"));
        regionKindsConf.set("SubregionRegionKind.displayInLimits", pluginConfig.getBoolean("SubregionRegionKind.DisplayInLimits"));
        regionKindsConf.set("SubregionRegionKind.displayInGUI", pluginConfig.getBoolean("SubregionRegionKind.DisplayInGUI"));
        regionKindsConf.set("SubregionRegionKind.paypackPercentage", pluginConfig.getDouble("SubregionRegionKind.PaypackPercentage"));
        pluginConfig.set("SubregionRegionKind", null);

        pluginConfig.set("RegionKinds.activateRegionKindPermissions", true);
        pluginConfig.set("PriceFormatting.locale", "US");
        pluginConfig.set("PriceFormatting.minimumFractionDigits", 2);
        pluginConfig.set("PriceFormatting.maximumFractionDigits", 2);
        pluginConfig.set("PriceFormatting.minimumIntegerDigits", 1);

        pluginConfig.set("Version", 1.75);

        regionKindsConf.save(regionKindsConfDic);
        AdvancedRegionMarket.getInstance().saveConfig();
    }

    private static void updateTo1p8(FileConfiguration pluginConfig) throws IOException {
        File regionConfDic = new File(AdvancedRegionMarket.getInstance().getDataFolder() + "/regions.yml");
        YamlConfiguration regionConf = YamlConfiguration.loadConfiguration(regionConfDic);

        if (regionConf.get("Regions") != null) {
            ConfigurationSection mainSection = regionConf.getConfigurationSection("Regions");
            List<String> worlds = new ArrayList<String>(mainSection.getKeys(false));
            if (worlds != null) {
                for (String worldString : worlds) {
                    if (mainSection.get(worldString) != null) {
                        ConfigurationSection worldSection = mainSection.getConfigurationSection(worldString);
                        List<String> regions = new ArrayList<String>(worldSection.getKeys(false));
                        if (regions != null) {
                            for (String regionname : regions) {
                                ConfigurationSection regionSection = worldSection.getConfigurationSection(regionname);
                                //SIGNS
                                List<String> regionsignsloc = regionSection.getStringList("signs");
                                for (int i = 0; i < regionsignsloc.size(); i++) {
                                    regionsignsloc.set(i, regionsignsloc.get(i) + ";NORTH;GROUND");
                                }
                                regionSection.set("signs", regionsignsloc);

                                List<Region> subregions = new ArrayList<>();
                                if (regionSection.getConfigurationSection("subregions") != null) {
                                    ConfigurationSection subregionsection = regionSection.getConfigurationSection("subregions");
                                    List<String> subregionIDS = new ArrayList<>((subregionsection).getKeys(false));
                                    if (subregionIDS != null) {
                                        for (String subregionName : subregionIDS) {
                                            List<String> subregionsignsloc = subregionsection.getStringList("signs");
                                            for (int i = 0; i < subregionsignsloc.size(); i++) {
                                                subregionsignsloc.set(i, subregionsignsloc.get(i) + ";NORTH;GROUND");
                                            }
                                            subregionsection.set("signs", subregionsignsloc);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        regionConf.save(AdvancedRegionMarket.getInstance().getDataFolder() + "/regions.yml");

        pluginConfig.set("Version", 1.8);
        AdvancedRegionMarket.getInstance().saveConfig();
    }

    private static void updateTo1p81(FileConfiguration pluginConfig) throws IOException {
        File regionConfDic = new File(AdvancedRegionMarket.getInstance().getDataFolder() + "/regions.yml");
        YamlConfiguration regionConf = YamlConfiguration.loadConfiguration(regionConfDic);

        if (regionConf.get("Regions") != null) {
            ConfigurationSection mainSection = regionConf.getConfigurationSection("Regions");
            List<String> worlds = new ArrayList<String>(mainSection.getKeys(false));
            if (worlds != null) {
                for (String worldString : worlds) {
                    if (mainSection.get(worldString) != null) {
                        ConfigurationSection worldSection = mainSection.getConfigurationSection(worldString);
                        List<String> regions = new ArrayList<String>(worldSection.getKeys(false));
                        if (regions != null) {
                            for (String regionname : regions) {
                                ConfigurationSection regionSection = worldSection.getConfigurationSection(regionname);
                                //SIGNS
                                List<String> regionsignsloc = regionSection.getStringList("signs");
                                for (int i = 0; i < regionsignsloc.size(); i++) {
                                    String signString = regionsignsloc.get(i);
                                    signString = signString.replace(";NORTH", "");
                                    signString = signString.replace(";EAST", "");
                                    signString = signString.replace(";SOUTH", "");
                                    signString = signString.replace(";WEST", "");
                                    signString = signString.replace(";UP", "");
                                    signString = signString.replace(";DOWN", "");
                                    signString = signString.replace(";NORTH_EAST", "");
                                    signString = signString.replace(";NORTH_WEST", "");
                                    signString = signString.replace(";SOUTH_EAST", "");
                                    signString = signString.replace(";SOUTH_WEST", "");
                                    signString = signString.replace(";WEST_NORTH_WEST", "");
                                    signString = signString.replace(";NORTH_NORTH_WEST", "");
                                    signString = signString.replace(";NORTH_NORTH_EAST", "");
                                    signString = signString.replace(";EAST_NORTH_EAST", "");
                                    signString = signString.replace(";EAST_SOUTH_EAST", "");
                                    signString = signString.replace(";SOUTH_SOUTH_EAST", "");
                                    signString = signString.replace(";SOUTH_SOUTH_WEST", "");
                                    signString = signString.replace(";WEST_SOUTH_WEST", "");
                                    signString = signString.replace(";SELF", "");
                                    regionsignsloc.set(i, signString);
                                }
                                regionSection.set("signs", regionsignsloc);

                                List<Region> subregions = new ArrayList<>();
                                if (regionSection.getConfigurationSection("subregions") != null) {
                                    ConfigurationSection subregionsection = regionSection.getConfigurationSection("subregions");
                                    List<String> subregionIDS = new ArrayList<>((subregionsection).getKeys(false));
                                    if (subregionIDS != null) {
                                        for (String subregionName : subregionIDS) {
                                            List<String> subregionsignsloc = subregionsection.getStringList("signs");
                                            for (int i = 0; i < subregionsignsloc.size(); i++) {
                                                String signString = subregionsignsloc.get(i);
                                                signString = signString.replace(";NORTH", "");
                                                signString = signString.replace(";EAST", "");
                                                signString = signString.replace(";SOUTH", "");
                                                signString = signString.replace(";WEST", "");
                                                signString = signString.replace(";UP", "");
                                                signString = signString.replace(";DOWN", "");
                                                signString = signString.replace(";NORTH_EAST", "");
                                                signString = signString.replace(";NORTH_WEST", "");
                                                signString = signString.replace(";SOUTH_EAST", "");
                                                signString = signString.replace(";SOUTH_WEST", "");
                                                signString = signString.replace(";WEST_NORTH_WEST", "");
                                                signString = signString.replace(";NORTH_NORTH_WEST", "");
                                                signString = signString.replace(";NORTH_NORTH_EAST", "");
                                                signString = signString.replace(";EAST_NORTH_EAST", "");
                                                signString = signString.replace(";EAST_SOUTH_EAST", "");
                                                signString = signString.replace(";SOUTH_SOUTH_EAST", "");
                                                signString = signString.replace(";SOUTH_SOUTH_WEST", "");
                                                signString = signString.replace(";WEST_SOUTH_WEST", "");
                                                signString = signString.replace(";SELF", "");
                                                subregionsignsloc.set(i, signString);
                                            }
                                            subregionsection.set("signs", subregionsignsloc);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        regionConf.save(AdvancedRegionMarket.getInstance().getDataFolder() + "/regions.yml");

        pluginConfig.set("Version", 1.81);
        AdvancedRegionMarket.getInstance().saveConfig();
    }

    private static void updateTo1p83(FileConfiguration pluginConfig) throws IOException {
        File regionConfDic = new File(AdvancedRegionMarket.getInstance().getDataFolder() + "/regions.yml");
        YamlConfiguration regionConf = YamlConfiguration.loadConfiguration(regionConfDic);

        if (regionConf.get("Regions") != null) {
            ConfigurationSection mainSection = regionConf.getConfigurationSection("Regions");
            List<String> worlds = new ArrayList<String>(mainSection.getKeys(false));
            if (worlds != null) {
                for (String worldString : worlds) {
                    if (mainSection.get(worldString) != null) {
                        ConfigurationSection worldSection = mainSection.getConfigurationSection(worldString);
                        List<String> regions = new ArrayList<String>(worldSection.getKeys(false));
                        if (regions != null) {
                            for (String regionname : regions) {
                                ConfigurationSection regionSection = worldSection.getConfigurationSection(regionname);
                                //SIGNS
                                List<String> regionsignsloc = regionSection.getStringList("signs");
                                for (int i = 0; i < regionsignsloc.size(); i++) {
                                    regionsignsloc.set(i, regionsignsloc.get(i) + ";NORTH");
                                }
                                regionSection.set("signs", regionsignsloc);

                                List<Region> subregions = new ArrayList<>();
                                if (regionSection.getConfigurationSection("subregions") != null) {
                                    ConfigurationSection subregionsection = regionSection.getConfigurationSection("subregions");
                                    List<String> subregionIDS = new ArrayList<>((subregionsection).getKeys(false));
                                    if (subregionIDS != null) {
                                        for (String subregionName : subregionIDS) {
                                            List<String> subregionsignsloc = subregionsection.getStringList(subregionName + ".signs");
                                            for (int i = 0; i < subregionsignsloc.size(); i++) {
                                                subregionsignsloc.set(i, subregionsignsloc.get(i) + ";NORTH");
                                            }
                                            subregionsection.set(subregionName + ".signs", subregionsignsloc);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        regionConf.save(AdvancedRegionMarket.getInstance().getDataFolder() + "/regions.yml");

        pluginConfig.set("Version", 1.83);
        AdvancedRegionMarket.getInstance().saveConfig();
    }

    private static void updateTo1p9(FileConfiguration pluginConfig) throws IOException {
        pluginConfig.set("GUI.FlageditorItem", "BANNER");
        pluginConfig.set("GUI.FlagItem", "SIGN");
        pluginConfig.set("GUI.FlagSettingsSelectedItem", "EMERALD_BLOCK");
        pluginConfig.set("GUI.FlagSettingsNotSelectedItem", "REDSTONE_BLOCK");
        pluginConfig.set("GUI.FlagGroupSelectedItem", "EMERALD_BLOCK");
        pluginConfig.set("GUI.FlagGroupNotSelectedItem", "REDSTONE_BLOCK");
        pluginConfig.set("GUI.FlagRemoveItem", "BARRIER");
        pluginConfig.set("GUI.FlagUserInputItem", "WRITABLE_BOOK");
        pluginConfig.set("GUI.FlageditorResetItem", "TNT");

        pluginConfig.set("SignLinkingMode.regionblacklist.world", Arrays.asList("blacklistedTestRegionInWorld1", "blacklistedTestRegionInWorld2"));
        pluginConfig.set("SignLinkingMode.regionblacklist.world_nether", Arrays.asList("blacklistedTestRegionInWorld_nether1", "blacklistedTestRegionInWorld_nether2"));
        pluginConfig.set("SignLinkingMode.regionblacklist.world_the_end", Arrays.asList("anotherBlacklistedRegion"));

        pluginConfig.set("Version", 1.9);
        AdvancedRegionMarket.getInstance().saveConfig();
    }

    private static void updateTo1p92(FileConfiguration pluginConfig) throws IOException {
        pluginConfig.set("Other.Sendstats", true);
        pluginConfig.set("Version", 1.92);
        AdvancedRegionMarket.getInstance().saveConfig();
    }

    private static void updateTo1p95(FileConfiguration pluginConfig) throws IOException {
        pluginConfig.set("Version", 1.95);
        AdvancedRegionMarket.getInstance().saveConfig();
    }

    private static void updateTo1p97(FileConfiguration pluginConfig) throws IOException {
        pluginConfig.set("Version", 1.97);

        File schemfolder = new File(AdvancedRegionMarket.getInstance().getDataFolder() + "/schematics");
        if (schemfolder.isDirectory()) {
            List<File> worldFolders = new ArrayList<>();
            for (File listedFile : schemfolder.listFiles()) {
                if (listedFile.isDirectory()) {
                    worldFolders.add(listedFile);
                }
            }

            for (File worldfolder : worldFolders) {
                List<File> builtblocksFiles = new ArrayList<>();
                for (File schemFile : worldfolder.listFiles()) {
                    if (schemFile.getName().endsWith("--builtblocks.schematic")) {
                        builtblocksFiles.add(schemFile);
                    }
                }

                for (File builtBlocksFile : builtblocksFiles) {
                    FileReader fileReader = new FileReader(builtBlocksFile);
                    BufferedReader bReader = new BufferedReader(fileReader);
                    List<String> blocks = new LinkedList<>();
                    String line;
                    while ((line = bReader.readLine()) != null) {
                        blocks.add(line);
                    }
                    bReader.close();
                    fileReader.close();
                    builtBlocksFile.delete();

                    File newBuiltBlocksFile = new File(builtBlocksFile.getAbsolutePath().substring(0, builtBlocksFile.getAbsolutePath().length() - 23) + ".builtblocks");
                    FileWriter fileWriter = new FileWriter(newBuiltBlocksFile);
                    BufferedWriter bWriter = new BufferedWriter(fileWriter);

                    for (String location : blocks) {
                        String[] coordinates = location.split(";", 2);
                        bWriter.write(coordinates[1]);
                        bWriter.newLine();
                    }
                    bWriter.close();
                    fileWriter.close();
                }
            }
        }
        AdvancedRegionMarket.getInstance().saveConfig();
    }

    private static void updateTo2p00(FileConfiguration pluginConfig) throws IOException {
        pluginConfig.set("Version", 2.00);
        pluginConfig.set("DefaultInactivityExpiration.resetAfter", "none");
        pluginConfig.set("DefaultInactivityExpiration.takeOverAfter", "none");

        pluginConfig.set("InactivityExpiration.examplegroup1.resetAfter", "30d");
        pluginConfig.set("InactivityExpiration.examplegroup1.takeOverAfter", "20d");
        pluginConfig.set("InactivityExpiration.examplegroup2.resetAfter", "60d");
        pluginConfig.set("InactivityExpiration.examplegroup2.takeOverAfter", "50d");
        pluginConfig.set("InactivityExpiration.examplegroup3.resetAfter", "none");
        pluginConfig.set("Subregions.SubregionInactivityReset", pluginConfig.getBoolean("Subregions.SubregionAutoReset"));
        pluginConfig.set("InactivityExpiration.examplegroup3.takeOverAfter", "none");
        pluginConfig.set("Other.userResetCooldown", "7d");
        pluginConfig.set("AutoResetAndTakeOver", null);
        AdvancedRegionMarket.getInstance().saveConfig();

        File regionConfDic = new File(AdvancedRegionMarket.getInstance().getDataFolder() + "/regions.yml");
        YamlConfiguration regionConf = YamlConfiguration.loadConfiguration(regionConfDic);

        ConfigurationSection regionsSection = regionConf.getConfigurationSection("Regions");
        if (regionsSection != null) {
            ArrayList<String> worlds = new ArrayList<String>(regionsSection.getKeys(false));
            for (String worldName : worlds) {
                ConfigurationSection worldSection = regionsSection.getConfigurationSection(worldName);
                if (worldSection == null) {
                    continue;
                }
                ArrayList<String> regions = new ArrayList<String>(worldSection.getKeys(false));
                for (String regionID : regions) {
                    ConfigurationSection regSection = worldSection.getConfigurationSection(regionID);
                    if (regSection == null) {
                        continue;
                    }
                    boolean autoreset = regSection.getBoolean("autoreset");
                    regSection.set("inactivityReset", autoreset);
                    regSection.set("lastLogin", new GregorianCalendar().getTimeInMillis());
                    if (regSection.getString("regiontype").equalsIgnoreCase("rentregion")) {
                        long extendTime = regSection.getLong("rentExtendPerClick");
                        regSection.set("extendTime", extendTime);
                        regSection.set("rentExtendPerClick", null);
                    }
                    ConfigurationSection subSection = regSection.getConfigurationSection("subregions");
                    if (subSection != null) {
                        ArrayList<String> subregions = new ArrayList<String>(subSection.getKeys(false));
                        for (String subregionID : subregions) {
                            ConfigurationSection subregionSection = subSection.getConfigurationSection(subregionID);
                            if (subregionSection == null) {
                                continue;
                            }
                            subregionSection.set("lastLogin", new GregorianCalendar().getTimeInMillis());
                            if (subregionSection.getString("regiontype").equalsIgnoreCase("rentregion")) {
                                long extendTime = subregionSection.getLong("rentExtendPerClick");
                                subregionSection.set("extendTime", extendTime);
                                subregionSection.set("rentExtendPerClick", null);
                            }
                        }
                    }
                }
            }
        }


        regionConf.save(regionConfDic);

        File messagesConfDic = new File(AdvancedRegionMarket.getInstance().getDataFolder() + "/messages.yml");
        YamlConfiguration messagesConf = YamlConfiguration.loadConfiguration(messagesConfDic);
        ConfigurationSection messagesSection = messagesConf.getConfigurationSection("Messages");
        if (messagesSection != null) {
            ArrayList<String> messageKeys = new ArrayList<String>(messagesSection.getKeys(false));

            for (String key : messageKeys) {
                Object msgObject = messagesSection.get(key);
                if (msgObject instanceof List) {
                    List<String> msgList = (List) msgObject;
                    for (int i = 0; i < msgList.size(); i++) {
                        msgList.set(i, msgList.get(i).replace("%extendperclick%", "%extendtime%"));
                        msgList.set(i, msgList.get(i).replace("%extend%", "%extendtime%"));
                        msgList.set(i, msgList.get(i).replace("%isautoreset%", "%isinactivityreset%"));
                        msgList.set(i, msgList.get(i).replace("%remainingdays%", "%remainingusersellcooldown%"));
                        msgList.set(i, msgList.get(i).replace("%days%", "%userresetcooldown%"));
                    }
                } else if (msgObject instanceof String) {
                    String msgString = (String) msgObject;
                    msgString = msgString.replace("%extendperclick%", "%extendtime%");
                    msgString = msgString.replace("%extend%", "%extendtime%");
                    msgString = msgString.replace("%isautoreset%", "%isinactivityreset%");
                    msgString = msgString.replace("%remainingdays%", "%remainingusersellcooldown%");
                    msgString = msgString.replace("%days%", "%userresetcooldown%");

                    messagesSection.set(key, msgString);
                }
            }
        }
        messagesConf.set("Messages.ResetRegionCooldownError", "&7You have to wait&6 %remainingusersellcooldown% &7till you can reset your region again");
        messagesConf.set("Messages.RegionInfoSellregionAdmin", new ArrayList<>(Arrays.asList("&6=========[Region Info]=========",
                "&9ID: &e%regionid% &7(Type: &r%selltype%&7)",
                "&9Sold: &e%issold%",
                "&9Price: &e%price%",
                "&9Owner: &e%owner%",
                "&9Members: &e%members%",
                "&9Regionkind: &e%regionkinddisplay% &9FlagGroup: &e%flaggroup%",
                "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
                "&9UserResettable: &e%isuserresettable% &9InactivityResetEnabled: &e%isinactivityreset%",
                "&9Owners last login: &e%lastownerlogin%",
                "&9InactivityReset in: &e%inactivityresetin%",
                "&9TakeOver possible in: &e%takeoverin%",
                "&9DoBlockReset: &e%isdoblockreset% &9Autoprice: &e%autoprice%",
                "&9Allowed Subregions: &e%subregionlimit%",
                "&9Subregions: &e%subregions%")));
        messagesConf.set("Messages.RegionInfoRentregionAdmin", new ArrayList<>(Arrays.asList("&6=========[Region Info]=========",
                "&9ID: &e%regionid% &7(Type: &r%selltype%&7)",
                "&9Sold: &e%issold%",
                "&9Price: &e%price% &7per &e%extendtime% &7max.: &e%maxrenttime%",
                "&9Remaining time: &e%remaining%",
                "&9Owner: &e%owner%",
                "&9Members: &e%members%",
                "&9Regionkind: &e%regionkinddisplay% &9FlagGroup: &e%flaggroup%",
                "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
                "&9UserResettable: &e%isuserresettable% &9InactivityResetEnabled: &e%isinactivityreset%",
                "&9Owners last login: &e%lastownerlogin%",
                "&9InactivityReset in: &e%inactivityresetin%",
                "&9TakeOver possible in: &e%takeoverin%",
                "&9DoBlockReset: &e%isdoblockreset% &9Autoprice: &e%autoprice%",
                "&9Allowed Subregions: &e%subregionlimit%",
                "&9Subregions: &e%subregions%")));
        messagesConf.set("Messages.RegionInfoContractregionAdmin", new ArrayList<>(Arrays.asList("&6=========[Region Info]=========",
                "&9ID: &e%regionid% &7(Type: &r%selltype%&7)",
                "&9Sold: &e%issold%",
                "&9Price: &e%price% &7per &e%extendtime% &7(auto extend)",
                "&9Next extend in: &e%remaining%",
                "&9Owner: &e%owner%",
                "&9Members: &e%members%",
                "&9Regionkind: &e%regionkinddisplay% &9FlagGroup: &e%flaggroup%",
                "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
                "&9UserResettable: &e%isuserresettable% &9InactivityResetEnabled: &e%isinactivityreset%",
                "&9Owners last login: &e%lastownerlogin%",
                "&9InactivityReset in: &e%inactivityresetin%",
                "&9TakeOver possible in: &e%takeoverin%",
                "&9DoBlockReset: &e%isdoblockreset% &9Autoprice: &e%autoprice%",
                "&9Allowed Subregions: &e%subregionlimit%",
                "&9Subregions: &e%subregions%")));
        messagesConf.set("Messages.RegionInfoSellregionUser", new ArrayList<>(Arrays.asList("&6=========[Region Info]=========",
                "&9ID: &e%regionid% &7(Type: &r%selltype%&7)",
                "&9Sold: &e%issold%",
                "&9Price: &e%price%",
                "&9Owner: &e%owner%",
                "&9Members: &e%members%",
                "&9Regionkind: &e%regionkinddisplay% &9FlagGroup: &e%flaggroup%",
                "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
                "&9UserResettable: &e%isuserresettable% &9InactivityResetEnabled: &e%isinactivityreset%",
                "&9Owners last login: &e%lastownerlogin%",
                "&9InactivityReset in: &e%inactivityresetin%",
                "&9TakeOver possible in: &e%takeoverin%",
                "&9DoBlockReset: &e%isdoblockreset% &9Autoprice: &e%autoprice%",
                "&9Allowed Subregions: &e%subregionlimit%",
                "&9Subregions: &e%subregions%")));
        messagesConf.set("Messages.RegionInfoRentregionUser", new ArrayList<>(Arrays.asList("&6=========[Region Info]=========",
                "&9ID: &e%regionid% &7(Type: &r%selltype%&7)",
                "&9Sold: &e%issold%",
                "&9Price: &e%price% &7per &e%extendtime% &7max.: &e%maxrenttime%",
                "&9Remaining time: &e%remaining%",
                "&9Owner: &e%owner%",
                "&9Members: &e%members%",
                "&9Regionkind: &e%regionkinddisplay% &9FlagGroup: &e%flaggroup%",
                "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
                "&9UserResettable: &e%isuserresettable% &9InactivityResetEnabled: &e%isinactivityreset%",
                "&9Owners last login: &e%lastownerlogin%",
                "&9InactivityReset in: &e%inactivityresetin%",
                "&9TakeOver possible in: &e%takeoverin%",
                "&9DoBlockReset: &e%isdoblockreset% &9Autoprice: &e%autoprice%",
                "&9Allowed Subregions: &e%subregionlimit%",
                "&9Subregions: &e%subregions%")));
        messagesConf.set("Messages.RegionInfoContractregionUser", new ArrayList<>(Arrays.asList("&6=========[Region Info]=========",
                "&9ID: &e%regionid% &7(Type: &r%selltype%&7)",
                "&9Sold: &e%issold%",
                "&9Price: &e%price% &7per &e%extendtime% &7(auto extend)",
                "&9Next extend in: &e%remaining%",
                "&9Owner: &e%owner%",
                "&9Members: &e%members%",
                "&9Regionkind: &e%regionkinddisplay% &9FlagGroup: &e%flaggroup%",
                "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
                "&9UserResettable: &e%isuserresettable% &9InactivityResetEnabled: &e%isinactivityreset%",
                "&9Owners last login: &e%lastownerlogin%",
                "&9InactivityReset in: &e%inactivityresetin%",
                "&9TakeOver possible in: &e%takeoverin%",
                "&9DoBlockReset: &e%isdoblockreset% &9Autoprice: &e%autoprice%",
                "&9Allowed Subregions: &e%subregionlimit%",
                "&9Subregions: &e%subregions%")));
        messagesConf.set("Messages.RegionInfoSellregionSubregion", new ArrayList<>(Arrays.asList("&6=========[Region Info]=========",
                "&9ID: &e%regionid% &7(Type: &r%selltype%&7, Subregion)",
                "&9Sold: &e%issold%",
                "&9Price: &e%price%",
                "&9Owner: &e%owner%",
                "&9Members: &e%members%",
                "&9Regionkind: &e%regionkinddisplay% &9FlagGroup: &e%flaggroup%",
                "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
                "&9UserResettable: &e%isuserresettable% &9InactivityResetEnabled: &e%isinactivityreset%",
                "&9Owners last login: &e%lastownerlogin%",
                "&9InactivityReset in: &e%inactivityresetin%",
                "&9TakeOver possible in: &e%takeoverin%",
                "&9DoBlockReset: &e%isdoblockreset% &9Autoprice: &e%autoprice%")));
        messagesConf.set("Messages.RegionInfoRentregionSubregion", new ArrayList<>(Arrays.asList("&6=========[Region Info]=========",
                "&9ID: &e%regionid% &7(Type: &r%selltype%&7, Subregion)",
                "&9Sold: &e%issold%",
                "&9Price: &e%price% &7per &e%extendtime% &7max.: &e%maxrenttime%",
                "&9Remaining time: &e%remaining%",
                "&9Owner: &e%owner%",
                "&9Members: &e%members%",
                "&9Regionkind: &e%regionkinddisplay% &9FlagGroup: &e%flaggroup%",
                "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
                "&9UserResettable: &e%isuserresettable% &9InactivityResetEnabled: &e%isinactivityreset%",
                "&9Owners last login: &e%lastownerlogin%",
                "&9InactivityReset in: &e%inactivityresetin%",
                "&9TakeOver possible in: &e%takeoverin%",
                "&9DoBlockReset: &e%isdoblockreset% &9Autoprice: &e%autoprice%")));
        messagesConf.set("Messages.RegionInfoContractregionSubregion", new ArrayList<>(Arrays.asList("&6=========[Region Info]=========",
                "&9ID: &e%regionid% &7(Type: &r%selltype%&7, Subregion)",
                "&9Sold: &e%issold%",
                "&9Price: &e%price% &7per &e%extendtime% &7(auto extend)",
                "&9Next extend in: &e%remaining%",
                "&9Owner: &e%owner%",
                "&9Members: &e%members%",
                "&9Regionkind: &e%regionkinddisplay% &9FlagGroup: &e%flaggroup%",
                "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
                "&9UserResettable: &e%isuserresettable% &9InactivityResetEnabled: &e%isinactivityreset%",
                "&9Owners last login: &e%lastownerlogin%",
                "&9InactivityReset in: &e%inactivityresetin%",
                "&9TakeOver possible in: &e%takeoverin%",
                "&9DoBlockReset: &e%isdoblockreset% &9Autoprice: &e%autoprice%")));
        messagesConf.set("Messages.TakeOverItemLore", new ArrayList<>(Arrays.asList("&aYou are a member of this region.",
                "&aThe owner of it hasn''t been",
                "&aonline for a long time. You",
                "&acan transfer the owner rights to your",
                "&aaccount for free. The actual owner",
                "&aof it will become a member of the region.",
                "&cIf the region does not get transferred",
                "&cor the owner does not come online",
                "&cwithin &7%inactivityresetin% &cthe",
                "&cregion will be resetted and everybody on it",
                "&cwill lose their rights.",
                "&cAfterwards it will go back for sale!")));
        messagesConf.save(messagesConfDic);

        UpdateHelpMethods.replaceVariableInFlagGroupsYML("%extend%", "%extendtime%");
        UpdateHelpMethods.replaceVariableInFlagGroupsYML("%extendperclick%", "%extendtime%");
    }

    private static void updateTo2p08(FileConfiguration pluginConfig) throws IOException {
        UpdateHelpMethods.replaceVariableInMessagesYML("%remainingusersellcooldown%", "%remaininguserresetcooldown%");

        File regionkindsConfDic = new File(AdvancedRegionMarket.getInstance().getDataFolder() + "/regionkinds.yml");
        YamlConfiguration regionkindsConf = YamlConfiguration.loadConfiguration(regionkindsConfDic);
        ConfigurationSection regionKindsSection = regionkindsConf.getConfigurationSection("RegionKinds");
        if (regionKindsSection != null) {
            ArrayList<String> worlds = new ArrayList<String>(regionKindsSection.getKeys(false));
            for (String regionkindName : worlds) {
                ConfigurationSection regionKindSection = regionKindsSection.getConfigurationSection(regionkindName);
                if (regionKindSection == null) {
                    continue;
                }
                regionKindSection.set("displayInRegionfinder", regionKindSection.getBoolean("displayInGUI"));
            }
        }
        regionkindsConf.save(regionkindsConfDic);
        pluginConfig.set("Version", 2.08);
        AdvancedRegionMarket.getInstance().saveConfig();
    }

    private static void updateTo2p09(FileConfiguration pluginConfig) throws IOException {
        UpdateHelpMethods.replaceVariableInMessagesYML("%extendtime%", "%extendtime-short%");
        UpdateHelpMethods.replaceVariableInMessagesYML("%remaining%", "%remainingtime-countdown-short%");
        UpdateHelpMethods.replaceVariableInMessagesYML("%maxrenttime%", "%maxrenttime-short%");
        UpdateHelpMethods.replaceVariableInMessagesYML("%takeoverin%", "%takeoverin-countdown-short%");
        UpdateHelpMethods.replaceVariableInMessagesYML("%inactivityresetin%", "%inactivityresetin-countdown-short%");
        UpdateHelpMethods.replaceVariableInMessagesYML("%remaininguserresetcooldown%", "%remaininguserresetcooldown-countdown-short%");
        UpdateHelpMethods.replaceVariableInFlagGroupsYML("%extendtime%", "%extendtime-short%");
        UpdateHelpMethods.replaceVariableInFlagGroupsYML("%remaining%", "%remainingtime-countdown-short%");
        UpdateHelpMethods.replaceVariableInFlagGroupsYML("%maxrenttime%", "%maxrenttime-short%");
        UpdateHelpMethods.replaceVariableInFlagGroupsYML("%takeoverin%", "%takeoverin-countdown-short%");
        UpdateHelpMethods.replaceVariableInFlagGroupsYML("%inactivityresetin%", "%inactivityresetin-countdown-short%");
        UpdateHelpMethods.replaceVariableInFlagGroupsYML("%remaininguserresetcooldown%", "%remaininguserresetcooldown-countdown-short%");
        UpdateHelpMethods.setMessageInMessageYML("Messages.SignRemovedFromRegion", "&7Regionsign removed! %remaining% Sign(s) remaining before "
                + "region gets removed from ARM!");
        pluginConfig.set("Other.RemainingTimeFormat", null);
        pluginConfig.set("Other.ShortCountdown", null);
        pluginConfig.set("Version", 2.09);
        AdvancedRegionMarket.getInstance().saveConfig();
    }

    private static void updateTo2p12(FileConfiguration pluginConfig) throws IOException {
        File regionConfDic = new File(AdvancedRegionMarket.getInstance().getDataFolder() + "/regions.yml");
        YamlConfiguration regionConf = YamlConfiguration.loadConfiguration(regionConfDic);

        if (regionConf.get("Regions") != null) {
            ConfigurationSection mainSection = regionConf.getConfigurationSection("Regions");
            List<String> worlds = new ArrayList<String>(mainSection.getKeys(false));
            if (worlds != null) {
                for (String worldString : worlds) {
                    if (mainSection.get(worldString) != null) {
                        ConfigurationSection worldSection = mainSection.getConfigurationSection(worldString);
                        List<String> regions = new ArrayList<String>(worldSection.getKeys(false));
                        if (regions != null) {
                            for (String regionname : regions) {
                                ConfigurationSection regionSection = worldSection.getConfigurationSection(regionname);
                                regionSection.set("userrestorable", regionSection.getBoolean("isUserResettable"));
                                regionSection.set("autorestore", regionSection.getBoolean("doBlockReset"));
                                regionSection.set("doBlockReset", null);
                                regionSection.set("isUserResettable", null);
                            }
                        }
                    }
                }
            }
        }
        regionConf.save(AdvancedRegionMarket.getInstance().getDataFolder() + "/regions.yml");

        UpdateHelpMethods.replaceVariableInMessagesYML("%isuserresettable%", "%isuserrestorable%");
        UpdateHelpMethods.replaceVariableInMessagesYML("%isdoblockreset%", "%isautorestore%");
        pluginConfig.set("FlagGroups.enabled", true);
        pluginConfig.set("Subregions.AllowSubregionUserRestore", pluginConfig.getBoolean("Subregions.AllowSubRegionUserReset"));
        pluginConfig.set("Subregions.SubregionAutoRestore", pluginConfig.getBoolean("Subregions.SubregionBlockReset"));
        pluginConfig.set("Subregions.AllowSubRegionUserReset", null);
        pluginConfig.set("Subregions.SubregionBlockReset", null);
        pluginConfig.set("Other.Language", "en");
        pluginConfig.set("Version", 2.12);
        AdvancedRegionMarket.getInstance().saveConfig();
    }

    private static void updateTo2p14(FileConfiguration pluginConfig) throws IOException {
        File schematicDic = new File(AdvancedRegionMarket.getInstance().getDataFolder() + "/schematics");
        File[] schematicDicContent = schematicDic.listFiles();
        if (schematicDicContent != null) {
            List<File> worldFolders = new ArrayList<>();
            for (File schematicDicFile : schematicDicContent) {
                if (schematicDicFile.isDirectory()) {
                    worldFolders.add(schematicDicFile);
                }
            }

            for (File worldFolder : worldFolders) {
                File[] worldFolderContent = worldFolder.listFiles();
                List<File> schematicFiles = new ArrayList<>();

                for (File worldFolderFile : worldFolderContent) {
                    if (worldFolderFile.isFile()) {
                        schematicFiles.add(worldFolderFile);
                    }
                }
                for (File schematicFile : schematicFiles) {
                    String fileName = schematicFile.getName();
                    String[] filePaths = fileName.split(Pattern.quote("."));
                    if (filePaths.length < 2) {
                        continue;
                    }
                    String fileEnding = filePaths[filePaths.length - 1];
                    String fileNameWithoutEnding = fileName.replace("." + fileEnding, "");
                    File schematicFileWithoutEnding = new File(worldFolder + "/" + fileNameWithoutEnding);
                    schematicFileWithoutEnding.mkdirs();
                    if (!fileEnding.equals("builtblocks")) {
                        Files.copy(schematicFile.getAbsoluteFile().toPath(),
                                new File(schematicFileWithoutEnding.getAbsolutePath() + "/schematic." + fileEnding).toPath(),
                                StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        Files.copy(schematicFile.toPath(), new File(schematicFileWithoutEnding.getAbsolutePath() + "/builtblocks.builtblocks").toPath(),
                                StandardCopyOption.REPLACE_EXISTING);
                    }
                    schematicFile.delete();
                }
            }

        }

        pluginConfig.set("Version", 2.14);
        pluginConfig.set("Other.RemoveEntitiesOnRegionRestore", pluginConfig.getBoolean("Other.RemoveEntitiesOnRegionBlockReset"));
        pluginConfig.set("Other.RemoveEntitiesOnRegionBlockReset", false);
        pluginConfig.set("Subregions.deleteSubregionsOnParentRegionRestore", pluginConfig.getBoolean("Subregions.deleteSubregionsOnParentRegionBlockReset"));
        pluginConfig.set("Subregions.deleteSubregionsOnParentRegionBlockReset", false);
        pluginConfig.set("Backups.createBackupOnRegionRestore", true);
        pluginConfig.set("Backups.createBackupOnRegionUnsell", true);
        AdvancedRegionMarket.getInstance().saveConfig();
    }

    private static void updateTo3p0(FileConfiguration pluginConfig) {
        pluginConfig.set("Subregions.SubregionMaxMembers", -1);
        pluginConfig.set("Subregions.SubregionPaybackPercentage", 0);
        pluginConfig.set("Version", 3.0);
        AdvancedRegionMarket.getInstance().saveConfig();
    }

    private static class UpdateHelpMethods {


        private static void replaceVariableInMessagesYML(String variable, String replacement) throws IOException {
            File messagesConfDic = new File(AdvancedRegionMarket.getInstance().getDataFolder() + "/messages.yml");
            YamlConfiguration messagesConf = YamlConfiguration.loadConfiguration(messagesConfDic);
            ConfigurationSection messagesSection = messagesConf.getConfigurationSection("Messages");
            if (messagesSection != null) {
                ArrayList<String> messageKeys = new ArrayList<String>(messagesSection.getKeys(false));

                for (String key : messageKeys) {
                    Object msgObject = messagesSection.get(key);
                    if (msgObject instanceof List) {
                        List<String> msgList = (List) msgObject;
                        for (int i = 0; i < msgList.size(); i++) {
                            msgList.set(i, msgList.get(i).replace(variable, replacement));
                        }
                    } else if (msgObject instanceof String) {
                        String msgString = (String) msgObject;
                        msgString = msgString.replace(variable, replacement);
                        messagesSection.set(key, msgString);
                    }
                }
            }
            messagesConf.save(messagesConfDic);
        }

        private static void setMessageInMessageYML(String key, Object obj) throws IOException {
            File messagesConfDic = new File(AdvancedRegionMarket.getInstance().getDataFolder() + "/messages.yml");
            YamlConfiguration messagesConf = YamlConfiguration.loadConfiguration(messagesConfDic);
            messagesConf.set(key, obj);
            messagesConf.save(messagesConfDic);
        }

        private static void replaceVariableInFlagGroupsYML(String variable, String replacement) throws IOException {
            File flaggroupsConfDic = new File(AdvancedRegionMarket.getInstance().getDataFolder() + "/flaggroups.yml");
            YamlConfiguration flaggroupsConf = YamlConfiguration.loadConfiguration(flaggroupsConfDic);

            class FlagUpdater {
                void updateFlagGroup(ConfigurationSection cSection) {
                    if (cSection == null) {
                        return;
                    }
                    ConfigurationSection availableSection = cSection.getConfigurationSection("available");
                    ConfigurationSection soldSection = cSection.getConfigurationSection("sold");
                    updateFlagSet(availableSection);
                    updateFlagSet(soldSection);
                }

                void updateFlagSet(ConfigurationSection cSection) {
                    if (cSection == null) {
                        return;
                    }
                    ArrayList<String> keys = new ArrayList<String>(cSection.getKeys(false));
                    for (String key : keys) {
                        String setting = cSection.getString(key + ".setting");
                        if (setting == null) {
                            continue;
                        }
                        setting = setting.replace(variable, replacement);
                        cSection.set(key + ".setting", setting);
                    }
                }
            }

            FlagUpdater flagUpdater = new FlagUpdater();
            ConfigurationSection groupsSection = flaggroupsConf.getConfigurationSection("FlagGroups");
            if (groupsSection != null) {
                ArrayList<String> groupKeys = new ArrayList<String>(groupsSection.getKeys(false));
                for (String key : groupKeys) {
                    flagUpdater.updateFlagGroup(groupsSection.getConfigurationSection(key));
                }
            }
            flagUpdater.updateFlagGroup(flaggroupsConf.getConfigurationSection("DefaultFlagGroup"));
            flagUpdater.updateFlagGroup(flaggroupsConf.getConfigurationSection("SubregionFlagGroup"));
            flaggroupsConf.save(flaggroupsConfDic);
        }

    }

    private static class Version implements Comparable<Version> {
        private int[] version;

        Version(int... version) {
            if(version != null) {
                this.version = version.clone();
            }
        }

        public boolean biggerThan(Version other) {
            return this.compareTo(other) > 0;
        }

        @Override
        public int compareTo(Version other) {
            for(int i = 0; i < this.version.length; i++) {
                int thisPart = this.version[i];
                if(other.version.length < i + 1) {
                    if(thisPart == 0) {
                        continue;
                    }
                    return 1;
                }
                int thatPart = other.version[i];
                if(thisPart > thatPart) {
                    return 1;
                }
                if(thisPart < thatPart) {
                    return -1;
                }
            }
            return 0;
        }
    }
}
