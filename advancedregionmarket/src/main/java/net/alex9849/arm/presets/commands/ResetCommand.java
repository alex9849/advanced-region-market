package net.alex9849.arm.presets.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.presets.ActivePresetManager;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResetCommand extends PresetOptionModifyCommand<Object> {

    public ResetCommand(PresetType presetType) {
        super("reset", Arrays.asList(Permission.ADMIN_PRESET_RESET), presetType);
    }

    @Override
    protected Object getSettingsFromString(CommandSender sender, String setting) throws InputException {
        return null;
    }

    @Override
    protected void applySetting(CommandSender sender, Preset object, Object setting) {
        ActivePresetManager.deletePreset((Player) sender, this.getPresetType());
    }

    @Override
    protected void sendSuccessMessage(CommandSender sender, Preset obj, Object settingsObj) {
        sender.sendMessage(Messages.PREFIX + Messages.PRESET_REMOVED);
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String settings) {
        return new ArrayList<>();
    }
}
