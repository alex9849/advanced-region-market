package net.alex9849.arm.regionkind.regionkindgroupcommands;

import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regionkind.RegionKindGroup;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetDisplayInLimitsCommand extends RegionKindGroupOptionModifyCommand<Boolean> {
    public SetDisplayInLimitsCommand() {
        super("setdisplayinlimits", Arrays.asList(Permission.REGIONKINDGROUP_SET_DISPLAY_IN_LIMITS), "(true|false)", "[true/false]", "");
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String setting) {
        List<String> returnme = new ArrayList<>();
        if ("true".startsWith(setting)) {
            returnme.add("true");
        }
        if ("false".startsWith(setting)) {
            returnme.add("false");
        }
        return returnme;
    }

    @Override
    protected Boolean getSettingsFromString(CommandSender sender, String setting) {
        return Boolean.parseBoolean(setting);
    }

    @Override
    protected void applySetting(CommandSender sender, RegionKindGroup object, Boolean setting) throws InputException {
        object.setDisplayInLimits(setting);
    }
}
