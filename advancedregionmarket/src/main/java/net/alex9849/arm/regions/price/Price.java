package net.alex9849.arm.regions.price;

import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.inter.WGRegion;

import java.text.DecimalFormat;
import java.util.Locale;

public class Price {
    protected AutoPrice autoPrice;
    protected double price;
    protected boolean hasPriceBeenCalced;
    protected double calcedAutoPrice;

    public Price(double price) {
        if(price < 0) {
            this.price = (-1) * price;
        } else {
            this.price = price;
        }
        this.calcedAutoPrice = 0;
        this.autoPrice = null;
        this.hasPriceBeenCalced = false;
    }

    public Price(AutoPrice autoPrice) {
        this.autoPrice = autoPrice;
        this.calcedAutoPrice = 0;
        this.price = 0;
        this.hasPriceBeenCalced = false;
    }

    public double calcPrice(WGRegion wgRegion){
        if(this.isAutoPrice()) {
            if(this.hasPriceBeenCalced) {
                return this.calcedAutoPrice;
            } else {
                int m2 = wgRegion.getVolume() / ((wgRegion.getMaxPoint().getBlockY() - wgRegion.getMinPoint().getBlockY()) + 1);
                this.calcedAutoPrice = this.autoPrice.getCalculatedPrice(m2, wgRegion.getVolume());
                this.hasPriceBeenCalced = true;
                return this.calcedAutoPrice;
            }
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

    public static String formatPrice(double price) {
        float epsilon = 0.004f; // 4 tenths of a cent
        if (Math.abs(Math.round(price) - price) < epsilon) {
            return String.format("%1.0f", price); // sdb
        } else {
            return String.format("%1.2f", price); // dj_segfault
        }
    }

    /*
    public static double roundPrice(double price) {
        double rounded = price;
        rounded = rounded * 100;
        rounded = (int) rounded;
        return rounded / 100;
    }
    */
}
