package net.alex9849.arm.Preseter;

import org.bukkit.entity.Player;

public enum PresetType {
    SELLPRESET {
        public String getName() {
            return "sellpreset";
        }
    }, RENTPRESET {
        public String getName() {
            return "rentpreset";
        }
    }, CONTRACTPRESET {
        public String getName() {
            return "contractpreset";
        }
    };

    public abstract String getName();

    public static PresetType getPresetType(String type) {
        if (type.equalsIgnoreCase("sellpreset")) {
            return SELLPRESET;
        } else if (type.equalsIgnoreCase("rentpreset")) {
            return RENTPRESET;
        } else if (type.equalsIgnoreCase("contractpreset")) {
            return CONTRACTPRESET;
        } else {
            return null;
        }
    }

    public static Preset create(PresetType presetType, Player player) {
        if(presetType == SELLPRESET) {
            SellPreset sellPreset = new SellPreset(player);
            SellPreset.getList().add(sellPreset);
            return sellPreset;
        } else if(presetType == RENTPRESET) {
            RentPreset rentPreset = new RentPreset(player);
            RentPreset.getList().add(rentPreset);
            return rentPreset;
        } else if(presetType == CONTRACTPRESET) {
            ContractPreset contractPreset = new ContractPreset(player);
            ContractPreset.getList().add(contractPreset);
            return contractPreset;
        } else {
            return null;
        }
    }
}
