package net.alex9849.arm.subregions.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.exceptions.ProtectionOfContinuanceException;
import net.alex9849.arm.exceptions.SchematicNotFoundException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class RestoreCommand extends BasicArmCommand {

    public RestoreCommand(AdvancedRegionMarket plugin) {
        super(true, plugin, "restore",
                Arrays.asList("(?i)restore [^;\n ]+"),
                Arrays.asList("restore [REGION]"),
                Arrays.asList(Permission.SUBREGION_RESTORE));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        Player player = (Player) sender;
        Region region = getPlugin().getRegionManager()
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

        if (!region.isUserRestorable()) {
            throw new InputException(sender, Messages.REGION_NOT_RESTORABLE);
        }
        if (region.isProtectionOfContinuance() && !player.hasPermission(Permission.MEMBER_RESTORE_PROTECTION_OF_CONTINUANCE)) {
            throw new InputException(sender, Messages.REGION_RESTORE_PROTECTION_OF_CONTINUANCE_ERROR);
        }

        try {
            region.setProtectionOfContinuance(false);
            region.restoreRegion(Region.ActionReason.MANUALLY_BY_PARENT_REGION_OWNER, true, false);
            sender.sendMessage(Messages.PREFIX + Messages.COMPLETE);
        } catch (SchematicNotFoundException e) {
            getPlugin().getLogger()
                    .log(Level.WARNING, region.replaceVariables(Messages.COULD_NOT_FIND_OR_LOAD_SCHEMATIC_LOG));
            throw new InputException(sender, Messages.SCHEMATIC_NOT_FOUND_ERROR_USER.replace("%regionid%", e.getRegion().getId()));
        } catch (ProtectionOfContinuanceException e) {
            throw new InputException(sender, Messages.REGION_RESTORE_PROTECTION_OF_CONTINUANCE_ERROR);
        }
        return true;
    }

    @Override
    protected List<String> onTabCompleteArguements(Player player, String[] args) {
        if(args.length != 2) {
            return new ArrayList<>();
        }
        return getPlugin().getRegionManager()
                .completeTabRegions(player, args[1], PlayerRegionRelationship.PARENTREGION_OWNER, false, true);
    }
}