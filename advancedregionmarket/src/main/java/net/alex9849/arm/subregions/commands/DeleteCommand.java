package net.alex9849.arm.subregions.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeleteCommand extends BasicArmCommand {
    private final String rootCommand = "delete";
    private final String regex = "(?i)delete [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("delete [REGION]"));

    public DeleteCommand() {
        super(false, "delete",
                Arrays.asList("(?i)delete [^;\n ]+"),
                Arrays.asList("delete [REGION]"),
                Arrays.asList(Permission.SUBREGION_DELETE_SOLD,
                        Permission.SUBREGION_DELETE_AVAILABLE));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        Player player = (Player) sender;
        Region region = AdvancedRegionMarket.getInstance().getRegionManager()
                .getRegionbyNameAndWorldCommands(command.split(" ")[1], player.getWorld().getName());

        if (region == null) {
            throw new InputException(player, Messages.REGION_DOES_NOT_EXIST);
        }

        if (!region.isSubregion()) {
            throw new InputException(sender, Messages.REGION_NOT_A_SUBREGION);
        }

        if (!region.getParentRegion().getRegion().hasOwner(player.getUniqueId())) {
            throw new InputException(sender, Messages.PARENT_REGION_NOT_OWN);
        }

        if (region.isSold() && (!player.hasPermission(Permission.SUBREGION_DELETE_SOLD))) {
            throw new InputException(player, Messages.NOT_ALLOWED_TO_REMOVE_SUB_REGION_SOLD);
        }
        if ((!region.isSold()) && (!player.hasPermission(Permission.SUBREGION_DELETE_AVAILABLE))) {
            throw new InputException(player, Messages.NOT_ALLOWED_TO_REMOVE_SUB_REGION_AVAILABLE);
        }

        region.delete(AdvancedRegionMarket.getInstance().getRegionManager());
        player.sendMessage(Messages.PREFIX + Messages.REGION_DELETED);
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        if (args.length != 2) {
            return new ArrayList<>();
        }
        return AdvancedRegionMarket.getInstance().getRegionManager()
                .completeTabRegions(player, args[1], PlayerRegionRelationship.PARENTREGION_OWNER, false, true);
    }
}
