package net.alex9849.arm.flaggroups;

import net.alex9849.arm.adapters.util.ConcatedIterator;
import net.alex9849.arm.adapters.util.YamlFileManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class FlagGroupManager extends YamlFileManager<FlagGroup> {

    public FlagGroupManager(File savepath) {
        super(savepath);
    }

    @Override
    protected List<FlagGroup> loadSavedObjects(YamlConfiguration yamlConfiguration) {
        if (yamlConfiguration.get("DefaultFlagGroup") != null) {
            ConfigurationSection defaultRkConfig = yamlConfiguration.getConfigurationSection("DefaultFlagGroup");
            FlagGroup.DEFAULT = FlagGroup.parse(defaultRkConfig, "Default");
        }
        if (yamlConfiguration.get("SubregionFlagGroup") != null) {
            ConfigurationSection subregionRkConfig = yamlConfiguration.getConfigurationSection("SubregionFlagGroup");
            FlagGroup.SUBREGION = FlagGroup.parse(subregionRkConfig, "SubRegion");
        }

        List<FlagGroup> flagGroupList = new ArrayList<>();
        ConfigurationSection flagGroupsSection = yamlConfiguration.getConfigurationSection("FlagGroups");
        if (flagGroupsSection == null) {
            return flagGroupList;
        }
        Set<String> flagGroupNames = flagGroupsSection.getKeys(false);
        if (flagGroupNames == null) {
            return flagGroupList;
        }
        for (String flaggroupsName : flagGroupNames) {
            ConfigurationSection flagGroupSection = flagGroupsSection.getConfigurationSection(flaggroupsName);
            if (flagGroupSection == null) {
                continue;
            }
            flagGroupList.add(FlagGroup.parse(flagGroupSection, flaggroupsName));
        }
        return flagGroupList;
    }

    @Override
    public Iterator<FlagGroup> iterator() {
        return new ConcatedIterator<>(Arrays.asList(FlagGroup.DEFAULT, FlagGroup.SUBREGION).iterator(), super.iterator());
    }

    @Override
    public boolean staticSaveQuenued() {
        return FlagGroup.DEFAULT.needsSave() || FlagGroup.SUBREGION.needsSave();
    }

    @Override
    protected void saveObjectToYamlObject(FlagGroup flagGroup, YamlConfiguration yamlConfiguration) {
        yamlConfiguration.set("FlagGroups." + flagGroup.getName(), flagGroup.toConfigurationSection());
    }

    @Override
    protected void writeStaticSettings(YamlConfiguration yamlConfiguration) {
        yamlConfiguration.set("DefaultFlagGroup", FlagGroup.DEFAULT.toConfigurationSection());
        FlagGroup.DEFAULT.setSaved();
        yamlConfiguration.set("SubregionFlagGroup", FlagGroup.SUBREGION.toConfigurationSection());
        FlagGroup.SUBREGION.setSaved();
    }

    public FlagGroup getFlagGroup(String id) {
        for (FlagGroup flagGroup : this) {
            if (flagGroup.getName().equalsIgnoreCase(id)) {
                return flagGroup;
            }
        }
        return null;
    }

    public List<String> tabCompleteFlaggroup(String name) {
        List<String> returnme = new ArrayList<>();

        for (FlagGroup flagGroup : this) {
            if (flagGroup.getName().startsWith(name)) {
                returnme.add(flagGroup.getName());
            }
        }
        return returnme;
    }
}
