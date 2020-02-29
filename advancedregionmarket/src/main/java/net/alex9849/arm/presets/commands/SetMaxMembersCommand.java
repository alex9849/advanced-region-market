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

public class SetMaxMembersCommand extends PresetOptionModifyCommand<Integer> {

    public SetMaxMembersCommand(PresetType presetType) {
        super("maxmembers", Arrays.asList(Permission.ADMIN_PRESET_SET_MAX_MEMBERS),
                "([0-9]+|unlimited)", "[AMOUNT/unlimited]", "", presetType);
    }

    @Override
    protected Integer getSettingsFromString(CommandSender sender, String setting) throws InputException {
        int newMaxMembers = -1;
        if(!setting.equalsIgnoreCase("unlimited")) {
            newMaxMembers = Integer.parseInt(setting);
        }
        return newMaxMembers;
    }

    @Override
    protected void applySetting(CommandSender sender, Preset object, Integer setting) {
        object.setMaxMembers(setting);
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String settings) {
        if("unlimited".startsWith(settings)) {
            return Arrays.asList("unlimited");
        }
        return new ArrayList<>();
    }
}