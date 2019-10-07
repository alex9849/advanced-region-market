package net.alex9849.arm.presets;

import net.alex9849.arm.presets.presets.Preset;
import org.bukkit.entity.Player;

public class PresetPlayerPair {
    private Player player;
    private Preset preset;

    public PresetPlayerPair(Player player, Preset preset) {
        this.player = player;
        this.preset = preset;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Preset getPreset() {
        return this.preset;
    }
}
