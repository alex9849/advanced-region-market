package net.alex9849.arm.regionkind;

import net.alex9849.arm.util.YamlFileManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegionKindManager extends YamlFileManager<RegionKind> {
    private RegionKindGroupManager regionKindGroupManager = null;

    public RegionKindManager(File savepath) {
        super(savepath);
    }

    @Override
    public boolean remove(RegionKind regionKind) {
        if(super.remove(regionKind) && regionKindGroupManager != null) {
            regionKindGroupManager.notifyRegionKindDelete(regionKind);
            return true;
        }
        return false;
    }

    @Override
    protected List<RegionKind> loadSavedObjects(YamlConfiguration yamlConfiguration) {
        List<RegionKind> regionKindList = new ArrayList<>();
        boolean fileupdated = false;

        yamlConfiguration.options().copyDefaults(true);

        if (yamlConfiguration.get("DefaultRegionKind") != null) {
            ConfigurationSection defaultRkConfig = yamlConfiguration.getConfigurationSection("DefaultRegionKind");
            fileupdated |= updateDefaults(defaultRkConfig);
            RegionKind.DEFAULT = RegionKind.parse(defaultRkConfig, "Default");
        }

        if (yamlConfiguration.get("SubregionRegionKind") != null) {
            ConfigurationSection subregionRkConfig = yamlConfiguration.getConfigurationSection("SubregionRegionKind");
            fileupdated |= updateDefaults(subregionRkConfig);
            RegionKind.SUBREGION = RegionKind.parse(subregionRkConfig, "Subregion");
        }

        if (yamlConfiguration.get("RegionKinds") != null) {
            ConfigurationSection regionKindsSection = yamlConfiguration.getConfigurationSection("RegionKinds");
            List<String> regionKinds = new ArrayList<>(regionKindsSection.getKeys(false));
            if (regionKinds != null) {
                for (String regionKindID : regionKinds) {
                    if (regionKindsSection.get(regionKindID) != null) {
                        ConfigurationSection rkConfSection = regionKindsSection.getConfigurationSection(regionKindID);
                        if (rkConfSection != null) {
                            fileupdated |= updateDefaults(rkConfSection);
                            regionKindList.add(RegionKind.parse(rkConfSection, regionKindID));
                        }
                    }
                }
            }
        }

        if (fileupdated) {
            this.saveFile();
        }
        yamlConfiguration.options().copyDefaults(false);
        return regionKindList;
    }

    @Override
    public boolean staticSaveQuenued() {
        return RegionKind.DEFAULT.needsSave() || RegionKind.SUBREGION.needsSave();
    }

    @Override
    protected void saveObjectToYamlObject(RegionKind regionKind, YamlConfiguration yamlConfiguration) {
        yamlConfiguration.set("RegionKinds." + regionKind.getName(), regionKind.toConfigurationSection());
    }

    @Override
    protected void writeStaticSettings(YamlConfiguration yamlConfiguration) {
        yamlConfiguration.set("DefaultRegionKind", RegionKind.DEFAULT.toConfigurationSection());
        RegionKind.DEFAULT.setSaved();
        yamlConfiguration.set("SubregionRegionKind", RegionKind.SUBREGION.toConfigurationSection());
        RegionKind.SUBREGION.setSaved();
    }

    public List<String> completeTabRegionKinds(String arg, String returnPrefix) {
        return completeTabRegionKinds(arg, returnPrefix, null);
    }

    public List<String> completeTabRegionKinds(String arg, String returnPrefix, Player player) {
        List<String> returnme = new ArrayList<>();

        for (RegionKind regionkind : this) {
            if ((returnPrefix + regionkind.getName()).toLowerCase().startsWith(arg)) {
                returnme.add(returnPrefix + regionkind.getName());
            }
        }
        if ((returnPrefix + "default").startsWith(arg)) {
            returnme.add(returnPrefix + "default");
        }
        if ((returnPrefix + "subregion").startsWith(arg)) {
            returnme.add(returnPrefix + "subregion");
        }

        return returnme;
    }

    public boolean kindExists(String kind) {

        for (RegionKind regionKind : this) {
            if (regionKind.getName().equalsIgnoreCase(kind)) {
                return true;
            }
        }

        if (kind.equalsIgnoreCase("default")) {
            return true;
        }
        if (kind.equalsIgnoreCase(RegionKind.DEFAULT.getDisplayName())) {
            return true;
        }
        if (kind.equalsIgnoreCase("subregion")) {
            return true;
        }
        return kind.equalsIgnoreCase(RegionKind.SUBREGION.getDisplayName());
    }

    public RegionKind getRegionKind(String name) {

        for (RegionKind regionKind : this) {
            if (regionKind.getName().equalsIgnoreCase(name)) {
                return regionKind;
            }
        }

        if (name.equalsIgnoreCase("default") || name.equalsIgnoreCase(RegionKind.DEFAULT.getDisplayName())) {
            return RegionKind.DEFAULT;
        }
        if (name.equalsIgnoreCase("subregion") || name.equalsIgnoreCase(RegionKind.SUBREGION.getDisplayName())) {
            return RegionKind.SUBREGION;
        }
        return null;
    }

    private boolean updateDefaults(ConfigurationSection section) {
        boolean fileupdated = false;
        fileupdated |= addDefault(section, "item", "RED_BED");
        fileupdated |= addDefault(section, "displayName", "Default Displayname");
        fileupdated |= addDefault(section, "displayInLimits", true);
        fileupdated |= addDefault(section, "displayInGUI", true);
        fileupdated |= addDefault(section, "lore", new ArrayList<String>(Arrays.asList("Default lore")));
        return fileupdated;
    }

    public void setRegionKindGroupManager(RegionKindGroupManager regionKindGroupManger) {
        this.regionKindGroupManager = regionKindGroupManger;
    }
}
