package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.FeatureDisabledException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.regions.Region;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class SetFlaggroupCommand extends RegionOptionModifyCommand<FlagGroup> {

    public SetFlaggroupCommand(AdvancedRegionMarket plugin) {
        super("setflaggroup", plugin, Arrays.asList(Permission.ADMIN_SET_FLAGGROUP),
                true, "FlagGroup", "[^;\n ]+", "[FLAGGROUP]",
                false, Messages.SUBREGION_FLAGGROUP_ERROR, Messages.FLAGGROUP_DOES_NOT_EXIST);
    }

    @Override
    protected void applySetting(Player sender, Region region, FlagGroup setting) {
        region.setFlagGroup(setting);
        try {
            region.applyFlagGroup(FlagGroup.ResetMode.COMPLETE, false);
        } catch (FeatureDisabledException e) {
            //Ignore
        }
    }

    @Override
    protected FlagGroup getSettingFromString(Player player, String settingsString) throws InputException {
        FlagGroup flagGroup = getPlugin().getFlagGroupManager().getFlagGroup(settingsString);

        if (flagGroup == FlagGroup.SUBREGION) {
            throw new InputException(player, Messages.SUBREGION_FLAGGROUP_ONLY_FOR_SUBREGIONS);
        }
        return flagGroup;
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String setting) {
        return getPlugin().getFlagGroupManager().tabCompleteFlaggroup(setting);
    }
}