package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.*;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BuyCommand extends BasicArmCommand {
    private final String regex_with_args = "(?i)buy [^;\n ]+";

    public BuyCommand(AdvancedRegionMarket plugin) {
        super(false, plugin, "buy",
                Arrays.asList("(?i)buy", "(?i)buy [^;\n ]+"),
                Arrays.asList("buy [REGION]", "buy"),
                Arrays.asList(Permission.MEMBER_BUY));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException {
        Player player = (Player) sender;
        Region region;

        if (command.matches(this.regex_with_args)) {
            region = getPlugin().getRegionManager().getRegionAtPositionOrNameCommand(player, command.split(" ")[1]);
        } else {
            region = getPlugin().getRegionManager().getRegionAtPositionOrNameCommand(player, null);
        }

        try {
            region.buy(player);
        } catch (NoPermissionException | OutOfLimitExeption | NotEnoughMoneyException
                | AlreadySoldException | ProtectionOfContinuanceException e) {
            if (e.hasMessage()) player.sendMessage(Messages.PREFIX + e.getMessage());
        }

        return true;
    }

    @Override
    protected List<String> onTabCompleteArguments(Player player, String[] args) {
        if(args.length != 2) {
            return new ArrayList<>();
        }
        return getPlugin().getRegionManager().
                completeTabRegions(player, args[1], PlayerRegionRelationship.AVAILABLE, true, true);
    }
}
