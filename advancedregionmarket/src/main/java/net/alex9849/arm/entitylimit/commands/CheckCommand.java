package net.alex9849.arm.entitylimit.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.entitylimit.EntityLimit;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckCommand extends BasicArmCommand {
    private final String rootCommand = "check";
    private final String regex = "(?i)check [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("check [REGION]"));

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
        if (!sender.hasPermission(Permission.MEMBER_ENTITYLIMIT_CHECK)) {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
        Region region = AdvancedRegionMarket.getRegionManager().getRegionbyNameAndWorldCommands(args[1], player.getWorld().getName());

        if(region == null) {
            throw new InputException(player, Messages.REGION_DOES_NOT_EXIST);
        }

        if(!region.getRegion().hasMember(player.getUniqueId()) && !region.getRegion().hasOwner(player.getUniqueId()) && !player.hasPermission(Permission.ADMIN_ENTITYLIMIT_CHECK)) {
            throw new InputException(player, Messages.NOT_A_MEMBER_OR_OWNER);
        }
        List<Entity> entities = region.getFilteredInsideEntities(false, true, true, true, true, true, false, false, false);

        player.sendMessage(region.getConvertedMessage(Messages.ENTITYLIMIT_CHECK_HEADLINE));
        String totalstatus = region.getEntityLimitGroup().getConvertedMessage(Messages.ENTITYLIMIT_CHECK_PATTERN, entities, region.getExtraTotalEntitys());

        if((region.getEntityLimitGroup().getSoftLimit(region.getExtraTotalEntitys()) < region.getEntityLimitGroup().getHardLimit()) && !region.isSubregion()) {
            totalstatus = totalstatus.replace("%entityextensioninfo%", region.getEntityLimitGroup().getConvertedMessage(Messages.ENTITYLIMIT_CHECK_EXTENSION_INFO, entities, region.getExtraTotalEntitys()));
        } else {
            totalstatus = totalstatus.replace("%entityextensioninfo%", "");
        }
        player.sendMessage(totalstatus);
        for(EntityLimit entityLimit : region.getEntityLimitGroup().getEntityLimits()) {
            String entitystatus = entityLimit.getConvertedMessage(Messages.ENTITYLIMIT_CHECK_PATTERN, entities, region.getExtraEntityAmount(entityLimit.getLimitableEntityType()));
            if((entityLimit.getSoftLimit(region.getExtraEntityAmount(entityLimit.getLimitableEntityType())) < entityLimit.getHardLimit()) && !region.isSubregion()) {
                entitystatus = entitystatus.replace("%entityextensioninfo%", entityLimit.getConvertedMessage(Messages.ENTITYLIMIT_CHECK_EXTENSION_INFO, entities, region.getExtraEntityAmount(entityLimit.getLimitableEntityType())));
            } else {
                entitystatus = entitystatus.replace("%entityextensioninfo%", "");
            }
            player.sendMessage(entitystatus);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if (!player.hasPermission(Permission.MEMBER_ENTITYLIMIT_CHECK)) {
            return returnme;
        }

        if(args.length >= 1) {
            if(args.length == 1) {
                if (this.rootCommand.startsWith(args[0])) {
                    returnme.add(this.rootCommand);
                }
            } else if((args.length == 2) && (args[0].equalsIgnoreCase(this.rootCommand))) {
                if (this.rootCommand.startsWith(args[0])) {
                    if(player.hasPermission(Permission.ADMIN_ENTITYLIMIT_CHECK)) {
                        returnme.addAll(AdvancedRegionMarket.getRegionManager().completeTabRegions(player, args[1], PlayerRegionRelationship.ALL, true, true));
                    } else {
                        returnme.addAll(AdvancedRegionMarket.getRegionManager().completeTabRegions(player, args[1], PlayerRegionRelationship.MEMBER_OR_OWNER, true, true));
                    }
                }
            }
        }
        return returnme;
    }
}
