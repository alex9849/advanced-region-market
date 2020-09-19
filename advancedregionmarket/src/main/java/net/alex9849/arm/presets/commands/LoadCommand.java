package net.alex9849.arm.presets.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.presets.ActivePresetManager;
import net.alex9849.arm.presets.PresetSenderPair;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoadCommand extends BasicArmCommand {
    private PresetType presetType;

    public LoadCommand(PresetType presetType) {
        super(true, "load",
                Arrays.asList("(?i)load [^;\n ]+"),
                Arrays.asList("load [PRESETNAME]"),
                Arrays.asList(Permission.ADMIN_PRESET_LOAD));
        this.presetType = presetType;
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        Preset toAssign = AdvancedRegionMarket.getInstance().getPresetPatternManager()
                .getPreset(command.split(" ")[1], this.presetType);

        if (toAssign == null) {
            throw new InputException(sender, Messages.PRESET_NOT_FOUND);
        }

        Preset toAssignCopy = null;
        try {
            toAssignCopy = (Preset) toAssign.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        ActivePresetManager.add(new PresetSenderPair(sender, toAssignCopy));
        sender.sendMessage(Messages.PREFIX + Messages.PRESET_LOADED);
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        if (args.length != 2) {
            return new ArrayList<>();
        }
        return AdvancedRegionMarket.getInstance().getPresetPatternManager()
                .onTabCompleteCompleteSavedPresets(this.presetType, args[1]);
    }
}
