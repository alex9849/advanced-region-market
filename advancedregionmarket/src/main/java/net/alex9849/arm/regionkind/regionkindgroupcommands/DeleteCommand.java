package net.alex9849.arm.regionkind.regionkindgroupcommands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regionkind.RegionKindGroup;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeleteCommand extends BasicArmCommand {

    public DeleteCommand() {
        super(true, "delete",
                Arrays.asList("(?i)delete [^;\n ]+"),
                Arrays.asList("delete [REGIONKINDGROUP]"),
                Arrays.asList(Permission.REGIONKINDGROUP_DELETE));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException {
        String[] args = command.split(" ");
        RegionKindGroup regionKind = AdvancedRegionMarket.getInstance().getRegionKindGroupManager().getRegionKindGroup(args[1]);
        if (regionKind == null) {
            throw new InputException(sender, Messages.REGIONKINDGROUP_NOT_EXISTS);
        }

        AdvancedRegionMarket.getInstance().getRegionKindGroupManager().remove(regionKind);

        sender.sendMessage(Messages.PREFIX + Messages.REGIONKINDGROUP_DELETED);
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
