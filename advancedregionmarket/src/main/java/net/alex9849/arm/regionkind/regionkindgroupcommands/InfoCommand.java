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

public class InfoCommand extends BasicArmCommand {

    public InfoCommand() {
        super(true, "info",
                Arrays.asList("(?i)info [^;\n ]+"),
                Arrays.asList("info [REGIONKINDGROUP]"),
                Arrays.asList(Permission.REGIONKINDGROUP_INFO));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        RegionKindGroup regionKindGroup = AdvancedRegionMarket.getInstance().getRegionKindGroupManager().getRegionKindGroup(command.split(" ")[1]);
        if (regionKindGroup == null) {
            throw new InputException(sender, Messages.REGIONKINDGROUP_NOT_EXISTS);
        }

        List<String> messages = new ArrayList<>(Messages.REGIONKINDGROUP_INFO);

        for(String msg : messages) {
            sender.sendMessage(regionKindGroup.replaceVariables(msg));
        }
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        if(args.length != 2) {
            return new ArrayList<>();
        }
        return AdvancedRegionMarket.getInstance().getRegionKindGroupManager().tabCompleteRegionKindGroups(args[1]);
    }
}
