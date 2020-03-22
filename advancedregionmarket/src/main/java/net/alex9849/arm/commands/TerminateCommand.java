package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.*;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.ContractRegion;
import net.alex9849.arm.regions.Region;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TerminateCommand extends BasicArmCommand {
    private final String regex_with_args = "(?i)terminate [^;\n ]+ (false|true)";

    public TerminateCommand() {
        super(false, "terminate",
                Arrays.asList("(?i)terminate [^;\n ]+ (false|true)", "(?i)terminate (false|true)"),
                Arrays.asList("terminate [REGION] [true/false]", "terminate [true/false]"),
                Arrays.asList(Permission.MEMBER_BUY, Permission.ADMIN_TERMINATE_CONTRACT));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        Player player = (Player) sender;
        String[] args = command.split(" ");
        Region region;
        boolean termination;

        if (command.matches(regex_with_args)) {
            region = AdvancedRegionMarket.getInstance().getRegionManager().getRegionAtPositionOrNameCommand(player, args[1]);
            termination = Boolean.parseBoolean(args[2]);
        } else {
            region = AdvancedRegionMarket.getInstance().getRegionManager().getRegionAtPositionOrNameCommand(player, "");
            termination = Boolean.parseBoolean(args[1]);
        }

        if (!(region instanceof ContractRegion)) {
            throw new InputException(sender, Messages.REGION_IS_NOT_A_CONTRACT_REGION);
        }

        try {
            ((ContractRegion) region).changeTerminated(player);
        } catch (OutOfLimitExeption | NoPermissionException | NotSoldException | RegionNotOwnException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if (args.length == 2) {
            PlayerRegionRelationship playerRegionRelationship = null;
            if (player.hasPermission(Permission.ADMIN_TERMINATE_CONTRACT)) {
                playerRegionRelationship = PlayerRegionRelationship.ALL;
            } else {
                playerRegionRelationship = PlayerRegionRelationship.OWNER;
            }
            returnme.addAll(AdvancedRegionMarket.getInstance().getRegionManager()
                    .completeTabRegions(player, args[1], playerRegionRelationship, true, true));
            if ("true".startsWith(args[1])) {
                returnme.add("true");
            }
            if ("false".startsWith(args[1])) {
                returnme.add("false");
            }

        } else if (args.length == 3) {
            if ("true".startsWith(args[2])) {
                returnme.add("true");
            }
            if ("false".startsWith(args[2])) {
                returnme.add("false");
            }
        }
        return returnme;
    }
}
