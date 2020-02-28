package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.Region;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetSubregionLimit extends OptionModifyCommand<Integer> {

    public SetSubregionLimit() {
        super("setsubregionlimit", Arrays.asList(Permission.ADMIN_SET_SUBREGION_LIMIT),
                "setsubregionlimit", "[0-9]+", "[AMOUNT]", false,
                ChatColor.RED + "Subregions can not have subregions");
    }

    @Override
    protected void applySetting(Region region, Integer setting) {
        region.setAllowedSubregions(setting);
    }

    @Override
    protected Integer getSettingFromString(Player player, String settingsString) {
        return Integer.parseInt(settingsString);
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String setting) {
        return new ArrayList<>();
    }
}
