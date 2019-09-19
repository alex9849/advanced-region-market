package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.handler.CommandHandler;
import net.alex9849.arm.regions.Region;
import net.alex9849.exceptions.CmdSyntaxException;
import net.alex9849.exceptions.InputException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListRegionsCommand implements BasicArmCommand {
    private final String rootCommand = "listregions";
    private final String regex = "(?i)listregions";
    private final List<String> usage = new ArrayList<>(Arrays.asList("listregions", "listregions [PLAYER]"));
    private final String regex_with_args = "(?i)listregions [^;\n ]+";

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
    public boolean runCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args, String allargs) throws InputException, CmdSyntaxException {

        if(allargs.matches(this.regex)) {
            if(sender.hasPermission(Permission.MEMBER_LISTREGIONS) || sender.hasPermission(Permission.ADMIN_LISTREGIONS)) {
                if(!(sender instanceof Player)) {
                    throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
                }
                Player player = (Player) sender;
                listRegionsCommand(sender, player.getName());
                return true;
            } else {
                throw new InputException(sender, Messages.NO_PERMISSION);
            }
        } else if (allargs.matches(this.regex_with_args)) {
            if(sender.hasPermission(Permission.ADMIN_LISTREGIONS)) {
                listRegionsCommand(sender, args[1]);
            } else {
                throw new InputException(sender, Messages.NO_PERMISSION);
            }
        }

        return true;
    }

    private void listRegionsCommand(CommandSender sender, String playerName) throws InputException {
        OfflinePlayer oplayer = Bukkit.getOfflinePlayer(playerName);
        if(oplayer == null) {
            throw new InputException(sender, Messages.PLAYER_NOT_FOUND);
        }

        List<Region> regionsOwner = AdvancedRegionMarket.getInstance().getRegionManager().getRegionsByOwner(oplayer.getUniqueId());
        List<Region> regionsMember = AdvancedRegionMarket.getInstance().getRegionManager().getRegionsByMember(oplayer.getUniqueId());

        String regionstring = "";
        if(regionsOwner.size() > 0) {
            regionstring = regionsOwner.get(0).getRegion().getId();
        }
        for(int i = 1; i < regionsOwner.size(); i++) {
            regionstring += ", " + regionsOwner.get(i).getRegion().getId();
        }

        sender.sendMessage(ChatColor.GOLD + "Owner: " + regionstring);

        regionstring = "";
        if(regionsMember.size() > 0) {
            regionstring = regionsMember.get(0).getRegion().getId();
        }
        for(int i = 1; i < regionsMember.size(); i++) {
            regionstring += ", " + regionsMember.get(i).getRegion().getId();
        }

        sender.sendMessage(ChatColor.GOLD + "Member: " + regionstring);


    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length >= 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.ADMIN_LISTREGIONS) || player.hasPermission(Permission.MEMBER_LISTREGIONS)) {
                    if(args.length == 1) {
                        returnme.add(this.rootCommand);
                    } else if(args.length == 2 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        if(player.hasPermission(Permission.ADMIN_LISTREGIONS)) {
                            returnme.addAll(CommandHandler.tabCompleteOnlinePlayers(args[1]));
                        }
                    }
                }
            }
        }
        return returnme;

    }
}
