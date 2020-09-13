package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.*;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.Region;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TpToFreeRegion extends BasicArmCommand {

    public TpToFreeRegion() {
        super(false, "tptofreeregion",
                Arrays.asList("(?i)tptofreeregion [^;\n ]+( -buy)?"),
                Arrays.asList("tptofreeregion [REGIONKIND] (-buy)"),
                Arrays.asList(Permission.MEMBER_TP_TO_FREE_REGION));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        Player player = (Player) sender;
        String[] commandParts = command.split(" ");

        RegionKind regionKind = AdvancedRegionMarket.getInstance().getRegionKindManager()
                .getRegionKind(commandParts[1]);
        if (regionKind == null) {
            throw new InputException(player, Messages.REGIONKIND_DOES_NOT_EXIST);
        }
        Region tpRegion = AdvancedRegionMarket.getInstance().getRegionManager().teleportToFreeRegion(regionKind, player);
        if(commandParts[commandParts.length - 1].equalsIgnoreCase("-buy")) {
            try {
                tpRegion.buy(player);
            } catch (NoPermissionException | OutOfLimitExeption | NotEnoughMoneyException |
                    AlreadySoldException e) {
                if (e.hasMessage()) player.sendMessage(Messages.PREFIX + e.getMessage());
            }
        }
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        if(args.length == 2) {
            return AdvancedRegionMarket.getInstance().getRegionKindManager().completeTabRegionKinds(args[1], "", player);
        }
        if(args.length == 3 && "-buy".startsWith(args[2].toLowerCase())) {
            return Arrays.asList("-buy");
        }
        return new ArrayList<>();
    }
}
