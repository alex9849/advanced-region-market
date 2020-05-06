package net.alex9849.arm.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regions.Region;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetPaybackPercentageCommand extends RegionOptionModifyCommand<Integer> {
    public SetPaybackPercentageCommand() {
        super("setpaybackpercentage", Arrays.asList(Permission.ADMIN_SET_PAYBACKPERCENTAGE),
                true, "paybackPercentage", "[0-9]+", "[PERCENTAGE]",
                false, Messages.SUBREGION_PAYBACKPERCENTAGE_ERROR, "");
    }

    @Override
    protected void applySetting(Region region, Integer setting) {
        region.setPaybackPercentage(setting);
    }

    @Override
    protected Integer getSettingFromString(Player player, String settingsString) throws InputException {
        return Integer.parseInt(settingsString);
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String setting) {
        return new ArrayList<>();
    }
}
