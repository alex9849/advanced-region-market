package net.alex9849.arm.regionkind.regionkindgroupcommands;

import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regionkind.RegionKindGroup;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetDisplayNameCommand extends RegionKindGroupOptionModifyCommand<String> {
    public SetDisplayNameCommand() {
        super("setdisplayname",
                Arrays.asList(Permission.REGIONKINDGROUP_SET_DISPLAYNAME), "[^;\n]+",
                "[Displayname]", "");
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String setting) {
        return new ArrayList<>();
    }

    @Override
    protected String getSettingsFromString(CommandSender sender, String setting) {
        return setting;
    }

    @Override
    protected void applySetting(CommandSender sender, RegionKindGroup object, String setting) throws InputException {
        object.setDisplayName(setting);
    }
}
