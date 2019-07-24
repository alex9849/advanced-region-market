package net.alex9849.arm.flaggroups;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.RegionGroupFlag;
import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.util.Saveable;
import net.alex9849.arm.util.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FlagGroup implements Saveable {
    public static FlagGroup DEFAULT = new FlagGroup("Default", 10, new HashMap<>(), new HashMap<>());
    public static FlagGroup SUBREGION = new FlagGroup("Subregion", 10, new HashMap<>(), new HashMap<>());
    private boolean needsSave;
    private Map<Flag, Tuple<String, Boolean>> flagMapSold;
    private Map<Flag, Tuple<String, Boolean>> flagMapAvailable;
    private int priority;
    private String name;


    public FlagGroup(String name, int priority, Map<Flag, Tuple<String, Boolean>> flagsSold, Map<Flag, Tuple<String, Boolean>> flagsAvailable) {
        this.needsSave = false;
        this.name = name;
        this.priority = priority;
        this.flagMapSold = new HashMap<>();
        this.flagMapSold.putAll(flagsSold);
        this.flagMapAvailable = new HashMap<>();
        this.flagMapAvailable.putAll(flagsAvailable);
    }

    static FlagGroup parse(ConfigurationSection configurationSection, String name) {
        Map<Flag, Tuple<String, Boolean>> flagMapSold = new HashMap<>();
        Map<Flag, Tuple<String, Boolean>> flagMapAvailable = new HashMap<>();

        ConfigurationSection soldSection = configurationSection.getConfigurationSection("sold");
        if(soldSection != null) {
            flagMapSold = parseFlags(soldSection);
        }

        ConfigurationSection availableSection = configurationSection.getConfigurationSection("available");
        if(availableSection != null) {
            flagMapAvailable = parseFlags(availableSection);
        }

        return new FlagGroup(name, configurationSection.getInt("priority"), flagMapSold, flagMapAvailable);
    }

    private static Map<Flag, Tuple<String, Boolean>> parseFlags(ConfigurationSection yamlConfiguration) {
        Map<Flag, Tuple<String, Boolean>> flagMap = new HashMap<>();

        Set<String> flagNames = yamlConfiguration.getKeys(false);
        for(String flagName : flagNames) {
                String settings = yamlConfiguration.getString(flagName + ".setting");
                boolean editable = yamlConfiguration.getBoolean(flagName + ".editable");

                Flag flag = AdvancedRegionMarket.getWorldGuardInterface().fuzzyMatchFlag(flagName);

                if(flag == null) {
                    Bukkit.getLogger().info("Could not find flag " + flagName + "! Please check your flaggroups.yml");
                    continue;
                }
                flagMap.put(flag, new Tuple<>(settings, editable));
            }
        return flagMap;
    }

    public void applyToRegion(Region region, ResetMode resetMode) {
        if(region.isSold()) {
            this.applyFlagMapToRegion(this.flagMapSold, region, resetMode);
        } else {
            this.applyFlagMapToRegion(this.flagMapAvailable, region, resetMode);
        }
    }

    private void applyFlagMapToRegion(Map<Flag, Tuple<String, Boolean>> flagMap, Region region, ResetMode resetMode) {
        for(Flag rgFlag : flagMap.keySet()) {
            Tuple<String, Boolean> flagSettingsTupel = flagMap.get(rgFlag);
            if(resetMode == ResetMode.NON_EDITABLE && flagSettingsTupel.getValue2()) {
                continue;
            }

            if(flagSettingsTupel.getValue1() == null || flagSettingsTupel.getValue1().isEmpty()
                    || flagSettingsTupel.getValue1().equalsIgnoreCase("remove")) {
                region.getRegion().deleteFlags(rgFlag);
            } else {
                RegionGroupFlag groupFlag = rgFlag.getRegionGroupFlag();
                String flagSettings = null;
                RegionGroup groupFlagSettings = null;

                if(groupFlag == null) {
                    flagSettings = flagSettingsTupel.getValue1();
                } else {
                    for(String part : flagSettingsTupel.getValue1().split(" ")) {
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
                            if(flagSettings == null) {
                                flagSettings = part;
                            } else {
                                flagSettings += " " + part;
                            }
                        }
                    }
                }

                if(flagSettings != null) {
                    try {
                        Object wgFlagSettings = AdvancedRegionMarket.getWorldGuardInterface().parseFlagInput(rgFlag, region.getConvertedMessage(flagSettings));
                        region.getRegion().setFlag(rgFlag, wgFlagSettings);
                    } catch (InvalidFlagFormat invalidFlagFormat) {
                        Bukkit.getLogger().info("Could not parse flag-settings for flag " + rgFlag.getName() + "! Flag will be ignored! Please check your flaggroups.yml");
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

    public enum ResetMode {
        COMPLETE, NON_EDITABLE
    }

    public String getName() {
        return this.name;
    }

    public String getConvertedMessage(String message) {
        return message.replace("%flaggroup%", this.name);
    }
}
