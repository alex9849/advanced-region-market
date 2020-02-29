package net.alex9849.arm.commands;

import net.alex9849.arm.Permission;
import net.alex9849.arm.gui.Gui;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiCommand extends BasicArmCommand {

    public GuiCommand() {
        super(false, "gui",
                Arrays.asList("(?i)gui"),
                Arrays.asList("gui"),
                Arrays.asList(Permission.MEMBER_GUI));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) {
        Gui.openARMGui((Player) sender);
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        return new ArrayList<>();
    }
}
