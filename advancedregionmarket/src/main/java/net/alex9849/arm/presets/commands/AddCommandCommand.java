package net.alex9849.arm.presets.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.presets.ActivePresetManager;
import net.alex9849.arm.presets.PresetPlayerPair;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddCommandCommand extends BasicArmCommand {
    private final String rootCommand = "addcommand";
    private final String regex_remove = "(?i)addcommand [^;\n]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("addcommand [COMMAND]"));
    private PresetType presetType;

    public AddCommandCommand(PresetType presetType) {
        this.presetType = presetType;
    }

    @Override
    public boolean matchesRegex(String command) {
        return command.matches(this.regex_remove);
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

        if(!sender.hasPermission(Permission.ADMIN_PRESET_ADDCOMMAND)) {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
        if(presetType == null) {
            return false;
        }

        Preset preset = ActivePresetManager.getPreset(player, presetType);

        if(preset == null) {
            preset = this.presetType.create();
            ActivePresetManager.add(new PresetPlayerPair(player, preset));
        }

        String addCommand = "";

        for(int i = 1; i < args.length; i++) {
            if(i < (args.length - 1)) {
                addCommand += args[i] + " ";
            } else {
                addCommand += args[i];
            }
        }

        preset.addCommand(addCommand);

        player.sendMessage(Messages.PREFIX + Messages.PRESET_SET);
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if(player.hasPermission(Permission.ADMIN_PRESET_ADDCOMMAND)) {
            if(args.length >= 1) {
                if(args.length == 1) {
                    if(this.rootCommand.startsWith(args[0])) {
                        returnme.add(this.rootCommand);
                    }
                }
            }
        }
        return returnme;
    }
}
