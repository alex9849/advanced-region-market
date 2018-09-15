package net.alex9849.arm.Preseter;

import net.alex9849.arm.Permission;
import net.alex9849.arm.Messages;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regions.RegionKind;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedList;

public class SellPreset extends Preset{
    protected static final String SET_PRICE = " (?i)sellpreset (?i)price [+-]?([0-9]+[.])?[0-9]+";
    protected static final String REMOVE_PRICE = " (?i)sellpreset (?i)price (?i)remove";
    protected static final String SET_REGIONKIND = " (?i)sellpreset (?i)regionkind [^;\n ]+";
    protected static final String REMOVE_REGIONKIND = " (?i)sellpreset (?i)regionkind (?i)remove";
    protected static final String SET_AUTO_RESET = " (?i)sellpreset (?i)autoreset (false|true)";
    protected static final String REMOVE_AUTO_RESET = " (?i)sellpreset (?i)autoreset (?i)remove";
    protected static final String SET_HOTEL = " (?i)sellpreset (?i)hotel (false|true)";
    protected static final String REMOVE_HOTEL = " (?i)sellpreset (?i)hotel (?i)remove";
    protected static final String SET_DO_BLOCK_RESET = " (?i)sellpreset (?i)doblockreset (false|true)";
    protected static final String REMOVE_DO_BLOCK_RESET = " (?i)sellpreset (?i)doblockreset (?i)remove";
    protected static final String RESET = " (?i)sellpreset (?i)reset";
    protected static final String INFO = " (?i)sellpreset (?i)info";
    protected static final String LIST = " (?i)sellpreset (?i)list";
    protected static final String HELP = " (?i)sellpreset (?i)help";
    protected static final String LOAD = " (?i)sellpreset (?i)load [^;\n ]+";
    protected static final String REMOVE = " (?i)sellpreset (?i)delete [^;\n ]+";
    protected static final String SAVE = " (?i)sellpreset (?i)save [^;\n ]+";
    protected static ArrayList<SellPreset> list = new ArrayList<>();
    protected static ArrayList<SellPreset> patterns = new ArrayList<>();

    public SellPreset(Player player){
        super(player);
    }

    public static ArrayList<SellPreset> getList(){
        return SellPreset.list;
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

    public static boolean hasPreset(Player player){
        for(int i = 0; i < getList().size(); i++) {
            if(getList().get(i).getAssignedPlayer() == player) {
                return true;
            }
        }
        return false;
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

    public static boolean onCommand(CommandSender sender, String command, String[] args) throws InputException {
        if(!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;
        if(args[1].equalsIgnoreCase("price")) {
            if(!player.hasPermission(Permission.ADMIN_PRESET_SET_PRICE)) {
                throw new InputException(sender, Messages.NO_PERMISSION);
            }
            if(command.matches(SET_PRICE)){
                if(hasPreset(player)) {
                    getPreset(player).setPrice(Double.parseDouble(args[2]));
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_SET);
                    player.sendMessage(Messages.PREFIX + "Price-line on signs can now be let empty");
                    return true;
                } else {
                    getList().add(new SellPreset(player));
                    getPreset(player).setPrice(Double.parseDouble(args[2]));
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_SET);
                    player.sendMessage(Messages.PREFIX + "Price-line on signs can now be let empty");
                    return true;
                }
            } else if(command.matches(REMOVE_PRICE)){
                if(hasPreset(player)){
                    getPreset(player).removePrice();
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_REMOVED);
                    return true;
                } else {
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_REMOVED);
                    return true;
                }
            } else {
                sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm sellpreset price ([PRICE]/remove)");
            }
        }

