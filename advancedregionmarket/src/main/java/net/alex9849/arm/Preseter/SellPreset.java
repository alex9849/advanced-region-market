package net.alex9849.arm.Preseter;

import net.alex9849.arm.Permission;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Preseter.commands.*;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regions.RegionKind;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SellPreset extends Preset{
    private static ArrayList<BasicPresetCommand> commands = new ArrayList<>();
    protected static ArrayList<SellPreset> list = new ArrayList<>();
    protected static ArrayList<SellPreset> patterns = new ArrayList<>();

    public SellPreset(Player player){
        super(player);
    }

    public static ArrayList<SellPreset> getList(){
        return SellPreset.list;
    }

    public static ArrayList<SellPreset> getPatterns(){
        return SellPreset.patterns;
    }

    public static void reset(){
        list = new ArrayList<>();
        patterns = new ArrayList<>();
    }

    public SellPreset getCopy(){
        SellPreset copy = new SellPreset(null);
        if(this.hasPrice) {
            copy.setPrice(this.price);
        }
        if(this.hasDoBlockReset) {
            copy.setDoBlockReset(this.doBlockReset);
        }
        if(this.hasIsHotel) {
            copy.setHotel(this.isHotel);
        }
        if(this.hasAutoReset) {
            copy.setAutoReset(this.autoReset);
        }
        if(this.hasRegionKind) {
            copy.setRegionKind(this.regionKind);
        }
        return copy;
    }

    @Override
    public void remove() {
        SellPreset.removePreset(this.getAssignedPlayer());
    }

    public static SellPreset getPreset(Player player) {
        for(int i = 0; i < getList().size(); i++) {
            if(getList().get(i).getAssignedPlayer() == player) {
                return getList().get(i);
            }
        }
        return null;
    }

    public static boolean removePreset(Player player){
        for(int i = 0; i < getList().size(); i++) {
            if(getList().get(i).getAssignedPlayer() == player) {
                getList().remove(i);
                return true;
            }
        }
        return false;
    }

    public void getPresetInfo(Player player) {
        String price = "not defined";
        if(this.hasPrice()) {
            price = this.getPrice() + "";
        }
        RegionKind regKind = RegionKind.DEFAULT;
        if(this.hasRegionKind()) {
            regKind = this.getRegionKind();
        }

        player.sendMessage(ChatColor.GOLD + "=========[SellPreset INFO]=========");
        player.sendMessage(Messages.REGION_INFO_PRICE + price);
        player.sendMessage(Messages.REGION_INFO_TYPE + regKind.getName());
        player.sendMessage(Messages.REGION_INFO_AUTORESET + this.isAutoReset());
        player.sendMessage(Messages.REGION_INFO_HOTEL + this.isHotel());
        player.sendMessage(Messages.REGION_INFO_DO_BLOCK_RESET + this.isDoBlockReset());
    }

    public static void loadCommands() {
        commands.add(new AutoResetCommand());
        commands.add(new DeleteCommand());
        commands.add(new DoBlockResetCommand());
        commands.add(new HelpCommand());
        commands.add(new HotelCommand());
        commands.add(new InfoCommand());
        commands.add(new ListCommand());
        commands.add(new LoadCommand());
        commands.add(new PriceCommand());
        commands.add(new RegionKindCommand());
        commands.add(new ResetCommand());
        commands.add(new SaveCommand());
    }

    public static boolean onCommand(CommandSender sender, String command, String[] args) throws InputException {

        if(!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;

        for(int i = 0; i < commands.size(); i++) {
            if(commands.get(i).getRootCommand().equalsIgnoreCase(args[0])) {
                if(commands.get(i).matchesRegex(command)) {
                    return commands.get(i).runCommand(player, args, command, PresetType.SELLPRESET);
                } else {
                    sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm sellpreset " + commands.get(i).getUsage());
                    return true;
                }
            }
        }

        return false;
    }

    public static List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        for(BasicPresetCommand command : commands) {
            returnme.addAll(command.onTabComplete(player, args, PresetType.SELLPRESET));
        }

        return returnme;
    }

    public static void loadPresets(){
        YamlConfiguration config = getConfig();
        if(config.get("SellPresets") != null) {
            LinkedList<String> presets = new LinkedList<String>(config.getConfigurationSection("SellPresets").getKeys(false));
            if(presets != null) {
                for(int i = 0; i < presets.size(); i++) {
                    String name = presets.get(i);
                    Boolean hasPrice = config.getBoolean("SellPresets." + presets.get(i) + ".hasPrice");
                    Boolean hasRegionKind = config.getBoolean("SellPresets." + presets.get(i) + ".hasRegionKind");
                    Boolean hasAutoReset = config.getBoolean("SellPresets." + presets.get(i) + ".hasAutoReset");
                    Boolean hasIsHotel = config.getBoolean("SellPresets." + presets.get(i) + ".hasIsHotel");
                    Boolean hasDoBlockReset = config.getBoolean("SellPresets." + presets.get(i) + ".hasDoBlockReset");
                    double price = config.getDouble("SellPresets." + presets.get(i) + ".price");
                    RegionKind regionKind = RegionKind.getRegionKind(config.getString("SellPresets." + presets.get(i) + ".regionKind"));
                    Boolean autoReset = config.getBoolean("SellPresets." + presets.get(i) + ".autoReset");
                    Boolean isHotel = config.getBoolean("SellPresets." + presets.get(i) + ".isHotel");
                    Boolean doBlockReset = config.getBoolean("SellPresets." + presets.get(i) + ".doBlockReset");
                    SellPreset preset = new SellPreset(null);

                    preset.setName(name);
                    if(hasPrice){
                        preset.setPrice(price);
                    }
                    if(hasRegionKind){
                        preset.setRegionKind(regionKind);
                    }
                    if(hasAutoReset){
                        preset.setAutoReset(autoReset);
                    }
                    if(hasIsHotel){
                        preset.setHotel(isHotel);
                    }
                    if(hasDoBlockReset){
                        preset.setDoBlockReset(doBlockReset);
                    }
                    patterns.add(preset);
                }
            }
        }
    }

    public static boolean assignToPlayer(Player player, String name) {
        for(int i = 0; i < patterns.size(); i++) {
            if(patterns.get(i).getName().equalsIgnoreCase(name)) {
                removePreset(player);
                SellPreset preset = patterns.get(i).getCopy();
                preset.setPlayer(player);
                list.add(preset);
                return true;
            }
        }
        return false;
    }

    public static boolean removePattern(String name) {
        for(int i = 0; i < patterns.size(); i++) {
            if(patterns.get(i).getName().equalsIgnoreCase(name)) {
                patterns.remove(i);
                YamlConfiguration config = getConfig();
                config.set("SellPresets." + name, null);
                saveRegionsConf(config);
                return true;
            }
        }
        return false;
    }

    public static boolean patternExists(String name) {
        for(int i = 0; i < patterns.size(); i++) {
            if(patterns.get(i).getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public static void listPresets(Player player) {
        String presets = "";

        player.sendMessage(Messages.PREFIX + ChatColor.GOLD + "SellPresets:");
        for(int i = 0; i < patterns.size(); i++) {
            player.sendMessage(ChatColor.GOLD + " - " + patterns.get(i).getName());
        }
    }

    public static void showHelp(Player player) {
        player.sendMessage(ChatColor.GOLD + "/arm sellpreset list");
        player.sendMessage(ChatColor.GOLD + "/arm sellpreset load [NAME]");
        player.sendMessage(ChatColor.GOLD + "/arm sellpreset save [NAME]");
        player.sendMessage(ChatColor.GOLD + "/arm sellpreset delete [NAME]");
        player.sendMessage(ChatColor.GOLD + "/arm sellpreset price ([PRICE]/remove)");
        player.sendMessage(ChatColor.GOLD + "/arm sellpreset regionkind ([REGIONKIND]/remove)");
        player.sendMessage(ChatColor.GOLD + "/arm sellpreset autoreset (true/false/remove)");
        player.sendMessage(ChatColor.GOLD + "/arm sellpreset hotel (true/false/remove)");
        player.sendMessage(ChatColor.GOLD + "/arm sellpreset doblockreset (true/false/remove)");
        player.sendMessage(ChatColor.GOLD + "/arm sellpreset info");
        player.sendMessage(ChatColor.GOLD + "/arm sellpreset reset");
    }

    @Override
    public boolean save(String name){
        if(patternExists(name)) {
            return false;
        }
        YamlConfiguration config = getConfig();
        config.set("SellPresets." + name + ".hasPrice", hasPrice);
        config.set("SellPresets." + name + ".hasRegionKind", hasRegionKind);
        config.set("SellPresets." + name + ".hasAutoReset", hasAutoReset);
        config.set("SellPresets." + name + ".hasIsHotel", hasIsHotel);
        config.set("SellPresets." + name + ".hasDoBlockReset", hasDoBlockReset);
        config.set("SellPresets." + name + ".price", price);
        config.set("SellPresets." + name + ".regionKind", regionKind.getName());
        config.set("SellPresets." + name + ".autoReset", autoReset);
        config.set("SellPresets." + name + ".isHotel", isHotel);
        config.set("SellPresets." + name + ".doBlockReset", doBlockReset);
        saveRegionsConf(config);

        SellPreset preset = new SellPreset(null);
        preset.setName(name);
        if(hasPrice){
            preset.setPrice(price);
        }
        if(hasRegionKind){
            preset.setRegionKind(regionKind);
        }
        if(hasAutoReset){
            preset.setAutoReset(autoReset);
        }
        if(hasIsHotel){
            preset.setHotel(isHotel);
        }
        if(hasDoBlockReset){
            preset.setDoBlockReset(doBlockReset);
        }
        patterns.add(preset);

        return true;
    }

}
