package net.alex9849.arm.Preseter;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regions.RentRegion;
import net.alex9849.arm.regions.RegionKind;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class ContractPreset extends Preset {
    protected static final String SET_PRICE = " (?i)contractpreset (?i)price [+-]?([0-9]+[.])?[0-9]+";
    protected static final String REMOVE_PRICE = " (?i)contractpreset (?i)price remove";
    protected static final String SET_EXTEND = " (?i)contractpreset (?i)extend ([0-9]+(s|m|h|d))";
    protected static final String REMOVE_EXTEND = " (?i)contractpreset (?i)extend remove";
    protected static final String SET_REGIONKIND = " (?i)contractpreset (?i)regionkind [^;\n ]+";
    protected static final String REMOVE_REGIONKIND = " (?i)contractpreset (?i)regionkind remove";
    protected static final String SET_AUTO_RESET = " (?i)contractpreset (?i)autoreset (false|true)";
    protected static final String REMOVE_AUTO_RESET = " (?i)contractpreset (?i)autoreset remove";
    protected static final String SET_HOTEL = " (?i)contractpreset (?i)hotel (false|true)";
    protected static final String REMOVE_HOTEL = " (?i)contractpreset (?i)hotel remove";
    protected static final String SET_DO_BLOCK_RESET = " (?i)contractpreset (?i)doblockreset (false|true)";
    protected static final String REMOVE_DO_BLOCK_RESET = " (?i)contractpreset (?i)doblockreset remove";
    protected static final String RESET = " (?i)contractpreset (?i)reset";
    protected static final String INFO = " (?i)contractpreset (?i)info";
    protected static final String HELP = " (?i)contractpreset (?i)help";
    protected static final String LOAD = " (?i)contractpreset (?i)load [^;\n ]+";
    protected static final String REMOVE = " (?i)contractpreset (?i)delete [^;\n ]+";
    protected static final String SAVE = " (?i)contractpreset (?i)save [^;\n ]+";
    protected static final String LIST = " (?i)contractpreset (?i)list";
    protected static ArrayList<ContractPreset> list = new ArrayList<>();
    protected static ArrayList<ContractPreset> patterns = new ArrayList<>();
    protected boolean hasExtend = false;
    protected long extend = 0;

    public ContractPreset(Player player) {
        super(player);
    }

    public static void reset(){
        list = new ArrayList<>();
        patterns = new ArrayList<>();
    }

    public ContractPreset getCopy(){
        ContractPreset copy = new ContractPreset(null);
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
        if(this.hasExtend) {
            copy.setExtend(this.extend);
        }
        return copy;
    }

    public boolean hasExtend() {
        return hasExtend;
    }

    public void removeExtend(){
        this.hasExtend = false;
        this.extend = 0;
    }

    public long getExtend(){
        return this.extend;
    }

    public void setExtend(String string) {
        this.hasExtend = true;
        this.extend = RentRegion.stringToTime(string);
    }

    public void setExtend(long time) {
        this.hasExtend = true;
        this.extend = time;
    }

    public static ArrayList<ContractPreset> getList(){
        return ContractPreset.list;
    }

    public static boolean hasPreset(Player player){
        for(int i = 0; i < getList().size(); i++) {
            if(getList().get(i).getAssignedPlayer() == player) {
                return true;
            }
        }
        return false;
    }

    public static ContractPreset getPreset(Player player) {
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

    public String longToTime(long time){

        long remainingDays = TimeUnit.DAYS.convert(time, TimeUnit.MILLISECONDS);
        time = time - (remainingDays * 1000 * 60 * 60 *24);

        long remainingHours = TimeUnit.HOURS.convert(time, TimeUnit.MILLISECONDS);
        time = time - (remainingHours * 1000 * 60 * 60);

        long remainingMinutes = TimeUnit.MINUTES.convert(time, TimeUnit.MILLISECONDS);
        time = time - (remainingMinutes * 1000 * 60);

        long remainingSeconds = TimeUnit.SECONDS.convert(time, TimeUnit.MILLISECONDS);


        String timetoString = "";
        if(remainingDays != 0) {
            timetoString = timetoString + remainingDays + "d";
        }
        if(remainingHours != 0) {
            timetoString = timetoString + remainingHours + "h";
        }
        if(remainingMinutes != 0) {
            timetoString = timetoString + remainingMinutes + "m";
        }
        if(remainingSeconds != 0) {
            timetoString = timetoString + remainingSeconds + "s";
        }

        return timetoString;
    }

    @Override
    public void getPresetInfo(Player player) {
        String price = "not defined";
        if(this.hasPrice()) {
            price = this.getPrice() + "";
        }
        RegionKind regKind = RegionKind.DEFAULT;
        if(this.hasRegionKind()) {
            regKind = this.getRegionKind();
        }

        player.sendMessage(ChatColor.GOLD + "=========[ContractPreset INFO]=========");
        player.sendMessage(Messages.REGION_INFO_PRICE + price);
        player.sendMessage(Messages.REGION_INFO_AUTO_EXTEND_TIME + longToTime(this.extend));
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
                    return true;
                } else {
                    getList().add(new ContractPreset(player));
                    getPreset(player).setPrice(Double.parseDouble(args[2]));
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_SET);
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
                sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm contractpreset price ([PRICE]/remove)");
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
                        getList().add(new ContractPreset(player));
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
                sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm contractpreset regionkind ([REGIONKIND]/remove)");
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
                    getList().add(new ContractPreset(player));
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
                sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm contractpreset autoreset (true/false/remove)");
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
                    getList().add(new ContractPreset(player));
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
                sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm contractpreset hotel (true/false/remove)");
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
                    getList().add(new ContractPreset(player));
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
                sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm contractpreset doblockreset (true/false/remove)");
            }
        }

        else if(args[1].equalsIgnoreCase("extend")) {
            if(!player.hasPermission(Permission.ADMIN_PRESET_SET_EXTEND)) {
                throw new InputException(sender, Messages.NO_PERMISSION);
            }
            if(command.matches(SET_EXTEND)) {
                if(hasPreset(player)) {
                    getPreset(player).setExtend(args[2]);
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_SET);
                    return true;
                } else {
                    getList().add(new ContractPreset(player));
                    getPreset(player).setExtend(args[2]);
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_SET);
                    return true;
                }
            } else if(command.matches(REMOVE_EXTEND)) {
                if(hasPreset(player)){
                    getPreset(player).removeExtend();
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_REMOVED);
                    return true;
                } else {
                    player.sendMessage(Messages.PREFIX + Messages.PRESET_REMOVED);
                    return true;
                }
            }  else {
                sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm contractpreset extend ([TIME(Expample: 10h)]/remove)");
                sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "For example: /arm contractpreset extend 1d");
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
                sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm contractpreset save [NAME]");
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
                sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm contractpreset delete [NAME]");
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
                sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm contractpreset load [NAME]");
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
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "/arm contractpreset help");
        }
        return true;
    }

    public static boolean assignToPlayer(Player player, String name) {
        for(int i = 0; i < patterns.size(); i++) {
            if(patterns.get(i).getName().equalsIgnoreCase(name)) {
                removePreset(player);
                ContractPreset preset = patterns.get(i).getCopy();
                preset.setPlayer(player);
                list.add(preset);
                return true;
            }
        }
        return false;
    }

    public static void loadPresets(){
        YamlConfiguration config = getConfig();
        if(config.get("ContractPresets") != null) {
            LinkedList<String> presets = new LinkedList<String>(config.getConfigurationSection("ContractPresets").getKeys(false));
            if(presets != null) {
                for(int i = 0; i < presets.size(); i++) {
                    String name = presets.get(i);
                    Boolean hasPrice = config.getBoolean("ContractPresets." + presets.get(i) + ".hasPrice");
                    Boolean hasRegionKind = config.getBoolean("ContractPresets." + presets.get(i) + ".hasRegionKind");
                    Boolean hasAutoReset = config.getBoolean("ContractPresets." + presets.get(i) + ".hasAutoReset");
                    Boolean hasIsHotel = config.getBoolean("ContractPresets." + presets.get(i) + ".hasIsHotel");
                    Boolean hasDoBlockReset = config.getBoolean("ContractPresets." + presets.get(i) + ".hasDoBlockReset");
                    Boolean hasExtend = config.getBoolean("ContractPresets." + presets.get(i) + ".hasExtend");
                    double price = config.getDouble("ContractPresets." + presets.get(i) + ".price");
                    RegionKind regionKind = RegionKind.getRegionKind(config.getString("ContractPresets." + presets.get(i) + ".regionKind"));
                    Boolean autoReset = config.getBoolean("ContractPresets." + presets.get(i) + ".autoReset");
                    Boolean isHotel = config.getBoolean("ContractPresets." + presets.get(i) + ".isHotel");
                    Boolean doBlockReset = config.getBoolean("ContractPresets." + presets.get(i) + ".doBlockReset");
                    Long extend = config.getLong("ContractPresets." + presets.get(i) + ".extend");
                    ContractPreset preset = new ContractPreset(null);

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
                    if(hasExtend) {
                        preset.setExtend(extend);
                    }
                    patterns.add(preset);
                }
            }
        }
    }

    public static boolean removePattern(String name) {
        for(int i = 0; i < patterns.size(); i++) {
            if(patterns.get(i).getName().equalsIgnoreCase(name)) {
                patterns.remove(i);
                YamlConfiguration config = getConfig();
                config.set("ContractPresets." + name, null);
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

        player.sendMessage(Messages.PREFIX + ChatColor.GOLD + "ContractPresets:");
        for(int i = 0; i < patterns.size(); i++) {
            player.sendMessage(ChatColor.GOLD + " - " + patterns.get(i).getName());
        }
    }

    public boolean save(String name){
        if(patternExists(name)) {
            return false;
        }
        YamlConfiguration config = getConfig();
        config.set("ContractPresets." + name + ".hasPrice", hasPrice);
        config.set("ContractPresets." + name + ".hasRegionKind", hasRegionKind);
        config.set("ContractPresets." + name + ".hasAutoReset", hasAutoReset);
        config.set("ContractPresets." + name + ".hasIsHotel", hasIsHotel);
        config.set("ContractPresets." + name + ".hasDoBlockReset", hasDoBlockReset);
        config.set("ContractPresets." + name + ".hasExtend", hasExtend);
        config.set("ContractPresets." + name + ".extend", extend);
        config.set("ContractPresets." + name + ".price", price);
        config.set("ContractPresets." + name + ".regionKind", regionKind.getName());
        config.set("ContractPresets." + name + ".autoReset", autoReset);
        config.set("ContractPresets." + name + ".isHotel", isHotel);
        config.set("ContractPresets." + name + ".doBlockReset", doBlockReset);
        saveRegionsConf(config);

        ContractPreset preset = new ContractPreset(null);
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
        if(hasExtend){
            preset.setExtend(extend);
        }
        patterns.add(preset);

        return true;
    }

    public static void showHelp(Player player) {
        player.sendMessage(ChatColor.GOLD + "/arm contractpreset list");
        player.sendMessage(ChatColor.GOLD + "/arm contractpreset load [NAME]");
        player.sendMessage(ChatColor.GOLD + "/arm contractpreset save [NAME]");
        player.sendMessage(ChatColor.GOLD + "/arm contractpreset delete [NAME]");
        player.sendMessage(ChatColor.GOLD + "/arm contractpreset price ([PRICE]/remove)");
        player.sendMessage(ChatColor.GOLD + "/arm contractpreset regionkind ([REGIONKIND]/remove)");
        player.sendMessage(ChatColor.GOLD + "/arm contractpreset extend [TIME(Expample: 10h)]");
        player.sendMessage(ChatColor.GOLD + "/arm contractpreset autoreset (true/false/remove)");
        player.sendMessage(ChatColor.GOLD + "/arm contractpreset hotel (true/false/remove)");
        player.sendMessage(ChatColor.GOLD + "/arm contractpreset doblockreset (true/false/remove)");
        player.sendMessage(ChatColor.GOLD + "/arm contractpreset info");
        player.sendMessage(ChatColor.GOLD + "/arm contractpreset reset");
    }

}
