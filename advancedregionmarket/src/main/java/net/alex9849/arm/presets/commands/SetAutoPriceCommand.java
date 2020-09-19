package net.alex9849.arm.presets.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.presets.ActivePresetManager;
import net.alex9849.arm.presets.PresetSenderPair;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetAutoPriceCommand extends BasicArmCommand {
    private final String regex_remove = "(?i)autoprice (?i)remove";
    private PresetType presetType;

    public SetAutoPriceCommand(PresetType presetType) {
        super(true, "autoprice",
                Arrays.asList("(?i)autoprice [^;\n ]+", "(?i)autoprice (?i)remove"),
                Arrays.asList("autoprice [AUTOPRICE]", "autoprice remove"),
                Arrays.asList(Permission.ADMIN_PRESET_SET_AUTOPRICE));
        this.presetType = presetType;
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        Preset preset = ActivePresetManager.getPreset(sender, presetType);

        if (preset == null) {
            preset = this.presetType.create();
            ActivePresetManager.add(new PresetSenderPair(sender, preset));
        }

        if (command.matches(this.regex_remove)) {
            preset.setAutoPrice(null);
        } else {
            AutoPrice autoPrice = AutoPrice.getAutoprice(command.split(" ")[1]);
            if (autoPrice == null) {
                throw new InputException(sender, ChatColor.RED + "AutoPrice does not exist!");
            }
            preset.setAutoPrice(autoPrice);
            if (preset.canPriceLineBeLetEmpty()) {
                sender.sendMessage(Messages.PREFIX + "You can leave the price-line on signs empty now");
            }
        }
        sender.sendMessage(Messages.PREFIX + Messages.PRESET_SET);
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if (args.length == 2) {
            if ("remove".startsWith(args[1])) {
                returnme.add("remove");
            }
            returnme.addAll(AutoPrice.tabCompleteAutoPrice(args[1]));
        }
        return returnme;
    }
}
