package net.alex9849.arm.presets.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class FlaggroupCommand extends PresetOptionModifyCommand<FlagGroup> {

    public FlaggroupCommand(PresetType presetType) {
        super("flaggroup", Arrays.asList(Permission.ADMIN_PRESET_SET_FLAGGROUP),
                "[^;\n ]+", "[FLAGGROUP]", Messages.FLAGGROUP_DOES_NOT_EXIST, presetType);
    }

    @Override
    protected FlagGroup getSettingsFromString(CommandSender sender, String setting) throws InputException {
        FlagGroup fg = AdvancedRegionMarket.getInstance().getFlagGroupManager()
                .getFlagGroup(setting);
        if(fg == FlagGroup.SUBREGION) {
            throw new InputException(sender, Messages.ENTITYLIMITGROUP_SUBREGION_GROUP_ONLY_FOR_SUBREGIONS);
        }
        return fg;
    }

    @Override
    protected void applySetting(CommandSender sender, Preset object, FlagGroup setting) {
        object.setFlagGroup(setting);
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String settings) {
        return AdvancedRegionMarket.getInstance().getFlagGroupManager()
                .tabCompleteFlaggroup(settings);
    }
}
