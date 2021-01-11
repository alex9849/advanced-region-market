package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
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

    public RegionfinderCommand(AdvancedRegionMarket plugin) {
        super(false, plugin, "regionfinder",
                Arrays.asList("(?i)regionfinder", "(?i)regionfinder [^;\n ]+"),
                Arrays.asList("regionfinder", "regionfinder [REGIONKIND]"),
                Arrays.asList(Permission.MEMBER_REGIONFINDER));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        Player player = (Player) sender;
        if(command.matches(regex_with_args)) {
            RegionKind regionKind = getPlugin()
                    .getRegionKindManager().getRegionKind(command.split(" ")[1]);
            if(regionKind == null) {
                throw new InputException(player, Messages.REGIONKIND_DOES_NOT_EXIST);
            }
            if(!regionKind.isDisplayInRegionfinder()) {
                throw new InputException(player, Messages.REGIONKIND_CAN_NOT_BE_SEARCHED_IN_REGIONFINDER);
            }
            Gui.openRegionFinderSellTypeSelector(player, getPlugin()
                    .getRegionManager().getBuyableRegions(regionKind), null);
        } else {
            Gui.openRegionFinder(player, null);
        }
        return true;
    }

    @Override
    protected List<String> onTabCompleteArguments(Player player, String[] args) {
        if(args.length != 2) {
            return new ArrayList<>();
        }
        return getPlugin().getRegionKindManager().completeTabRegionKinds(args[1], "");
    }
}
