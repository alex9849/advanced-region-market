package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.util.UtilMethods;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeleteCommand extends BasicArmCommand {
    private final String regex_with_args = "(?i)delete [^;\n ]+";

    public DeleteCommand(AdvancedRegionMarket plugin) {
        super(false, plugin, "delete",
                Arrays.asList("(?i)delete [^;\n ]+", "(?i)delete"),
                Arrays.asList("delete [REGION]"),
                Arrays.asList(Permission.ADMIN_DELETEREGION));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException {
        Player player = (Player) sender;
        Region region;
        if (command.matches(this.regex_with_args)) {
            region = getPlugin().getRegionManager().getRegionAtPositionOrNameCommand(player, command.split(" ")[1]);
        } else {
            region = getPlugin().getRegionManager().getRegionAtPositionOrNameCommand(player, "");
        }

        region.unsell(Region.ActionReason.DELETE, false, true);
        region.delete();
        UtilMethods.deleteFilesRec(region.getRegionSchematicFolder());
        getPlugin().getRegionManager().remove(region);

        player.sendMessage(Messages.PREFIX + region.getRegion().getId() + " deleted!");
        return true;
    }

    @Override
    protected List<String> onTabCompleteArguments(Player player, String[] args) {
        if(args.length != 2) {
            return new ArrayList<>();
        }
        return getPlugin().getRegionManager()
                .completeTabRegions(player, args[1], PlayerRegionRelationship.ALL, true, true);
    }
}
