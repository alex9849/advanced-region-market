package net.alex9849.arm.presets.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.presets.ActivePresetManager;
import net.alex9849.arm.presets.PresetPlayerPair;
import net.alex9849.arm.presets.presets.ContractPreset;
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
        super(false, "extendtime",
                Arrays.asList("(?i)extendtime ([0-9]+(s|m|h|d))", "(?i)extendtime (?i)remove"),
                Arrays.asList("extendtime ([TIME(Example: 10h)]/remove)"),
                Arrays.asList(Permission.ADMIN_PRESET_SET_EXTENDTIME));
        this.presetType = presetType;
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        Player player = (Player) sender;

        Preset preset = ActivePresetManager.getPreset(player, this.presetType);

        if (preset == null) {
            preset = this.presetType.create();
            ActivePresetManager.add(new PresetPlayerPair(player, preset));
        }

        if (!(preset instanceof ContractPreset)) {
            return false;
        }
        ContractPreset contractPreset = (ContractPreset) preset;

        if (command.matches(this.regex_remove)) {
            contractPreset.removeExtendTime();
            player.sendMessage(Messages.PREFIX + Messages.PRESET_REMOVED);
        } else {
            contractPreset.setExtendTime(command.split(" ")[1]);
            player.sendMessage(Messages.PREFIX + Messages.PRESET_SET);
            if (contractPreset.canPriceLineBeLetEmpty()) {
                player.sendMessage(Messages.PREFIX + "You can leave the price-line on signs empty now");
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
