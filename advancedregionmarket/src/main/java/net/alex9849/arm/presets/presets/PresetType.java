package net.alex9849.arm.presets.presets;

public enum PresetType {
    SELLPRESET {
        public String getName() {
            return "sellpreset";
        }

        public String getMajorityName() {
            return "SellPresets";
        }

        public SellPreset create() {
            return new SellPreset();
        }

    }, RENTPRESET {
        public String getName() {
            return "rentpreset";
        }

        public String getMajorityName() {
            return "RentPresets";
        }

        public RentPreset create() {
            return new RentPreset();
        }

    }, CONTRACTPRESET {
        public String getName() {
            return "contractpreset";
        }

        public String getMajorityName() {
            return "ContractPresets";
        }

        public ContractPreset create() {
            return new ContractPreset();
        }
    };

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

    public abstract String getName();

    public abstract String getMajorityName();

    public abstract Preset create();
}
