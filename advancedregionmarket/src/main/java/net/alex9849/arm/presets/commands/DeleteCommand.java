package net.alex9849.arm.presets.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeleteCommand extends BasicArmCommand {
    private PresetType presetType;

    public DeleteCommand(PresetType presetType, AdvancedRegionMarket plugin) {
        super(true, plugin, "delete",
                Arrays.asList("(?i)delete [^;\n ]+"),
                Arrays.asList("delete [PRESETNAME]"),
                Arrays.asList(Permission.ADMIN_PRESET_DELETE));
        this.presetType = presetType;
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        Preset toDeletePreset = getPlugin().getPresetPatternManager()
                .getPreset(command.split(" ")[1], this.presetType);

        if (toDeletePreset == null) {
            throw new InputException(sender, Messages.PRESET_NOT_FOUND);
        }

        getPlugin().getPresetPatternManager().remove(toDeletePreset);
        sender.sendMessage(Messages.PREFIX + Messages.PRESET_DELETED);
        return true;
    }

    @Override
    protected List<String> onTabCompleteArguments(Player player, String[] args) {
        if (args.length != 2) {
            return new ArrayList<>();
        }
        return getPlugin().getPresetPatternManager()
                .onTabCompleteCompleteSavedPresets(this.presetType, args[1]);
    }
}
