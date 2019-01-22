package net.alex9849.arm.regions.price.Autoprice;

import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class AutoPrice {
    private static List<AutoPrice> autoPrices = new ArrayList<>();
    public static AutoPrice DEFAULT = new AutoPrice("default", 0, 0, 0, AutoPriceCalculation.STATIC);
    private AutoPriceCalculation autoPriceCalculation;
    private double price;
    private long extendtime;
    private long maxrenttime;
    private String name;

    public AutoPrice(String name, double price, long extendtime, long maxrenttime, AutoPriceCalculation autoPriceCalculation){
        this.price = price;
        this.name = name;
        this.extendtime = extendtime;
        this.maxrenttime = maxrenttime;
    }

    public boolean equals(String name){
        return this.name.equalsIgnoreCase(name);
    }

    @Override
    public String toString(){
        return this.name;
    }

    public double getPrice(){
        return this.price;
    }

    public double getCalculatedPrice(int m2, int m3) {
        if(this.autoPriceCalculation == AutoPriceCalculation.STATIC) {
            return price;
        }
        if(this.autoPriceCalculation == AutoPriceCalculation.PER_M2) {
            return price * m2;
        }
        if(this.autoPriceCalculation == AutoPriceCalculation.PER_M3) {
            return price * m3;
        }
        return 0;
    }

    public long getExtendtime() {
        return this.extendtime;
    }

    public long getMaxrenttime() {
        return this.maxrenttime;
    }

    public static AutoPrice getAutoprice(String name) {
        for(AutoPrice autoPrice : autoPrices) {
            if(autoPrice.equals(name)) {
                return autoPrice;
            }
        }
        return null;
    }

    public static void reset(){
        autoPrices = new ArrayList<>();
    }

    public static void loadAutoprices(ConfigurationSection section) {
        AutoPrice.reset();
        if(section == null) {
            return;
        }

        List<String> autoPrices = new ArrayList<>(section.getKeys(false));
        if(autoPrices != null) {
            for(String autoPriceName : autoPrices){
                addDefaults(section.getConfigurationSection(autoPriceName));
                String name = autoPriceName;
                double price = section.getDouble(autoPriceName + ".price");
                long extendTime = section.getLong(autoPriceName + ".extendTime");
                long maxrenttime = section.getLong(autoPriceName + ".maxrenttime");
                AutoPriceCalculation autoPriceCalculation = AutoPriceCalculation.getAutoPriceType(section.getString(autoPriceName + ".autoPriceCalculation"));
                if(autoPriceCalculation == null) {
                    autoPriceCalculation = AutoPriceCalculation.STATIC;
                }

                AutoPrice.autoPrices.add(new AutoPrice(name, price, extendTime, maxrenttime, autoPriceCalculation));

            }
        }
    }

    private static void addDefaults(ConfigurationSection section) {
        section.addDefault("price", 0);
        section.addDefault("extendTime", 0);
        section.addDefault("maxrenttime", 0);
        section.addDefault("autoPriceCalculation", "static");
    }
}
