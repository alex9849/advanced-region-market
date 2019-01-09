package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.minifeatures.teleporter.Teleporter;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.RegionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TPCommand extends BasicArmCommand {

    private final String rootCommand = "tp";
    private final String regex = "(?i)tp [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("tp [REGION]"));

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
        if (!sender.hasPermission(Permission.ADMIN_TP) && !sender.hasPermission(Permission.MEMBER_TP)) {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
        if (!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;
        Region region = RegionManager.getRegionbyNameAndWorldCommands(args[1], player.getWorld().getName());

        if (region == null) {
            throw new InputException(sender, Messages.REGION_DOES_NOT_EXIST);
        }

        if(!region.getRegion().hasMember(player.getUniqueId()) && !region.getRegion().hasOwner(player.getUniqueId())){
            if(!player.hasPermission(Permission.ADMIN_TP)){
                throw new InputException(sender, Messages.NOT_A_MEMBER_OR_OWNER);
            }
        }

        Teleporter.teleport(player, region);
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length >= 1) {
            if(this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.ADMIN_TP) || player.hasPermission(Permission.MEMBER_TP)) {
                    if(args.length == 1) {
                        returnme.add(this.rootCommand);
                    } else if(args.length == 2 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        PlayerRegionRelationship playerRegionRelationship = null;
                        if(player.hasPermission(Permission.ADMIN_TP)) {
                            playerRegionRelationship = PlayerRegionRelationship.ALL;
                        } else {
                            playerRegionRelationship = PlayerRegionRelationship.MEMBER_OR_OWNER;
                        }
                        returnme.addAll(RegionManager.completeTabRegions(player, args[1], playerRegionRelationship));
                    }
                }
            }
        }
        return returnme;
    }
}
