package net.alex9849.arm.subregions.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.subregions.SubRegionCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateCommand extends BasicArmCommand {

    public CreateCommand() {
        super(false, "create",
                Arrays.asList("(?i)create"),
                Arrays.asList("create"),
                Arrays.asList(Permission.SUBREGION_CREATE_CONTRACT,
                        Permission.SUBREGION_CREATE_RENT,
                        Permission.SUBREGION_CREATE_SELL));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        Player player = (Player) sender;
        SubRegionCreator selection = SubRegionCreator.getSubRegioncreator(player);
        if (selection == null) {
            throw new InputException(player, Messages.SELECTION_INVALID);
        }
        selection.createWGRegion();
        for (String msg : Messages.SELECTION_SAVED_CREATE_SIGN) {
            player.sendMessage(Messages.PREFIX + msg);
        }
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        return new ArrayList<>();
    }
}
