package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.gui.Gui;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SellBackCommand extends BasicArmCommand {
    private final String regex_nomoney = "(?i)sellback [^;\n ]+ (?i)nomoney";

    public SellBackCommand() {
        super(false, "sellback",
                Arrays.asList("(?i)sellback [^;\n ]+", "(?i)sellback [^;\n ]+ (?i)nomoney"),
                Arrays.asList("sellback [REGION]"),
                Arrays.asList(Permission.MEMBER_SELLBACK));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException {
        Player player = (Player) sender;
        boolean noMoney = false;

        Region region = AdvancedRegionMarket.getInstance().getRegionManager()
                .getRegionbyNameAndWorldCommands(command.split(" ")[1], player.getLocation().getWorld().getName());

        if (region == null) {
            throw new InputException(player, Messages.REGION_DOES_NOT_EXIST);
        }
        if (!region.getRegion().hasOwner(player.getUniqueId())) {
            throw new InputException(player, Messages.REGION_NOT_OWN);
        }
        if (!region.isSold()) {
            throw new InputException(player, Messages.REGION_NOT_SOLD);
        }
        String confirmQuestion = Messages.SELLBACK_WARNING;
        if(command.matches(this.regex_nomoney)) {
            confirmQuestion = confirmQuestion.replace("%paybackmoney%", Double.toString(0));
            noMoney = true;
        }
        confirmQuestion = region.replaceVariables(confirmQuestion);
        player.sendMessage(Messages.PREFIX + confirmQuestion);
        Gui.openSellWarning(player, region, noMoney, false);
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if(args.length == 2) {
            returnme.addAll(AdvancedRegionMarket.getInstance().getRegionManager()
                    .completeTabRegions(player, args[1], PlayerRegionRelationship.OWNER, true, true));
        } else if(args.length == 3) {
            if("nomoney".toLowerCase().startsWith(args[2])) {
                returnme.add("nomoney");
            }
        }
        return returnme;
    }
}
