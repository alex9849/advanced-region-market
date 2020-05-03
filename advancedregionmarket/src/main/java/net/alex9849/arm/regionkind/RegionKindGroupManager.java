package net.alex9849.arm.regionkind;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.util.YamlFileManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RegionKindGroupManager extends YamlFileManager<RegionKindGroup> {

    public RegionKindGroupManager(File savepath, RegionKindManager regionKindManager) {
        super(savepath);
        regionKindManager.setRegionKindGroupManager(this);
    }

    @Override
    public boolean staticSaveQuenued() {
        return false;
    }

    @Override
    protected List<RegionKindGroup> loadSavedObjects(YamlConfiguration yamlConfiguration) {
        List<RegionKindGroup> loadedGroups = new ArrayList<>();
        boolean fileupdated = false;
        yamlConfiguration.options().copyDefaults(true);
        ConfigurationSection allGroupsSection = yamlConfiguration.getConfigurationSection("RegionkindGroups");
        if(allGroupsSection == null) {
            return loadedGroups;
        }
        for(String groupName : allGroupsSection.getKeys(false)) {
            ConfigurationSection groupSection = allGroupsSection.getConfigurationSection(groupName);
            if(groupSection == null) {
                continue;
            }
            fileupdated |= updateDefaults(groupSection);
            RegionKindGroup rkg = new RegionKindGroup(groupName);
            rkg.setDisplayName(groupSection.getString("displayName"));
            rkg.setDisplayInLimits(groupSection.getBoolean("displayInLimits"));
            for(String regionkindName : groupSection.getStringList("regionKinds")) {
                RegionKind regionKind = AdvancedRegionMarket.getInstance().getRegionKindManager().getRegionKind(regionkindName);
                if(regionKind == null) {
                    continue;
                }
                rkg.addRegionKind(regionKind);
            }
            loadedGroups.add(rkg);
            rkg.setSaved();
        }
        if (fileupdated) {
            this.saveFile();
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

    private boolean updateDefaults(ConfigurationSection section) {
        boolean fileupdated = false;
        fileupdated |= addDefault(section, "displayName", "Default Displayname");
        fileupdated |= addDefault(section, "displayInLimits", true);
        fileupdated |= addDefault(section, "regionKinds", new ArrayList<String>());
        return fileupdated;
    }

    void notifyRegionKindDelete(RegionKind regionKind) {
        for(RegionKindGroup rkg : this) {
            rkg.removeRegionKind(regionKind);
        }
    }

    public RegionKindGroup getRegionKindGroup(String name) {
        for(RegionKindGroup rkg : this) {
            if(rkg.getName().equalsIgnoreCase(name)) {
                return rkg;
            }
        }
        return null;
    }

    public List<String> tabCompleteRegionKindGroups(String name) {
        List<String> completions = new ArrayList<>();
        for(RegionKindGroup rkg : this) {
            if(rkg.getName().toLowerCase().startsWith(name)) {
                completions.add(rkg.getName());
            }
        }
        return completions;
    }

    public List<RegionKindGroup> getRegionKindGroupsForRegionKind(RegionKind regionKind) {
        List<RegionKindGroup> result = new ArrayList<>();
        for(RegionKindGroup rkg : this) {
            if(rkg.contains(regionKind)) {
                result.add(rkg);
            }
        }
        return result;
    }
}
