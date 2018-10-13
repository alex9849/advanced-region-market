package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.ContractRegion;
import net.alex9849.arm.regions.Region;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TerminateCommand extends BasicArmCommand {
    private final String rootCommand = "terminate";
    private final String regex = "(?i)terminate [^;\n ]+ (false|true)";
    private final String usage = "/arm terminate [REGION] [true/false]";

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
        if (sender.hasPermission(Permission.ARM_BUY_CONTRACTREGION) || sender.hasPermission(Permission.ADMIN_TERMINATE_CONTRACT)) {
            if(!(sender instanceof Player)) {
                throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
            }
            Player player = (Player) sender;
            Region region = Region.searchRegionbyNameAndWorld(args[1], player.getWorld().getName());
            if(region == null){
                throw new InputException(sender, Messages.REGION_DOES_NOT_EXIST);
            }
            if(!(region instanceof ContractRegion)) {
                throw new InputException(sender, Messages.REGION_IS_NOT_A_CONTRACT_REGION);
            }

            if(!region.isSold()) {
                throw new InputException(sender, Messages.REGION_NOT_SOLD);
            }

            if(!AdvancedRegionMarket.getWorldGuardInterface().hasOwner(player, region.getRegion())) {
                if(!player.hasPermission(Permission.ADMIN_TERMINATE_CONTRACT)){
                    throw new InputException(sender, Messages.REGION_NOT_OWN);
                }
            }

            ContractRegion contractRegion = (ContractRegion) region;
            contractRegion.setTerminated(Boolean.parseBoolean(args[2]), player);
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
                if(player.hasPermission(Permission.ADMIN_TERMINATE_CONTRACT) || player.hasPermission(Permission.ARM_BUY_CONTRACTREGION)) {
                    if(args.length == 1) {
                        returnme.add(this.rootCommand);
                    } else if(args.length == 2 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        PlayerRegionRelationship playerRegionRelationship = null;
                        if(player.hasPermission(Permission.ADMIN_TERMINATE_CONTRACT)) {
                            playerRegionRelationship = PlayerRegionRelationship.ALL;
                        } else {
                            playerRegionRelationship = PlayerRegionRelationship.OWNER;
                        }
                        returnme.addAll(Region.completeTabRegions(player, args[1], playerRegionRelationship));
                    } else if(args.length == 3 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        if("true".startsWith(args[2])) {
                            returnme.add("true");
                        }
                        if("false".startsWith(args[2])) {
                            returnme.add("false");
                        }
                    }
                }
            }
        }
        return returnme;
    }
}
