package net.alex9849.arm.regionkind.regionkindgroupcommands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regionkind.RegionKindGroup;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class RemoveRegionkindCommand extends RegionKindGroupOptionModifyCommand<RegionKind> {
    public RemoveRegionkindCommand() {
        super("removeregionkind", Arrays.asList(Permission.REGIONKINDGROUP_REMOVEREGIONKIND),
                "[^;\n ]+", "[REGIONKIND]", Messages.REGIONKIND_DOES_NOT_EXIST);
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String setting) {
        return AdvancedRegionMarket.getInstance().getRegionKindManager().completeTabRegionKinds(setting, "");
    }

    @Override
    protected RegionKind getSettingsFromString(CommandSender sender, String setting) {
        return AdvancedRegionMarket.getInstance().getRegionKindManager().getRegionKind(setting);
    }

    @Override
    protected void applySetting(CommandSender sender, RegionKindGroup object, RegionKind setting) throws InputException {
        object.removeRegionKind(setting);
    }
}
