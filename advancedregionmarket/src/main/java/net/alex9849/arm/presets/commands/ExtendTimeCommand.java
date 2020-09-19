package net.alex9849.arm.presets.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.presets.ActivePresetManager;
import net.alex9849.arm.presets.PresetSenderPair;
import net.alex9849.arm.presets.presets.CountdownPreset;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExtendTimeCommand extends BasicArmCommand {
    private final String regex_remove = "(?i)extendtime (?i)remove";
    private PresetType presetType;

    public ExtendTimeCommand(PresetType presetType) {
        super(true, "extendtime",
                Arrays.asList("(?i)extendtime ([0-9]+(s|m|h|d))", "(?i)extendtime (?i)remove"),
                Arrays.asList("extendtime ([TIME(Example: 10h)]/remove)"),
                Arrays.asList(Permission.ADMIN_PRESET_SET_EXTENDTIME));
        this.presetType = presetType;
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        Preset preset = ActivePresetManager.getPreset(sender, this.presetType);
        if (preset == null) {
            preset = this.presetType.create();
            ActivePresetManager.add(new PresetSenderPair(sender, preset));
        }

        if (!(preset instanceof CountdownPreset)) {
            return false;
        }
        CountdownPreset contractPreset = (CountdownPreset) preset;

        if (command.matches(this.regex_remove)) {
            contractPreset.setExtendTime((Long) null);
            sender.sendMessage(Messages.PREFIX + Messages.PRESET_REMOVED);
        } else {
            try {
                contractPreset.setExtendTime(command.split(" ")[1]);
                sender.sendMessage(Messages.PREFIX + Messages.PRESET_SET);
                if (contractPreset.canPriceLineBeLetEmpty()) {
                    sender.sendMessage(Messages.PREFIX + "You can leave the price-line on signs empty now");
                }
            } catch (IllegalArgumentException e) {
                throw new InputException(sender, e.getMessage());
            }
        }
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if (args.length == 2) {
            if ("remove".startsWith(args[1])) {
                returnme.add("remove");
            }
            if (args[1].matches("[0-9]+")) {
                returnme.add(args[1] + "s");
                returnme.add(args[1] + "m");
                returnme.add(args[1] + "h");
                returnme.add(args[1] + "d");
            }
        }
        return returnme;
    }
}
