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

public class PaybackPercentageCommand extends PresetOptionModifyCommand<Integer> {
    public PaybackPercentageCommand(PresetType presetType) {
        super("paybackpercentage",
                Arrays.asList(Permission.ADMIN_PRESET_SET_PAYBACKPERCENTAGE),
                "[0-9]+", "[PERCENTAGE]",
                "", presetType);
    }

    @Override
    protected Integer getSettingsFromString(CommandSender sender, String setting) throws InputException {
        return Integer.parseInt(setting);
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String settings) {
        return new ArrayList<>();
    }

    @Override
    protected void applySetting(CommandSender sender, Preset object, Integer setting) throws InputException {
        object.setPaybackPercentage(setting);
    }
}
