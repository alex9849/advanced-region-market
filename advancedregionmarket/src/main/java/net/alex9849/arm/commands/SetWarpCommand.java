package net.alex9849.arm.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.regions.Region;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetWarpCommand extends RegionOptionModifyCommand<Location> {

    public SetWarpCommand() {
        super("setwarp", Arrays.asList(Permission.ADMIN_SETWARP), "teleportLocation",
                false, Messages.SUB_REGION_TELEPORT_LOCATION_ERROR);
    }

    @Override
    protected void applySetting(Region region, Location setting) {
        region.setTeleportLocation(setting);
    }

    @Override
    protected Location getSettingFromString(Player player, String settingsString) {
        return player.getLocation();
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String setting) {
        return new ArrayList<>();
    }
}
