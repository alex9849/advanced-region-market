package net.alex9849.arm.regions.price;

import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import static net.alex9849.arm.util.TimeUtil.convertStringToTime;

public class ContractPrice extends Price {
    protected long extendTime;

    /**
     * Creates a ContractPrice
     * @param price The price per period
     * @param extendTime periodlength needs to be bigger than 1000, otherwise it will automatically be set to 1000
     */
    public ContractPrice(double price, long extendTime) {
        super(price);
        if (extendTime < 1000)
            throw new IllegalArgumentException("ExtendTime needs to be at least one second!");
        this.extendTime = extendTime;
    }

    public ContractPrice(AutoPrice autoPrice) {
        super(autoPrice);
        this.extendTime = autoPrice.getExtendtime();
    }

    public static long stringToTime(String stringtime) throws IllegalArgumentException {
        return convertStringToTime(stringtime);
    }

    public long getExtendTime() {
        return this.extendTime;
    }
}
