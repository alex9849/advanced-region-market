package net.alex9849.arm.entitylimit.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
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
                Arrays.asList(Permission.ADMIN_ENTITYLIMIT_LIST));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        sender.sendMessage(Messages.ENTITYLIMITGROUP_LIST_HEADLINE);
        for (EntityLimitGroup entityLimitGroup : AdvancedRegionMarket.getInstance().getEntityLimitGroupManager()) {
            sender.sendMessage("- " + entityLimitGroup.getName());
        }
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        return new ArrayList<>();
    }
}