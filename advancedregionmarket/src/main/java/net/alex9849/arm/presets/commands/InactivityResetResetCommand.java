package net.alex9849.arm.presets.commands;

import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InactivityResetResetCommand extends PresetOptionModifyCommand<Boolean> {

    public InactivityResetResetCommand(PresetType presetType) {
        super("inactivityreset", Arrays.asList(Permission.ADMIN_PRESET_SET_INACTIVITYRESET),
                "(false|true)", "(true/false)", "", presetType);
    }

    @Override
    protected Boolean getSettingsFromString(CommandSender sender, String setting) throws InputException {
        return Boolean.parseBoolean(setting);
    }

    @Override
    protected void applySetting(CommandSender sender, Preset object, Boolean setting) {
        object.setInactivityReset(setting);
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String settings) {
        List<String> returnme = new ArrayList<>();
        if ("true".startsWith(settings)) {
            returnme.add("true");
        }
        if ("false".startsWith(settings)) {
            returnme.add("false");
        }
        return returnme;
    }
}
