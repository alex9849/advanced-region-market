package net.alex9849.arm.flaggroups;

import net.alex9849.arm.util.YamlFileManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FlagGroupManager extends YamlFileManager<FlagGroup> {

    public FlagGroupManager(File savepath) {
        super(savepath);
    }

    @Override
    public List loadSavedObjects(YamlConfiguration yamlConfiguration) {
        if(yamlConfiguration.get("DefaultFlagGroup") != null) {
            ConfigurationSection defaultRkConfig = yamlConfiguration.getConfigurationSection("DefaultFlagGroup");
            FlagGroup.DEFAULT = FlagGroup.parse(defaultRkConfig, "Default");
        }
        if(yamlConfiguration.get("SubregionFlagGroup") != null) {
            ConfigurationSection subregionRkConfig = yamlConfiguration.getConfigurationSection("SubregionFlagGroup");
            FlagGroup.SUBREGION = FlagGroup.parse(subregionRkConfig, "SubRegion");
        }

        List<FlagGroup> flagGroupList = new ArrayList<>();
        ConfigurationSection flagGroupsSection = yamlConfiguration.getConfigurationSection("FlagGroups");
        if(flagGroupsSection == null) {
            return flagGroupList;
        }
        Set<String> flagGroupNames = flagGroupsSection.getKeys(false);
        if(flagGroupNames == null) {
            return flagGroupList;
        }
        for(String flaggroupsName : flagGroupNames) {
            ConfigurationSection flagGroupSection = flagGroupsSection.getConfigurationSection(flaggroupsName);
            if(flagGroupSection == null) {
                continue;
            }
            flagGroupList.add(FlagGroup.parse(flagGroupSection, flaggroupsName));
        }
        return flagGroupList;
    }

    @Override
    public boolean staticSaveQuenued() {
        return false;
    }

    @Override
    public void saveObjectToYamlObject(FlagGroup flagGroup, YamlConfiguration yamlConfiguration) {

    }

    @Override
    public void writeStaticSettings(YamlConfiguration yamlConfiguration) {

    }

    public FlagGroup getFlagGroup(String id) {
        if("default".equalsIgnoreCase(id)) {
            return FlagGroup.DEFAULT;
        }
        if("subregion".equalsIgnoreCase(id)) {
            return FlagGroup.SUBREGION;
        }
        for(FlagGroup flagGroup : this) {
            if(flagGroup.getName().equalsIgnoreCase(id)) {
                return flagGroup;
            }
        }
        return null;
    }

    public List<String> tabCompleteFlaggroup(String name) {
        List<String> returnme = new ArrayList<>();

        for(FlagGroup flagGroup : this) {
            if(flagGroup.getName().startsWith(name)) {
                returnme.add(flagGroup.getName());
            }
        }

        if("default".startsWith(name)) {
            returnme.add("Default");
        }

        if("subregion".startsWith(name)) {
            returnme.add("Subregion");
        }

        return returnme;
    }
}
