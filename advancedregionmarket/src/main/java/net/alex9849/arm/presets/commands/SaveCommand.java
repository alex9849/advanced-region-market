package net.alex9849.arm.presets.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SaveCommand extends PresetOptionModifyCommand<String> {

    public SaveCommand(PresetType presetType) {
        super("save", Arrays.asList(Permission.ADMIN_PRESET_SAVE), false, "[^;\n ]+",
                "[PRESETNAME]", Messages.PRESET_PLAYER_DONT_HAS_PRESET, presetType);
    }

    @Override
    protected String getSettingsFromString(CommandSender sender, String setting) throws InputException {
        if (AdvancedRegionMarket.getInstance().getPresetPatternManager().getPreset(setting, this.getPresetType()) != null) {
            throw new InputException(sender, Messages.PRESET_ALREADY_EXISTS);
        }
        return setting;
    }

    @Override
    protected void applySetting(CommandSender sender, Preset object, String setting) throws InputException {
        Preset savePreset = null;
        try {
            savePreset = (Preset) object.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        savePreset.setName(setting);
        AdvancedRegionMarket.getInstance().getPresetPatternManager().add(savePreset);
    }

    @Override
    protected void sendSuccessMessage(CommandSender sender, Preset obj, String settingsObj) {
        sender.sendMessage(Messages.PREFIX + Messages.PRESET_SAVED);
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String settings) {
        return new ArrayList<>();
    }
}
