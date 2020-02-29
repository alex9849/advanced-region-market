package net.alex9849.arm.commands;

import net.alex9849.arm.Permission;
import net.alex9849.arm.limitgroups.LimitGroup;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LimitCommand extends BasicArmCommand {

    public LimitCommand() {
        super(false, "limit",
                Arrays.asList("(?i)limit"),
                Arrays.asList("limit"),
                Arrays.asList(Permission.MEMBER_LIMIT));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) {
        Player player = (Player) sender;
        LimitGroup.getLimitChat(player);
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        return new ArrayList<>();
    }
}
