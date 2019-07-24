package net.alex9849.arm.flaggroups;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
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
            try {
                Tuple<String, Boolean> flagSettings = flagMap.get(rgFlag);
                if(resetMode == ResetMode.NON_EDITABLE && flagSettings.getValue2()) {
                    continue;
                }
                Object wgFlagSettings = AdvancedRegionMarket.getWorldGuardInterface().parseFlagInput(rgFlag, region.getConvertedMessage(flagSettings.getValue1()));
                region.getRegion().setFlag(rgFlag, wgFlagSettings);
            } catch (InvalidFlagFormat invalidFlagFormat) {
                Bukkit.getLogger().info("Could not parse flag-settings for flag " + rgFlag.getName() + "! Please check your flaggroups.yml");
            }
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
}
