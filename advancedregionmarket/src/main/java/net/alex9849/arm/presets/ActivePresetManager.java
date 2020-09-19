package net.alex9849.arm.presets;

import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ActivePresetManager {
    private static List<PresetSenderPair> presetSenderPairList = new ArrayList();

    public static Preset getPreset(CommandSender sender, PresetType presetType) {
        for (PresetSenderPair ppp : presetSenderPairList) {
            if ((ppp.getSender() == sender) && (ppp.getPreset().getPresetType() == presetType)) {
                return ppp.getPreset();
            }
        }
        return null;
    }

    public static void add(PresetSenderPair ppp) {
        deletePreset(ppp.getSender(), ppp.getPreset().getPresetType());
        presetSenderPairList.add(ppp);
    }

    public static void deletePreset(CommandSender sender) {
        for (int i = 0; i < presetSenderPairList.size(); i++) {
            if (presetSenderPairList.get(i).getSender() == sender) {
                presetSenderPairList.remove(i);
                i--;
            }
        }
    }

    public static void deletePreset(CommandSender sender, PresetType presetType) {
        for (int i = 0; i < presetSenderPairList.size(); i++) {
            if ((presetSenderPairList.get(i).getSender() == sender) && presetSenderPairList.get(i).getPreset().getPresetType() == presetType) {
                presetSenderPairList.remove(i);
                i--;
            }
        }
    }

    public static void reset() {
        ActivePresetManager.presetSenderPairList = new ArrayList<>();
    }
}
