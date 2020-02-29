package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemoveMemberCommand extends BasicArmCommand {
    private final String regex_with_args = "(?i)removemember [^;\n ]+ [^;\n ]+";

    public RemoveMemberCommand() {
        super(false, "removemember",
                Arrays.asList("(?i)removemember [^;\n ]+", "(?i)removemember [^;\n ]+ [^;\n ]+"),
                Arrays.asList("removemember [MEMBER]", "removemember [REGION] [MEMBER]"),
                Arrays.asList(Permission.MEMBER_REMOVEMEMBER, Permission.ADMIN_REMOVEMEMBER));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException {
        Player player = (Player) sender;
        String[] args = command.split(" ");

        Region region;
        OfflinePlayer removemember;
        if (command.matches(this.regex_with_args)) {
            region = AdvancedRegionMarket.getInstance().getRegionManager().getRegionAtPositionOrNameCommand(player, args[1]);
            removemember = Bukkit.getOfflinePlayer(args[2]);
        } else {
            region = AdvancedRegionMarket.getInstance().getRegionManager().getRegionAtPositionOrNameCommand(player, "");
            removemember = Bukkit.getOfflinePlayer(args[1]);
        }

        if (!(region.getRegion().hasOwner(player.getUniqueId()) || player.hasPermission(Permission.ADMIN_REMOVEMEMBER))) {
            throw new InputException(sender, Messages.REGION_NOT_OWN);
        }

        if (!region.getRegion().hasMember(removemember.getUniqueId())) {
            throw new InputException(sender, Messages.REGION_REMOVE_MEMBER_NOT_A_MEMBER);
        }

        region.getRegion().removeMember(removemember.getUniqueId());
        sender.sendMessage(Messages.PREFIX + Messages.REGION_REMOVE_MEMBER_REMOVED);
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        if(args.length != 2) {
            return new ArrayList<>();
        }
        PlayerRegionRelationship playerRegionRelationship = null;
        if (player.hasPermission(Permission.ADMIN_REMOVEMEMBER)) {
            playerRegionRelationship = PlayerRegionRelationship.ALL;
        } else {
            playerRegionRelationship = PlayerRegionRelationship.OWNER;
        }
        return AdvancedRegionMarket.getInstance().getRegionManager().completeTabRegions(player, args[1], playerRegionRelationship, true, true);

    }
}
