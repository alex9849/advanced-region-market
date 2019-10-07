package net.alex9849.arm.regions.price;

import net.alex9849.arm.regions.price.Autoprice.AutoPrice;

public class ContractPrice extends Price {
    protected long extendTime;

    public ContractPrice(double price, long extendTime) {
        super(price);
        if (extendTime < 1000) {
            this.extendTime = 1000;
        } else {
            this.extendTime = extendTime;
        }
    }

    public ContractPrice(AutoPrice autoPrice) {
        super(autoPrice);
        this.extendTime = autoPrice.getExtendtime();
    }

    public static long stringToTime(String stringtime) throws IllegalArgumentException {
        long time = 0;
        if (stringtime.matches("[\\d]+d")) {
            time = Long.parseLong(stringtime.split("d")[0]);
            time = time * 1000 * 60 * 60 * 24;
        } else if (stringtime.matches("[\\d]+h")) {
            time = Long.parseLong(stringtime.split("h")[0]);
            time = time * 1000 * 60 * 60;
        } else if (stringtime.matches("[\\d]+m")) {
            time = Long.parseLong(stringtime.split("m")[0]);
            time = time * 1000 * 60;
        } else if (stringtime.matches("[\\d]+s")) {
            time = Long.parseLong(stringtime.split("s")[0]);
            time = time * 1000;
        } else if (stringtime.matches("[\\d]+")) {
            time = Long.parseLong(stringtime);
        } else {
            throw new IllegalArgumentException();
        }
        return time;
    }

    public long getExtendTime() {
        return this.extendTime;
    }
}
