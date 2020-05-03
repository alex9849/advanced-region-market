package net.alex9849.arm.regionkind.regionkindcommands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.Region;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeleteCommand extends BasicArmCommand {

    public DeleteCommand() {
        super(true, "delete",
                Arrays.asList("(?i)delete [^;\n ]+"),
                Arrays.asList("delete [REGIONKIND]"),
                Arrays.asList(Permission.REGIONKIND_DELETE));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        String[] args = command.split(" ");
        RegionKind regionKind = AdvancedRegionMarket.getInstance().getRegionKindManager().getRegionKind(args[1]);
        if (regionKind == null) {
            throw new InputException(sender, Messages.REGIONKIND_DOES_NOT_EXIST);
        }
        if (regionKind == RegionKind.DEFAULT) {
            throw new InputException(sender, Messages.REGIONKIND_CAN_NOT_REMOVE_SYSTEM);
        }
        if (regionKind == RegionKind.SUBREGION) {
            throw new InputException(sender, Messages.REGIONKIND_CAN_NOT_REMOVE_SYSTEM);
        }

        AdvancedRegionMarket.getInstance().getRegionKindManager().remove(regionKind);

        for (Region region : AdvancedRegionMarket.getInstance().getRegionManager()) {
            if (region.getRegionKind() == regionKind) {
                region.setRegionKind(RegionKind.DEFAULT);
            }
        }
        sender.sendMessage(Messages.PREFIX + Messages.REGIONKIND_DELETED);
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        if(args.length != 2) {
            return new ArrayList<>();
        }
        return AdvancedRegionMarket.getInstance().getRegionKindManager().completeTabRegionKinds(args[1], "");
    }
}
