package net.alex9849.arm.Preseter.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.Preseter.Preset;
import net.alex9849.arm.Preseter.PresetType;
import net.alex9849.arm.Preseter.RentPreset;
import net.alex9849.arm.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RentPresetMaxRentTimeCommand extends BasicPresetCommand {

    private final String rootCommand = "maxrenttime";
    private final String regex_set = "(?i)maxrenttime ([0-9]+(s|m|h|d))";
    private final String regex_remove = "(?i)maxrenttime (?i)remove";
    private final String usage = "maxrenttime ([TIME(Example: 10h)]/remove)";

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
    public String getUsage() {
        return this.usage;
    }

    @Override
    public boolean runCommand(Player player, String[] args, String allargs, PresetType presetType) throws InputException {

        if(!player.hasPermission(Permission.ADMIN_PRESET_SET_MAXRENTTIME)) {
            throw new InputException(player, Messages.NO_PERMISSION);
        }

        if(presetType != PresetType.RENTPRESET) {
            return false;
        }

        Preset preset = Preset.getPreset(presetType, player);

        if(preset == null) {
            preset = PresetType.create(presetType, player);
        }

        if(!(preset instanceof RentPreset)) {
            return false;
        }
        RentPreset rentPreset = (RentPreset) preset;

        if(allargs.matches(this.regex_set)) {
            rentPreset.setMaxRentTime(args[1]);
            player.sendMessage(Messages.PREFIX + Messages.PRESET_SET);
            if(rentPreset.hasPrice() && rentPreset.hasMaxRentTime() && rentPreset.hasExtendPerClick()) {
                player.sendMessage(Messages.PREFIX + "You can leave the price-line on signs empty now");
            }
            return true;
        } else {
            rentPreset.removeMaxRentTime();
            player.sendMessage(Messages.PREFIX + Messages.PRESET_REMOVED);
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args, PresetType presetType) {
        List<String> returnme = new ArrayList<>();
        if(player.hasPermission(Permission.ADMIN_PRESET_SET_MAXRENTTIME)) {
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
