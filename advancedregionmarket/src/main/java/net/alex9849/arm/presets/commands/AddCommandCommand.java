package net.alex9849.arm.presets.commands;

import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.CommandUtil;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddCommandCommand extends PresetOptionModifyCommand<String> {

    public AddCommandCommand(PresetType presetType) {
        super("addcommand", Arrays.asList(Permission.ADMIN_PRESET_ADDCOMMAND),
                "[^;\n]+", "[COMMAND]", "", presetType);
    }

    @Override
    protected String getSettingsFromCommand(CommandSender sender, String command) throws InputException {
        String[] args = command.split(" ");
        List<String> commandArgs = new ArrayList<>();
        for(int i = 1; i < args.length; i++) {
            commandArgs.add(args[i]);
        }

        return CommandUtil.getStringList(commandArgs, x -> x, " ");
    }

    @Override
    protected void applySetting(CommandSender sender, Preset object, String setting) throws InputException {
        object.addCommand(setting);
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String[] args) {
        return new ArrayList<>();
    }
}
