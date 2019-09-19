package net.alex9849.arm.regionkind.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.util.MaterialFinder;
import net.alex9849.exceptions.InputException;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateCommand implements BasicArmCommand {
    private final String rootCommand = "create";
    private final String regex = "(?i)create [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("create [REGIONKINDNAME]"));

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
        if(!sender.hasPermission(Permission.REGIONKIND_CREATE)) {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
        if(AdvancedRegionMarket.getInstance().getRegionKindManager().getRegionKind(args[1]) != null) {
            throw new InputException(sender, Messages.REGIONKIND_ALREADY_EXISTS);
        }

        Material item = MaterialFinder.getRedBed();
        String internalName = args[1];
        String displayName = args[1];
        List<String> lore = new ArrayList<>(Arrays.asList("Default Lore"));
        boolean displayInGui = true;
        boolean displayInLimits = true;
        double paybackPercentage = 0.5;

        AdvancedRegionMarket.getInstance().getRegionKindManager().add(new RegionKind(internalName, item, lore, displayName, displayInGui, displayInLimits, paybackPercentage));
        sender.sendMessage(Messages.PREFIX + Messages.REGIONKIND_CREATED);
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length == 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.REGIONKIND_CREATE)) {
                    returnme.add(this.rootCommand);
                }
            }
        }
        return returnme;
    }
}
