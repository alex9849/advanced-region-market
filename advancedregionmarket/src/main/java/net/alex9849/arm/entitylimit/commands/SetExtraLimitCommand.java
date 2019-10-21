package net.alex9849.arm.entitylimit.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.entitylimit.EntityLimit;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetExtraLimitCommand implements BasicArmCommand {
    private final String rootCommand = "setextralimit";
    private final String regex = "(?i)setextralimit [^;\n ]+ [^;\n ]+ [0-9]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("setextralimit [REGION] [ENTITYTYPE] [AMOUNT]"));

    @Override
    public boolean matchesRegex(String command) {
        return command.matches(this.regex);
    }

    @Override
    public String getRootCommand() {
        return this.rootCommand;
    }

    @Override
    public List<String> getUsage() {
        return this.usage;
    }

    @Override
    public boolean runCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args, String allargs) throws InputException {
        if (!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;
        if (!sender.hasPermission(Permission.ADMIN_ENTITYLIMIT_SET_EXTRA)) {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }

        Region region = AdvancedRegionMarket.getInstance().getRegionManager().getRegionbyNameAndWorldCommands(args[1], player.getWorld().getName());

        if (region == null) {
            throw new InputException(player, Messages.REGION_DOES_NOT_EXIST);
        }

        if (region.isSubregion()) {
            throw new InputException(player, Messages.ENTITYLIMITGROUP_EXTRA_ENTITIES_SET_SUBREGION_ERROR);
        }

        int amount = Integer.parseInt(args[3]);

        if (amount < 0) {
            amount = 0;
        }

        if (args[2].equalsIgnoreCase("total")) {
            region.setExtraTotalEntitys(amount);
        } else {
            EntityLimit.LimitableEntityType limitableEntityType = EntityLimit.getLimitableEntityType(args[2]);
            if (limitableEntityType == null) {
                throw new InputException(sender, Messages.ENTITYTYPE_DOES_NOT_EXIST.replace("%entitytype%", args[2]));
            }

            if (!region.getEntityLimitGroup().containsLimit(limitableEntityType)) {
                throw new InputException(player, Messages.ENTITYLIMITGROUP_ENTITYLIMIT_ALREADY_UNLIMITED);
            }
            region.setExtraEntityAmount(limitableEntityType, amount);
        }


        player.sendMessage(Messages.PREFIX + Messages.ENTITYLIMITGROUP_EXTRA_ENTITIES_SET);

        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if (!player.hasPermission(Permission.ADMIN_ENTITYLIMIT_SET_EXTRA)) {
            return returnme;
        }

        if (args.length >= 1) {
            if (args.length == 1) {
                if (this.rootCommand.startsWith(args[0])) {
                    returnme.add(this.rootCommand);
                }
            } else if ((args.length == 2) && (args[0].equalsIgnoreCase(this.rootCommand))) {
                if (this.rootCommand.startsWith(args[0])) {
                    returnme.addAll(AdvancedRegionMarket.getInstance().getRegionManager().completeTabRegions(player, args[1], PlayerRegionRelationship.ALL, true, false));

                }
            } else if ((args.length == 3) && (args[0].equalsIgnoreCase(this.rootCommand))) {
                for (EntityLimit.LimitableEntityType entityType : EntityLimit.entityTypes) {
                    if (entityType.toString().toLowerCase().startsWith(args[2])) {
                        returnme.add(entityType.toString());
                    }
                }
                if ("total".startsWith(args[2])) {
                    returnme.add("total");
                }
            }
        }
        return returnme;
    }
}