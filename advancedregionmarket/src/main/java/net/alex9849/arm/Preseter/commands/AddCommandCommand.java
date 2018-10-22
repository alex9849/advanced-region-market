package net.alex9849.arm.Preseter.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.Preseter.Preset;
import net.alex9849.arm.Preseter.PresetType;
import net.alex9849.arm.exceptions.InputException;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AddCommandCommand extends BasicPresetCommand {
    private final String rootCommand = "addcommand";
    private final String regex_remove = "(?i)addcommand [^;\n]+";
    private final String usage = "addcommand [COMMAND]";

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

        if(!player.hasPermission(Permission.ADMIN_PRESET_ADDCOMMAND)) {
            throw new InputException(player, Messages.NO_PERMISSION);
        }
        if(presetType == null) {
            return false;
        }

        Preset preset = Preset.getPreset(presetType, player);

        if(preset == null) {
            preset = PresetType.create(presetType, player);
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
    public List<String> onTabComplete(Player player, String[] args, PresetType presetType) {
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
