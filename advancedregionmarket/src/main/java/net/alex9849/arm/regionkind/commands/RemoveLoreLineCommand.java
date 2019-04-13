package net.alex9849.arm.regionkind.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemoveLoreLineCommand extends BasicArmCommand {
    private final String rootCommand = "removeloreline";
    private final String regex = "(?i)removeloreline [^;\n ]+ [0-9]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("removeloreline [REGIONKIND] [Lore-line]"));

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
        if(!sender.hasPermission(Permission.REGIONKIND_REMOVE_LORE_LINE)) {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
        RegionKind regionKind = AdvancedRegionMarket.getRegionKindManager().getRegionKind(args[1]);
        if(regionKind == null) {
            throw new InputException(sender, Messages.REGIONKIND_DOES_NOT_EXIST);
        }

        int lineIndex = Integer.parseInt(args[2]) - 1;
        List<String> lore = regionKind.getRawLore();

        if((lineIndex < 0) || (lineIndex >= lore.size())) {
            throw new InputException(sender, Messages.REGIONKIND_LORE_LINE_NOT_EXIST);
        } else {
            lore.remove(lineIndex);
            regionKind.queueSave();
        }

        sender.sendMessage(Messages.PREFIX + Messages.REGIONKIND_MODIFIED);
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(!player.hasPermission(Permission.REGIONKIND_REMOVE_LORE_LINE)) {
            return returnme;
        }

        if(args.length == 1) {
            if (this.rootCommand.startsWith(args[0])) {
                returnme.add(this.rootCommand);
            }
        } else if(args.length == 2 && (args[0].equalsIgnoreCase(this.rootCommand))) {
            returnme.addAll(AdvancedRegionMarket.getRegionKindManager().completeTabRegionKinds(args[1], ""));
        } else if(args.length == 3 && (args[0].equalsIgnoreCase(this.rootCommand))) {
            RegionKind regionKind = AdvancedRegionMarket.getRegionKindManager().getRegionKind(args[2]);
            if(regionKind == null) {
                return returnme;
            } else {
                int loresize = regionKind.getRawLore().size();
                for(int i = 1; i < loresize + 1; i++) {
                    if(args[3].startsWith(i + "")) {
                        returnme.add(i + "");
                    }
                }
            }
        }
        return returnme;
    }
}
