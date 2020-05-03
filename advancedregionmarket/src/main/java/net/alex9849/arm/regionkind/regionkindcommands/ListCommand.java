package net.alex9849.arm.regionkind.regionkindcommands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regionkind.RegionKind;
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
                Arrays.asList(Permission.REGIONKIND_LIST));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        sender.sendMessage(Messages.REGIONKIND_LIST_HEADLINE);
        sender.sendMessage("- " + RegionKind.DEFAULT.getName());
        sender.sendMessage("- " + RegionKind.SUBREGION.getName());
        for (RegionKind regionKind : AdvancedRegionMarket.getInstance().getRegionKindManager()) {
            sender.sendMessage("- " + regionKind.getName());
        }
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        return new ArrayList<>();
    }
}