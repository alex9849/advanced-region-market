package net.alex9849.arm.presets.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.presets.ActivePresetManager;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SaveCommand extends BasicArmCommand {

    private final String rootCommand = "save";
    private final String regex = "(?i)save [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("save [PRESETNAME]"));
    private PresetType presetType;

    public SaveCommand(PresetType presetType) {
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

        if(!player.hasPermission(Permission.ADMIN_PRESET_SAVE)) {
            throw new InputException(player, Messages.NO_PERMISSION);
        }

        if(presetType == null) {
            return false;
        }

        Preset preset = ActivePresetManager.getPreset(player, this.presetType);
        String name = args[1];

        if(preset == null) {
            throw new InputException(player, Messages.PRESET_PLAYER_DONT_HAS_PRESET);
        }

        AdvancedRegionMarket.getPresetPatternManager().getPreset(name, this.presetType);
        if(AdvancedRegionMarket.getPresetPatternManager().getPreset(name, this.presetType) == null) {
            Preset savePreset = preset.getCopy();
            savePreset.setName(name);
            AdvancedRegionMarket.getPresetPatternManager().add(savePreset);
            player.sendMessage(Messages.PRESET_SAVED);
            return true;
        } else {
            throw new InputException(player, Messages.PRESET_ALREADY_EXISTS);
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if(player.hasPermission(Permission.ADMIN_PRESET_LOAD)) {
            if(args.length >= 1) {
                if(args.length == 1) {
                    if(this.rootCommand.startsWith(args[0])) {
                        returnme.add(this.rootCommand);
                    }
                }
                if(args.length == 2 && args[0].equalsIgnoreCase(this.rootCommand)) {
                    returnme.add(args[1]);
                }
            }
        }
        return returnme;
    }
}
