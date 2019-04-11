package net.alex9849.arm.Preseter;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Preseter.presets.*;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.entitylimit.EntityLimitGroupManager;
import net.alex9849.arm.regions.regionkind.RegionKind;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PresetPatternManager {
    private static List<Preset> presetList = new ArrayList<>();
    private static YamlConfiguration presetConfig;


    public static Preset getPreset(String name, PresetType presetType) {
        for(Preset preset : presetList) {
            if(preset.getName().equalsIgnoreCase(name)) {
                if(preset.getPresetType() == presetType) {
                    return preset;
                }
            }
        }
        return null;
    }


    public static List<Preset> getPresets(PresetType presetType) {
        List<Preset> presets = new ArrayList<>();
        for(Preset preset : presetList) {
            if(preset.getPresetType() == presetType) {
                presets.add(preset);
            }
        }
        return presets;
    }

    public static boolean add(Preset preset, String name) {
        for(Preset presetFromList : presetList) {
            if(presetFromList.getName().equalsIgnoreCase(name)) {
                return false;
            }
        }

        Preset copyPrest = preset.getCopy();
        copyPrest.setName(name);
        presetList.add(copyPrest);
        writePresetPatternToYamlObject(copyPrest);
        savePresetPatternConf();
        return true;
    }

    public static void remove(Preset preset) {
        presetList.remove(preset);
        writeAllPresetPatternToYamlObject();
    }

    private static void writeAllPresetPatternToYamlObject() {
        presetConfig = new YamlConfiguration();
        for(Preset preset : presetList) {
            writePresetPatternToYamlObject(preset);
        }
        savePresetPatternConf();
    }

    private static void writePresetPatternToYamlObject(Preset preset) {
        presetConfig.set(preset.getPresetType().getMajorityName() + "." + preset.getName() + ".hasPrice", preset.hasPrice());
        presetConfig.set(preset.getPresetType().getMajorityName() + "." + preset.getName() + ".price", preset.getPrice());
        presetConfig.set(preset.getPresetType().getMajorityName() + "." + preset.getName() + ".regionKind", preset.getRegionKind().getName());
        presetConfig.set(preset.getPresetType().getMajorityName() + "." + preset.getName() + ".isHotel", preset.isHotel());
        presetConfig.set(preset.getPresetType().getMajorityName() + "." + preset.getName() + ".doBlockReset", preset.isDoBlockReset());
        presetConfig.set(preset.getPresetType().getMajorityName() + "." + preset.getName() + ".entityLimitGroup", preset.getEntityLimitGroup().getName());
        presetConfig.set(preset.getPresetType().getMajorityName() + "." + preset.getName() + ".autoreset", preset.isAutoReset());
        if(preset.hasAutoPrice()) {
            presetConfig.set(preset.getPresetType().getMajorityName() + "." + preset.getName() + ".autoPrice", preset.getAutoPrice().getName());
        } else {
            presetConfig.set(preset.getPresetType().getMajorityName() + "." + preset.getName() + ".autoPrice", null);
        }
        presetConfig.set(preset.getPresetType().getMajorityName() + "." + preset.getName() + ".setupcommands", preset.getCommands());
        if(preset.getPresetType() == PresetType.RENTPRESET) {
            RentPreset rentpreset = (RentPreset) preset;
            presetConfig.set(preset.getPresetType().getMajorityName() + "." + preset.getName() + ".hasMaxRentTime", rentpreset.hasMaxRentTime());
            presetConfig.set(preset.getPresetType().getMajorityName() + "." + preset.getName() + ".maxRentTime", rentpreset.getMaxRentTime());
            presetConfig.set(preset.getPresetType().getMajorityName() + "." + preset.getName() + ".hasExtendPerClick", rentpreset.hasExtendPerClick());
            presetConfig.set(preset.getPresetType().getMajorityName() + "." + preset.getName() + ".extendPerClick", rentpreset.getExtendPerClick());
        }
        if(preset.getPresetType() == PresetType.CONTRACTPRESET) {
            ContractPreset contractPresetpreset = (ContractPreset) preset;
            presetConfig.set(preset.getPresetType().getMajorityName() + "." + preset.getName() + ".hasExtend", contractPresetpreset.hasExtend());
            presetConfig.set(preset.getPresetType().getMajorityName() + "." + preset.getName() + ".extendTime", contractPresetpreset.getExtend());
        }
    }

    public static void loadPresetPatterns() {
        PresetPatternManager.setConfig();
        presetList = new ArrayList<>();

        ConfigurationSection sellPresetsection = presetConfig.getConfigurationSection(PresetType.SELLPRESET.getMajorityName());
        if (sellPresetsection != null) {
            LinkedList<String> presets = new LinkedList<String>(presetConfig.getConfigurationSection(PresetType.SELLPRESET.getMajorityName()).getKeys(false));
            if(presets != null) {
                for(String presetName : presets) {
                    presetList.add(generatePresetObject(presetConfig.getConfigurationSection(PresetType.SELLPRESET.getMajorityName()+ "." + presetName), presetName, PresetType.SELLPRESET));
                }
            }
        }

        ConfigurationSection rentPresetsection = presetConfig.getConfigurationSection(PresetType.RENTPRESET.getMajorityName());
        if (rentPresetsection != null) {
            LinkedList<String> presets = new LinkedList<String>(presetConfig.getConfigurationSection(PresetType.RENTPRESET.getMajorityName()).getKeys(false));
            if(presets != null) {
                for(String presetName : presets) {
                    presetList.add(generatePresetObject(presetConfig.getConfigurationSection(PresetType.RENTPRESET.getMajorityName()+ "." + presetName), presetName, PresetType.RENTPRESET));
                }
            }
        }

        ConfigurationSection contractPresetsection = presetConfig.getConfigurationSection(PresetType.CONTRACTPRESET.getMajorityName());
        if (contractPresetsection != null) {
            LinkedList<String> presets = new LinkedList<String>(presetConfig.getConfigurationSection(PresetType.CONTRACTPRESET.getMajorityName()).getKeys(false));
            if(presets != null) {
                for(String presetName : presets) {
                    presetList.add(generatePresetObject(presetConfig.getConfigurationSection(PresetType.CONTRACTPRESET.getMajorityName()+ "." + presetName), presetName, PresetType.CONTRACTPRESET));
                }
            }
        }
        presetConfig.options().copyDefaults(true);
        savePresetPatternConf();
    }

    private static Preset generatePresetObject(ConfigurationSection section, String name, PresetType presetType) {
        updateDefaults(section, presetType);
        boolean hasprice = section.getBoolean("hasPrice");
        double price = section.getDouble("price");
        String regionKindString = section.getString("regionKind");
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
            return new SellPreset(name, hasprice, price, regionKind, isHotel, doBlockReset, autoreset, isUserResettable, allowedSubregions, autoPrice, entityLimitGroup, setupcommands);
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
            return new RentPreset(name, hasprice, price, regionKind, isHotel, doBlockReset, autoreset, hasMaxRentTime, maxRentTime, hasExtendPerClick, extendPerClick, isUserResettable, allowedSubregions, autoPrice, entityLimitGroup, setupcommands);
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
            return new ContractPreset(name, hasprice, price, regionKind, isHotel, doBlockReset, autoreset, hasExtend, extendTime, isUserResettable, allowedSubregions, autoPrice, entityLimitGroup, setupcommands);
        }
        return null;
    }

    private static void updateDefaults(ConfigurationSection section, PresetType presetType) {
        section.addDefault("hasPrice", false);
        section.addDefault("price", 0);
        section.addDefault("autoPrice", null);
        section.addDefault("regionKind", "Default");
        section.addDefault("isHotel", false);
        section.addDefault("doBlockReset", true);
        section.addDefault("entityLimitGroup", "Default");
        section.addDefault("autoreset", true);
        section.addDefault("isUserResettable", true);
        section.addDefault("allowedSubregions", 0);
        section.addDefault("setupcommands", new ArrayList<String>());
        if(presetType == PresetType.RENTPRESET) {
            section.addDefault("hasMaxRentTime", false);
            section.addDefault("maxRentTime", 0);
            section.addDefault("hasExtendPerClick", false);
            section.addDefault("extendPerClick", 0);
        }
        if(presetType == PresetType.CONTRACTPRESET) {
            section.addDefault("hasExtend", false);
            section.addDefault("extendTime", 0);
        }
    }

    public static void resetPresetPatterns() {

    }

    public static void generatedefaultConfig() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket");
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File messagesdic = new File(pluginfolder + "/presets.yml");
        if(!messagesdic.exists()){
            try {
                InputStream stream = plugin.getResource("presets.yml");
                byte[] buffer = new byte[stream.available()];
                stream.read(buffer);
                OutputStream output = new FileOutputStream(messagesdic);
                output.write(buffer);
                output.close();
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void setConfig(){
        PresetPatternManager.generatedefaultConfig();
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File presetsconfigdic = new File(pluginfolder + "/presets.yml");
        PresetPatternManager.presetConfig = YamlConfiguration.loadConfiguration(presetsconfigdic);
    }

    public static void savePresetPatternConf() {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File presetPatternConfigDic = new File(pluginfolder + "/presets.yml");
        try {
            PresetPatternManager.presetConfig.save(presetPatternConfigDic);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> onTabCompleteCompleteSavedPresets(PresetType presetType, String presetname) {
        List<String> returnme = new ArrayList<>();

        for(Preset preset : presetList) {
            if((preset.getPresetType() == presetType) && (preset.getName().startsWith(presetname))) {
                returnme.add(preset.getName());
            }
        }

        return returnme;
    }
}
