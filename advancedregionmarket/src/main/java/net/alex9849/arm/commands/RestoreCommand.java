package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.exceptions.SchematicNotFoundException;
import net.alex9849.arm.gui.Gui;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;

public class RestoreCommand extends BasicArmCommand {
    private final String regex_with_args = "(?i)restore [^;\n ]+";

    public RestoreCommand() {
        super(false, "restore",
                Arrays.asList("(?i)restore", "(?i)restore [^;\n ]+"),
                Arrays.asList("restore", "restore [REGION]"),
                Arrays.asList(Permission.ADMIN_RESTORE, Permission.MEMBER_RESTORE));
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

        if (player.hasPermission(Permission.ADMIN_RESTORE)) {
            try {
                resregion.restoreRegion(Region.ActionReason.MANUALLY_BY_ADMIN, true, false);
            } catch (SchematicNotFoundException e) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, resregion.getConvertedMessage(Messages.COULD_NOT_FIND_OR_LOAD_SCHEMATIC_LOG));
                player.sendMessage(Messages.PREFIX + Messages.SCHEMATIC_NOT_FOUND_ERROR_USER.replace("%regionid%", e.getRegion().getId()));
            }
            player.sendMessage(Messages.PREFIX + Messages.RESET_COMPLETE);
            return true;
        } else {
            if (resregion.getRegion().hasOwner(player.getUniqueId())) {
                if (!resregion.isUserRestorable()) {
                    throw new InputException(player, Messages.REGION_NOT_RESETTABLE);
                }
                if ((new GregorianCalendar().getTimeInMillis()) >= AdvancedRegionMarket.getInstance().getPluginSettings().getUserResetCooldown() + resregion.getLastreset()) {
                    Gui.openRegionResetWarning(player, resregion, false);
                    return true;
                } else {
                    String message = resregion.getConvertedMessage(Messages.RESET_REGION_COOLDOWN_ERROR);
                    throw new InputException(player, message);
                }
            } else {
                throw new InputException(player, Messages.REGION_NOT_OWN);
            }
        }
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        if(args.length != 2) {
            return new ArrayList<>();
        }
        PlayerRegionRelationship playerRegionRelationship = null;
        if (player.hasPermission(Permission.ADMIN_RESTORE)) {
            playerRegionRelationship = PlayerRegionRelationship.ALL;
        } else {
            playerRegionRelationship = PlayerRegionRelationship.OWNER;
        }
        return AdvancedRegionMarket.getInstance().getRegionManager().completeTabRegions(player, args[1], playerRegionRelationship, true, true);
    }
}
