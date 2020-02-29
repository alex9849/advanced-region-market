package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnsellCommand extends BasicArmCommand {
    private final String regex_with_args = "(?i)unsell [^;\n ]+";

    public UnsellCommand() {
        super(false, "unsell",
                Arrays.asList("(?i)unsell", "(?i)unsell [^;\n ]+"),
                Arrays.asList("unsell", "unsell [REGION]"),
                Arrays.asList(Permission.ADMIN_UNSELL));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        Player player = (Player) sender;

        Region region;
        if (command.matches(this.regex_with_args)) {
            region = AdvancedRegionMarket.getInstance().getRegionManager()
                    .getRegionAtPositionOrNameCommand(player, command.split(" ")[1]);
        } else {
            region = AdvancedRegionMarket.getInstance().getRegionManager().getRegionAtPositionOrNameCommand(player, "");
        }

        region.unsell(Region.ActionReason.MANUALLY_BY_ADMIN, true, false);

        player.sendMessage(Messages.PREFIX + Messages.REGION_NOW_AVIABLE);
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        if(args.length == 2) {
            return AdvancedRegionMarket.getInstance().getRegionManager()
                    .completeTabRegions(player, args[1], PlayerRegionRelationship.ALL, true, true);
        }
        return new ArrayList<>();
    }
}
