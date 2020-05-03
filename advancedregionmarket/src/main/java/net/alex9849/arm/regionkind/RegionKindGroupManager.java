package net.alex9849.arm.regionkind;

import net.alex9849.arm.util.YamlFileManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RegionKindGroupManager extends YamlFileManager<RegionKindGroup> {
    private RegionKindManager regionkindManager;

    public RegionKindGroupManager(File savepath, RegionKindManager regionKindManager) {
        super(savepath);
        this.regionkindManager = regionKindManager;
        this.regionkindManager.setRegionKindGroupManager(this);
    }

    @Override
    public boolean staticSaveQuenued() {
        return false;
    }

    @Override
    protected List<RegionKindGroup> loadSavedObjects(YamlConfiguration yamlConfiguration) {
        List<RegionKindGroup> loadedGroups = new ArrayList<>();
        ConfigurationSection allGroupsSection = yamlConfiguration.getConfigurationSection("RegionkindGroups");
        if(allGroupsSection == null) {
            return loadedGroups;
        }
        for(String groupName : allGroupsSection.getKeys(false)) {
            ConfigurationSection groupSection = allGroupsSection.getConfigurationSection(groupName);
            if(groupSection == null) {
                continue;
            }
            RegionKindGroup rkg = new RegionKindGroup(groupName);
            rkg.setDisplayName(groupSection.getString("displayName"));
            rkg.setDisplayInLimits(groupSection.getBoolean("displayInLimits"));
            for(String regionkindName : groupSection.getStringList("regionKinds")) {
                RegionKind regionKind = this.regionkindManager.getRegionKind(regionkindName);
                if(regionKind == null) {
                    continue;
                }
                rkg.addRegionKind(regionKind);
            }
            rkg.setSaved();
        }
        return loadedGroups;
    }

    @Override
    protected void saveObjectToYamlObject(RegionKindGroup regionKindGroup, YamlConfiguration yamlConfiguration) {
        yamlConfiguration.set("RegionkindGroups." + regionKindGroup.getName(), regionKindGroup.toConfigurationSection());
    }

    @Override
    protected void writeStaticSettings(YamlConfiguration yamlConfiguration) {

    }

    void notifyRegionKindDelete(RegionKind regionKind) {
        for(RegionKindGroup rkg : this) {
            rkg.removeRegionKind(regionKind);
        }
    }
}
