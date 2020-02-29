package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.Diagram;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegionstatsCommand extends BasicArmCommand {
    private final String regex_with_args = "(?i)regionstats [^;\n]+";

    public RegionstatsCommand() {
        super(true, "regionstats",
                Arrays.asList("(?i)regionstats", "(?i)regionstats [^;\n]+"),
                Arrays.asList("regionstats [RegionKind/Nothing]"),
                Arrays.asList(Permission.ADMIN_REGION_STATS));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException {
        if (command.matches(regex_with_args)) {
            return Diagram.sendRegionStats(sender, command.split(" ")[1]);
        } else {
            return Diagram.sendRegionStats(sender);
        }
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        if(args.length != 2) {
            return new ArrayList<>();
        }
        List<String> returnme = new ArrayList<>();
        returnme.addAll(AdvancedRegionMarket.getInstance().getRegionKindManager().completeTabRegionKinds(args[1], ""));
        if ("rentregion".startsWith(args[1])) {
            returnme.add("rentregion");
        }
        if ("sellregion".startsWith(args[1])) {
            returnme.add("sellregion");
        }
        if ("contractregion".startsWith(args[1])) {
            returnme.add("contractregion");
        }
        return returnme;
    }

}
