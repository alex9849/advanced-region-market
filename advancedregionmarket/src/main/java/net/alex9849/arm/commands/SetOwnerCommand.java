package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.handler.CommandHandler;
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

public class SetOwnerCommand extends BasicArmCommand {

    private final String rootCommand = "setowner";
    private final String regex = "(?i)setowner [^;\n ]+ [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("setowner [REGION] [PLAYER]"));

    @Override
    public boolean matchesRegex(String command) {
        return command.matches(this.regex);
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
        if(!sender.hasPermission(Permission.ADMIN_SETOWNER)) {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }

        if(!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player playersender = (Player) sender;
        Region region = AdvancedRegionMarket.getRegionManager().getRegionbyNameAndWorldCommands(args[1], playersender.getWorld().getName());
        if (region == null) {
            throw new InputException(sender, Messages.REGION_DOES_NOT_EXIST);
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(args[2]);

        if (player == null) {
            throw new InputException(sender, "Player not found!");
        }

        region.setSold(player);
        sender.sendMessage(Messages.PREFIX + "Owner set!");
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length >= 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.ADMIN_SETOWNER)) {
                    if(args.length == 1) {
                        returnme.add(this.rootCommand);
                    } else if(args.length == 2 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        PlayerRegionRelationship playerRegionRelationship = null;
                        if(player.hasPermission(Permission.ADMIN_SETOWNER)) {
                            playerRegionRelationship = PlayerRegionRelationship.ALL;
                        } else {
                            playerRegionRelationship = PlayerRegionRelationship.OWNER;
                        }
                        returnme.addAll(AdvancedRegionMarket.getRegionManager().completeTabRegions(player, args[1], playerRegionRelationship, true,true));
                    } else if(args.length == 3 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        returnme.addAll(CommandHandler.tabCompleteOnlinePlayers(args[2]));
                    }
                }
            }
        }
        return returnme;
    }
}
