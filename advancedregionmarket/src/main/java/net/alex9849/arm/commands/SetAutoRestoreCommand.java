package net.alex9849.arm.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.regions.Region;

import java.util.Arrays;

public class SetAutoRestoreCommand extends BooleanRegionOptionModifyCommand {

    public SetAutoRestoreCommand() {
        super("setautorestore", Arrays.asList(Permission.ADMIN_SET_AUTORESTORE),
                "AutoRestore", false, Messages.SUB_REGION_AUTORESTORE_ERROR);
    }

    @Override
    protected void applySetting(Region region, Boolean setting) {
        region.setAutoRestore(setting);
    }
}
