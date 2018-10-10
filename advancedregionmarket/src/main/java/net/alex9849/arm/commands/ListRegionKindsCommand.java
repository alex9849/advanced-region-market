package net.alex9849.arm.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regions.RegionKind;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ListRegionKindsCommand extends BasicArmCommand {

    private final String rootCommand = "listregionkinds";
    private final String regex = "(?i)listregionkinds";
    private final String usage = "/arm listregionkinds";

    @Override
    public boolean matchesRegex(String command) {
        return command.matches(this.regex);
    }

    @Override
    public String getRootCommand() {
        return this.rootCommand;
    }

    @Override
    public String getUsage() {
        return this.usage;
    }

    @Override
    public boolean runCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args, String allargs) throws InputException {
        if(!sender.hasPermission(Permission.ADMIN_LISTREGIONKINDS)) {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }

        sender.sendMessage(Messages.REGIONKINDS);
        sender.sendMessage("- " + "default");
        for (int i = 0; i < RegionKind.getRegionKindList().size(); i++){
            sender.sendMessage("- " + RegionKind.getRegionKindList().get(i).getName());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length == 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.ADMIN_LISTREGIONKINDS)) {
                    returnme.add(this.rootCommand);
                }
            }
        }

        return returnme;
    }
}
