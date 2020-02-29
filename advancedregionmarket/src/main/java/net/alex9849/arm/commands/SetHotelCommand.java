package net.alex9849.arm.commands;

import net.alex9849.arm.Permission;
import net.alex9849.arm.regions.Region;

import java.util.Arrays;

public class SetHotelCommand extends BooleanRegionOptionModifyCommand {

    public SetHotelCommand() {
        super("sethotel", Arrays.asList(Permission.ADMIN_SET_IS_HOTEL), "isHotel", true, "");
    }

    @Override
    protected void applySetting(Region region, Boolean setting) {
        region.setHotel(setting);
    }
}
