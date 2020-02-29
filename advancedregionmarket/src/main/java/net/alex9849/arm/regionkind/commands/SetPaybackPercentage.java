package net.alex9849.arm.regionkind.commands;

import net.alex9849.arm.Permission;
import net.alex9849.arm.regionkind.RegionKind;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetPaybackPercentage extends RegionKindOptionModifyCommand<Double> {

    public SetPaybackPercentage() {
        super("setpaybackpercentage", Arrays.asList(Permission.REGIONKIND_SET_PAYBACKPERCENTAGE),
                "[0-9]+", "[Percentage]", "");
    }

    @Override
    protected Double getSettingsFromString(CommandSender sender, String setting) {
        return Integer.parseInt(setting) / 100d;
    }

    @Override
    protected void applySetting(CommandSender sender, RegionKind object, Double setting) {
        object.setPaybackPercentage(setting);
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String setting) {
        return new ArrayList<>();
    }
}
