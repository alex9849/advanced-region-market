package net.alex9849.arm.Preseter.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.Preseter.*;
import net.alex9849.arm.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand extends BasicPresetCommand {

    private final String rootCommand = "info";
    private final String regex = "(?i)info";
    private final String usage = "info";

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
        if(!player.hasPermission(Permission.ADMIN_PRESET_HELP)) {
            throw new InputException(player, Messages.NO_PERMISSION);
        }
        if(presetType == PresetType.SELLPRESET) {
            SellPreset.showHelp(player);
        } else if(presetType == PresetType.RENTPRESET) {
            RentPreset.showHelp(player);
        } else if(presetType == PresetType.CONTRACTPRESET) {
            ContractPreset.showHelp(player);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args, PresetType presetType) {
        List<String> returnme = new ArrayList<>();
        if(player.hasPermission(Permission.ADMIN_PRESET_HELP)) {
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
