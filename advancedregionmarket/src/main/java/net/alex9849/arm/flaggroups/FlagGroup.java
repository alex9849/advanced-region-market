package net.alex9849.arm.flaggroups;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.RegionGroupFlag;
import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.SellType;
import net.alex9849.arm.util.Saveable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class FlagGroup implements Saveable {
    public static FlagGroup DEFAULT = new FlagGroup("Default", 10, new ArrayList<>(), new ArrayList<>());
    public static FlagGroup SUBREGION = new FlagGroup("Subregion", 10, new ArrayList<>(), new ArrayList<>());
    private boolean needsSave;

    private List<FlagSettings> flagSettingsSold;
    private List<FlagSettings> flagSettingsAvailable;

    private int priority;
    private String name;


    public FlagGroup(String name, int priority, List<FlagSettings> flagsSold, List<FlagSettings> flagsAvailable) {
        this.needsSave = false;
        this.name = name;
        this.priority = priority;
        this.flagSettingsSold = flagsSold;
        this.flagSettingsAvailable = flagsAvailable;
    }

    static FlagGroup parse(ConfigurationSection configurationSection, String name) {
        List<FlagSettings> flagListSold = new ArrayList<>();
        List<FlagSettings> flagListAvailable = new ArrayList<>();

        ConfigurationSection soldSection = configurationSection.getConfigurationSection("sold");
        if(soldSection != null) {
            flagListSold = parseFlags(soldSection);
        }

        ConfigurationSection availableSection = configurationSection.getConfigurationSection("available");
        if(availableSection != null) {
            flagListAvailable = parseFlags(availableSection);
        }

        return new FlagGroup(name, configurationSection.getInt("priority"), flagListSold, flagListAvailable);
    }

    private static List<FlagSettings> parseFlags(ConfigurationSection yamlConfiguration) {
        List<FlagSettings> flagSettingsList = new ArrayList<>();

        Set<String> flagNames = yamlConfiguration.getKeys(false);
        for(String id : flagNames) {
            String settings = yamlConfiguration.getString(id + ".setting");
            String flagName = yamlConfiguration.getString(id + ".flag");
            String editPermission = yamlConfiguration.getString(id + ".editPermission");
            boolean editable = yamlConfiguration.getBoolean(id + ".editable");
            List<String> applyToString = yamlConfiguration.getStringList(id + ".applyto");
            Set<SellType> applyTo = new TreeSet<>();
            List<String> guiDescriptionList = yamlConfiguration.getStringList(id + ".guidescription");
            List<String> guidescription = new ArrayList<>();
            if(editPermission == null || editPermission.contains(" ")) {
                editPermission = "";
            }

            if(applyToString == null || applyToString.isEmpty()) {
                applyTo.addAll(Arrays.asList(SellType.values()));
            }

            if(guiDescriptionList != null) {
                for(String msg:guiDescriptionList) {
                    guidescription.add(ChatColor.translateAlternateColorCodes('&', msg));
                }
            }

            for(String sellTypeString : applyToString) {
                SellType sellType = SellType.getSelltype(sellTypeString);
                if(sellType != null) {
                    applyTo.add(sellType);
                }
            }

            Flag flag = AdvancedRegionMarket.getWorldGuardInterface().fuzzyMatchFlag(flagName);

            if(flag == null) {
                Bukkit.getLogger().info("Could not find flag " + flagName + "! Please check your flaggroups.yml");
                continue;
            }
            flagSettingsList.add(new FlagSettings(flag, editable, settings, applyTo, guidescription, editPermission));
            }
        return flagSettingsList;
    }

    public void applyToRegion(Region region, ResetMode resetMode) {
        if(region.isSold()) {
            this.applyFlagMapToRegion(this.flagSettingsSold, region, resetMode);
        } else {
            this.applyFlagMapToRegion(this.flagSettingsAvailable, region, resetMode);
        }
    }

    private void applyFlagMapToRegion(List<FlagSettings> flagSettingsList, Region region, ResetMode resetMode) {
        if(resetMode == ResetMode.COMPLETE) {
            region.getRegion().deleteAllFlags();
        }

        for(FlagSettings flagSettings : flagSettingsList) {
            if(!flagSettings.getApplyTo().contains(region.getSellType())) {
                continue;
            }
            if(resetMode == ResetMode.NON_EDITABLE && flagSettings.isEditable()) {
                continue;
            }

            if(flagSettings.getSettings() == null || flagSettings.getSettings().isEmpty()
                    || flagSettings.getSettings().equalsIgnoreCase("remove")) {
                region.getRegion().deleteFlags(flagSettings.getFlag());
            } else {
                RegionGroupFlag groupFlag = flagSettings.getFlag().getRegionGroupFlag();
                String settings = null;
                RegionGroup groupFlagSettings = null;

                if(groupFlag == null) {
                    settings = flagSettings.getSettings();
                } else {
                    for(String part : flagSettings.getSettings().split(" ")) {
                        if(part.startsWith("g:")) {
                            if(part.length() > 2) {
                                try {
                                    groupFlagSettings = AdvancedRegionMarket.getWorldGuardInterface().parseFlagInput(groupFlag, part.substring(2));
                                } catch (InvalidFlagFormat iff) {
                                    Bukkit.getLogger().info("Could not parse groupflag-settings for groupflag " + groupFlag.getName() + "! Flag will be ignored! Please check your flaggroups.yml");
                                    continue;
                                }
                            }
                        } else {
                            if(settings == null) {
                                settings = part;
                            } else {
                                settings += " " + part;
                            }
                        }
                    }
                }

                if(settings != null) {
                    try {
                        Object wgFlagSettings = AdvancedRegionMarket.getWorldGuardInterface().parseFlagInput(flagSettings.getFlag(), region.getConvertedMessage(settings));
                        region.getRegion().setFlag(flagSettings.getFlag(), wgFlagSettings);
                    } catch (InvalidFlagFormat invalidFlagFormat) {
                        Bukkit.getLogger().info("Could not parse flag-settings for flag " + flagSettings.getFlag().getName() + "! Flag will be ignored! Please check your flaggroups.yml");
                        continue;
                    }
                }
                if(groupFlagSettings != null) {
                    if(groupFlagSettings == groupFlag.getDefault()) {
                        region.getRegion().deleteFlags(groupFlag);
                    } else {
                        region.getRegion().setFlag(groupFlag, groupFlagSettings);
                    }
                }



            }
        }
        if(!region.isSubregion()) {
            region.getRegion().setPriority(this.priority);
        }
    }

    @Override
    public ConfigurationSection toConfigurationSection() {
        return null;
    }

    @Override
    public void queueSave() {
        this.needsSave = true;
    }

    @Override
    public void setSaved() {
        this.needsSave = false;
    }

    @Override
    public boolean needsSave() {
        return this.needsSave;
    }

    public List<FlagSettings> getFlagSettingsSold() {
        return flagSettingsSold;
    }

    public List<FlagSettings> getFlagSettingsAvailable() {
        return flagSettingsAvailable;
    }

    public enum ResetMode {
        COMPLETE, NON_EDITABLE
    }

    public String getName() {
        return this.name;
    }

    public String getConvertedMessage(String message) {
        if(message.contains("%flaggroup%")) message = message.replace("%flaggroup%", this.name);
        return message;
    }
}
