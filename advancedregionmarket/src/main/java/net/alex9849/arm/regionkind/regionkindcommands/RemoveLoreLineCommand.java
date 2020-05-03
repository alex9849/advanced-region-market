package net.alex9849.arm.regionkind.regionkindcommands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regionkind.RegionKind;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemoveLoreLineCommand extends BasicArmCommand {

    public RemoveLoreLineCommand() {
        super(true, "removeloreline",
                Arrays.asList("(?i)removeloreline [^;\n ]+ [0-9]+"),
                Arrays.asList("removeloreline [REGIONKIND] [Lore-line]"),
                Arrays.asList(Permission.REGIONKIND_REMOVE_LORE_LINE));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        String[] args = command.split(" ");
        RegionKind regionKind = AdvancedRegionMarket.getInstance().getRegionKindManager().getRegionKind(args[1]);
        if (regionKind == null) {
            throw new InputException(sender, Messages.REGIONKIND_DOES_NOT_EXIST);
        }

        int lineIndex = Integer.parseInt(args[2]) - 1;
        List<String> lore = regionKind.getRawLore();

        if ((lineIndex < 0) || (lineIndex >= lore.size())) {
            throw new InputException(sender, Messages.REGIONKIND_LORE_LINE_NOT_EXIST);
        } else {
            lore.remove(lineIndex);
            regionKind.queueSave();
        }

        sender.sendMessage(Messages.PREFIX + Messages.REGIONKIND_MODIFIED);
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if (args.length == 2) {
            return AdvancedRegionMarket.getInstance().getRegionKindManager().completeTabRegionKinds(args[1], "");
        } else if (args.length == 3) {
            RegionKind regionKind = AdvancedRegionMarket.getInstance().getRegionKindManager().getRegionKind(args[2]);
            if (regionKind == null) {
                return returnme;
            } else {
                int loresize = regionKind.getRawLore().size();
                for (int i = 1; i < loresize + 1; i++) {
                    if (args[3].startsWith(i + "")) {
                        returnme.add(i + "");
                    }
                }
            }
        }
        return returnme;
    }
}
