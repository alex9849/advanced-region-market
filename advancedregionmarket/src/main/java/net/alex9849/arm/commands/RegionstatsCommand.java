package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.minifeatures.Diagram;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegionstatsCommand extends BasicArmCommand {

    private final String rootCommand = "regionstats";
    private final String regex = "(?i)regionstats";
    private final String regex_with_args = "(?i)regionstats [^;\n]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("regionstats [RegionKind/Nothing]"));

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
        if (sender.hasPermission(Permission.ADMIN_REGION_STATS)) {

            if(allargs.matches(regex)) {
                return Diagram.sendRegionStats(sender);
            } else {
                return Diagram.sendRegionStats(sender, args[1]);
            }
        } else {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }

    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length >= 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.ADMIN_REGION_STATS)) {
                    if(args.length == 1) {
                        returnme.add(this.rootCommand);
                    } else if(args.length == 2 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        returnme.addAll(AdvancedRegionMarket.getRegionKindManager().completeTabRegionKinds(args[1], ""));
                        if("rentregion".startsWith(args[1])) {
                            returnme.add("rentregion");
                        }
                        if("sellregion".startsWith(args[1])) {
                            returnme.add("sellregion");
                        }
                        if("contractregion".startsWith(args[1])) {
                            returnme.add("contractregion");
                        }
                    }
                }
            }
        }
        return returnme;
    }

}
