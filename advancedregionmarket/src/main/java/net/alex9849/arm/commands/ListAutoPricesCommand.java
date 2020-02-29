package net.alex9849.arm.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListAutoPricesCommand extends BasicArmCommand {

    public ListAutoPricesCommand() {
        super(true, "listautoprices",
                Arrays.asList("(?i)listautoprices"),
                Arrays.asList("listautoprices"),
                Arrays.asList(Permission.ADMIN_LISTAUTOPRICES));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        sender.sendMessage(Messages.AUTOPRICE_LIST);
        sender.sendMessage("- " + "default");
        for (AutoPrice autoPrice : AutoPrice.getAutoPrices()) {
            sender.sendMessage("- " + autoPrice.getName());
        }
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        return new ArrayList<>();
    }
}
