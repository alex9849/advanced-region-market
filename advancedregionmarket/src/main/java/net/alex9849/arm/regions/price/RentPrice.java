package net.alex9849.arm.regions.price;

import net.alex9849.arm.regions.price.Autoprice.AutoPrice;

public class RentPrice extends ContractPrice {
    protected long maxRentTime;

    public RentPrice(double price, long extendTime, long maxRentTime) {
        super(price, extendTime);
        if (maxRentTime < 0) {
            this.maxRentTime = (-1) * maxRentTime;
        } else {
            this.maxRentTime = maxRentTime;
        }
    }

    public RentPrice(AutoPrice autoPrice) {
        super(autoPrice);
        this.maxRentTime = autoPrice.getMaxrenttime();
    }

    public long getMaxRentTime() {
        return this.maxRentTime;
    }
}
