package net.alex9849.arm.presets.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.commands.OptionModifyCommand;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.presets.ActivePresetManager;
import net.alex9849.arm.presets.PresetPlayerPair;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class PresetOptionModifyCommand<SettingsObj> extends OptionModifyCommand<Preset, SettingsObj> {
    private PresetType presetType;

    public PresetOptionModifyCommand(String rootCommand, List<String> permissions, boolean allowNullValueSetting, String optionRegex,
                                     String optionDescription, String settingNotFoundMsg, PresetType presetType) {
        super(false, !allowNullValueSetting, rootCommand,
                Arrays.asList("(?i)" + rootCommand + " " + optionRegex),
                Arrays.asList(rootCommand + " " + optionDescription),
                permissions, "", settingNotFoundMsg);
        this.presetType = presetType;
    }

    public PresetOptionModifyCommand(String rootCommand, List<String> permissions, PresetType presetType) {
        super(false, false, rootCommand,
                Arrays.asList("(?i)" + rootCommand),
                Arrays.asList(rootCommand),
                permissions, "", "");
        this.presetType = presetType;
    }

    protected final PresetType getPresetType() {
        return this.presetType;
    }

    @Override
    protected final Preset getObjectFromCommand(CommandSender sender, String command) throws InputException {
        Player player = (Player) sender;
        Preset preset = ActivePresetManager.getPreset(player, this.presetType);
        if (preset == null) {
            preset = this.presetType.create();
            ActivePresetManager.add(new PresetPlayerPair(player, preset));
        }
        return preset;
    }

    @Override
    protected final SettingsObj getSettingsFromCommand(CommandSender sender, String command) throws InputException {
        String[] args = command.split(" ");
        List<String> settingsArgs = new ArrayList<>();
        for(int i = 1; i < args.length; i++) {
            settingsArgs.add(args[i]);
        }
        return getSettingsFromString(sender, Messages.getStringList(settingsArgs, x -> x, " "));
    }

    protected abstract SettingsObj getSettingsFromString(CommandSender sender, String setting) throws InputException;

    @Override
    protected void sendSuccessMessage(CommandSender sender, Preset obj, SettingsObj settingsObj) {
        sender.sendMessage(Messages.PREFIX + Messages.PRESET_SET);
    }

    @Override
    protected List<String> tabCompleteObject(Player player, String[] args) {
        return new ArrayList<>();
    }

    @Override
    protected final List<String> tabCompleteSettingsObject(Player player, String[] args) {
        if(args.length < 2) {
            return new ArrayList<>();
        }
        List<String> settingsArgs = new ArrayList<>();
        for(int i = 1; i < args.length; i++) {
            settingsArgs.add(args[i]);
        }
        return tabCompleteSettingsObject(player, Messages.getStringList(settingsArgs, x -> x, " "));
    }

    protected abstract List<String> tabCompleteSettingsObject(Player player, String settings);
}
