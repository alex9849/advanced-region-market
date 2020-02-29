package net.alex9849.arm.regionkind.commands;

import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.CommandUtil;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regionkind.RegionKind;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetDisplayNameCommand extends RegionKindOptionModifyCommand<String> {

    public SetDisplayNameCommand() {
        super("setdisplayname",
                Arrays.asList(Permission.REGIONKIND_SET_DISPLAYNAME), "[^;\n]+",
                "[Displayname]", "");
    }

    @Override
    protected String getSettingsFromCommand(CommandSender sender, String command) throws InputException {
        String[] args = command.split(" ");
        List<String> displayNameArgs = new ArrayList<>();
        for (int i = 2; i < args.length; i++) {
            displayNameArgs.add(args[i]);
        }
        return CommandUtil.getStringList(displayNameArgs, x -> x, " ");
    }

    @Override
    protected String getSettingsFromString(CommandSender sender, String setting) {
        return null;
    }

    @Override
    protected void applySetting(CommandSender sender, RegionKind object, String setting) {
        object.setDisplayName(setting);
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String[] args) {
        return new ArrayList<>();
    }
}