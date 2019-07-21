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
}
