package net.alex9849.arm.presets;

import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ActivePresetManager {
    private static List<PresetPlayerPair> presetPlayerPairList = new ArrayList();

    public static Preset getPreset(Player player, PresetType presetType) {
        for (PresetPlayerPair ppp : presetPlayerPairList) {
            if ((ppp.getPlayer().getUniqueId() == player.getUniqueId()) && (ppp.getPreset().getPresetType() == presetType)) {
                return ppp.getPreset();
            }
        }
        return null;
    }

    public static void add(PresetPlayerPair ppp) {
        deletePreset(ppp.getPlayer(), ppp.getPreset().getPresetType());
        presetPlayerPairList.add(ppp);
    }

    public static void deletePreset(Player player) {
        for (int i = 0; i < presetPlayerPairList.size(); i++) {
            if (presetPlayerPairList.get(i).getPlayer().getUniqueId() == player.getUniqueId()) {
                presetPlayerPairList.remove(i);
                i--;
            }
        }
    }

    public static void deletePreset(Player player, PresetType presetType) {
        for (int i = 0; i < presetPlayerPairList.size(); i++) {
            if ((presetPlayerPairList.get(i).getPlayer().getUniqueId() == player.getUniqueId()) && presetPlayerPairList.get(i).getPreset().getPresetType() == presetType) {
                presetPlayerPairList.remove(i);
                i--;
            }
        }
    }

    public static void reset() {
        ActivePresetManager.presetPlayerPairList = new ArrayList<>();
    }
}
