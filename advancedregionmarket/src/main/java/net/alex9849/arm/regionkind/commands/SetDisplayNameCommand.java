package net.alex9849.arm.regionkind.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regionkind.RegionKind;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetDisplayNameCommand implements BasicArmCommand {
    private final String rootCommand = "setdisplayname";
    private final String regex = "(?i)setdisplayname [^;\n ]+ [^;\n]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("setdisplayname [REGIONKIND] [Displayname]"));

    @Override
    public boolean matchesRegex(String command) {
        return command.matches(this.regex);
    }

    @Override
    public String getRootCommand() {
        return this.rootCommand;
    }

    @Override
    public List<String> getUsage() {
        return this.usage;
    }

    @Override
    public boolean runCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args, String allargs) throws InputException {
        if (!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        if (!sender.hasPermission(Permission.REGIONKIND_SET_DISPLAYNAME)) {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
        RegionKind regionKind = AdvancedRegionMarket.getInstance().getRegionKindManager().getRegionKind(args[1]);
        if (regionKind == null) {
            throw new InputException(sender, Messages.REGIONKIND_DOES_NOT_EXIST);
        }

        String displayname = "";

        for (int i = 2; i < args.length; i++) {
            displayname += args[i];
            if (i < args.length - 1) {
                displayname += " ";
            }
        }

        regionKind.setDisplayName(displayname);

        sender.sendMessage(Messages.PREFIX + Messages.REGIONKIND_MODIFIED);
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if (!player.hasPermission(Permission.REGIONKIND_SET_DISPLAYNAME)) {
            return returnme;
        }

        if (args.length == 1) {
            if (this.rootCommand.startsWith(args[0])) {
                returnme.add(this.rootCommand);
            }
        } else if (args.length == 2 && (args[0].equalsIgnoreCase(this.rootCommand))) {
            returnme.addAll(AdvancedRegionMarket.getInstance().getRegionKindManager().completeTabRegionKinds(args[1], ""));
        }
        return returnme;
    }
}
