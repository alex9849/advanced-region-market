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
    protected RegionKind getObjectFromCommand(CommandSender sender, String command) throws InputException {
        return AdvancedRegionMarket.getInstance().getRegionKindManager().getRegionKind(command.split(" ")[1]);
    }

    @Override
    protected SettingsObj getSettingsFromCommand(CommandSender sender, String command) throws InputException {
        return getSettingsFromString(sender, command.split(" ")[2]);
    }

    /**
     *
     * @param sender
     * @param setting
     * @return @Nullable
     */
    protected abstract SettingsObj getSettingsFromString(CommandSender sender, String setting);

    @Override
    protected void sendSuccessMessage(CommandSender sender, RegionKind obj, SettingsObj settingsObj) {
        sender.sendMessage(Messages.PREFIX + Messages.REGIONKIND_MODIFIED);
    }

    @Override
    protected List<String> tabCompleteObject(Player player, String[] args) {
        if(args.length != 2) {
            return new ArrayList<>();
        }
        return AdvancedRegionMarket.getInstance().getRegionKindManager().completeTabRegionKinds(args[1], "");
    }
}
