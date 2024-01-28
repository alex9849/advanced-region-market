package net.alex9849.arm.regions.price;

import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.adapters.WGRegion;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;

import java.text.NumberFormat;

public class Price {
    private AdvancedRegionMarket plugin = AdvancedRegionMarket.getInstance();
    private static NumberFormat priceFormatter;
    protected AutoPrice autoPrice;
    protected double price;
    protected Double cachedAutoPrice;
    private WGRegion cachedWgRegion = null;

    /**
     * Creates a price Object, that can be used for SellRegions
     *
     * @param price the price. Needs to be positive. If negative price will automatically be negated!
     */
    public Price(double price) {
        if (price < 0) {
            price = -1 * price;
        }
        this.price = price;
        this.cachedAutoPrice = null;
        this.autoPrice = null;
    }

    public Price(AutoPrice autoPrice) {
        this.autoPrice = autoPrice;
        this.cachedAutoPrice = null;
        this.price = 0;
    }

    public static void setPriceFormatter(NumberFormat formatter) {
        Price.priceFormatter = formatter;
    }

    public static String formatPrice(double price) {
        return Price.priceFormatter.format(price).replace("\u00A0", " ");
    }

    public double calcPrice(WGRegion wgRegion) {
        if (this.cachedWgRegion == null || this.cachedWgRegion.unwrap() != wgRegion.unwrap()) {
            this.cachedWgRegion = wgRegion;
            this.cachedAutoPrice = null;
        }
        if (this.isAutoPrice()) {
            if (this.cachedAutoPrice != null) {
                return this.cachedAutoPrice;
            } else {
                int m2 = wgRegion.getVolume() / ((wgRegion.getMaxPoint().getBlockY() - wgRegion.getMinPoint().getBlockY()) + 1);
                this.cachedAutoPrice = this.autoPrice.getCalculatedPrice(m2, wgRegion.getVolume());
                return this.cachedAutoPrice;
            }
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
