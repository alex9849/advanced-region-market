package net.alex9849.arm.commands;

import net.alex9849.arm.Handler.CommandHandler;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.exceptions.InputException;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.OldRegionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListRegionsCommand extends BasicArmCommand {

    private final String rootCommand = "listregions";
    private final String regex = "(?i)listregions";
    private final List<String> usage = new ArrayList<>(Arrays.asList("listregions", "listregions [PLAYER]"));
    private final String regex_with_args = "(?i)listregions [^;\n ]+";

    @Override
    public boolean matchesRegex(String command) {
        if(command.matches(this.regex)){
            return true;
        } else {
            return command.matches(this.regex_with_args);
        }
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
        if (allargs.matches(regex_with_args)) {
            return listRegionsCommand(sender, args[1]);
        } else {
            return listRegionsCommand(sender);
        }
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

    private boolean listRegionsCommand(CommandSender sender, String args) throws InputException {
        if(!(sender instanceof Player)){
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;
        if(sender.hasPermission(Permission.ADMIN_LISTREGIONS) || (player.getName().equalsIgnoreCase(args) && player.hasPermission(Permission.MEMBER_LISTREGIONS))){
            OfflinePlayer oplayer = Bukkit.getOfflinePlayer(args);
            if(oplayer == null){
                throw new InputException(sender, "Player does not exist!");
            }
            List<Region> regionsOwner = OldRegionManager.getRegionsByOwner(oplayer.getUniqueId());
            List<Region> regionsMember = OldRegionManager.getRegionsByMember(oplayer.getUniqueId());
            List<String> selectedRegionsOwner = new ArrayList<>();
            List<String> selectedRegionsMember = new ArrayList<>();

            for(Region region : regionsOwner) {
                selectedRegionsOwner.add(region.getRegion().getId());
            }

            for(Region region : regionsMember) {
                selectedRegionsMember.add(region.getRegion().getId());
            }

            String regionstring = "";
            for(int i = 0; i < selectedRegionsOwner.size() - 1; i++) {
                regionstring = regionstring + selectedRegionsOwner.get(i) + ", ";
            }
            if(selectedRegionsOwner.size() != 0){
                regionstring = regionstring + selectedRegionsOwner.get(selectedRegionsOwner.size() - 1);
            }
            sender.sendMessage(ChatColor.GOLD + "Owner: " + regionstring);

            regionstring = "";
            for(int i = 0; i < selectedRegionsMember.size() - 1; i++) {
                regionstring = regionstring + selectedRegionsMember.get(i) + ", ";
            }
            if(selectedRegionsMember.size() != 0){
                regionstring = regionstring + selectedRegionsMember.get(selectedRegionsMember.size() - 1);
            }
            sender.sendMessage(ChatColor.GOLD + "Member: " + regionstring);
            return true;


        } else {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
    }

    private boolean listRegionsCommand(CommandSender sender) throws InputException {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(player.hasPermission(Permission.MEMBER_LISTREGIONS)){
                listRegionsCommand(player, player.getName());
                return true;
            } else {
                throw new InputException(sender, Messages.NO_PERMISSION);
            }
        } else {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
    }
}
