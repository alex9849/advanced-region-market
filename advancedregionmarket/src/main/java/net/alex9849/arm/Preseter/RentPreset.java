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
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RentPreset extends Preset {
    private boolean hasMaxRentTime = false;
    private long maxRentTime = 0;
    private boolean hasExtendPerClick = false;
    private long extendPerClick = 0;

    public RentPreset(String name, boolean hasPrice, double price, RegionKind regionKind, boolean autoReset, boolean isHotel, boolean doBlockReset, boolean hasMaxRentTime,
                      long maxRentTime, boolean hasExtendPerClick, long extendPerClick, List<String> setupCommands){
        super(name, hasPrice, price, regionKind, autoReset, isHotel, doBlockReset, setupCommands);
        this.hasMaxRentTime = hasMaxRentTime;
        this.maxRentTime = maxRentTime;
        this.hasExtendPerClick = hasExtendPerClick;
        this.extendPerClick = extendPerClick;
    }

    public RentPreset getCopy(){
        List<String> newsetupCommands = new ArrayList<>();
        for(String cmd : setupCommands) {
            newsetupCommands.add(cmd);
        }
        return new RentPreset(this.name, this.hasPrice, this.price, this.regionKind, this.autoReset, this.isHotel, this.doBlockReset, this.hasMaxRentTime, this.maxRentTime, this.hasExtendPerClick, this.extendPerClick, newsetupCommands);
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

    private String longToTime(long time){

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
    public void getAdditionalInfo(Player player) {
        player.sendMessage(Messages.REGION_INFO_EXTEND_PER_CLICK + longToTime(this.extendPerClick));
        player.sendMessage(Messages.REGION_INFO_MAX_RENT_TIME + longToTime(this.maxRentTime));
    }

    @Override
    public PresetType getPresetType() {
        return PresetType.RENTPRESET;
    }

    /*
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
                    return commands.get(i).runCommand(player, args, command, PresetType.RENTPRESET);
                } else {
                    sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm rentpreset " + commands.get(i).getUsage());
                    return true;
                }
            }
        }

        return false;
    }

    public static List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        for(BasicPresetCommand command : commands) {
            returnme.addAll(command.onTabComplete(player, args, PresetType.RENTPRESET));
        }

        return returnme;
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
    */

}
