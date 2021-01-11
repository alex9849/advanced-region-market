package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.regions.Region;
import org.bukkit.entity.Player;

import java.util.Arrays;


public class SetIsUserRestorableCommand extends BooleanRegionOptionModifyCommand {

    public SetIsUserRestorableCommand(AdvancedRegionMarket plugin) {
        super("setuserrestorable", plugin,
                Arrays.asList(Permission.ADMIN_SET_IS_USERRESTORABLE),
                "IsUserRestorable",
                false, Messages.SUBREGION_IS_USER_RESETTABLE_ERROR);
    }

    @Override
    protected void applySetting(Player sender, Region region, Boolean setting) {
        region.setUserRestorable(setting);
    }
}
