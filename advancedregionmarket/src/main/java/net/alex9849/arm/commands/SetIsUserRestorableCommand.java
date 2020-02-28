package net.alex9849.arm.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.regions.Region;

import java.util.Arrays;


public class SetIsUserRestorableCommand extends BooleanRegionOptionModifyCommand {

    public SetIsUserRestorableCommand() {
        super("setuserrestorable",
                Arrays.asList(Permission.ADMIN_SET_IS_USERRESTORABLE),
                "IsUserRestorable",
                false, Messages.SUB_REGION_IS_USER_RESETTABLE_ERROR);
    }

    @Override
    protected void applySetting(Region region, Boolean setting) {
        region.setUserRestorable(setting);
    }
}
