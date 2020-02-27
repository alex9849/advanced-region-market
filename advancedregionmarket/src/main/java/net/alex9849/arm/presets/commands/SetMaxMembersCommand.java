package net.alex9849.arm.presets.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.presets.ActivePresetManager;
import net.alex9849.arm.presets.PresetPlayerPair;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetMaxMembersCommand implements BasicArmCommand {
    private final String rootCommand = "maxmembers";
    private final String regex_set = "(?i)maxmembers ([0-9]+|unlimited)";
    private final List<String> usage = new ArrayList<>(Arrays.asList("maxmembers [NUMBER]"));
    private PresetType presetType;

    public SetMaxMembersCommand(PresetType presetType) {
        this.presetType = presetType;
    }

    @Override
    public boolean matchesRegex(String command) {
        return command.matches(this.regex_set);
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
        if (!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;

        if (!player.hasPermission(Permission.ADMIN_PRESET_SET_MAX_MEMBERS)) {
            throw new InputException(player, Messages.NO_PERMISSION);
        }
        if (presetType == null) {
            return false;
        }

        Preset preset = ActivePresetManager.getPreset(player, presetType);

        if (preset == null) {
            preset = this.presetType.create();
            ActivePresetManager.add(new PresetPlayerPair(player, preset));
        }

        int maxMembers = -1;
        if(!args[1].equalsIgnoreCase("unlimited")) {
            maxMembers = Integer.parseInt(args[1]);
        }
        preset.setMaxMembers(maxMembers);
        player.sendMessage(Messages.PREFIX + Messages.PRESET_SET);
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if (player.hasPermission(Permission.ADMIN_PRESET_SET_MAX_MEMBERS)) {
            if (args.length >= 1) {
                if (args.length == 1) {
                    if (this.rootCommand.startsWith(args[0])) {
                        returnme.add(this.rootCommand);
                    }
                }
                if (args.length == 2 && this.rootCommand.equalsIgnoreCase(args[0])) {
                    if ("unlimited".startsWith(args[1])) {
                        returnme.add("unlimited");
                    }
                }
            }
        }
        return returnme;
    }
}
