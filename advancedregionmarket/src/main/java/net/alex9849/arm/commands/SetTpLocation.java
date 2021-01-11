package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.exceptions.NoPermissionException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.minifeatures.teleporter.Teleporter;
import net.alex9849.arm.regions.Region;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetTpLocation extends BasicArmCommand {
    private static final String regex = "(?)settplocation";
    private static final String regex_delete = "(?i)settplocation (?i)delete";
    private static final String regex_with_args = "(?i)settplocation [^;\n ]+";
    private static final String regex_with_args_delete = "(?i)settplocation [^;\n ]+ (?i)delete";


    public SetTpLocation(AdvancedRegionMarket plugin) {
        super(false, plugin, "settplocation",
                Arrays.asList(regex, regex_delete, regex_with_args, regex_with_args_delete),
                Arrays.asList("settplocation delete", "settplocation"),
                Arrays.asList(Permission.MEMBER_SET_TP_LOCATION, Permission.ADMIN_SET_TP_LOCATION));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException, NoPermissionException {
        Player player = (Player) sender;

        Location location = null;
        Region region;
        if (command.matches(regex)) {
            location = player.getLocation();
            region = getPlugin().getRegionManager()
                    .getRegionAtPositionOrNameCommand(player, null);
        } else if (command.matches(regex_delete)) {
            region = getPlugin().getRegionManager()
                    .getRegionAtPositionOrNameCommand(player, null);
        } else if (command.matches(regex_with_args)) {
            location = player.getLocation();
            region = getPlugin().getRegionManager()
                    .getRegionAtPositionOrNameCommand(player, command.split(" ")[1]);
        } else {
            region = getPlugin().getRegionManager()
                    .getRegionAtPositionOrNameCommand(player, command.split(" ")[1]);
        }

        if (!region.getRegion().hasOwner(player.getUniqueId()) && !player.hasPermission(Permission.ADMIN_SET_TP_LOCATION)) {
            throw new InputException(player, Messages.REGION_NOT_OWN);
        }

        if (location != null && !region.getRegion()
                .contains(location.getBlockX(), location.getBlockY(), location.getBlockZ())) {
            throw new InputException(sender, Messages.TELEPORT_LOCATION_HAS_TO_BE_INSIDE_REGION);
        }
        if (location != null && !Teleporter.isSaveTeleport(location)) {
            throw new InputException(sender, Messages.TELEPORT_LOCATION_IS_UNSAFE);
        }
        region.setPlayerTeleportLocation(location);
        player.sendMessage(Messages.PREFIX + Messages.TELEPORT_LOCATION_UPDATED);
        return true;
    }

    @Override
    protected List<String> onTabCompleteArguments(Player player, String[] args) {
        List<String> returnMe = new ArrayList<>();
        if (args.length == 2) {
            PlayerRegionRelationship prr = PlayerRegionRelationship.OWNER;
            if (player.hasPermission(Permission.ADMIN_SET_TP_LOCATION)) {
                prr = PlayerRegionRelationship.ALL;
            }
            returnMe.addAll(getPlugin().getRegionManager().completeTabRegions(player, args[1], prr, true, true));
            if("delete".startsWith(args[1])) {
                returnMe.add("delete");
            }
        }
        if (args.length == 3 && "delete".startsWith(args[2])) {
            returnMe.add("delete");
        }
        return returnMe;
    }
}
