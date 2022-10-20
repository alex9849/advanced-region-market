package net.alex9849.arm.presets.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.presets.ActivePresetManager;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpdateCommand extends BasicArmCommand {
    private PresetType presetType;

    public UpdateCommand(PresetType presetType, AdvancedRegionMarket plugin) {
        super(true, plugin, "update",
                Arrays.asList("(?i)update [^;\n ]+"),
                Arrays.asList("update [PRESETNAME]"),
                Arrays.asList(Permission.ADMIN_PRESET_UPDATE));
        this.presetType = presetType;
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        Preset toUpdate = ActivePresetManager.getPreset(sender, this.presetType);
        if (toUpdate == null) {
            throw new InputException(sender, Messages.PRESET_PLAYER_DONT_HAS_PRESET);
        }
        Preset toDeletePreset = getPlugin().getPresetPatternManager()
                .getPreset(command.split(" ")[1], this.presetType);
        if (toDeletePreset == null) {
            throw new InputException(sender, Messages.PRESET_NOT_FOUND);
        }
        toUpdate.setName(toDeletePreset.getName());

        try {
            Preset toSafeCopy = (Preset) toUpdate.clone();
            getPlugin().getPresetPatternManager().remove(toDeletePreset);
            getPlugin().getPresetPatternManager().add(toSafeCopy);
            sender.sendMessage(Messages.PREFIX + Messages.PRESET_UPDATED);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

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
