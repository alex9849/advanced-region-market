package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.regions.Region;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SetAutoRestoreCommand extends BooleanRegionOptionModifyCommand {

    public SetAutoRestoreCommand(AdvancedRegionMarket plugin) {
        super("setautorestore", plugin, Arrays.asList(Permission.ADMIN_SET_AUTORESTORE),
                "AutoRestore", false, Messages.SUBREGION_AUTORESTORE_ERROR);
    }

    @Override
    protected void applySetting(Player sender, Region region, Boolean setting) {
        region.setAutoRestore(setting);
    }
}
