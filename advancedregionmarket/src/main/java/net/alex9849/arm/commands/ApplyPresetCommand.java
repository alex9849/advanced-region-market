package net.alex9849.arm.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.presets.ActivePresetManager;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import net.alex9849.arm.regions.Region;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApplyPresetCommand extends RegionOptionModifyCommand<Preset> {

    public ApplyPresetCommand() {
        super("applypreset", Arrays.asList(Permission.ADMIN_PRESET_APPLY), true,
                "preset", "(?i)(sellpreset|rentpreset|contractpreset)", "[PRESETTYPE]", false,
                Messages.APPLY_PRESET_SUBREGION_ERROR, Messages.PRESET_PLAYER_DONT_HAS_PRESET);
    }

    @Override
    protected void applySetting(Region region, Preset setting) {
        setting.applyToRegion(region);
    }

    @Override
    protected Preset getSettingFromString(Player player, String settingsString) throws InputException {
        return ActivePresetManager.getPreset(player, PresetType.getPresetType(settingsString));
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String setting) {
        List<String> returnMe = new ArrayList<>();
        if("sellpreset".toLowerCase().startsWith(setting)) {
            returnMe.add("sellpreset");
        }
        if("rentpreset".toLowerCase().startsWith(setting)) {
            returnMe.add("rentpreset");
        }
        if("contractpreset".toLowerCase().startsWith(setting)) {
            returnMe.add("contractpreset");
        }
        return returnMe;
    }
}
