package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regionkind.RegionKind;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TpToFreeRegion extends BasicArmCommand {

    public TpToFreeRegion() {
        super(false, "tptofreeregion",
                Arrays.asList("(?i)tptofreeregion [^;\n ]+"),
                Arrays.asList("tptofreeregion [REGIONKIND]"),
                Arrays.asList(Permission.MEMBER_TP_TO_FREE_REGION));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        Player player = (Player) sender;

        RegionKind regionKind = AdvancedRegionMarket.getInstance().getRegionKindManager()
                .getRegionKind(command.split(" ")[1]);
        if (regionKind == null) {
            throw new InputException(player, Messages.REGIONKIND_DOES_NOT_EXIST);
        }
        if (!RegionKind.hasPermission(player, regionKind)) {
            throw new InputException(player, Messages.NO_PERMISSION);
        }
        AdvancedRegionMarket.getInstance().getRegionManager().teleportToFreeRegion(regionKind, player);

        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        if(args.length == 2) {
            return AdvancedRegionMarket.getInstance().getRegionKindManager().completeTabRegionKinds(args[1], "", player);
        }
        return new ArrayList<>();
    }
}
