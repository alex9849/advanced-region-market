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

    public SellBackCommand() {
        super(false, "sellback",
                Arrays.asList("(?i)sellback [^;\n ]+"),
                Arrays.asList("sellback [REGION]"),
                Arrays.asList(Permission.MEMBER_SELLBACK));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException {
        Player player = (Player) sender;

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
        String confirmQuestion = region.getConvertedMessage(Messages.SELLBACK_WARNING);
        player.sendMessage(Messages.PREFIX + confirmQuestion);
        Gui.openSellWarning(player, region, false);
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        if(args.length != 2) {
            return new ArrayList<>();
        }
        return AdvancedRegionMarket.getInstance().getRegionManager()
                .completeTabRegions(player, args[1], PlayerRegionRelationship.OWNER, true, true);
    }
}
