package net.alex9849.arm.Preseter;

import net.alex9849.arm.Preseter.presets.Preset;
import net.alex9849.arm.Preseter.presets.PresetType;
import net.alex9849.arm.Preseter.presets.RentPreset;
import net.alex9849.arm.Preseter.presets.SellPreset;
import net.alex9849.arm.regions.RegionKind;
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
                if(preset.getPresetType() == PresetType.SELLPRESET) {
                    return preset;
                }
                if(preset.getPresetType() == PresetType.RENTPRESET) {
                    return preset;
                }
                if(preset.getPresetType() == PresetType.CONTRACTPRESET) {
                    return preset;
                }
            }
        }
        return null;
    }

    public static void add(Preset preset) {
        presetList.add(preset);
        writePresetPatternToYamlObject(preset);
        savePresetPatternConf();
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
        presetConfig.set(preset.getPresetType().getName() + "." + preset.getName() + ".hasPrice", preset.hasPrice());
        presetConfig.set(preset.getPresetType().getName() + "." + preset.getName() + ".price", preset.getPrice());
        presetConfig.set(preset.getPresetType().getName() + "." + preset.getName() + ".regionKind", preset.getRegionKind());
        presetConfig.set(preset.getPresetType().getName() + "." + preset.getName() + ".isHotel", preset.isHotel());
        presetConfig.set(preset.getPresetType().getName() + "." + preset.getName() + ".doBlockReset", preset.isDoBlockReset());
        presetConfig.set(preset.getPresetType().getName() + "." + preset.getName() + ".autoreset", preset.isAutoReset());
        presetConfig.set(preset.getPresetType().getName() + "." + preset.getName() + ".setupcommands", preset.getCommands());
        if(preset.getPresetType() == PresetType.RENTPRESET) {
            RentPreset rentpreset = (RentPreset) preset;
            presetConfig.set(preset.getPresetType().getName() + "." + preset.getName() + ".hasMaxRentTime", rentpreset.hasMaxRentTime());
            presetConfig.set(preset.getPresetType().getName() + "." + preset.getName() + ".maxRentTime", rentpreset.getMaxRentTime());
            presetConfig.set(preset.getPresetType().getName() + "." + preset.getName() + ".hasExtendPerClick", rentpreset.hasExtendPerClick());
            presetConfig.set(preset.getPresetType().getName() + "." + preset.getName() + ".extendPerClick", rentpreset.getExtendPerClick());
        }
        if(preset.getPresetType() == PresetType.CONTRACTPRESET) {
            ContractPreset contractPresetpreset = (ContractPreset) preset;
            presetConfig.set(preset.getPresetType().getName() + "." + preset.getName() + ".hasExtend", contractPresetpreset.hasExtend());
            presetConfig.set(preset.getPresetType().getName() + "." + preset.getName() + ".extendTime", contractPresetpreset.getExtend());
        }
    }

    public static void loadPresetPatterns() {
        PresetPatternManager.setConfig();
        presetList = new ArrayList<>();

        ConfigurationSection sellPresetsection = presetConfig.getConfigurationSection(PresetType.SELLPRESET.getName());
        if (sellPresetsection != null) {
            LinkedList<String> presets = new LinkedList<String>(presetConfig.getConfigurationSection(PresetType.SELLPRESET.getName()).getKeys(false));
            if(presets != null) {
                for(String presetName : presets) {
                    presetList.add(generatePresetObject(presetConfig.getConfigurationSection(PresetType.SELLPRESET.getName()+ "." + presetName), presetName, PresetType.SELLPRESET));
                }
            }
        }

        ConfigurationSection rentPresetsection = presetConfig.getConfigurationSection(PresetType.RENTPRESET.getName());
        if (rentPresetsection != null) {
            LinkedList<String> presets = new LinkedList<String>(presetConfig.getConfigurationSection(PresetType.RENTPRESET.getName()).getKeys(false));
            if(presets != null) {
                for(String presetName : presets) {
                    presetList.add(generatePresetObject(presetConfig.getConfigurationSection(PresetType.RENTPRESET.getName()+ "." + presetName), presetName, PresetType.RENTPRESET));
                }
            }
        }

        ConfigurationSection contractPresetsection = presetConfig.getConfigurationSection(PresetType.CONTRACTPRESET.getName());
        if (contractPresetsection != null) {
            LinkedList<String> presets = new LinkedList<String>(presetConfig.getConfigurationSection(PresetType.CONTRACTPRESET.getName()).getKeys(false));
            if(presets != null) {
                for(String presetName : presets) {
                    presetList.add(generatePresetObject(presetConfig.getConfigurationSection(PresetType.CONTRACTPRESET.getName()+ "." + presetName), presetName, PresetType.CONTRACTPRESET));
                }
            }
        }
    }

    private static Preset generatePresetObject(ConfigurationSection section, String name, PresetType presetType) {
        boolean hasprice = section.getBoolean("hasPrice");
        double price = section.getDouble("price");
        String regionKindString = section.getString("regionKind");
        boolean isHotel = section.getBoolean("isHotel");
        boolean doBlockReset = section.getBoolean("doBlockReset");
        boolean autoreset = section.getBoolean("autoreset");
        List<String> setupcommands = section.getStringList("setupcommands");
        RegionKind regionKind = RegionKind.getRegionKind(regionKindString);
        if(regionKind == null) {
            regionKind = RegionKind.DEFAULT;
        }
        if(presetType == PresetType.SELLPRESET) {
            return new SellPreset(name, hasprice, price, regionKind, isHotel, doBlockReset, autoreset, setupcommands);
        }
        if(presetType == PresetType.RENTPRESET) {
            boolean hasMaxRentTime = section.getBoolean("hasMaxRentTime");
            long maxRentTime = section.getLong("maxRentTime");
            boolean hasExtendPerClick = section.getBoolean("hasExtendPerClick");
            long extendPerClick = section.getLong("extendPerClick");
            return new RentPreset(name, hasprice, price, regionKind, isHotel, doBlockReset, autoreset, hasMaxRentTime, maxRentTime, hasExtendPerClick, extendPerClick, setupcommands);
        }
        if(presetType == PresetType.CONTRACTPRESET) {
            boolean hasExtend = section.getBoolean("hasExtend");
            long extendTime = section.getLong("extendTime");
            return new ContractPreset(name, hasprice, price, regionKind, isHotel, doBlockReset, autoreset, hasExtend, extendTime, setupcommands);
        }
        return null;
    }

    public static void resetPresetPatterns() {

    }

    private static void generatedefaultConfig() {
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

    /*
    public static List<String> onTabCompleteCompleteSavedPresets(String presetname, PresetType presetType) {
        List<String> returnme = new ArrayList<>();
        if(presetType == PresetType.SELLPRESET) {
            for(SellPreset preset: SellPreset.getPatterns()) {
                if(preset.getName().toLowerCase().startsWith(presetname)) {
                    returnme.add(preset.getName());
                }
            }
        }
        if(presetType == PresetType.RENTPRESET) {
            for(RentPreset preset: RentPreset.getPatterns()) {
                if(preset.getName().toLowerCase().startsWith(presetname)) {
                    returnme.add(preset.getName());
                }
            }
        }
        if(presetType == PresetType.CONTRACTPRESET) {
            for(ContractPreset preset: ContractPreset.getPatterns()) {
                if(preset.getName().toLowerCase().startsWith(presetname)) {
                    returnme.add(preset.getName());
                }
            }
        }
        return returnme;
    }
    */
}
