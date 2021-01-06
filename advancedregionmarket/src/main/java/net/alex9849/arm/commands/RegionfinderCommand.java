package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.gui.Gui;
import net.alex9849.arm.regionkind.RegionKind;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegionfinderCommand extends BasicArmCommand {
    private final String regex_with_args = "(?i)regionfinder [^;\n ]+";

    public RegionfinderCommand() {
        super(false, "regionfinder",
                Arrays.asList("(?i)regionfinder", "(?i)regionfinder [^;\n ]+"),
                Arrays.asList("regionfinder", "regionfinder [REGIONKIND]"),
                Arrays.asList(Permission.MEMBER_REGIONFINDER));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        Player player = (Player) sender;
        if(command.matches(regex_with_args)) {
            RegionKind regionKind = AdvancedRegionMarket.getInstance()
                    .getRegionKindManager().getRegionKind(command.split(" ")[1]);
            Gui.openRegionFinderSellTypeSelector(player, AdvancedRegionMarket.getInstance()
                    .getRegionManager().getBuyableRegions(regionKind), null);
        } else {
            Gui.openRegionFinder(player, false);
        }
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        if(args.length != 2) {
            return new ArrayList<>();
        }
        return AdvancedRegionMarket.getInstance().getRegionKindManager().completeTabRegionKinds(args[1], "");
    }
}
