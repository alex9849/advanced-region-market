package net.alex9849.arm.regionkind.regionkindgroupcommands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.commands.OptionModifyCommand;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regionkind.RegionKindGroup;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class RegionKindGroupOptionModifyCommand<SettingsObj> extends OptionModifyCommand<RegionKindGroup, SettingsObj> {
    public RegionKindGroupOptionModifyCommand(String rootCommand, List<String> permissions, String optionRegex,
                                              String optionDescription, String settingNotFoundMsg) {
        super(true, true, rootCommand,
                Arrays.asList("(?i)" + rootCommand + " [^;\n ]+ " + optionRegex),
                Arrays.asList(rootCommand + " [REGIONKINDGROUP] " + optionDescription),
                permissions, Messages.REGIONKINDGROUP_NOT_EXISTS, settingNotFoundMsg);
    }

    @Override
    protected RegionKindGroup getObjectFromCommand(CommandSender sender, String command) throws InputException {
        return AdvancedRegionMarket.getInstance().getRegionKindGroupManager().getRegionKindGroup(command.split(" ")[1]);
    }

    @Override
    protected SettingsObj getSettingsFromCommand(CommandSender sender, String command) throws InputException {
        String[] args = command.split(" ");
        List<String> settingsArgs = new ArrayList<>();
        for(int i = 2; i < args.length; i++) {
            settingsArgs.add(args[i]);
        }
        return getSettingsFromString(sender, Messages.getStringList(settingsArgs, x -> x, " "));
    }

    @Override
    protected void sendSuccessMessage(CommandSender sender, RegionKindGroup obj, SettingsObj settingsObj) {
        sender.sendMessage(Messages.PREFIX + Messages.REGIONKINDGROUP_MODIFIED);
    }

    @Override
    protected List<String> tabCompleteObject(Player player, String[] args) {
        if(args.length != 2) {
            return new ArrayList<>();
        }
        return AdvancedRegionMarket.getInstance().getRegionKindGroupManager().tabCompleteRegionKindGroups(args[1]);
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String[] args) {
        if(args.length < 3) {
            return new ArrayList<>();
        }
        List<String> settingsArgs = new ArrayList<>();
        for(int i = 2; i < args.length; i++) {
            settingsArgs.add(args[i]);
        }
        return tabCompleteSettingsObject(player, Messages.getStringList(settingsArgs, x -> x, " "));
    }

    protected abstract List<String> tabCompleteSettingsObject(Player player, String setting);

    protected abstract SettingsObj getSettingsFromString(CommandSender sender, String setting);
}
