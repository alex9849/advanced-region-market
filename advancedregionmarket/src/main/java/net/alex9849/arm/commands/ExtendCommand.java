package net.alex9849.arm.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.RentRegion;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ExtendCommand extends BasicArmCommand {

    private final String rootCommand = "extend";
    private final String regex = "(?i)extend [^;\n ]+";
    private final String usage = "/arm extend [REGION]";

    @Override
    public boolean matchesRegex(String command) {
        return command.matches(this.regex);
    }

    @Override
    public String getRootCommand() {
        return this.rootCommand;
    }

    @Override
    public String getUsage() {
        return this.usage;
    }

    @Override
    public boolean runCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args, String allargs) throws InputException {
        if (sender.hasPermission(Permission.ARM_BUY_RENTREGION)) {
            if(!(sender instanceof Player)) {
                throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
            }
            Player player = (Player) sender;
            Region region = Region.searchRegionbyNameAndWorld(args[1], player.getWorld().getName());
            if(region == null){
                throw new InputException(sender, Messages.REGION_DOES_NOT_EXIST);
            }

            if(!(region instanceof RentRegion)) {
                throw new InputException(sender, Messages.REGION_IS_NOT_A_RENTREGION);
            }

            ((RentRegion) region).extendRegion(player);

            return true;
        } else {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length >= 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.ADMIN_EXTEND) || player.hasPermission(Permission.ARM_BUY_RENTREGION)) {
                    if(args.length == 1) {
                        returnme.add(this.rootCommand);
                    } else if(args.length == 2 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        PlayerRegionRelationship playerRegionRelationship = null;
                        if(player.hasPermission(Permission.ADMIN_EXTEND)) {
                            playerRegionRelationship = PlayerRegionRelationship.ALL;
                        } else {
                            playerRegionRelationship = PlayerRegionRelationship.OWNER;
                        }
                        returnme.addAll(Region.completeTabRegions(player, args[1], playerRegionRelationship));
                    }
                }
            }
        }
        return returnme;
    }
}