        else if(args[1].equalsIgnoreCase("regionkind")) {
            if(!player.hasPermission(Permission.ADMIN_PRESET_SET_REGIONKIND)) {
                throw new InputException(sender, Messages.NO_PERMISSION);

            }
            if(command.matches(SET_REGIONKIND)){
                RegionKind regkind = RegionKind.getRegionKind(args[2]);
                if(regkind != null){
                    if(hasPreset(player)) {
                        getPreset(player).setRegionKind(regkind);
                        player.sendMessage(Messages.PREFIX + Messages.PRESET_SET);
                    } else {
                        getList().add(new SellPreset(player));
                        getPreset(player).setRegionKind(regkind);
                        player.sendMessage(Messages.PREFIX + Messages.PRESET_SET);
                    }
                } else {
                    player.sendMessage(Messages.PREFIX + Messages.REGIONKIND_DOES_NOT_EXIST);
                    return true;
                }
            } else if(command.matches(REMOVE_REGIONKIND)){
                if(hasPreset(player)){
                    getPreset(player).removeRegionKind();
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_REMOVED);
                    return true;
                } else {
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_REMOVED);
                    return true;
                }
            } else {
                sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm sellpreset regionkind ([REGIONKIND]/remove)");
            }
        }

        else if(args[1].equalsIgnoreCase("autoreset")) {
            if(!player.hasPermission(Permission.ADMIN_PRESET_SET_AUTORESET)) {
                throw new InputException(sender, Messages.NO_PERMISSION);
            }
            if(command.matches(SET_AUTO_RESET)) {
                if(hasPreset(player)) {
                    getPreset(player).setAutoReset(Boolean.parseBoolean(args[2]));
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_SET);
                    return true;
                } else {
                    getList().add(new SellPreset(player));
                    getPreset(player).setAutoReset(Boolean.parseBoolean(args[2]));
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_SET);
                    return true;
                }
            } else if(command.matches(REMOVE_AUTO_RESET)) {
                if(hasPreset(player)){
                    getPreset(player).removeAutoReset();
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_REMOVED);
                    return true;
                } else {
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_REMOVED);
                    return true;
                }
            } else {
                sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm sellpreset autoreset (true/false/remove)");
            }
        }

        else if(args[1].equalsIgnoreCase("hotel")) {
            if(!player.hasPermission(Permission.ADMIN_PRESET_SET_HOTEL)) {
                throw new InputException(sender, Messages.NO_PERMISSION);
            }
            if(command.matches(SET_HOTEL)) {
                if(hasPreset(player)) {
                    getPreset(player).setHotel(Boolean.parseBoolean(args[2]));
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_SET);
                    return true;
                } else {
                    getList().add(new SellPreset(player));
                    getPreset(player).setHotel(Boolean.parseBoolean(args[2]));
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_SET);
                    return true;
                }
            } else if(command.matches(REMOVE_HOTEL)) {
                if(hasPreset(player)){
                    getPreset(player).removeHotel();
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_REMOVED);
                    return true;
                } else {
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_REMOVED);
                    return true;
                }
            } else {
                sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm sellpreset hotel (true/false/remove)");
            }
        }

        else if(args[1].equalsIgnoreCase("doblockreset")) {
            if(!player.hasPermission(Permission.ADMIN_PRESET_SET_DOBLOCKRESET)) {
                throw new InputException(sender, Messages.NO_PERMISSION);
            }
            if(command.matches(SET_DO_BLOCK_RESET)) {
                if(hasPreset(player)) {
                    getPreset(player).setDoBlockReset(Boolean.parseBoolean(args[2]));
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_SET);
                    return true;
                } else {
                    getList().add(new SellPreset(player));
                    getPreset(player).setDoBlockReset(Boolean.parseBoolean(args[2]));
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_SET);
                    return true;
                }
            } else if(command.matches(REMOVE_DO_BLOCK_RESET)) {
                if(hasPreset(player)){
                    getPreset(player).removeDoBlockReset();
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_REMOVED);
                    return true;
                } else {
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_REMOVED);
                    return true;
                }
            }  else {
                sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm sellpreset doblockreset (true/false/remove)");
            }
        }

        else if(args[1].equalsIgnoreCase("save")) {
            if(!player.hasPermission(Permission.ADMIN_PRESET_SAVE)) {
                throw new InputException(sender, Messages.NO_PERMISSION);
            }
            if(command.matches(SAVE)) {
                if(hasPreset(player)){
                    if(getPreset(player).save(args[2])){
                        player.sendMessage(Messages.PREFIX + Messages.PRESET_SAVED);
                    } else {
                        player.sendMessage(Messages.PREFIX + Messages.PRESET_ALREADY_EXISTS);
                    }
                    return true;
                } else {
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_PLAYER_DONT_HAS_PRESET);
                    return true;
                }
            } else {
                sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm sellpreset save [NAME]");
            }
        }

        else if(args[1].equalsIgnoreCase("delete")) {
            if(!player.hasPermission(Permission.ADMIN_PRESET_DELETE)) {
                throw new InputException(sender, Messages.NO_PERMISSION);
            }
            if(command.matches(REMOVE)) {
                if(removePattern(args[2])){
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_DELETED);
                } else {
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_NOT_FOUND);
                }
            } else {
                sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm sellpreset delete [NAME]");
            }
        }

        else if(args[1].equalsIgnoreCase("load")) {
            if(!player.hasPermission(Permission.ADMIN_PRESET_LOAD)) {
                throw new InputException(sender, Messages.NO_PERMISSION);
            }
            if(command.matches(LOAD)) {
                if(assignToPlayer(player, args[2])){
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_LOADED);
                } else {
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_NOT_FOUND);
                }
            } else {
                sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm sellpreset load [NAME]");
            }
        }

         else if(command.matches(RESET)) {
            if(!player.hasPermission(Permission.ADMIN_PRESET_RESET)) {
                throw new InputException(sender, Messages.NO_PERMISSION);
            }
            if(removePreset(player)){
                player.sendMessage(Messages.PREFIX + Messages.PRESET_REMOVED);
                return true;
            } else {
                player.sendMessage(Messages.PREFIX + Messages.PRESET_PLAYER_DONT_HAS_PRESET);
                return true;
            }

        } else if(command.matches(INFO)) {
            if(!player.hasPermission(Permission.ADMIN_PRESET_INFO)) {
                throw new InputException(sender, Messages.NO_PERMISSION);
            }
            if(hasPreset(player)){
                getPreset(player).getPresetInfo(player);
                return true;
            } else {
                player.sendMessage(Messages.PREFIX + Messages.PRESET_PLAYER_DONT_HAS_PRESET);
                return true;
            }

        } else if(command.matches(LIST)) {
            if(!player.hasPermission(Permission.ADMIN_PRESET_LIST)) {
                throw new InputException(sender, Messages.NO_PERMISSION);
            }
            listPresets(player);
            return true;

        } else if(command.matches(HELP)) {
            if(!player.hasPermission(Permission.ADMIN_PRESET_HELP)) {
                throw new InputException(sender, Messages.NO_PERMISSION);
            }
            showHelp(player);
            return true;

        } else {
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "/arm sellpreset help");
        }
        return true;
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
