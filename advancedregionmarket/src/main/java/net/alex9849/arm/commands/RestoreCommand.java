package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.adapters.util.TimeUtil;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.exceptions.ProtectionOfContinuanceException;
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
import java.util.stream.Collectors;

public class RestoreCommand extends BasicArmCommand {
    private final String regex_with_args = "(?i)restore [^;\n ]+";

    public RestoreCommand(AdvancedRegionMarket plugin) {
        super(false, plugin, "restore",
                Arrays.asList("(?i)restore", "(?i)restore [^;\n ]+"),
                Arrays.asList("restore", "restore [REGION]"),
                Arrays.asList(Permission.ADMIN_RESTORE, Permission.MEMBER_RESTORE));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException {
        Player player = (Player) sender;

        Region resregion;
        if (command.matches(this.regex_with_args)) {
            resregion = getPlugin().getRegionManager().getRegionAtPositionOrNameCommand(player, command.split(" ")[1]);
        } else {
            resregion = getPlugin().getRegionManager().getRegionAtPositionOrNameCommand(player, "");
        }

        if (player.hasPermission(Permission.ADMIN_RESTORE)) {
            try {
                resregion.restoreRegion(Region.ActionReason.MANUALLY_BY_ADMIN, true, false);
            } catch (SchematicNotFoundException e) {
                getPlugin().getLogger().log(Level.WARNING, resregion.replaceVariables(Messages.COULD_NOT_FIND_OR_LOAD_SCHEMATIC_LOG));
                throw new InputException(sender, Messages.SCHEMATIC_NOT_FOUND_ERROR_USER.replace("%regionid%", e.getRegion().getId()));
            } catch (ProtectionOfContinuanceException e) {
                throw new InputException(sender, Messages.REGION_RESTORE_PROTECTION_OF_CONTINUANCE_ERROR);
            }
            player.sendMessage(Messages.PREFIX + Messages.RESET_COMPLETE);
            return true;
        } else {
            if (resregion.getRegion().hasOwner(player.getUniqueId())) {
                if (!resregion.isUserRestorable()) {
                    throw new InputException(player, Messages.REGION_NOT_RESTORABLE);
                }
                if(resregion.isProtectionOfContinuance() && !player.hasPermission(Permission.MEMBER_RESTORE_PROTECTION_OF_CONTINUANCE)) {
                    throw new InputException(sender, Messages.REGION_RESTORE_PROTECTION_OF_CONTINUANCE_ERROR);
                }
                if ((new GregorianCalendar().getTimeInMillis()) >= getPlugin().getPluginSettings().getUserResetCooldown() + resregion.getLastreset()) {
                    String coolDownTime = TimeUtil.timeInMsToString(AdvancedRegionMarket.getInstance().getPluginSettings().getUserResetCooldown(), true, false);
                    List<String> lore = resregion.replaceVariables(Messages.GUI_RESET_REGION_BUTTON_LORE).stream()
                            .map(m -> m.replace("%userrestorecooldown%", coolDownTime)).collect(Collectors.toList());
                    Gui.openRegionRestoreWarning(player, resregion, lore, null);
                    return true;
                } else {
                    String message = resregion.replaceVariables(Messages.RESET_REGION_COOLDOWN_ERROR);
                    throw new InputException(player, message);
                }
            } else {
                throw new InputException(player, Messages.REGION_NOT_OWN);
            }
        }
    }

    @Override
    protected List<String> onTabCompleteArguments(Player player, String[] args) {
        if(args.length != 2) {
            return new ArrayList<>();
        }
        PlayerRegionRelationship playerRegionRelationship = null;
        if (player.hasPermission(Permission.ADMIN_RESTORE)) {
            playerRegionRelationship = PlayerRegionRelationship.ALL;
        } else {
            playerRegionRelationship = PlayerRegionRelationship.OWNER;
        }
        return getPlugin().getRegionManager().completeTabRegions(player, args[1], playerRegionRelationship, true, true);
    }
}
