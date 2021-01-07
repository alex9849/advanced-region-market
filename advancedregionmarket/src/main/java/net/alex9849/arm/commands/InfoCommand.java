package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InfoCommand extends BasicArmCommand {
    private static final String regex = "(?)info";
    private static final String regex_with_args = "(?i)info [^;\n ]+";

    public InfoCommand(AdvancedRegionMarket plugin) {
        super(false, plugin, "info",
                Arrays.asList(regex, regex_with_args),
                Arrays.asList("info [REGION]", "info"),
                Arrays.asList(Permission.MEMBER_INFO, Permission.ADMIN_INFO));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException {
        Player player = (Player) sender;

        Region selectedRegion;
        if (command.matches(regex_with_args)) {
            selectedRegion = getPlugin().getRegionManager()
                    .getRegionAtPositionOrNameCommand(player, command.split(" ")[1]);
        } else {
            selectedRegion = getPlugin()
                    .getRegionManager().getRegionAtPositionOrNameCommand(player, "");
        }
        selectedRegion.regionInfo(player);
        return true;
    }

    @Override
    protected List<String> onTabCompleteArguments(Player player, String[] args) {
        if(args.length != 2) {
            return new ArrayList<>();
        }
        return getPlugin().getRegionManager()
                .completeTabRegions(player, args[1], PlayerRegionRelationship.ALL, true, true);
    }
}
