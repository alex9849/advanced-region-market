package net.alex9849.arm.minifeatures;

public enum SellType {
    SELL, RENT, CONTRACT;

    public static SellType getSelltype(String string) {
        if (string.equalsIgnoreCase("sellregion")) {
            return SellType.SELL;
        } else if (string.equalsIgnoreCase("rentregion")) {
            return SellType.RENT;
        } else if (string.equalsIgnoreCase("contractregion")) {
            return SellType.CONTRACT;
        }
        return null;
    }

}
