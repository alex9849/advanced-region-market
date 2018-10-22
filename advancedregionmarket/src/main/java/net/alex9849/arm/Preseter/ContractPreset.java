package net.alex9849.arm.Preseter;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Preseter.commands.*;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regions.RentRegion;
import net.alex9849.arm.regions.RegionKind;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ContractPreset extends Preset {
    private static ArrayList<BasicPresetCommand> commands = new ArrayList<>();
    protected static ArrayList<ContractPreset> list = new ArrayList<>();
    private static ArrayList<ContractPreset> patterns = new ArrayList<>();
    private boolean hasExtend = false;
    private long extend = 0;

    public ContractPreset(Player player) {
        super(player);
    }

    public static ArrayList<ContractPreset> getPatterns(){
        return ContractPreset.patterns;
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
        copy.addCommand(this.runCommands);
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
    public void remove() {
        ContractPreset.removePreset(this.getAssignedPlayer());
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
        player.sendMessage(Messages.PRESET_COMMANDS);
        for(int i = 0; i < this.runCommands.size(); i++) {
            String message = (i + 1) +". /" + this.runCommands.get(i);
            player.sendMessage(ChatColor.GOLD + message);
        }
    }

    public static void loadCommands() {
        commands.add(new AutoResetCommand());
        commands.add(new ContractPresetExtendCommand());
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
        commands.add(new AddCommandCommand());
        commands.add(new RemoveCommandCommand());
    }

    public static boolean onCommand(CommandSender sender, String command, String[] args) throws InputException {

        if(!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;

        for(int i = 0; i < commands.size(); i++) {
            if(commands.get(i).getRootCommand().equalsIgnoreCase(args[0])) {
                if(commands.get(i).matchesRegex(command)) {
                    return commands.get(i).runCommand(player, args, command, PresetType.CONTRACTPRESET);
                } else {
                    sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm contractpreset " + commands.get(i).getUsage());
                    return true;
                }
            }
        }

        return false;
    }

    public static List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        for(BasicPresetCommand command : commands) {
            returnme.addAll(command.onTabComplete(player, args, PresetType.CONTRACTPRESET));
        }

        return returnme;
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
                    List<String> commands = config.getStringList("ContractPresets." + presets.get(i) + ".commands");
                    if(commands == null) {
                        commands = new ArrayList<>();
                    }
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
                    preset.addCommand(commands);
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

    @Override
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
        config.set("ContractPresets." + name + ".commands", this.runCommands);
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
        preset.addCommand(this.runCommands);
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
