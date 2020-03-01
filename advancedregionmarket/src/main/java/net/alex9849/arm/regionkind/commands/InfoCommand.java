package net.alex9849.arm.regionkind.commands;

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

public class InfoCommand extends BasicArmCommand {

    public InfoCommand() {
        super(true, "info",
                Arrays.asList("(?i)info [^;\n ]+"),
                Arrays.asList("info [REGIONKIND]"),
                Arrays.asList(Permission.REGIONKIND_INFO));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        RegionKind regionKind = AdvancedRegionMarket.getInstance().getRegionKindManager().getRegionKind(command.split(" ")[1]);
        if (regionKind == null) {
            throw new InputException(sender, Messages.REGIONKIND_DOES_NOT_EXIST);
        }

        List<String> lore = regionKind.getLore();

        sender.sendMessage(regionKind.getConvertedMessage(Messages.REGIONKIND_INFO_HEADLINE));
        sender.sendMessage(regionKind.getConvertedMessage(Messages.REGIONKIND_INFO_INTERNAL_NAME));
        sender.sendMessage(regionKind.getConvertedMessage(Messages.REGIONKIND_INFO_DISPLAY_NAME));
        sender.sendMessage(regionKind.getConvertedMessage(Messages.REGIONKIND_INFO_MATERIAL));
        sender.sendMessage(regionKind.getConvertedMessage(Messages.REGIONKIND_INFO_DISPLAY_IN_GUI));
        sender.sendMessage(regionKind.getConvertedMessage(Messages.REGIONKIND_INFO_DISPLAY_IN_LIMITS));
        sender.sendMessage(Messages.REGIONKIND_INFO_LORE);

        for (int i = 0; i < lore.size(); i++) {
            sender.sendMessage((i + 1) + ". " + lore.get(i));
        }
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
