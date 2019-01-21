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

    public SellPreset(String name, boolean hasPrice, double price, RegionKind regionKind, boolean autoReset, boolean isHotel, boolean doBlockReset, List<String> setupCommands){
        super(name, hasPrice, price, regionKind, autoReset, isHotel, doBlockReset, setupCommands);
    }

    @Override
    public void getAdditionalInfo(Player player) {
    }

    @Override
    public PresetType getPresetType() {
        return PresetType.SELLPRESET;
    }

    public SellPreset getCopy(){
        List<String> newsetupCommands = new ArrayList<>();
        for(String cmd : setupCommands) {
            newsetupCommands.add(cmd);
        }
        return new SellPreset(this.name, this.hasPrice, this.price, this.regionKind, this.autoReset, this.isHotel, this.doBlockReset, newsetupCommands);
    }

    /*
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
                    return commands.get(i).runCommand(player, args, command, PresetType.SELLPRESET);
                } else {
                    sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm sellpreset " + commands.get(i).getUsage());
                    return true;
                }
            }
        }

        return false;
    }
    */

    public static List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        for(BasicPresetCommand command : commands) {
            returnme.addAll(command.onTabComplete(player, args, PresetType.SELLPRESET));
        }

        return returnme;
    }

    /*
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
    */

}
