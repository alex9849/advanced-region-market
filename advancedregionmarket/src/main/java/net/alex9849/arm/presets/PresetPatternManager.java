package net.alex9849.arm.presets;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.presets.presets.*;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.util.YamlFileManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PresetPatternManager extends YamlFileManager<Preset> {

    public PresetPatternManager(File savepath) {
        super(savepath);
    }

    @Override
    public List<Preset> loadSavedObjects(YamlConfiguration yamlConfiguration) {
        List<Preset> presetList = new ArrayList<>();
        boolean fileupdated = false;
        yamlConfiguration.options().copyDefaults(true);

        ConfigurationSection sellPresetsection = yamlConfiguration.getConfigurationSection(PresetType.SELLPRESET.getName());
        if (sellPresetsection != null) {
            ArrayList<String> presets = new ArrayList<String>(yamlConfiguration.getConfigurationSection(PresetType.SELLPRESET.getName()).getKeys(false));
            if(presets != null) {
                for(String presetName : presets) {
                    ConfigurationSection presetSection = yamlConfiguration.getConfigurationSection(PresetType.SELLPRESET.getName()+ "." + presetName);
                    fileupdated |= updateDefaults(presetSection, PresetType.SELLPRESET);
                    presetList.add(generatePresetObject(presetSection, presetName, PresetType.SELLPRESET));
                }
            }
        }

        ConfigurationSection rentPresetsection = yamlConfiguration.getConfigurationSection(PresetType.RENTPRESET.getName());
        if (rentPresetsection != null) {
            ArrayList<String> presets = new ArrayList<String>(yamlConfiguration.getConfigurationSection(PresetType.RENTPRESET.getName()).getKeys(false));
            if(presets != null) {
                for(String presetName : presets) {
                    ConfigurationSection presetSection = yamlConfiguration.getConfigurationSection(PresetType.RENTPRESET.getName()+ "." + presetName);
                    fileupdated |= updateDefaults(presetSection, PresetType.RENTPRESET);
                    presetList.add(generatePresetObject(presetSection, presetName, PresetType.RENTPRESET));
                }
            }
        }

        ConfigurationSection contractPresetsection = yamlConfiguration.getConfigurationSection(PresetType.CONTRACTPRESET.getName());
        if (contractPresetsection != null) {
            ArrayList<String> presets = new ArrayList<String>(yamlConfiguration.getConfigurationSection(PresetType.CONTRACTPRESET.getName()).getKeys(false));
            if(presets != null) {
                for(String presetName : presets) {
                    ConfigurationSection presetSection = yamlConfiguration.getConfigurationSection(PresetType.CONTRACTPRESET.getName()+ "." + presetName);
                    fileupdated |= updateDefaults(presetSection, PresetType.CONTRACTPRESET);
                    presetList.add(generatePresetObject(presetSection, presetName, PresetType.CONTRACTPRESET));
                }
            }
        }

        if(fileupdated) {
            this.saveFile();
        }

        yamlConfiguration.options().copyDefaults(false);

        return presetList;
    }

    private static Preset generatePresetObject(ConfigurationSection section, String name, PresetType presetType) {
        boolean hasprice = section.getBoolean("hasPrice");
        double price = section.getDouble("price");
        String regionKindString = section.getString("regionKind");
        String flagGroupString = section.getString("flaggroup");
        boolean isHotel = section.getBoolean("isHotel");
        boolean doBlockReset = section.getBoolean("doBlockReset");
        String entityLimitGroupString = section.getString("entityLimitGroup");
        boolean autoreset = section.getBoolean("autoreset");
        boolean isUserResettable = section.getBoolean("isUserResettable");
        int allowedSubregions = section.getInt("allowedSubregions");
        String autoPriceString = section.getString("autoPrice");
        AutoPrice autoPrice = null;

        List<String> setupcommands = section.getStringList("setupcommands");
        RegionKind regionKind = AdvancedRegionMarket.getRegionKindManager().getRegionKind(regionKindString);
        if(regionKind == null) {
            regionKind = RegionKind.DEFAULT;
        }
        FlagGroup flagGroup = AdvancedRegionMarket.getFlagGroupManager().getFlagGroup(flagGroupString);
        if(flagGroup == null) {
            flagGroup = FlagGroup.DEFAULT;
        }

        EntityLimitGroup entityLimitGroup = AdvancedRegionMarket.getEntityLimitGroupManager().getEntityLimitGroup(entityLimitGroupString);
        if(entityLimitGroup == null) {
            entityLimitGroup = EntityLimitGroup.DEFAULT;
        }
        if(presetType == PresetType.SELLPRESET) {
            if(autoPriceString != null) {
                autoPrice = AutoPrice.getAutoprice(autoPriceString);
                if(autoPrice == null) {
                    autoPrice = AutoPrice.DEFAULT;
                }
                hasprice = false;
                price = 0;
            }
            return new SellPreset(name, hasprice, price, regionKind, flagGroup, isHotel, doBlockReset, autoreset, isUserResettable, allowedSubregions, autoPrice, entityLimitGroup, setupcommands);
        }
        if(presetType == PresetType.RENTPRESET) {
            boolean hasMaxRentTime = section.getBoolean("hasMaxRentTime");
            long maxRentTime = section.getLong("maxRentTime");
            boolean hasExtendPerClick = section.getBoolean("hasExtendPerClick");
            long extendPerClick = section.getLong("extendPerClick");
            if(autoPriceString != null) {
                autoPrice = AutoPrice.getAutoprice(section.getString("autoPrice"));
                if(autoPrice == null) {
                    autoPrice = AutoPrice.DEFAULT;
                }
                hasprice = false;
                price = 0;
                hasMaxRentTime = false;
                hasExtendPerClick = false;
                maxRentTime = 0;
                extendPerClick = 0;
            }
            return new RentPreset(name, hasprice, price, regionKind, flagGroup, isHotel, doBlockReset, autoreset, hasMaxRentTime, maxRentTime, hasExtendPerClick, extendPerClick, isUserResettable, allowedSubregions, autoPrice, entityLimitGroup, setupcommands);
        }
        if(presetType == PresetType.CONTRACTPRESET) {
            boolean hasExtend = section.getBoolean("hasExtend");
            long extendTime = section.getLong("extendTime");
            if(autoPriceString != null) {
                autoPrice = AutoPrice.getAutoprice(section.getString("autoPrice"));
                if(autoPrice == null) {
                    autoPrice = AutoPrice.DEFAULT;
                }
                hasprice = false;
                price = 0;
                hasExtend = false;
                extendTime = 0;
            }
            return new ContractPreset(name, hasprice, price, regionKind, flagGroup, isHotel, doBlockReset, autoreset, hasExtend, extendTime, isUserResettable, allowedSubregions, autoPrice, entityLimitGroup, setupcommands);
        }
        return null;
    }

    private boolean updateDefaults(ConfigurationSection section, PresetType presetType) {
        boolean updatedSomething = false;
        updatedSomething |= this.addDefault(section, "hasPrice", false);
        updatedSomething |= this.addDefault(section, "price", 0);
        updatedSomething |= this.addDefault(section, "regionKind", "Default");
        updatedSomething |= this.addDefault(section, "isHotel", false);
        updatedSomething |= this.addDefault(section, "doBlockReset", true);
        updatedSomething |= this.addDefault(section, "entityLimitGroup", "Default");
        updatedSomething |= this.addDefault(section, "autoreset", true);
        updatedSomething |= this.addDefault(section, "flaggroup", "Default");
        updatedSomething |= this.addDefault(section, "isUserResettable", true);
        updatedSomething |= this.addDefault(section, "allowedSubregions", 0);
        updatedSomething |= this.addDefault(section, "setupcommands", new ArrayList<String>());
        if(presetType == PresetType.RENTPRESET) {
            updatedSomething |= this.addDefault(section, "hasMaxRentTime", false);
            updatedSomething |= this.addDefault(section, "maxRentTime", 0);
            updatedSomething |= this.addDefault(section, "hasExtendPerClick", false);
            updatedSomething |= this.addDefault(section, "extendPerClick", 0);
        }
        if(presetType == PresetType.CONTRACTPRESET) {
            updatedSomething |= this.addDefault(section, "hasExtend", false);
            updatedSomething |= this.addDefault(section, "extendTime", 0);
        }
        return updatedSomething;
    }

    @Override
    public boolean staticSaveQuenued() {
        return false;
    }

    @Override
    public void saveObjectToYamlObject(Preset preset, YamlConfiguration yamlConfiguration) {
        if(preset instanceof SellPreset) {
            yamlConfiguration.set(PresetType.SELLPRESET.getName() + "." + preset.getName(), preset.toConfigurationSection());
        } else if (preset instanceof RentPreset) {
            yamlConfiguration.set(PresetType.RENTPRESET.getName() + "." + preset.getName(), preset.toConfigurationSection());
        } else if (preset instanceof ContractPreset) {
            yamlConfiguration.set(PresetType.CONTRACTPRESET.getName() + "." + preset.getName(), preset.toConfigurationSection());
        }
    }

    @Override
    public void writeStaticSettings(YamlConfiguration yamlConfiguration) {

    }

    public List<String> onTabCompleteCompleteSavedPresets(PresetType presetType, String presetname) {
        List<String> returnme = new ArrayList<>();

        for(Preset preset : this) {
            if((preset.getPresetType() == presetType) && (preset.getName().startsWith(presetname))) {
                returnme.add(preset.getName());
            }
        }

        return returnme;
    }

    public Preset getPreset(String name, PresetType presetType) {
        for(Preset preset : this) {
            if(preset.getName().equalsIgnoreCase(name)) {
                if(preset.getPresetType() == presetType) {
                    return preset;
                }
            }
        }
        return null;
    }


    public List<Preset> getPresets(PresetType presetType) {
        List<Preset> presets = new ArrayList<>();
        for(Preset preset : this) {
            if(preset.getPresetType() == presetType) {
                presets.add(preset);
            }
        }
        return presets;
    }
}
