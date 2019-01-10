package net.alex9849.arm.Preseter;

import net.alex9849.arm.Messages;
import net.alex9849.arm.regions.ContractRegion;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.RegionKind;
import net.alex9849.arm.regions.RentRegion;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Preset {
    private static YamlConfiguration config;
    protected Player assignedPlayer;
    protected boolean hasPrice = false;
    protected double price = 0;
    protected boolean hasRegionKind = false;
    protected RegionKind regionKind = RegionKind.DEFAULT;
    protected boolean hasAutoReset = false;
    protected boolean autoReset = true;
    protected boolean hasIsHotel = false;
    protected boolean isHotel = false;
    protected boolean hasDoBlockReset = false;
    protected boolean doBlockReset = true;
    protected List<String> runCommands = new ArrayList<>();
    protected String name = "default";

    public Preset(Player player){
        assignedPlayer = player;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void addCommand(String command) {
        this.runCommands.add(command);
    }

    public void addCommand(List<String> command) {
        this.runCommands.addAll(command);
    }

    public List<String> getCommands() {
        return this.runCommands;
    }

    public void executeSavedCommands(Player player, Region region) {
        for(String command : this.runCommands) {
            String cmd = region.getConvertedMessage(command);
            cmd = cmd.replace("%regionkind%", region.getRegionKind().getName());
            cmd = cmd.replace("%regionkinddisplay%", region.getRegionKind().getDisplayName());

            player.performCommand(cmd);
        }
    }

    public void removeCommand(int index) {

        if(index < 0) {
            return;
        }

        if(this.runCommands.size() > index) {
            this.runCommands.remove(index);
        }
    }

    public void setPlayer(Player player){
        this.assignedPlayer = player;
    }

    public Player getAssignedPlayer(){
        return this.assignedPlayer;
    }

    public static boolean assignToPlayer(PresetType presetType, Player player, String presetName) {
        if(presetType == PresetType.SELLPRESET) {
            return SellPreset.assignToPlayer(player, presetName);
        } else if(presetType == PresetType.RENTPRESET) {
            return RentPreset.assignToPlayer(player, presetName);
        } else if(presetType == PresetType.CONTRACTPRESET) {
            return ContractPreset.assignToPlayer(player, presetName);
        } else {
            return false;
        }
    }

    public abstract boolean save(String name);

    public static boolean delete(PresetType presetType, String presetName) {
        if(presetType == PresetType.SELLPRESET) {
            return SellPreset.removePattern(presetName);
        } else if(presetType == PresetType.RENTPRESET) {
            return RentPreset.removePattern(presetName);
        } else if(presetType == PresetType.CONTRACTPRESET) {
            return ContractPreset.removePattern(presetName);
        } else {
            return false;
        }
    }

    public void setPrice(double price){
        if(price < 0){
            price = price * (-1);
        }
        this.hasPrice = true;
        this.price = price;
    }

    public abstract void getPresetInfo(Player player);

    public void removePrice(){
        this.hasPrice = false;
        this.price = 0;
    }

    public boolean hasPrice() {
        return hasPrice;
    }

    public boolean hasRegionKind() {
        return hasRegionKind;
    }

    public boolean hasAutoReset() {
        return hasAutoReset;
    }

    public boolean hasIsHotel() {
        return hasIsHotel;
    }

    public boolean isHasDoBlockReset() {
        return hasDoBlockReset;
    }

    public void setRegionKind(RegionKind regionKind){
        if(regionKind == null) {
            regionKind = RegionKind.DEFAULT;
        }
        this.hasRegionKind = true;
        this.regionKind = regionKind;
    }

    public void setDoBlockReset(Boolean bool){
        this.hasDoBlockReset = true;
        this.doBlockReset = bool;
    }

    public void removeDoBlockReset(){
        this.hasDoBlockReset = false;
        this.doBlockReset = true;
    }

    public void removeRegionKind(){
        this.hasRegionKind = false;
        this.regionKind = RegionKind.DEFAULT;
    }

    public static Preset getPreset(PresetType presetType, Player player) {
        if(presetType == PresetType.SELLPRESET) {
            return SellPreset.getPreset(player);
        } else if(presetType == PresetType.RENTPRESET) {
            return RentPreset.getPreset(player);
        } else if(presetType == PresetType.CONTRACTPRESET) {
            return ContractPreset.getPreset(player);
        } else {
            return null;
        }
    }

    public void setAutoReset(Boolean autoReset) {
        this.hasAutoReset = true;
        this.autoReset = autoReset;
    }

    public void removeAutoReset(){
        this.hasAutoReset = false;
        this.autoReset = true;
    }

    public void setHotel(Boolean isHotel){
        this.hasIsHotel = true;
        this.isHotel = isHotel;
    }

    public void removeHotel(){
        this.hasIsHotel = false;
        this.isHotel = false;
    }

    public double getPrice() {
        return price;
    }

    public RegionKind getRegionKind() {
        return regionKind;
    }

    public boolean isAutoReset() {
        return autoReset;
    }

    public boolean isDoBlockReset() {
        return doBlockReset;
    }

    public boolean isHotel() {
        return isHotel;
    }

    public abstract void remove();

    public static YamlConfiguration getConfig(){
        return Preset.config;
    }

    public static void generatedefaultConfig(){
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

    public static void loadConfig(){
        Messages.generatedefaultConfig();
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File presetsconfigdic = new File(pluginfolder + "/presets.yml");
        Preset.config = YamlConfiguration.loadConfiguration(presetsconfigdic);
    }

    public static void saveRegionsConf(YamlConfiguration conf) {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File regionsconfigdic = new File(pluginfolder + "/presets.yml");
        try {
            conf.save(regionsconfigdic);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
}
