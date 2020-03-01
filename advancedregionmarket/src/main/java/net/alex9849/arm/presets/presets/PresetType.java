package net.alex9849.arm.presets.presets;

import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.regionkind.RegionKind;

import java.util.ArrayList;

public enum PresetType {
    SELLPRESET {
        public String getName() {
            return "sellpreset";
        }

        public String getMajorityName() {
            return "SellPresets";
        }

        public SellPreset create() {
            return new SellPreset("default", false, 0, RegionKind.DEFAULT, FlagGroup.DEFAULT,
                    true, false, true, true, 0,
                    null, EntityLimitGroup.DEFAULT, new ArrayList<>(), -1, 50);
        }

    }, RENTPRESET {
        public String getName() {
            return "rentpreset";
        }

        public String getMajorityName() {
            return "RentPresets";
        }

        public RentPreset create() {
            return new RentPreset("default", false, 0, RegionKind.DEFAULT, FlagGroup.DEFAULT,
                    true, false, true, false, 0,
                    false, 0, true, 0, null,
                    EntityLimitGroup.DEFAULT, new ArrayList<>(), -1, 50);
        }

    }, CONTRACTPRESET {
        public String getName() {
            return "contractpreset";
        }

        public String getMajorityName() {
            return "ContractPresets";
        }

        public ContractPreset create() {
            return new ContractPreset("default", false, 0, RegionKind.DEFAULT, FlagGroup.DEFAULT,
                    true, false, true, false, 0, true,
                    0, null, EntityLimitGroup.DEFAULT, new ArrayList<>(), -1, 50);
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
