package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Permission;
import net.alex9849.arm.regions.Region;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SetHotelCommand extends BooleanRegionOptionModifyCommand {

    public SetHotelCommand(AdvancedRegionMarket plugin) {
        super("sethotel", plugin, Arrays.asList(Permission.ADMIN_SET_IS_HOTEL), "isHotel", true, "");
    }

    @Override
    protected void applySetting(Player sender, Region region, Boolean setting) {
        region.setHotel(setting);
    }
}
