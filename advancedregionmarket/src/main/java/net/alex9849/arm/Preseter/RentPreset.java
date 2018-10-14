package net.alex9849.arm.Preseter;

import net.alex9849.arm.Permission;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Preseter.commands.*;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regions.RegionKind;
import net.alex9849.arm.regions.RentRegion;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class RentPreset extends Preset {
    private static ArrayList<BasicPresetCommand> commands = new ArrayList<>();
    protected static ArrayList<RentPreset> list = new ArrayList<>();
    protected static ArrayList<RentPreset> patterns = new ArrayList<>();
    private boolean hasMaxRentTime = false;
    private long maxRentTime = 0;
    private boolean hasExtendPerClick = false;
    private long extendPerClick = 0;

    public RentPreset(Player player) {
        super(player);
    }

    public static ArrayList<RentPreset> getPatterns(){
        return RentPreset.patterns;
    }


    public static void reset(){
        list = new ArrayList<>();
        patterns = new ArrayList<>();
    }

    public RentPreset getCopy(){
        RentPreset copy = new RentPreset(null);
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
        if(this.hasMaxRentTime) {
            copy.setMaxRentTime(this.maxRentTime);
        }
        if(this.hasExtendPerClick) {
            copy.setExtendPerClick(this.extendPerClick);
        }
        return copy;
    }

    public boolean hasExtendPerClick() {
        return hasExtendPerClick;
    }

    public void removeExtendPerClick(){
        this.hasExtendPerClick = false;
        this.extendPerClick = 0;
    }

    public long getMaxRentTime(){
        return this.maxRentTime;
    }

    public long getExtendPerClick(){
        return this.extendPerClick;
    }

    public void setExtendPerClick(String string) {
        this.hasExtendPerClick = true;
        this.extendPerClick = RentRegion.stringToTime(string);
    }

    public void setExtendPerClick(long time) {
        this.hasExtendPerClick = true;
        this.extendPerClick = time;
    }

    public void setMaxRentTime(String string) {
        this.hasMaxRentTime = true;
        this.maxRentTime = RentRegion.stringToTime(string);
    }

    public void setMaxRentTime(Long time) {
        this.hasMaxRentTime = true;
        this.maxRentTime = time;
    }

    public boolean hasMaxRentTime() {
        return hasMaxRentTime;
    }

    public void removeMaxRentTime() {
        this.hasMaxRentTime = false;
        this.maxRentTime = 0;
    }

    public static ArrayList<RentPreset> getList(){
        return RentPreset.list;
    }

    public static boolean hasPreset(Player player){
        for(int i = 0; i < getList().size(); i++) {
            if(getList().get(i).getAssignedPlayer() == player) {
                return true;
            }
        }
        return false;
    }

    public static RentPreset getPreset(Player player) {
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

        player.sendMessage(ChatColor.GOLD + "=========[RentPreset INFO]=========");
        player.sendMessage(Messages.REGION_INFO_PRICE + price);
        player.sendMessage(Messages.REGION_INFO_EXTEND_PER_CLICK + longToTime(this.extendPerClick));
        player.sendMessage(Messages.REGION_INFO_MAX_RENT_TIME + longToTime(this.maxRentTime));
        player.sendMessage(Messages.REGION_INFO_TYPE + regKind.getName());
        player.sendMessage(Messages.REGION_INFO_AUTORESET + this.isAutoReset());
        player.sendMessage(Messages.REGION_INFO_HOTEL + this.isHotel());
        player.sendMessage(Messages.REGION_INFO_DO_BLOCK_RESET + this.isDoBlockReset());
    }

    @Override
    public void remove() {
        RentPreset.removePreset(this.getAssignedPlayer());
    }

    public static void loadCommands() {
        commands.add(new AutoResetCommand());
        commands.add(new RentPresetExtendPerClickCommand());
        commands.add(new RentPresetMaxRentTimeCommand());
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

        String allargs = "";

        for (int i = 1; i < args.length; i++) {
            if(i == 1) {
                allargs = args[i];
            } else {
                allargs = allargs + " " + args[i];
            }
        }

        for(int i = 0; i < commands.size(); i++) {
            if(commands.get(i).getRootCommand().equalsIgnoreCase(args[0])) {
                if(commands.get(i).matchesRegex(allargs)) {
                    return commands.get(i).runCommand(player, args, allargs, PresetType.RENTPRESET);
                } else {
                    sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm rentpreset " + commands.get(i).getUsage());
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean assignToPlayer(Player player, String name) {
        for(int i = 0; i < patterns.size(); i++) {
            if(patterns.get(i).getName().equalsIgnoreCase(name)) {
                removePreset(player);
                RentPreset preset = patterns.get(i).getCopy();
                preset.setPlayer(player);
                list.add(preset);
                return true;
            }
        }
        return false;
    }

    public static void loadPresets(){
        YamlConfiguration config = getConfig();
        if(config.get("RentPresets") != null) {
            LinkedList<String> presets = new LinkedList<String>(config.getConfigurationSection("RentPresets").getKeys(false));
            if(presets != null) {
                for(int i = 0; i < presets.size(); i++) {
                    String name = presets.get(i);
                    Boolean hasPrice = config.getBoolean("RentPresets." + presets.get(i) + ".hasPrice");
                    Boolean hasRegionKind = config.getBoolean("RentPresets." + presets.get(i) + ".hasRegionKind");
                    Boolean hasAutoReset = config.getBoolean("RentPresets." + presets.get(i) + ".hasAutoReset");
                    Boolean hasIsHotel = config.getBoolean("RentPresets." + presets.get(i) + ".hasIsHotel");
                    Boolean hasDoBlockReset = config.getBoolean("RentPresets." + presets.get(i) + ".hasDoBlockReset");
                    Boolean hasMaxRentTime = config.getBoolean("RentPresets." + presets.get(i) + ".hasMaxRentTime");
                    Boolean hasExtendPerClick = config.getBoolean("RentPresets." + presets.get(i) + ".hasExtendPerClick");
                    double price = config.getDouble("RentPresets." + presets.get(i) + ".price");
                    RegionKind regionKind = RegionKind.getRegionKind(config.getString("RentPresets." + presets.get(i) + ".regionKind"));
                    Boolean autoReset = config.getBoolean("RentPresets." + presets.get(i) + ".autoReset");
                    Boolean isHotel = config.getBoolean("RentPresets." + presets.get(i) + ".isHotel");
                    Boolean doBlockReset = config.getBoolean("RentPresets." + presets.get(i) + ".doBlockReset");
                    Long maxRentTime = config.getLong("RentPresets." + presets.get(i) + ".maxRentTime");
                    Long extendPerClick = config.getLong("RentPresets." + presets.get(i) + ".extendPerClick");
                    RentPreset preset = new RentPreset(null);

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
                    if(hasMaxRentTime) {
                        preset.setMaxRentTime(maxRentTime);
                    }
                    if(hasExtendPerClick) {
                        preset.setExtendPerClick(extendPerClick);
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
                config.set("RentPresets." + name, null);
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

        player.sendMessage(Messages.PREFIX + ChatColor.GOLD + "RentPresets:");
        for(int i = 0; i < patterns.size(); i++) {
            player.sendMessage(ChatColor.GOLD + " - " + patterns.get(i).getName());
        }
    }

    @Override
    public boolean save(String name){
        if(patternExists(name)) {
            return false;
        }
        YamlConfiguration config = getConfig();
        config.set("RentPresets." + name + ".hasPrice", hasPrice);
        config.set("RentPresets." + name + ".hasRegionKind", hasRegionKind);
        config.set("RentPresets." + name + ".hasAutoReset", hasAutoReset);
        config.set("RentPresets." + name + ".hasIsHotel", hasIsHotel);
        config.set("RentPresets." + name + ".hasDoBlockReset", hasDoBlockReset);
        config.set("RentPresets." + name + ".hasMaxRentTime", hasMaxRentTime);
        config.set("RentPresets." + name + ".hasExtendPerClick", hasExtendPerClick);
        config.set("RentPresets." + name + ".extendPerClick", extendPerClick);
        config.set("RentPresets." + name + ".price", price);
        config.set("RentPresets." + name + ".maxRentTime", maxRentTime);
        config.set("RentPresets." + name + ".regionKind", regionKind.getName());
        config.set("RentPresets." + name + ".autoReset", autoReset);
        config.set("RentPresets." + name + ".isHotel", isHotel);
        config.set("RentPresets." + name + ".doBlockReset", doBlockReset);
        saveRegionsConf(config);

        RentPreset preset = new RentPreset(null);
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
        if(hasMaxRentTime){
            preset.setMaxRentTime(maxRentTime);
        }
        if(hasExtendPerClick){
            preset.setExtendPerClick(extendPerClick);
        }
        patterns.add(preset);

        return true;
    }

    public static void showHelp(Player player) {
        player.sendMessage(ChatColor.GOLD + "/arm rentpreset list");
        player.sendMessage(ChatColor.GOLD + "/arm rentpreset load [NAME]");
        player.sendMessage(ChatColor.GOLD + "/arm rentpreset save [NAME]");
        player.sendMessage(ChatColor.GOLD + "/arm rentpreset delete [NAME]");
        player.sendMessage(ChatColor.GOLD + "/arm rentpreset price ([PRICE]/remove)");
        player.sendMessage(ChatColor.GOLD + "/arm rentpreset regionkind ([REGIONKIND]/remove)");
        player.sendMessage(ChatColor.GOLD + "/arm rentpreset maxrenttime [TIME(Expample: 10h)]");
        player.sendMessage(ChatColor.GOLD + "/arm rentpreset extendperclick [TIME(Expample: 10h)]");
        player.sendMessage(ChatColor.GOLD + "/arm rentpreset autoreset (true/false/remove)");
        player.sendMessage(ChatColor.GOLD + "/arm rentpreset hotel (true/false/remove)");
        player.sendMessage(ChatColor.GOLD + "/arm rentpreset doblockreset (true/false/remove)");
        player.sendMessage(ChatColor.GOLD + "/arm rentpreset info");
        player.sendMessage(ChatColor.GOLD + "/arm rentpreset reset");
    }

}
