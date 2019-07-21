package net.alex9849.arm.flaggroups;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.util.Saveable;
import net.alex9849.arm.util.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FlagGroup implements Saveable {
    private boolean needsSave;
    private Map<Flag<?>, Tuple<String, Boolean>> flagMapSold;
    private Map<Flag<?>, Tuple<String, Boolean>> flagMapAvailable;
    private int priority;
    private String name;


    public FlagGroup(String name, int priority, Map<Flag<?>, Tuple<String, Boolean>> flagsSold, Map<Flag<?>, Tuple<String, Boolean>> flagsAvailable) {
        this.needsSave = false;
        this.name = name;
        this.priority = priority;
        this.flagMapSold = new HashMap<>();
        this.flagMapSold.putAll(flagsSold);
        this.flagMapAvailable = new HashMap<>();
        this.flagMapAvailable.putAll(flagsAvailable);
    }

    public static FlagGroup parse(ConfigurationSection configurationSection, String name) {
        Map<Flag<?>, Tuple<String, Boolean>> flagMapSold = new HashMap<>();
        Map<Flag<?>, Tuple<String, Boolean>> flagMapAvailable = new HashMap<>();

        ConfigurationSection soldSection = configurationSection.getConfigurationSection("sold");
        if(soldSection != null) {
            flagMapSold = parseFlags(soldSection);
        }

        ConfigurationSection availableSection = configurationSection.getConfigurationSection("available");
        if(availableSection != null) {
            flagMapAvailable = parseFlags(availableSection);
        }

        return new FlagGroup(name, 0, flagMapSold, flagMapAvailable);
    }

    private static Map<Flag<?>, Tuple<String, Boolean>> parseFlags(ConfigurationSection yamlConfiguration) {
        Map<Flag<?>, Tuple<String, Boolean>> flagMap = new HashMap<>();

        Set<String> flagNames = yamlConfiguration.getKeys(false);
        if(flagNames != null) {
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
        }
        return flagMap;
    }

    public void applyToRegion(Region region) {
        if(region.isSold()) {
            this.applyFlagMapToRegion(this.flagMapSold, region);
        } else {
            this.applyFlagMapToRegion(this.flagMapAvailable, region);
        }
    }

    private void applyFlagMapToRegion(Map<Flag<?>, Tuple<String, Boolean>> flagMap, Region region) {
        for(Flag rgFlag : flagMap.keySet()) {
            try {
                Object flagsetting = AdvancedRegionMarket.getWorldGuardInterface().parseFlagInput(rgFlag, region.getConvertedMessage(flagMap.get(rgFlag).getValue1()));
                region.getRegion().setFlag(rgFlag, flagsetting);
            } catch (InvalidFlagFormat invalidFlagFormat) {
                Bukkit.getLogger().info("Could not parse flag-settings for flag " + rgFlag.getName() + "! Please check your flaggroups.yml");
                continue;
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
}
