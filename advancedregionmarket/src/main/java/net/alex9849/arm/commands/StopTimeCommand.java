package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.exceptions.NoPermissionException;
import net.alex9849.arm.regions.CountdownRegion;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StopTimeCommand extends BasicArmCommand {
    public StopTimeCommand(AdvancedRegionMarket plugin) {
        super(true, plugin, "stoptime",
                Arrays.asList("(?i)stoptime (false|true)"),
                Arrays.asList("stoptime (false|true)"),
                Arrays.asList(Permission.ADMIN_STOP_TIME));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException, NoPermissionException {
        boolean setting = Boolean.parseBoolean(command.split(" ")[1]);
        CountdownRegion.haltCountdown(setting);
        if(setting) {
            sender.sendMessage(Messages.PREFIX + Messages.TIME_HALTED);
        } else {
            sender.sendMessage(Messages.PREFIX + Messages.TIME_CONTINUING);
        }
        return true;
    }

    @Override
    protected List<String> onTabCompleteArguments(Player player, String[] args) {
        if(args.length != 2) {
            return new ArrayList<>();
        }
        List<String> returnme = new ArrayList<>();
        if ("true".startsWith(args[1])) {
            returnme.add("true");
        }
        if ("false".startsWith(args[1])) {
            returnme.add("false");
        }
        return returnme;
    }
}
