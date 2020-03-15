package net.alex9849.arm.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.regions.Region;

import java.util.Arrays;

public class SetInactivityResetCommand extends BooleanRegionOptionModifyCommand {

    public SetInactivityResetCommand() {
        super("setinactivityreset", Arrays.asList(Permission.ADMIN_SET_INACTIVITYRESET),
                "InactivityReset", false, Messages.SUBREGION_INACTIVITYRESET_ERROR);
    }

    @Override
    protected void applySetting(Region region, Boolean setting) {
        region.setInactivityReset(setting);
    }
}
