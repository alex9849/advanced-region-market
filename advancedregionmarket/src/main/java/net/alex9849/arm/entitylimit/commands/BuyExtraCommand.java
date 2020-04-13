package net.alex9849.arm.entitylimit.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.entitylimit.EntityLimit;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BuyExtraCommand extends BasicArmCommand {
    private final String rootCommand = "buyextra";
    private final String regex = "(?i)buyextra [^;\n ]+ [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("buyextra [REGION] [ENTITYTYPE]"));

    public BuyExtraCommand() {
        super(false, "buyextra",
                Arrays.asList("(?i)buyextra [^;\n ]+ [^;\n ]+"),
                Arrays.asList("buyextra [REGION] [ENTITYTYPE]"),
                Arrays.asList(Permission.MEMBER_ENTITYLIMIT_BUY_EXTRA));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        String[] args = command.split(" ");
        Player player = (Player) sender;
        Region region = AdvancedRegionMarket.getInstance().getRegionManager()
                .getRegionbyNameAndWorldCommands(args[1], player.getWorld().getName());

        if (region == null) {
            throw new InputException(player, Messages.REGION_DOES_NOT_EXIST);
        }

        if (!(region.getRegion().hasOwner(player.getUniqueId()) || region.getRegion().hasMember(player.getUniqueId()))) {
            throw new InputException(player, Messages.REGION_NOT_OWN);
        }

        if (region.isSubregion()) {
            throw new InputException(player, Messages.ENTITYLIMITGROUP_EXTRA_ENTITIES_BUY_SUBREGION_ERROR);
        }

        List<Entity> entities = region.getFilteredInsideEntities(false, true,
                true, true, true, true,
                false, false, false);

        if (args[2].equalsIgnoreCase("total")) {

            if (region.getEntityLimitGroup().getHardLimit() <= region.getEntityLimitGroup().getSoftLimit(region.getExtraTotalEntitys())) {
                throw new InputException(player, region.getEntityLimitGroup().replaceVariables(Messages.ENTITYLIMITGROUP_EXTRA_ENTITIES_HARDLIMIT_REACHED, entities, region.getExtraTotalEntitys()));
            }
            if (AdvancedRegionMarket.getInstance().getEcon().getBalance(player) < region.getEntityLimitGroup().getPricePerExtraEntity()) {
                throw new InputException(player, Messages.NOT_ENOUGH_MONEY);
            }
            AdvancedRegionMarket.getInstance().getEcon().withdrawPlayer(player, region.getEntityLimitGroup().getPricePerExtraEntity());
            region.setExtraTotalEntitys(region.getExtraTotalEntitys() + 1);
            player.sendMessage(Messages.PREFIX + region.getEntityLimitGroup().replaceVariables(Messages.ENTITYLIMITGROUP_EXTRA_ENTITIES_EXPAND_SUCCESS, entities, region.getExtraTotalEntitys()));

        } else {

            EntityLimit.LimitableEntityType limitableEntityType = EntityLimit.getLimitableEntityType(args[2]);
            if (limitableEntityType == null) {
                throw new InputException(player, Messages.ENTITYTYPE_DOES_NOT_EXIST.replace("%entitytype%", args[2]));
            }
            EntityLimit entityLimit = region.getEntityLimitGroup().getEntityLimit(limitableEntityType);
            if (entityLimit == null) {
                throw new InputException(player, region.getEntityLimitGroup().replaceVariables(Messages.ENTITYLIMITGROUP_ENTITYLIMIT_ALREADY_UNLIMITED, entities, region.getExtraEntityAmount(limitableEntityType)));
            }
            if (region.getEntityLimitGroup().getHardLimit(limitableEntityType) <= region.getEntityLimitGroup().getSoftLimit(limitableEntityType, region.getExtraEntityAmount(limitableEntityType))) {
                throw new InputException(player, entityLimit.replaceVariables(Messages.ENTITYLIMITGROUP_EXTRA_ENTITIES_HARDLIMIT_REACHED, entities, region.getExtraEntityAmount(limitableEntityType)));
            }
            if (AdvancedRegionMarket.getInstance().getEcon().getBalance(player) < region.getEntityLimitGroup().getPricePerExtraEntity(limitableEntityType)) {
                throw new InputException(player, Messages.NOT_ENOUGH_MONEY);
            }
            AdvancedRegionMarket.getInstance().getEcon().withdrawPlayer(player, region.getEntityLimitGroup().getPricePerExtraEntity(limitableEntityType));
            region.setExtraEntityAmount(limitableEntityType, region.getExtraEntityAmount(limitableEntityType) + 1);
            player.sendMessage(Messages.PREFIX + entityLimit.replaceVariables(Messages.ENTITYLIMITGROUP_EXTRA_ENTITIES_EXPAND_SUCCESS, entities, region.getExtraEntityAmount(limitableEntityType)));
        }

        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if ((args.length == 2)) {
            if (this.rootCommand.startsWith(args[0])) {
                returnme.addAll(AdvancedRegionMarket.getInstance().getRegionManager().completeTabRegions(player, args[1], PlayerRegionRelationship.OWNER, true, false));

            }
        } else if ((args.length == 3)) {
            for (EntityLimit.LimitableEntityType entityType : EntityLimit.entityTypes) {
                if (entityType.toString().toLowerCase().startsWith(args[2])) {
                    Region region = AdvancedRegionMarket.getInstance().getRegionManager().getRegionbyNameAndWorldCommands(args[1], player.getWorld().getName());
                    if (region != null) {
                        if (region.getEntityLimitGroup().containsLimit(entityType)) {
                            returnme.add(entityType.toString());
                        }
                    } else {
                        returnme.add(entityType.toString());
                    }
                }
            }
            if ("total".startsWith(args[2])) {
                returnme.add("total");
            }
        }
        return returnme;
    }
}