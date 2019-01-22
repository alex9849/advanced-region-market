package net.alex9849.arm.regions.price;

import net.alex9849.arm.regions.price.Autoprice.AutoPrice;

public class RentPrice extends ContractPrice {
    protected long maxRentTime;

    public RentPrice(double price, long extendTime, long maxRentTime) {
        super(price, extendTime);
        this.maxRentTime = maxRentTime;
    }

    public RentPrice(AutoPrice autoPrice) {
        super(autoPrice);
    }

    public long getMaxRentTime() {
        return this.maxRentTime;
    }
}
