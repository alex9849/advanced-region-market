package net.alex9849.arm.regionkind.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.commands.CommandUtil;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regionkind.RegionKind;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddLoreLineCommand extends BasicArmCommand {

    public AddLoreLineCommand() {
        super(true, "addloreline",
                Arrays.asList("(?i)addloreline [^;\n ]+ [^;\n]+"),
                Arrays.asList("addloreline [REGIONKIND] [loreline]"),
                Arrays.asList(Permission.REGIONKIND_ADD_LORE_LINE));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        String[] args = command.split(" ");
        RegionKind regionKind = AdvancedRegionMarket.getInstance().getRegionKindManager().getRegionKind(args[1]);
        if (regionKind == null) {
            throw new InputException(sender, Messages.REGIONKIND_DOES_NOT_EXIST);
        }

        List<String> loreLine = new ArrayList<>();
        for (int i = 2; i < args.length; i++) {
            loreLine.add(args[i]);
        }
        regionKind.getRawLore().add(CommandUtil.getStringList(loreLine, x -> x, " "));
        regionKind.queueSave();

        sender.sendMessage(Messages.PREFIX + Messages.REGIONKIND_MODIFIED);
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        if (args.length != 2) {
            return new ArrayList<>();
        }
        return AdvancedRegionMarket.getInstance().getRegionKindManager().completeTabRegionKinds(args[1], "");
    }
}
