package net.alex9849.arm.entitylimit.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regions.Region;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeleteCommand extends BasicArmCommand {

    public DeleteCommand(AdvancedRegionMarket plugin) {
        super(true, plugin, "delete",
                Arrays.asList("(?i)delete [^;\n ]+"),
                Arrays.asList("delete [GROUPNAME]"),
                Arrays.asList(Permission.ADMIN_ENTITYLIMIT_DELETE));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException {
        EntityLimitGroup entityLimitGroup = getPlugin()
                .getEntityLimitGroupManager().getEntityLimitGroup(command.split(" ")[1]);
        if (entityLimitGroup == null) {
            throw new InputException(sender, Messages.ENTITYLIMITGROUP_DOES_NOT_EXIST);
        }
        if (entityLimitGroup == EntityLimitGroup.DEFAULT) {
            throw new InputException(sender, Messages.ENTITYLIMITGROUP_CAN_NOT_REMOVE_SYSTEM);
        }
        if (entityLimitGroup == EntityLimitGroup.SUBREGION) {
            throw new InputException(sender, Messages.ENTITYLIMITGROUP_CAN_NOT_REMOVE_SYSTEM);
        }

        getPlugin().getEntityLimitGroupManager().remove(entityLimitGroup);

        for (Region region : getPlugin().getRegionManager()) {
            if (region.getEntityLimitGroup() == entityLimitGroup) {
                region.setEntityLimitGroup(EntityLimitGroup.DEFAULT);
            }
        }

        sender.sendMessage(Messages.PREFIX + Messages.ENTITYLIMITGROUP_DELETED);
        return true;
    }

    @Override
    protected List<String> onTabCompleteArguements(Player player, String[] args) {
        if(args.length == 2) {
            return getPlugin().getEntityLimitGroupManager().tabCompleteEntityLimitGroups(args[1]);
        }
        return new ArrayList<>();
    }
}
