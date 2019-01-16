package net.alex9849.arm.SubRegions.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.RegionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeleteCommand extends BasicArmCommand {
    private final String rootCommand = "delete";
    private final String regex = "(?i)delete [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("delete [REGION]"));

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
        if ((!sender.hasPermission(Permission.SUBREGION_DELETE_SOLD)) && (!sender.hasPermission(Permission.SUBREGION_DELETE_AVAILABLE))) {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
        if (!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;
        Region region = RegionManager.getRegionbyNameAndWorldCommands(args[1], player.getWorld().getName());

        if (!region.isSubregion()) {
            //TODO make changeable
            throw new InputException(sender, "Region is not a subregion");
        }

        if(!region.getParentRegion().getRegion().hasOwner(player.getUniqueId())) {
            throw new InputException(sender, "You dont own the parent region of this subregion");
        }

        if(region.isSold() && (!player.hasPermission(Permission.SUBREGION_DELETE_SOLD))) {
            throw new InputException(player, "not allowed to remove sold subregions!");
        }
        if((!region.isSold()) && (!player.hasPermission(Permission.SUBREGION_DELETE_AVAILABLE))) {
            throw new InputException(player, "not allowed to remove available subregions!");
        }

        region.delete();
        player.sendMessage(Messages.PREFIX + "Region deleted!");
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length >= 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if(player.hasPermission(Permission.SUBREGION_DELETE_SOLD) || player.hasPermission(Permission.SUBREGION_DELETE_AVAILABLE)) {
                    if(args.length == 1) {
                        returnme.add(this.rootCommand);
                    } else if(args.length == 2 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        returnme.addAll(RegionManager.completeTabRegions(player, args[1], PlayerRegionRelationship.PARENTREGION_OWNER,false, true));
                    }
                }
            }
        }
        return returnme;
    }
}
