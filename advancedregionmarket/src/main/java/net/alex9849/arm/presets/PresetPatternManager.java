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
        Boolean isHotel = section.getObject("isHotel", Boolean.class);
        Boolean autorestore = section.getObject("autorestore", Boolean.class);
        Boolean inactivityReset = section.getObject("inactivityReset", Boolean.class);
        Boolean userrestorable = section.getObject("userrestorable", Boolean.class);
        Integer paybackPercentage = section.getObject("paybackPercentage", Integer.class);
        Integer allowedSubregions = section.getObject("allowedSubregions", Integer.class);
        Integer maxMembers = section.getObject("maxMembers", Integer.class);
        Double price = section.getObject("price", Double.class);
        String regionKindString = section.getString("regionKind");
        String flagGroupString = section.getString("flaggroup");
        String entityLimitGroupString = section.getString("entityLimitGroup");
        String autoPriceString = section.getString("autoPrice");
        List<String> setupcommands = section.getStringList("setupcommands");

        AutoPrice autoPrice = null;
        if(autoPriceString != null) {
            autoPrice = AutoPrice.getAutoprice(autoPriceString);
        }
        RegionKind regionKind = null;
        if (regionKindString != null) {
            regionKind = AdvancedRegionMarket.getInstance().getRegionKindManager().getRegionKind(regionKindString);
        }
        FlagGroup flagGroup = null;
        if (flagGroupString == null) {
            flagGroup = AdvancedRegionMarket.getInstance().getFlagGroupManager().getFlagGroup(flagGroupString);
        }
        EntityLimitGroup entityLimitGroup = null;
        if (entityLimitGroupString == null) {
            entityLimitGroup = AdvancedRegionMarket.getInstance().getEntityLimitGroupManager().getEntityLimitGroup(entityLimitGroupString);
        }

        Preset preset = null;
        if (presetType == PresetType.SELLPRESET) {
            preset = new SellPreset();

        } else if(presetType == PresetType.CONTRACTPRESET || presetType == PresetType.RENTPRESET) {
            Long extendTime = section.getLong("extendTime");
            CountdownPreset countdownPreset;
            if (presetType == PresetType.CONTRACTPRESET) {
                countdownPreset = new ContractPreset();
            } else {
                long maxRentTime = section.getLong("maxRentTime");
                RentPreset rentPreset = new RentPreset();
                rentPreset.setMaxRentTime(maxRentTime);
                countdownPreset = rentPreset;
            }
            countdownPreset.setExtendTime(extendTime);
            preset = countdownPreset;
        }

        preset.setHotel(isHotel);
        preset.setAutoRestore(autorestore);
        preset.setInactivityReset(inactivityReset);
        preset.setUserRestorable(userrestorable);
        preset.setPaybackPercentage(paybackPercentage);
        preset.setAllowedSubregions(allowedSubregions);
        preset.setMaxMembers(maxMembers);
        preset.setPrice(price);
        preset.setAutoPrice(autoPrice);
        preset.setRegionKind(regionKind);
        preset.setFlagGroup(flagGroup);
        preset.setEntityLimitGroup(entityLimitGroup);
        preset.setName(name);
        preset.addCommand(setupcommands);
        return preset;
    }

    @Override
    public List<Preset> loadSavedObjects(YamlConfiguration yamlConfiguration) {
        List<Preset> presetList = new ArrayList<>();

        ConfigurationSection sellPresetsection = yamlConfiguration.getConfigurationSection(PresetType.SELLPRESET.getName());
        if (sellPresetsection != null) {
            ArrayList<String> presets = new ArrayList<String>(sellPresetsection.getKeys(false));
            for (String presetName : presets) {
                ConfigurationSection presetSection = sellPresetsection.getConfigurationSection(presetName);
                presetList.add(generatePresetObject(presetSection, presetName, PresetType.SELLPRESET));
            }
        }

        ConfigurationSection rentPresetsection = yamlConfiguration.getConfigurationSection(PresetType.RENTPRESET.getName());
        if (rentPresetsection != null) {
            ArrayList<String> presets = new ArrayList<String>(rentPresetsection.getKeys(false));
            for (String presetName : presets) {
                ConfigurationSection presetSection = rentPresetsection.getConfigurationSection(presetName);
                presetList.add(generatePresetObject(presetSection, presetName, PresetType.RENTPRESET));
            }
        }

        ConfigurationSection contractPresetsection = yamlConfiguration.getConfigurationSection(PresetType.CONTRACTPRESET.getName());
        if (contractPresetsection != null) {
            ArrayList<String> presets = new ArrayList<String>(contractPresetsection.getKeys(false));
            for (String presetName : presets) {
                ConfigurationSection presetSection = contractPresetsection.getConfigurationSection(presetName);
                presetList.add(generatePresetObject(presetSection, presetName, PresetType.CONTRACTPRESET));
            }
        }

        return presetList;
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
