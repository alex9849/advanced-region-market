package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.*;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.RentRegion;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExtendCommand extends BasicArmCommand {
    private final String regex_with_args = "(?i)extend [^;\n ]+";

    public ExtendCommand() {
        super(false, "extend",
                Arrays.asList("(?i)extend [^;\n ]+", "(?i)extend"),
                Arrays.asList("extend [REGION]", "extend"),
                Arrays.asList(Permission.MEMBER_BUY));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException {
        Player player = (Player) sender;
        Region region;

        if (command.matches(this.regex_with_args)) {
            region = AdvancedRegionMarket.getInstance().getRegionManager()
                    .getRegionAtPositionOrNameCommand(player, command.split(" ")[1]);
        } else {
            region = AdvancedRegionMarket.getInstance().getRegionManager()
                    .getRegionAtPositionOrNameCommand(player, null);
        }

        if (!(region instanceof RentRegion)) {
            throw new InputException(sender, Messages.REGION_IS_NOT_A_RENTREGION);
        }

        try {
            ((RentRegion) region).extend(player);
        } catch (NoPermissionException | NotEnoughMoneyException
                | RegionNotOwnException | NotSoldException e) {
            if (e.hasMessage()) player.sendMessage(Messages.PREFIX + e.getMessage());
        }

        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        if(args.length != 2) {
            return new ArrayList<>();
        }

        PlayerRegionRelationship playerRegionRelationship = null;
        if (player.hasPermission(Permission.ADMIN_EXTEND)) {
            playerRegionRelationship = PlayerRegionRelationship.ALL;
        } else {
            playerRegionRelationship = PlayerRegionRelationship.OWNER;
        }
        return AdvancedRegionMarket.getInstance().getRegionManager()
                .completeTabRegions(player, args[1], playerRegionRelationship, true, true);
    }
}
