package net.alex9849.arm.Preseter.presets;

import net.alex9849.arm.regions.RegionKind;

import java.util.ArrayList;

public enum PresetType {
    SELLPRESET {
        public String getName() {
            return "sellpreset";
        }

        public SellPreset create() {
            return new SellPreset("default", false, 0, RegionKind.DEFAULT, true, false, true, new ArrayList<>());
        }

    }, RENTPRESET {
        public String getName() {
            return "rentpreset";
        }

        public RentPreset create() {
            return new RentPreset("default", false, 0, RegionKind.DEFAULT, true, false, true, false, 0, false, 0, new ArrayList<>());
        }

    }, CONTRACTPRESET {
        public String getName() {
            return "contractpreset";
        }

        public ContractPreset create() {
            return new ContractPreset("default", false, 0, RegionKind.DEFAULT, true, false, true, false, 0, new ArrayList<>());
        }
    };

    public abstract String getName();

    public abstract Preset create();

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
}
