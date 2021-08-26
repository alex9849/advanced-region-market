package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.exceptions.NoPermissionException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.CountdownRegion;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.price.ContractPrice;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddTimeCommand extends BasicArmCommand {
    public AddTimeCommand(AdvancedRegionMarket plugin) {
        super(false, plugin, "addtime",
                Arrays.asList("(?i)addtime [^;\n ]+ [0-9]+(s|m|h|d)"),
                Arrays.asList("addtime [REGION] [TIME (ex.: 5d)]"),
                Arrays.asList(Permission.ADMIN_ADD_TIME));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException, NoPermissionException {
        Player player = (Player) sender;
        String[] args = command.split(" ");
        Region region = getPlugin().getRegionManager()
                .getRegionbyNameAndWorldCommands(args[1], player.getWorld().getName());

        if (region == null) {
            throw new InputException(sender, Messages.REGION_DOES_NOT_EXIST);
        }
        if(!region.isSold()) {
            throw new InputException(sender, Messages.REGION_NOT_SOLD);
        }
        if (!(region instanceof CountdownRegion)) {
            throw new InputException(sender, Messages.REGION_IS_NOT_A_RENT_OR_CONTRACTREGION);
        }
        CountdownRegion countdownRegion = (CountdownRegion) region;
        countdownRegion.extend(ContractPrice.stringToTime(args[2]));
        sender.sendMessage(Messages.PREFIX + region.replaceVariables(Messages.REGION_TIME_ADDED));
        return true;
    }

    @Override
    protected List<String> onTabCompleteArguments(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if(args.length == 2) {
            returnme.addAll(getPlugin().getRegionManager().completeTabRegions(player,
                    args[1], PlayerRegionRelationship.ALL, true, true));
        }
        if(args.length == 3) {
            if (args[2].matches("[0-9]+")) {
                returnme.add(args[3] + "s");
                returnme.add(args[3] + "m");
                returnme.add(args[3] + "h");
                returnme.add(args[3] + "d");
            }
        }
        return returnme;
    }
}
