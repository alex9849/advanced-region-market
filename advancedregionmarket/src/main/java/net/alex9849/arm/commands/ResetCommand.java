package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.exceptions.SchematicNotFoundException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class ResetCommand extends BasicArmCommand {
    private final String regex_with_args = "(?i)reset [^;\n ]+";

    public ResetCommand() {
        super(false, "reset",
                Arrays.asList("(?i)reset [^;\n ]+", "(?i)reset"),
                Arrays.asList("reset [REGION]", "reset"),
                Arrays.asList(Permission.ADMIN_RESETREGION));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException {
        Player player = (Player) sender;

        Region resregion;
        if (command.matches(this.regex_with_args)) {
            resregion = AdvancedRegionMarket.getInstance().getRegionManager().getRegionAtPositionOrNameCommand(player, command.split(" ")[1]);
        } else {
            resregion = AdvancedRegionMarket.getInstance().getRegionManager().getRegionAtPositionOrNameCommand(player, "");
        }

        if (resregion == null) {
            throw new InputException(sender, Messages.REGION_DOES_NOT_EXIST);
        }

        try {
            resregion.resetRegion(Region.ActionReason.MANUALLY_BY_ADMIN, true);
        } catch (SchematicNotFoundException e) {
            AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, resregion.replaceVariables(Messages.COULD_NOT_FIND_OR_LOAD_SCHEMATIC_LOG));
            player.sendMessage(Messages.PREFIX + Messages.SCHEMATIC_NOT_FOUND_ERROR_USER.replace("%regionid%", e.getRegion().getId()));
        }
        sender.sendMessage(Messages.PREFIX + Messages.REGION_NOW_AVAILABLE);
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        if(args.length != 2) {
            return new ArrayList<>();
        }
        return AdvancedRegionMarket.getInstance().getRegionManager()
                .completeTabRegions(player, args[1], PlayerRegionRelationship.ALL, true, true);
    }
}
