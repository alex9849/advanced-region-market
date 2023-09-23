package net.alex9849.arm.regionkind;

import net.alex9849.arm.adapters.util.ConcatedIterator;
import net.alex9849.arm.adapters.util.YamlFileManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
        List<String> returnme = new ArrayList<>();
        for (RegionKind regionkind : this) {
            if ((returnPrefix + regionkind.getName()).toLowerCase().startsWith(arg)) {
                returnme.add(returnPrefix + regionkind.getName());
            }
        }
        return returnme;
    }

    @Override
    public Iterator<RegionKind> iterator() {
        return new ConcatedIterator<>(Arrays.asList(RegionKind.DEFAULT, RegionKind.SUBREGION).iterator(), super.iterator());
    }

    public boolean kindExists(String kind) {
        for (RegionKind regionKind : this) {
            if (regionKind.getName().equalsIgnoreCase(kind)) {
                return true;
            }
        }
        return false;
    }

    public RegionKind getRegionKind(String name) {
        for (RegionKind regionKind : this) {
            if (regionKind.getName().equalsIgnoreCase(name)) {
                return regionKind;
            }
        }
        return null;
    }

    private boolean updateDefaults(ConfigurationSection section) {
        boolean fileupdated = false;
        fileupdated |= addDefault(section, "item", "RED_BED");
        fileupdated |= addDefault(section, "displayName", "Default Displayname");
        fileupdated |= addDefault(section, "displayInLimits", true);
        fileupdated |= addDefault(section, "displayInRegionfinder", true);
        fileupdated |= addDefault(section, "lore", new ArrayList<String>(Arrays.asList("Default lore")));
        return fileupdated;
    }

    public void setRegionKindGroupManager(RegionKindGroupManager regionKindGroupManger) {
        this.regionKindGroupManager = regionKindGroupManger;
    }
}
