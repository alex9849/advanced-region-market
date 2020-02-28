package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.inactivityexpiration.InactivityExpirationGroup;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.Region;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetInactivityResetCommand extends BooleanOptionModifyCommand {

    public SetInactivityResetCommand() {
        super("setinactivityreset", Arrays.asList(Permission.ADMIN_SET_INACTIVITYRESET),
                "InactivityReset", false, Messages.SUB_REGION_INACTIVITYRESET_ERROR);
    }

    @Override
    protected void applySetting(Region region, Boolean setting) {
        region.setInactivityReset(setting);
    }
}
