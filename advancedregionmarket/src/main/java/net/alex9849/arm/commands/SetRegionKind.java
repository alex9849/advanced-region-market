package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.Region;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class SetRegionKind extends RegionOptionModifyCommand<RegionKind> {

    public SetRegionKind(AdvancedRegionMarket plugin) {
        super("setregionkind", plugin, Arrays.asList(Permission.ADMIN_SETREGIONKIND),
                true, "RegionKind",
                "[^;\n ]+", "[REGIONKIND]", false,
                Messages.SUBREGION_REGIONKIND_ERROR, Messages.REGIONKIND_DOES_NOT_EXIST);
    }

    @Override
    protected void applySetting(Player sender, Region region, RegionKind setting) {
        region.setRegionKind(setting);
    }

    @Override
    protected RegionKind getSettingFromString(Player player, String settingsString) throws InputException {
        RegionKind regionKind = getPlugin().getRegionKindManager().getRegionKind(settingsString);
        if (regionKind == RegionKind.SUBREGION) {
            throw new InputException(player, Messages.SUBREGION_REGIONKIND_ONLY_FOR_SUBREGIONS);
        }
        return regionKind;
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String setting) {
        return getPlugin().getRegionKindManager()
                .completeTabRegionKinds(setting, "");
    }
}
