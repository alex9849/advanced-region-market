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

    private static Preset generatePresetObject(ConfigurationSection section, String name, PresetType presetType) {
        boolean hasprice = section.getBoolean("hasPrice");
        double price = section.getDouble("price");
        String regionKindString = section.getString("regionKind");
        String flagGroupString = section.getString("flaggroup");
        boolean isHotel = section.getBoolean("isHotel");
        boolean autorestore = section.getBoolean("autorestore");
        String entityLimitGroupString = section.getString("entityLimitGroup");
        boolean inactivityReset = section.getBoolean("inactivityReset");
        boolean userrestorable = section.getBoolean("userrestorable");
        int paybackPercentage = section.getInt("paybackPercentage");
        int allowedSubregions = section.getInt("allowedSubregions");
        String autoPriceString = section.getString("autoPrice");
        int maxMembers = section.getInt("maxMembers");

        AutoPrice autoPrice = null;
        if(autoPriceString != null) {
            autoPrice = AutoPrice.getAutoprice(autoPriceString);
            if(autoPrice == null) {
                autoPrice = AutoPrice.DEFAULT;
            }
            hasprice = false;
            price = 0;
        }

        List<String> setupcommands = section.getStringList("setupcommands");
        RegionKind regionKind = AdvancedRegionMarket.getInstance().getRegionKindManager().getRegionKind(regionKindString);
        if (regionKind == null) {
            regionKind = RegionKind.DEFAULT;
        }
        FlagGroup flagGroup = AdvancedRegionMarket.getInstance().getFlagGroupManager().getFlagGroup(flagGroupString);
        if (flagGroup == null) {
            flagGroup = FlagGroup.DEFAULT;
        }

        EntityLimitGroup entityLimitGroup = AdvancedRegionMarket.getInstance().getEntityLimitGroupManager().getEntityLimitGroup(entityLimitGroupString);
        if (entityLimitGroup == null) {
            entityLimitGroup = EntityLimitGroup.DEFAULT;
        }
        if (presetType == PresetType.SELLPRESET) {
            return new SellPreset(name, hasprice, price, regionKind, flagGroup, inactivityReset, isHotel,
                    autorestore, userrestorable, allowedSubregions, autoPrice, entityLimitGroup, setupcommands,
                    maxMembers, paybackPercentage);
        }

        if(presetType == PresetType.CONTRACTPRESET || presetType == PresetType.RENTPRESET) {
            boolean hasExtendTime = section.getBoolean("hasExtendTime");
            long extendTime = section.getLong("extendTime");

            if (presetType == PresetType.CONTRACTPRESET) {
                if (autoPrice != null) {
                    hasExtendTime = false;
                    extendTime = 0;
                }
                return new ContractPreset(name, hasprice, price, regionKind, flagGroup, inactivityReset, isHotel,
                        autorestore, hasExtendTime, extendTime, userrestorable, allowedSubregions, autoPrice,
                        entityLimitGroup, setupcommands, maxMembers, paybackPercentage);

            } else {

                boolean hasMaxRentTime = section.getBoolean("hasMaxRentTime");
                long maxRentTime = section.getLong("maxRentTime");
                if (autoPrice != null) {
                    hasExtendTime = false;
                    extendTime = 0;
                    hasMaxRentTime = false;
                    maxRentTime = 0;
                }
                return new RentPreset(name, hasprice, price, regionKind, flagGroup, inactivityReset, isHotel,
                        autorestore, hasMaxRentTime, maxRentTime, hasExtendTime, extendTime, userrestorable,
                        allowedSubregions, autoPrice, entityLimitGroup, setupcommands, maxMembers, paybackPercentage);
            }


        }


        return null;
    }

    @Override
    public List<Preset> loadSavedObjects(YamlConfiguration yamlConfiguration) {
        List<Preset> presetList = new ArrayList<>();
        boolean fileupdated = false;
        yamlConfiguration.options().copyDefaults(true);

        ConfigurationSection sellPresetsection = yamlConfiguration.getConfigurationSection(PresetType.SELLPRESET.getName());
        if (sellPresetsection != null) {
            ArrayList<String> presets = new ArrayList<String>(yamlConfiguration.getConfigurationSection(PresetType.SELLPRESET.getName()).getKeys(false));
            if (presets != null) {
                for (String presetName : presets) {
                    ConfigurationSection presetSection = yamlConfiguration.getConfigurationSection(PresetType.SELLPRESET.getName() + "." + presetName);
                    fileupdated |= updateDefaults(presetSection, PresetType.SELLPRESET);
                    presetList.add(generatePresetObject(presetSection, presetName, PresetType.SELLPRESET));
                }
            }
        }

        ConfigurationSection rentPresetsection = yamlConfiguration.getConfigurationSection(PresetType.RENTPRESET.getName());
        if (rentPresetsection != null) {
            ArrayList<String> presets = new ArrayList<String>(yamlConfiguration.getConfigurationSection(PresetType.RENTPRESET.getName()).getKeys(false));
            if (presets != null) {
                for (String presetName : presets) {
                    ConfigurationSection presetSection = yamlConfiguration.getConfigurationSection(PresetType.RENTPRESET.getName() + "." + presetName);
                    fileupdated |= updateDefaults(presetSection, PresetType.RENTPRESET);
                    presetList.add(generatePresetObject(presetSection, presetName, PresetType.RENTPRESET));
                }
            }
        }

        ConfigurationSection contractPresetsection = yamlConfiguration.getConfigurationSection(PresetType.CONTRACTPRESET.getName());
        if (contractPresetsection != null) {
            ArrayList<String> presets = new ArrayList<String>(yamlConfiguration.getConfigurationSection(PresetType.CONTRACTPRESET.getName()).getKeys(false));
            if (presets != null) {
                for (String presetName : presets) {
                    ConfigurationSection presetSection = yamlConfiguration.getConfigurationSection(PresetType.CONTRACTPRESET.getName() + "." + presetName);
                    fileupdated |= updateDefaults(presetSection, PresetType.CONTRACTPRESET);
                    presetList.add(generatePresetObject(presetSection, presetName, PresetType.CONTRACTPRESET));
                }
            }
        }

        if (fileupdated) {
            this.saveFile();
        }

        yamlConfiguration.options().copyDefaults(false);

        return presetList;
    }

    private boolean updateDefaults(ConfigurationSection section, PresetType presetType) {
        boolean updatedSomething = false;
        updatedSomething |= this.addDefault(section, "hasPrice", false);
        updatedSomething |= this.addDefault(section, "price", 0);
        updatedSomething |= this.addDefault(section, "regionKind", "Default");
        updatedSomething |= this.addDefault(section, "paybackPercentage", 50);
        updatedSomething |= this.addDefault(section, "isHotel", false);
        updatedSomething |= this.addDefault(section, "autorestore", true);
        updatedSomething |= this.addDefault(section, "entityLimitGroup", "Default");
        updatedSomething |= this.addDefault(section, "inactivityReset", true);
        updatedSomething |= this.addDefault(section, "flaggroup", "Default");
        updatedSomething |= this.addDefault(section, "userrestorable", true);
        updatedSomething |= this.addDefault(section, "allowedSubregions", 0);
        updatedSomething |= this.addDefault(section, "maxMembers", -1);
        updatedSomething |= this.addDefault(section, "setupcommands", new ArrayList<String>());
        if (presetType == PresetType.CONTRACTPRESET || presetType == PresetType.RENTPRESET) {
            updatedSomething |= this.addDefault(section, "hasExtendTime", false);
            updatedSomething |= this.addDefault(section, "extendTime", 0);
        }
        if (presetType == PresetType.RENTPRESET) {
            updatedSomething |= this.addDefault(section, "hasExtendPerClick", false);
            updatedSomething |= this.addDefault(section, "extendPerClick", 0);
        }
        return updatedSomething;
    }

    @Override
    public boolean staticSaveQuenued() {
        return false;
    }

    @Override
    public void saveObjectToYamlObject(Preset preset, YamlConfiguration yamlConfiguration) {
        if (preset instanceof SellPreset) {
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

        for (Preset preset : this) {
            if ((preset.getPresetType() == presetType) && (preset.getName().startsWith(presetname))) {
                returnme.add(preset.getName());
            }
        }

        return returnme;
    }

    public Preset getPreset(String name, PresetType presetType) {
        for (Preset preset : this) {
            if (preset.getName().equalsIgnoreCase(name)) {
                if (preset.getPresetType() == presetType) {
                    return preset;
                }
            }
        }
        return null;
    }


    public List<Preset> getPresets(PresetType presetType) {
        List<Preset> presets = new ArrayList<>();
        for (Preset preset : this) {
            if (preset.getPresetType() == presetType) {
                presets.add(preset);
            }
        }
        return presets;
    }
}
