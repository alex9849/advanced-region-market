package net.alex9849.arm.regionkind.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.commands.OptionModifyCommand;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regionkind.RegionKind;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class RegionKindOptionModifyCommand<SettingsObj> extends OptionModifyCommand<RegionKind, SettingsObj> {
    public RegionKindOptionModifyCommand(String rootCommand, List<String> permissions, String optionRegex,
                                         String optionDescription, String settingNotFoundMsg) {
        super(true, true, rootCommand,
                Arrays.asList("(?i)" + rootCommand + " [^;\n ]+ " + optionRegex),
                Arrays.asList(rootCommand + " [REGIONKIND] " + optionDescription),
                permissions, Messages.REGIONKIND_DOES_NOT_EXIST, settingNotFoundMsg);
    }

    public RegionKindOptionModifyCommand(String rootCommand, List<String> permissions, String settingNotFoundMsg) {
        super(true, false, rootCommand,
                Arrays.asList("(?i)" + rootCommand + " [^;\n ]+"),
                Arrays.asList(rootCommand + " [REGIONKIND]"),
                permissions, Messages.REGIONKIND_DOES_NOT_EXIST, settingNotFoundMsg);
    }

    @Override
    protected final RegionKind getObjectFromCommand(CommandSender sender, String command) throws InputException {
        return AdvancedRegionMarket.getInstance().getRegionKindManager().getRegionKind(command.split(" ")[1]);
    }

    @Override
    protected final SettingsObj getSettingsFromCommand(CommandSender sender, String command) throws InputException {
        String[] args = command.split(" ");
        List<String> settingsArgs = new ArrayList<>();
        for(int i = 2; i < args.length; i++) {
            settingsArgs.add(args[i]);
        }
        return getSettingsFromString(sender, Messages.getStringList(settingsArgs, x -> x, " "));
    }

    @Override
    protected final List<String> tabCompleteObject(Player player, String[] args) {
        if(args.length != 2) {
            return new ArrayList<>();
        }
        return AdvancedRegionMarket.getInstance().getRegionKindManager().completeTabRegionKinds(args[1], "");
    }

    @Override
    protected final List<String> tabCompleteSettingsObject(Player player, String[] args) {
        if(args.length < 3) {
            return new ArrayList<>();
        }
        List<String> settingsArgs = new ArrayList<>();
        for(int i = 2; i < args.length; i++) {
            settingsArgs.add(args[i]);
        }
        return tabCompleteSettingsObject(player, Messages.getStringList(settingsArgs, x -> x, " "));
    }

    @Override
    protected void sendSuccessMessage(CommandSender sender, RegionKind obj, SettingsObj settingsObj) {
        sender.sendMessage(Messages.PREFIX + Messages.REGIONKIND_MODIFIED);
    }

    protected abstract List<String> tabCompleteSettingsObject(Player player, String setting);

    /**
     *
     * @param sender The sender of the command.
     * @param setting The part of the string, that containt the settings information, matching to the given optionregex
     * @return If a settings-regex has been given and this method returns null, an exception will be thrown
     */
    protected abstract SettingsObj getSettingsFromString(CommandSender sender, String setting);
}
