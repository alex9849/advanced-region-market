package net.alex9849.arm.regions.price;

import net.alex9849.arm.regions.price.Autoprice.AutoPrice;

public class RentPrice extends ContractPrice {
    protected long maxRentTime;

    public RentPrice(double price, long extendTime, long maxRentTime) {
        super(price, extendTime);
        if (maxRentTime < 1000)
            throw new IllegalArgumentException("MaxRentTime needs to be at least one second!");
        this.maxRentTime = maxRentTime;
    }

    public RentPrice(AutoPrice autoPrice) {
        super(autoPrice);
        this.maxRentTime = autoPrice.getMaxrenttime();
    }

    public long getMaxRentTime() {
        return this.maxRentTime;
    }
}
