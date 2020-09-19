package net.alex9849.arm.presets;

import net.alex9849.arm.presets.presets.Preset;
import org.bukkit.command.CommandSender;

public class PresetSenderPair {
    private CommandSender sender;
    private Preset preset;

    public PresetSenderPair(CommandSender sender, Preset preset) {
        this.sender = sender;
        this.preset = preset;
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public Preset getPreset() {
        return this.preset;
    }
}
