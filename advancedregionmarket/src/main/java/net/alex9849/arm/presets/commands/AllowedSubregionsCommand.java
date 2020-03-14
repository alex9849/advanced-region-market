package net.alex9849.arm.presets.commands;

import net.alex9849.arm.Permission;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllowedSubregionsCommand extends PresetOptionModifyCommand<Integer> {

    public AllowedSubregionsCommand(PresetType presetType) {
        super("allowedsubregions", Arrays.asList(Permission.ADMIN_PRESET_ALLOWEDSUBREGIONS),
                true, "([0-9]+|(?i)remove)", "[NUMBER/remove]", "", presetType);
    }

    @Override
    protected Integer getSettingsFromString(CommandSender sender, String setting) {
        if(setting.equalsIgnoreCase("remove")) {
            return null;
        }
        return Integer.parseInt(setting);
    }

    @Override
    protected void applySetting(CommandSender sender, Preset object, Integer setting) {
        object.setAllowedSubregions(setting);
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String settings) {
        List<String> returnme = new ArrayList<>();
        if ("remove".startsWith(settings)) {
            returnme.add("remove");
        }
        return returnme;
    }
}
