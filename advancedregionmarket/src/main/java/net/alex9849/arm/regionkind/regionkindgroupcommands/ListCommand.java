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

public class ListCommand extends BasicArmCommand {

    public ListCommand() {
        super(true, "list",
                Arrays.asList("(?i)list"),
                Arrays.asList("list"),
                Arrays.asList(Permission.REGIONKINDGROUP_LIST));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        sender.sendMessage(Messages.REGIONKINDGROUP_LIST_HEADLINE);
        for (RegionKindGroup rkg : AdvancedRegionMarket.getInstance().getRegionKindGroupManager()) {
            sender.sendMessage("- " + rkg.getName());
        }
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        return new ArrayList<>();
    }
}
