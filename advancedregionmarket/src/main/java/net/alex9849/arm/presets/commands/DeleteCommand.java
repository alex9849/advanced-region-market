package net.alex9849.arm.presets.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeleteCommand extends BasicArmCommand {

    private final String rootCommand = "delete";
    private final String regex = "(?i)delete [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("delete [PRESETNAME]"));
    private PresetType presetType;

    public DeleteCommand(PresetType presetType) {
        this.presetType = presetType;
    }

    @Override
    public boolean matchesRegex(String command) {
        return command.matches(regex);
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

        if(!player.hasPermission(Permission.ADMIN_PRESET_DELETE)) {
            throw new InputException(player, Messages.NO_PERMISSION);
        }

        Preset toDeletePreset = AdvancedRegionMarket.getPresetPatternManager().getPreset(args[1], this.presetType);

        if(toDeletePreset == null) {
            throw new InputException(player, Messages.PRESET_NOT_FOUND);
        }

        AdvancedRegionMarket.getPresetPatternManager().remove(toDeletePreset);
        player.sendMessage(Messages.PREFIX + Messages.PRESET_DELETED);

        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if(player.hasPermission(Permission.ADMIN_PRESET_DELETE)) {
            if(args.length >= 1) {
                if(args.length == 1) {
                    if(this.rootCommand.startsWith(args[0])) {
                        returnme.add(this.rootCommand);
                    }
                }
                if(args.length == 2 && this.rootCommand.equalsIgnoreCase(args[0])) {
                    returnme.addAll(AdvancedRegionMarket.getPresetPatternManager().onTabCompleteCompleteSavedPresets(this.presetType, args[1]));
                }
            }
        }
        return returnme;
    }
}
