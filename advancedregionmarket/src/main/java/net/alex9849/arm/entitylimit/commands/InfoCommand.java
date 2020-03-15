package net.alex9849.arm.entitylimit.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.entitylimit.EntityLimit;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InfoCommand extends BasicArmCommand {

    public InfoCommand() {
        super(true, "info",
                Arrays.asList("(?i)info [^;\n ]+"),
                Arrays.asList("info [GROUPNAME]"),
                Arrays.asList(Permission.MEMBER_ENTITYLIMIT_INFO));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        EntityLimitGroup entityLimitGroup = AdvancedRegionMarket.getInstance()
                .getEntityLimitGroupManager().getEntityLimitGroup(command.split(" ")[1]);
        if (entityLimitGroup == null) {
            throw new InputException(sender, Messages.ENTITYLIMITGROUP_DOES_NOT_EXIST);
        }

        sendInfoToSender(sender, entityLimitGroup);
        return true;
    }

    public static void sendInfoToSender(CommandSender sender, EntityLimitGroup entityLimitGroup) {
        sender.sendMessage(Messages.ENTITYLIMITGROUP_INFO_HEADLINE);
        sender.sendMessage(Messages.ENTITYLIMITGROUP_INFO_GROUPNAME + entityLimitGroup.getName());
        String totalstatus = entityLimitGroup.replaceVariables(Messages.ENTITYLIMITGROUP_INFO_PATTERN, new ArrayList<>(), 0);
        if (entityLimitGroup.getSoftLimit(0) < entityLimitGroup.getHardLimit()) {
            totalstatus = totalstatus.replace("%entityextensioninfo%", entityLimitGroup.replaceVariables(Messages.ENTITYLIMITGROUP_INFO_EXTENSION_INFO, new ArrayList<>(), 0));
        } else {
            totalstatus = totalstatus.replace("%entityextensioninfo%", "");
        }
        sender.sendMessage(totalstatus);

        for (EntityLimit entityLimit : entityLimitGroup.getEntityLimits()) {
            String entitystatus = entityLimit.replaceVariables(Messages.ENTITYLIMITGROUP_INFO_PATTERN, new ArrayList<>(), 0);
            if (entityLimit.getSoftLimit(0) < entityLimit.getHardLimit()) {
                entitystatus = entitystatus.replace("%entityextensioninfo%", entityLimit.replaceVariables(Messages.ENTITYLIMITGROUP_INFO_EXTENSION_INFO, new ArrayList<>(), 0));
            } else {
                entitystatus = entitystatus.replace("%entityextensioninfo%", "");
            }
            sender.sendMessage(entitystatus);
        }
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        if(args.length == 2) {
            return AdvancedRegionMarket.getInstance().getEntityLimitGroupManager().tabCompleteEntityLimitGroups(args[1]);
        }
        return new ArrayList<>();
    }
}
