package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.limitgroups.LimitGroup;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.ContractRegion;
import net.alex9849.arm.regions.Region;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TerminateCommand extends BasicArmCommand {
    private final String rootCommand = "terminate";
    private final String regex_with_args = "(?i)terminate [^;\n ]+ (false|true)";
    private final String regex = "(?i)terminate (false|true)";
    private final List<String> usage = new ArrayList<>(Arrays.asList("terminate [REGION] [true/false]", "terminate [true/false]"));

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
        if (Permission.hasAnyBuyPermission(sender) || sender.hasPermission(Permission.ADMIN_TERMINATE_CONTRACT)) {
            if(!(sender instanceof Player)) {
                throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
            }
            Player player = (Player) sender;
            Region region;
            boolean termination;

            if(allargs.matches(regex)) {
                region = AdvancedRegionMarket.getRegionManager().getRegionAtPositionOrNameCommand(player, "");
                termination = Boolean.parseBoolean(args[1]);
            } else {
                region = AdvancedRegionMarket.getRegionManager().getRegionAtPositionOrNameCommand(player, args[1]);
                termination = Boolean.parseBoolean(args[2]);
            }

            if(!(region instanceof ContractRegion)) {
                throw new InputException(sender, Messages.REGION_IS_NOT_A_CONTRACT_REGION);
            }

            if(!region.isSold()) {
                throw new InputException(sender, Messages.REGION_NOT_SOLD);
            }

            if(!region.getRegion().hasOwner(player.getUniqueId())) {
                if(!player.hasPermission(Permission.ADMIN_TERMINATE_CONTRACT)){
                    throw new InputException(sender, Messages.REGION_NOT_OWN);
                }
            }

            ContractRegion contractRegion = (ContractRegion) region;

            if(!termination && !LimitGroup.isInLimit(player, contractRegion)) {
                throw new InputException(player, LimitGroup.getRegionBuyOutOfLimitMessage(player, contractRegion.getRegionKind()));
            }

            contractRegion.setTerminated(termination, player);
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
                if(player.hasPermission(Permission.ADMIN_TERMINATE_CONTRACT) || Permission.hasAnyBuyPermission(player)) {
                    if(args.length == 1) {
                        returnme.add(this.rootCommand);
                    } else if(args.length == 2 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        PlayerRegionRelationship playerRegionRelationship = null;
                        if(player.hasPermission(Permission.ADMIN_TERMINATE_CONTRACT)) {
                            playerRegionRelationship = PlayerRegionRelationship.ALL;
                        } else {
                            playerRegionRelationship = PlayerRegionRelationship.OWNER;
                        }
                        returnme.addAll(AdvancedRegionMarket.getRegionManager().completeTabRegions(player, args[1], playerRegionRelationship, true,true));
                        if("true".startsWith(args[1])) {
                            returnme.add("true");
                        }
                        if("false".startsWith(args[1])) {
                            returnme.add("false");
                        }
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
