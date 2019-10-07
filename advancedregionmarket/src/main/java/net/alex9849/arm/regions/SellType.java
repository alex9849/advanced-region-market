package net.alex9849.arm.regions;

import net.alex9849.arm.Messages;

public enum SellType {
    SELL {
        public String getName() {
            return Messages.SELLREGION_NAME;
        }

        @Override
        public String getInternalName() {
            return "sellregion";
        }
    }, RENT {
        public String getName() {
            return Messages.RENTREGION_NAME;
        }

        @Override
        public String getInternalName() {
            return "rentregion";
        }
    }, CONTRACT {
        public String getName() {
            return Messages.CONTRACTREGION_NAME;
        }

        @Override
        public String getInternalName() {
            return "contractregion";
        }
    };

    public static SellType getSelltype(String string) {
        if (string.equalsIgnoreCase(SellType.SELL.getInternalName())) {
            return SellType.SELL;
        } else if (string.equalsIgnoreCase(SellType.RENT.getInternalName())) {
            return SellType.RENT;
        } else if (string.equalsIgnoreCase(SellType.CONTRACT.getInternalName())) {
            return SellType.CONTRACT;
        }
        return null;
    }

    public abstract String getName();

    public abstract String getInternalName();

}
