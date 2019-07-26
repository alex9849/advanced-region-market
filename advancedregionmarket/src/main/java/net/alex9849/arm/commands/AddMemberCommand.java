package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.handler.CommandHandler;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import net.alex9849.exceptions.InputException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddMemberCommand extends BasicArmCommand {

    private final String rootCommand = "addmember";
    private final String regex_with_args = "(?i)addmember [^;\n ]+ [^;\n ]+";
    private final String regex = "(?i)addmember [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("addmember [REGION] [NEWMEMBER]", "addmember [NEWMEMBER]"));

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
        Player addPlayer;

        if(allargs.matches(this.regex)) {
            region = AdvancedRegionMarket.getRegionManager().getRegionAtPositionOrNameCommand(player, "");
            addPlayer = Bukkit.getPlayer(args[1]);
        } else {
            region = AdvancedRegionMarket.getRegionManager().getRegionAtPositionOrNameCommand(player, args[1]);
            addPlayer = Bukkit.getPlayer(args[2]);
        }

        if(addPlayer == null) {
            throw new InputException(player, Messages.REGION_ADD_MEMBER_NOT_ONLINE);
        }

        if(region.getRegion().hasOwner(player.getUniqueId()) && player.hasPermission(Permission.MEMBER_ADDMEMBER)) {
            region.getRegion().addMember(addPlayer.getUniqueId());
            player.sendMessage(Messages.PREFIX + Messages.REGION_ADD_MEMBER_ADDED);
        } else if (player.hasPermission(Permission.ADMIN_ADDMEMBER)){
            region.getRegion().addMember(addPlayer.getUniqueId());
            player.sendMessage(Messages.PREFIX + Messages.REGION_ADD_MEMBER_ADDED);
        } else if (!(player.hasPermission(Permission.MEMBER_ADDMEMBER))){
            throw new InputException(player, Messages.NO_PERMISSION);
        } else {
            throw new InputException(player, Messages.REGION_ADD_MEMBER_DO_NOT_OWN);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length >= 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.ADMIN_ADDMEMBER) || player.hasPermission(Permission.MEMBER_ADDMEMBER)) {
                    if(args.length == 1) {
                        returnme.add(this.rootCommand);
                    } else if(args.length == 2 && args[0].equalsIgnoreCase(this.rootCommand)) {
                        PlayerRegionRelationship playerRegionRelationship = null;
                        if(player.hasPermission(Permission.ADMIN_ADDMEMBER)) {
                            playerRegionRelationship = PlayerRegionRelationship.ALL;
                        } else {
                            playerRegionRelationship = PlayerRegionRelationship.OWNER;
                        }
                        returnme.addAll(AdvancedRegionMarket.getRegionManager().completeTabRegions(player, args[1], playerRegionRelationship,true, true));
                        returnme.addAll(CommandHandler.tabCompleteOnlinePlayers(args[1]));
                    } else if(args.length == 3 && args[0].equalsIgnoreCase(this.rootCommand)) {
                        returnme.addAll(CommandHandler.tabCompleteOnlinePlayers(args[2]));
                    }
                }
            }
        }
        return returnme;
    }
}
