package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.handler.CommandHandler;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddMemberCommand extends BasicArmCommand {

    private final String regex_with_args = "(?i)addmember [^;\n ]+ [^;\n ]+";

    public AddMemberCommand() {
        super(false, "addmember",
                Arrays.asList("(?i)addmember [^;\n ]+ [^;\n ]+", "(?i)addmember [^;\n ]+"),
                Arrays.asList("addmember [REGION] [NEWMEMBER]", "addmember [NEWMEMBER]"),
                Arrays.asList(Permission.MEMBER_ADDMEMBER, Permission.ADMIN_ADDMEMBER));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException {
        //No console command sender must be a player
        Player player = (Player) sender;
        Region region;
        Player addPlayer;
        String[] args = command.split(" ");

        if (command.matches(this.regex_with_args)) {
            region = AdvancedRegionMarket.getInstance().getRegionManager().getRegionAtPositionOrNameCommand(player, args[1]);
            addPlayer = Bukkit.getPlayer(args[2]);
        } else {
            region = AdvancedRegionMarket.getInstance().getRegionManager().getRegionAtPositionOrNameCommand(player, "");
            addPlayer = Bukkit.getPlayer(args[1]);
        }

        if (addPlayer == null) {
            throw new InputException(player, Messages.REGION_ADD_MEMBER_NOT_ONLINE);
        }

        if (region.getRegion().hasOwner(player.getUniqueId())
                && player.hasPermission(Permission.MEMBER_ADDMEMBER)
                && !player.hasPermission(Permission.ADMIN_ADDMEMBER)) {
            if(region.getRegion().getMembers().size() + 1 > region.getMaxMembers() && region.getMaxMembers() != -1) {
                throw new InputException(player, region.replaceVariables(Messages.ADD_MEMBER_MAX_MEMBERS_EXCEEDED));
            }
            region.getRegion().addMember(addPlayer.getUniqueId());
            player.sendMessage(Messages.PREFIX + Messages.REGION_ADD_MEMBER_ADDED);

        } else if (player.hasPermission(Permission.ADMIN_ADDMEMBER)) {
            region.getRegion().addMember(addPlayer.getUniqueId());
            player.sendMessage(Messages.PREFIX + Messages.REGION_ADD_MEMBER_ADDED);

        } else if (!(player.hasPermission(Permission.MEMBER_ADDMEMBER))) {
            throw new InputException(player, Messages.NO_PERMISSION);

        } else {
            throw new InputException(player, Messages.REGION_NOT_OWN);

        }

        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if (args.length == 2) {
            PlayerRegionRelationship playerRegionRelationship = null;
            if (player.hasPermission(Permission.ADMIN_ADDMEMBER)) {
                playerRegionRelationship = PlayerRegionRelationship.ALL;
            } else {
                playerRegionRelationship = PlayerRegionRelationship.OWNER;
            }
            returnme.addAll(AdvancedRegionMarket.getInstance().getRegionManager()
                    .completeTabRegions(player, args[1], playerRegionRelationship, true, true));
            returnme.addAll(CommandHandler.tabCompleteOnlinePlayers(args[1]));

        } else if (args.length == 3) {
            returnme.addAll(CommandHandler.tabCompleteOnlinePlayers(args[2]));
        }

        return returnme;
    }
}
