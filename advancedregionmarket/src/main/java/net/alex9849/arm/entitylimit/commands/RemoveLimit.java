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

public class RemoveLimit extends BasicArmCommand {

    public RemoveLimit() {
        super(true, "removelimit",
                Arrays.asList("(?i)removelimit [^;\n ]+ [^;\n ]+"),
                Arrays.asList("removelimit [GROUPNAME] [ENTITYTYPE]"),
                Arrays.asList(Permission.ADMIN_ENTITYLIMIT_REMOVE_LIMIT));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        String[] args = command.split(" ");
        EntityLimitGroup entityLimitGroup = AdvancedRegionMarket.getInstance().getEntityLimitGroupManager().getEntityLimitGroup(args[1]);
        if (entityLimitGroup == null) {
            throw new InputException(sender, Messages.ENTITYLIMITGROUP_DOES_NOT_EXIST);
        }

        if (args[2].equalsIgnoreCase("total")) {
            entityLimitGroup.setSoftLimit(Integer.MAX_VALUE);
            sender.sendMessage(Messages.PREFIX + Messages.ENTITYLIMIT_REMOVED);
            return true;
        } else {
            EntityLimit.LimitableEntityType limitableEntityType = EntityLimit.getLimitableEntityType(args[2]);
            if (limitableEntityType == null) {
                throw new InputException(sender, Messages.ENTITYTYPE_DOES_NOT_EXIST.replace("%entitytype%", args[2]));
            }


            for (int i = 0; i < entityLimitGroup.getEntityLimits().size(); i++) {
                if (entityLimitGroup.getEntityLimits().get(i).getLimitableEntityType() == limitableEntityType) {
                    entityLimitGroup.getEntityLimits().remove(i);
                    entityLimitGroup.queueSave();
                    sender.sendMessage(Messages.PREFIX + Messages.ENTITYLIMIT_REMOVED);
                    return true;
                }
            }
        }

        throw new InputException(sender, Messages.ENTITYLIMITGROUP_DOES_NOT_CONTAIN_ENTITYLIMIT);
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if (args.length == 2) {
            returnme.addAll(AdvancedRegionMarket.getInstance().getEntityLimitGroupManager().tabCompleteEntityLimitGroups(args[1]));

        } else if (args.length == 3) {
            EntityLimitGroup entityLimitGroup = AdvancedRegionMarket.getInstance().getEntityLimitGroupManager().getEntityLimitGroup(args[1]);
            if (entityLimitGroup == null) {
                return returnme;
            }
            for (EntityLimit entityLimit : entityLimitGroup.getEntityLimits()) {
                if (entityLimit.getLimitableEntityType().toString().toLowerCase().startsWith(args[2])) {
                    returnme.add(entityLimit.getLimitableEntityType().toString());
                }
            }
            if ("total".startsWith(args[2])) {
                returnme.add("total");
            }
        }
        return returnme;
    }
}