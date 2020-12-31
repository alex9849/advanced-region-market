package net.alex9849.arm.regions.price;

import net.alex9849.arm.regions.price.Autoprice.AutoPrice;

public class RentPrice extends ContractPrice {
    protected long maxExtendTime;

    public RentPrice(double price, long extendTime, long maxExtendTime) {
        super(price, extendTime);
        if (maxExtendTime < 1000)
            throw new IllegalArgumentException("MaxExtendTime needs to be at least one second!");
        this.maxExtendTime = maxExtendTime;
    }

    public RentPrice(AutoPrice autoPrice) {
        super(autoPrice);
        this.maxExtendTime = autoPrice.getMaxExtendtime();
    }

    public long getMaxExtendTime() {
        return this.maxExtendTime;
    }
}
