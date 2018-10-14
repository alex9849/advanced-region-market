package net.alex9849.arm.Preseter.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.Preseter.Preset;
import net.alex9849.arm.Preseter.PresetType;
import net.alex9849.arm.Preseter.RentPreset;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regions.RegionKind;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RegionKindCommand extends BasicPresetCommand {

    private final String rootCommand = "regionkind";
    private final String regex_set = "(?i)regionkind [^;\n ]+";
    private final String regex_remove = "(?i)regionkind (?i)remove";
    private final String usage = "regionkind ([REGIONKIND]/remove)";

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
        if(!player.hasPermission(Permission.ADMIN_PRESET_SET_REGIONKIND)) {
            throw new InputException(player, Messages.NO_PERMISSION);
        }

        if(presetType == null) {
            return false;
        }

        Preset preset = Preset.getPreset(presetType, player);

        if(preset == null) {
            preset = PresetType.create(presetType, player);
        }

        if(allargs.matches(this.regex_set)) {
            RegionKind regkind = RegionKind.getRegionKind(args[1]);
            if(regkind == null) {
                player.sendMessage(Messages.PREFIX + Messages.REGIONKIND_DOES_NOT_EXIST);
                return true;
            }
            preset.setRegionKind(regkind);
            player.sendMessage(Messages.PREFIX + Messages.PRESET_SET);
            return true;
        } else {
            preset.removeRegionKind();
            player.sendMessage(Messages.PREFIX + Messages.PRESET_REMOVED);
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args, PresetType presetType) {
        List<String> returnme = new ArrayList<>();
        if(player.hasPermission(Permission.ADMIN_PRESET_SET_REGIONKIND)) {
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
                    for(RegionKind regionkind : RegionKind.getRegionKindList()) {
                        if(regionkind.getName().toLowerCase().startsWith(args[1])) {
                            returnme.add(regionkind.getName());
                        }
                    }
                    if("default".startsWith(args[1])) {
                        returnme.add("default");
                    }
                }
            }
        }
        return returnme;
    }
}
