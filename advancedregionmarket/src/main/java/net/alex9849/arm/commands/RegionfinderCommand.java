package net.alex9849.arm.commands;

import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.gui.Gui;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegionfinderCommand extends BasicArmCommand {

    public RegionfinderCommand() {
        super(false, "regionfinder",
                Arrays.asList("(?i)regionfinder"),
                Arrays.asList("regionfinder"),
                Arrays.asList(Permission.MEMBER_REGIONFINDER));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        Gui.openRegionFinder((Player) sender, false);
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        return new ArrayList<>();
    }
}
