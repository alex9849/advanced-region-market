package net.alex9849.arm.regionkind.regionkindgroupcommands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regionkind.RegionKindGroup;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateCommand extends BasicArmCommand {

    public CreateCommand(AdvancedRegionMarket plugin) {
        super(true, plugin, "create",
                Arrays.asList("(?i)create [^;\n\\. ]+"),
                Arrays.asList("create [REGIONKINDGROUPNAME]"),
                Arrays.asList(Permission.REGIONKINDGROUP_CREATE));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        String[] args = command.split(" ");
        if (getPlugin().getRegionKindGroupManager().getRegionKindGroup(args[1]) != null) {
            throw new InputException(sender, Messages.REGIONKINDGROUP_ALREADY_EXISTS);
        }
        getPlugin().getRegionKindGroupManager().add(new RegionKindGroup(args[1]));
        sender.sendMessage(Messages.PREFIX + Messages.REGIONKINDGROUP_CREATED);
        return true;
    }

    @Override
    protected List<String> onTabCompleteArguments(Player player, String[] args) {
        return new ArrayList<>();
    }
}
