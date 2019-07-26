package net.alex9849.arm.presets.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.presets.ActivePresetManager;
import net.alex9849.arm.presets.PresetPlayerPair;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import net.alex9849.arm.presets.presets.RentPreset;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RentPresetExtendPerClickCommand extends BasicArmCommand {

    private final String rootCommand = "extendperclick";
    private final String regex_set = "(?i)extendperclick ([0-9]+(s|m|h|d))";
    private final String regex_remove = "(?i)extendperclick (?i)remove";
    private final List<String> usage = new ArrayList<>(Arrays.asList("extendperclick ([TIME(Example: 10h)]/remove)"));
    private PresetType presetType;

    public RentPresetExtendPerClickCommand(PresetType presetType) {
        this.presetType = presetType;
    }

    @Override
    public boolean matchesRegex(String command) {
        if(command.matches(this.regex_set)) {
            return true;
        } else {
            return command.matches(this.regex_remove);
        }
    }

    @Override
    public String getRootCommand() {
        return this.rootCommand;
    }

    @Override
    public List<String> getUsage() {
        return this.usage;
    }

    @Override
    public boolean runCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args, String allargs) throws InputException {
        if(!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;

        if(!player.hasPermission(Permission.ADMIN_PRESET_SET_EXTENDPERCLICK)) {
            throw new InputException(player, Messages.NO_PERMISSION);
        }

        if(presetType != PresetType.RENTPRESET) {
            return false;
        }

        Preset preset = ActivePresetManager.getPreset(player, this.presetType);

        if(preset == null) {
            preset = this.presetType.create();
            ActivePresetManager.add(new PresetPlayerPair(player, preset));
        }

        if(!(preset instanceof RentPreset)) {
            return false;
        }
        RentPreset rentPreset = (RentPreset) preset;

        if(allargs.matches(this.regex_set)) {
            rentPreset.setExtendPerClick(args[1]);
            player.sendMessage(Messages.PREFIX + Messages.PRESET_SET);
            if(rentPreset.hasPrice() && rentPreset.hasMaxRentTime() && rentPreset.hasExtendPerClick()) {
                player.sendMessage(Messages.PREFIX + "You can leave the price-line on signs empty now");
            }
            return true;
        } else {
            rentPreset.removeExtendPerClick();
            player.sendMessage(Messages.PREFIX + Messages.PRESET_REMOVED);
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if(player.hasPermission(Permission.ADMIN_PRESET_SET_EXTENDPERCLICK)) {
            if(args.length >= 1) {
                if(args.length == 1) {
                    if(this.rootCommand.startsWith(args[0])) {
                        returnme.add(this.rootCommand);
                    }
                }
                if(args.length == 2 && this.rootCommand.equalsIgnoreCase(args[0])) {
                    if("remove".startsWith(args[1])) {
                        returnme.add("remove");
                    }
                    if(args[1].matches("[0-9]+")) {
                        returnme.add(args[1] + "s");
                        returnme.add(args[1] + "m");
                        returnme.add(args[1] + "h");
                        returnme.add(args[1] + "d");
                    }
                }
            }
        }
        return returnme;
    }
}
