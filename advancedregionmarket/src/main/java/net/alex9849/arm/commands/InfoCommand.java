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
    private final String regex_with_args = "(?i)info [^;\n ]+";

    public InfoCommand() {
        super(false, "info",
                Arrays.asList("(?i)info", "(?i)info [^;\n ]+"),
                Arrays.asList("info [REGION]", "info"),
                Arrays.asList(Permission.MEMBER_INFO, Permission.ADMIN_INFO));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException {
        Player player = (Player) sender;

        Region selectedRegion;
        if (command.matches(this.regex_with_args)) {
            selectedRegion = AdvancedRegionMarket.getInstance().getRegionManager()
                    .getRegionAtPositionOrNameCommand(player, command.split(" ")[1]);
        } else {
            selectedRegion = AdvancedRegionMarket.getInstance()
                    .getRegionManager().getRegionAtPositionOrNameCommand(player, "");
        }
        selectedRegion.regionInfo(player);
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        if(args.length != 2) {
            return new ArrayList<>();
        }
        return AdvancedRegionMarket.getInstance().getRegionManager()
                .completeTabRegions(player, args[1], PlayerRegionRelationship.ALL, true, true);
    }
}
