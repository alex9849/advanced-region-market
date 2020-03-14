package net.alex9849.arm.presets.commands;

import net.alex9849.arm.Permission;
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
                false, "[^;\n]+", "[COMMAND]", "", presetType);
    }

    @Override
    protected String getSettingsFromString(CommandSender sender, String setting) {
        return setting;
    }

    @Override
    protected void applySetting(CommandSender sender, Preset object, String setting) {
        object.addCommand(setting);
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String settings) {
        return new ArrayList<>();
    }
}
