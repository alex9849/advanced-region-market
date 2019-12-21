package net.alex9849.arm.flaggroups;

import com.sk89q.worldguard.protection.flags.Flag;
import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.exceptions.FeatureDisabledException;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.SellType;
import net.alex9849.arm.util.Saveable;
import net.alex9849.arm.util.stringreplacer.StringCreator;
import net.alex9849.arm.util.stringreplacer.StringReplacer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.*;

public class FlagGroup implements Saveable {
    public static FlagGroup DEFAULT = new FlagGroup("Default", 10, new ArrayList<>(), new ArrayList<>());
    public static FlagGroup SUBREGION = new FlagGroup("Subregion", 10, new ArrayList<>(), new ArrayList<>());
    private boolean needsSave;
    private StringReplacer stringReplacer;
    private static boolean featureEnabled = false;

    private List<FlagSettings> flagSettingsSold;
    private List<FlagSettings> flagSettingsAvailable;

    private int priority;
    private String name;


    {
        HashMap<String, StringCreator> variableReplacements = new HashMap<>();
        variableReplacements.put("%flaggroup%", () -> {
            return FlagGroup.isFeatureEnabled()? this.getName() : Messages.REGION_INFO_FEATURE_DISABLED;
        });

        this.stringReplacer = new StringReplacer(variableReplacements, 20);
    }


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
        if (soldSection != null) {
            flagListSold = parseFlags(soldSection);
        }

        ConfigurationSection availableSection = configurationSection.getConfigurationSection("available");
        if (availableSection != null) {
            flagListAvailable = parseFlags(availableSection);
        }

        return new FlagGroup(name, configurationSection.getInt("priority"), flagListSold, flagListAvailable);
    }

    private static List<FlagSettings> parseFlags(ConfigurationSection yamlConfiguration) {
        List<FlagSettings> flagSettingsList = new ArrayList<>();

        Set<String> flagNames = yamlConfiguration.getKeys(false);
        for (String id : flagNames) {
            String settings = yamlConfiguration.getString(id + ".setting");
            String flagName = yamlConfiguration.getString(id + ".flag");
            String editPermission = yamlConfiguration.getString(id + ".editPermission");
            boolean editable = yamlConfiguration.getBoolean(id + ".editable");
            List<String> applyToString = yamlConfiguration.getStringList(id + ".applyto");
            Set<SellType> applyTo = new TreeSet<>();
            List<String> guiDescriptionList = yamlConfiguration.getStringList(id + ".guidescription");
            List<String> guidescription = new ArrayList<>();
            if (editPermission == null || editPermission.contains(" ")) {
                editPermission = "";
            }

            if (applyToString == null || applyToString.isEmpty()) {
                applyTo.addAll(Arrays.asList(SellType.values()));
            } else {
                for (String sellTypeString : applyToString) {
                    SellType sellType = SellType.getSelltype(sellTypeString);
                    if (sellType != null) {
                        applyTo.add(sellType);
                    }
                }
            }

            if (guiDescriptionList != null) {
                for (String msg : guiDescriptionList) {
                    guidescription.add(msg);
                }
            }


            Flag flag = AdvancedRegionMarket.getInstance().getWorldGuardInterface().fuzzyMatchFlag(flagName);

            if (flag == null) {
                Bukkit.getLogger().info("Could not find flag " + flagName + "! Please check your flaggroups.yml");
                continue;
            }
            flagSettingsList.add(new FlagSettings(flag, editable, settings, applyTo, guidescription, editPermission));
        }
        return flagSettingsList;
    }

    /**
     * Applies all flags of the flaggroup to the region, that qualify
     * to the state of the region.
     * @param region the region
     * @param resetMode the resetmode. If COMPLETE all flags will be applied
     *                  if NON_EDITABLE only flags will be applied that cannot
     *                  be edited by players
     * @param forceApply if true the flaggroup will be applied even if the feature is disabled.
     *                   No exception will be thrown
     * @throws FeatureDisabledException if the FlagGroup-feature is disabled
     */
    public void applyToRegion(Region region, ResetMode resetMode, boolean forceApply) throws FeatureDisabledException {
        if (!(this.isFeatureEnabled() || forceApply)) {
            throw new FeatureDisabledException();
        }
        if (region.isSold()) {
            this.applyFlagMapToRegion(this.flagSettingsSold, region, resetMode);
        } else {
            this.applyFlagMapToRegion(this.flagSettingsAvailable, region, resetMode);
        }
    }

    private void applyFlagMapToRegion(List<FlagSettings> flagSettingsList, Region region, ResetMode resetMode) {
        if (resetMode == ResetMode.COMPLETE) {
            region.getRegion().deleteAllFlags();
        }

        for (FlagSettings flagSettings : flagSettingsList) {
            if (!flagSettings.getApplyTo().contains(region.getSellType())) {
                continue;
            }
            if (resetMode == ResetMode.NON_EDITABLE && flagSettings.isEditable()) {
                continue;
            }

            flagSettings.applyTo(region);
        }
        if (!region.isSubregion()) {
            region.getRegion().setPriority(this.priority);
        }
    }

    @Override
    public ConfigurationSection toConfigurationSection() {
        YamlConfiguration configurationSection = new YamlConfiguration();
        configurationSection.set("priority", this.priority);
        configurationSection.set("available", this.getFlagSettingsAsConfigurationSection(this.flagSettingsAvailable));
        configurationSection.set("sold", this.getFlagSettingsAsConfigurationSection(this.flagSettingsSold));
        return configurationSection;
    }

    private ConfigurationSection getFlagSettingsAsConfigurationSection(List<FlagSettings> flagSettings) {
        YamlConfiguration yamlConfiguration = new YamlConfiguration();

        for (int i = 0; i < flagSettings.size(); i++) {
            FlagSettings flagSetting = flagSettings.get(i);
            yamlConfiguration.set(i + ".setting", flagSetting.getSettings());
            yamlConfiguration.set(i + ".editable", flagSetting.isEditable());
            yamlConfiguration.set(i + ".flag", flagSetting.getFlag().getName());
            yamlConfiguration.set(i + ".editPermission", flagSetting.getEditPermission());
            yamlConfiguration.set(i + ".guidescription", flagSetting.getRawGuiDescription());
            List<String> applyTo = new ArrayList<>();
            if (!flagSetting.getApplyTo().containsAll(Arrays.asList(SellType.SELL, SellType.CONTRACT, SellType.RENT))) {
                for (SellType sellType : flagSetting.getApplyTo()) {
                    applyTo.add(sellType.getName());
                }
            }
            yamlConfiguration.set(i + ".applyto", applyTo);
        }

        return yamlConfiguration;
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

    public String getName() {
        return this.name;
    }

    public String getConvertedMessage(String message) {
        return this.stringReplacer.replace(message).toString();
    }

    public enum ResetMode {
        COMPLETE, NON_EDITABLE
    }

    public static void setFeatureEnabled(boolean enabled) {
        FlagGroup.featureEnabled = enabled;
    }

    public static boolean isFeatureEnabled() {
        return FlagGroup.featureEnabled;
    }
}
