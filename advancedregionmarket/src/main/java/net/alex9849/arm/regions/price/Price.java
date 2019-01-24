package net.alex9849.arm.regions.price;

import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.inter.WGRegion;

public class Price {
    protected AutoPrice autoPrice;
    protected double price;

    public Price(double price) {
        if(price < 0) {
            this.price = (-1) * price;
        } else {
            this.price = price;
        }
    }

    public Price(AutoPrice autoPrice) {
        this.autoPrice = autoPrice;
    }

    public double calcPrice(WGRegion wgRegion){
        if(this.isAutoPrice()) {
            int m2 = wgRegion.getVolume() / ((wgRegion.getMaxPoint().getBlockY() - wgRegion.getMinPoint().getBlockY()) + 1);
            return this.autoPrice.getCalculatedPrice(m2, wgRegion.getVolume());
        } else {
            return this.price;
        }
    }

    public double getPrice() {
        if(this.isAutoPrice()) {
            return this.autoPrice.getPrice();
        } else {
            return this.price;
        }
    }

    public boolean isAutoPrice() {
        return autoPrice != null;
    }

    public AutoPrice getAutoPrice() {
        return this.autoPrice;
    }
}
