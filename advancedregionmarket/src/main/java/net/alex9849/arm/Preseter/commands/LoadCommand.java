package net.alex9849.arm.Preseter.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.Preseter.presets.Preset;
import net.alex9849.arm.Preseter.presets.PresetType;
import net.alex9849.arm.exceptions.InputException;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LoadCommand extends BasicPresetCommand {

    private final String rootCommand = "load";
    private final String regex = "(?i)load [^;\n ]+";
    private final String usage = "load [PRESETNAME]";

    @Override
    public boolean matchesRegex(String command) {
        return command.matches(regex);
    }

    @Override
    public String getRootCommand() {
        return this.rootCommand;
    }

    @Override
    public String getUsage() {
        return this.usage;
    }

    @Override
    public boolean runCommand(Player player, String[] args, String allargs, PresetType presetType) throws InputException {
        if(!player.hasPermission(Permission.ADMIN_PRESET_LOAD)) {
            throw new InputException(player, Messages.NO_PERMISSION);
        }

        if(Preset.assignToPlayer(presetType, player, args[1])){
            player.sendMessage(Messages.PREFIX + Messages.PRESET_LOADED);
        } else {
            player.sendMessage(Messages.PREFIX + Messages.PRESET_NOT_FOUND);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args, PresetType presetType) {
        List<String> returnme = new ArrayList<>();
        if(player.hasPermission(Permission.ADMIN_PRESET_LOAD)) {
            if(args.length >= 1) {
                if(args.length == 1) {
                    if(this.rootCommand.startsWith(args[0])) {
                        returnme.add(this.rootCommand);
                    }
                }
                if(args.length == 2 && args[0].equalsIgnoreCase(this.rootCommand)) {
                    returnme.addAll(Preset.onTabCompleteCompleteSavedPresets(args[1], presetType));
                }
            }
        }
        return returnme;
    }
}
