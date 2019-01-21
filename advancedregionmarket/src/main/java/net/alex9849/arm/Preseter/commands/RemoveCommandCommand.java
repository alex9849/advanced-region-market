package net.alex9849.arm.Preseter.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.Preseter.presets.Preset;
import net.alex9849.arm.Preseter.presets.PresetType;
import net.alex9849.arm.exceptions.InputException;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RemoveCommandCommand extends BasicPresetCommand {
    private final String rootCommand = "removecommand";
    private final String regex_remove = "(?i)removecommand [0-9]+";
    private final String usage = "removecommand [ID]";

    @Override
    public boolean matchesRegex(String command) {
        return command.matches(this.regex_remove);
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

        if(!player.hasPermission(Permission.ADMIN_PRESET_REMOVECOMMAND)) {
            throw new InputException(player, Messages.NO_PERMISSION);
        }
        if(presetType == null) {
            return false;
        }

        Preset preset = Preset.getPreset(presetType, player);

        if(preset == null) {
            player.sendMessage(Messages.PREFIX + Messages.PRESET_REMOVED);
            return true;
        }

        preset.removeCommand(Integer.parseInt(args[1]) - 1);

        player.sendMessage(Messages.PREFIX + Messages.PRESET_SET);
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args, PresetType presetType) {
        List<String> returnme = new ArrayList<>();
        if(player.hasPermission(Permission.ADMIN_PRESET_REMOVECOMMAND)) {
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
