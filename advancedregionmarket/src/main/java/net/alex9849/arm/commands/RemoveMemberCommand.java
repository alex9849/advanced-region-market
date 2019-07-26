package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import net.alex9849.exceptions.InputException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemoveMemberCommand extends BasicArmCommand {

    private final String rootCommand = "removemember";
    private final String regex = "(?i)removemember [^;\n ]+";
    private final String regex_with_args = "(?i)removemember [^;\n ]+ [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("removemember [REGION] [MEMBER]", "removemember [MEMBER]"));

    @Override
    public boolean matchesRegex(String command) {
        return command.matches(this.regex) || command.matches(this.regex_with_args);
    }

    @Override
    public String getRootCommand() {
        return this.rootCommand;
    }

    @Override
    public List<String> getUsage() {
        return this.usage;
    }

    @Override
    public boolean runCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args, String allargs) throws InputException {
        if (!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;

        Region region;
        OfflinePlayer removemember;
        if(allargs.matches(this.regex)) {
            region = AdvancedRegionMarket.getRegionManager().getRegionAtPositionOrNameCommand(player, "");
            removemember = Bukkit.getOfflinePlayer(args[1]);
        } else {
            region = AdvancedRegionMarket.getRegionManager().getRegionAtPositionOrNameCommand(player, args[1]);
            removemember = Bukkit.getOfflinePlayer(args[2]);
        }

        if(region.getRegion().hasOwner(((Player) sender).getUniqueId()) && sender.hasPermission(Permission.MEMBER_REMOVEMEMBER)) {
            if(!region.getRegion().hasMember(removemember.getUniqueId())) {
                throw new InputException(sender, Messages.REGION_REMOVE_MEMBER_NOT_A_MEMBER);
            }
            region.getRegion().removeMember(removemember.getUniqueId());
            sender.sendMessage(Messages.PREFIX + Messages.REGION_REMOVE_MEMBER_REMOVED);
            return true;
        } else if (sender.hasPermission(Permission.ADMIN_REMOVEMEMBER)){
            if(!region.getRegion().hasMember(removemember.getUniqueId())) {
                throw new InputException(sender, Messages.REGION_REMOVE_MEMBER_NOT_A_MEMBER);
            }
            region.getRegion().removeMember(removemember.getUniqueId());
            sender.sendMessage(Messages.PREFIX + Messages.REGION_REMOVE_MEMBER_REMOVED);
            return true;
        } else if (!(sender.hasPermission(Permission.MEMBER_REMOVEMEMBER))){
            throw new InputException(sender, Messages.NO_PERMISSION);
        } else {
            throw new InputException(sender, Messages.REGION_REMOVE_MEMBER_DO_NOT_OWN);
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length >= 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.ADMIN_REMOVEMEMBER) || player.hasPermission(Permission.MEMBER_REMOVEMEMBER)) {
                    if(args.length == 1) {
                        returnme.add(this.rootCommand);
                    } else if(args.length == 2 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        PlayerRegionRelationship playerRegionRelationship = null;
                        if(player.hasPermission(Permission.ADMIN_REMOVEMEMBER)) {
                            playerRegionRelationship = PlayerRegionRelationship.ALL;
                        } else {
                            playerRegionRelationship = PlayerRegionRelationship.OWNER;
                        }
                        returnme.addAll(AdvancedRegionMarket.getRegionManager().completeTabRegions(player, args[1], playerRegionRelationship, true,true));

                    }
                }
            }
        }
        return returnme;
    }
}
