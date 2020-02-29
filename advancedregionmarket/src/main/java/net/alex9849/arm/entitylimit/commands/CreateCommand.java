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

public class CreateCommand extends BasicArmCommand {

    public CreateCommand() {
        super(true, "create",
                Arrays.asList("(?i)create [^;\n ]+"),
                Arrays.asList("create [GROUPNAME]"),
                Arrays.asList(Permission.ADMIN_ENTITYLIMIT_CREATE));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        String[] args = command.split(" ");
        if (AdvancedRegionMarket.getInstance().getEntityLimitGroupManager().getEntityLimitGroup(args[1]) != null) {
            throw new InputException(sender, Messages.ENTITYLIMITGROUP_ALREADY_EXISTS);
        }
        AdvancedRegionMarket.getInstance().getEntityLimitGroupManager()
                .add(new EntityLimitGroup(new ArrayList<>(), -1, -1, 0, args[1]));
        sender.sendMessage(Messages.PREFIX + Messages.ENTITYLIMITGROUP_CREATED);
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        return new ArrayList<>();
    }
}
