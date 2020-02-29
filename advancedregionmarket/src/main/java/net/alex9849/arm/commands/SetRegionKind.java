package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.Region;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class SetRegionKind extends RegionOptionModifyCommand<RegionKind> {

    public SetRegionKind() {
        super("setregionkind", Arrays.asList(Permission.ADMIN_SETREGIONKIND), "RegionKind",
                "[^;\n ]+", "[REGIONKIND]", false,
                Messages.SUB_REGION_REGIONKIND_ERROR);
    }

    @Override
    protected void applySetting(Region region, RegionKind setting) {
        region.setRegionKind(setting);
    }

    @Override
    protected RegionKind getSettingFromString(Player player, String settingsString) throws InputException {
        RegionKind regionKind = AdvancedRegionMarket.getInstance().getRegionKindManager().getRegionKind(settingsString);
        if (regionKind == null) {
            throw new InputException(player, Messages.REGIONKIND_DOES_NOT_EXIST);
        }
        if (regionKind == RegionKind.SUBREGION) {
            throw new InputException(player, Messages.SUB_REGION_REGIONKIND_ONLY_FOR_SUB_REGIONS);
        }
        return regionKind;
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String setting) {
        return AdvancedRegionMarket.getInstance().getRegionManager()
                .completeTabRegions(player, setting, PlayerRegionRelationship.ALL, true, false);
    }
}
