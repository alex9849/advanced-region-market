package net.alex9849.arm.regions.price.Autoprice;

import net.alex9849.arm.regions.price.ContractPrice;
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
        if(autoPriceCalculation == null) {
            this.autoPriceCalculation = AutoPriceCalculation.STATIC;
        } else {
            this.autoPriceCalculation = autoPriceCalculation;
        }
    }

    public boolean equals(String name){
        return this.name.equalsIgnoreCase(name);
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
        if(name.equalsIgnoreCase("default")) {
            return AutoPrice.DEFAULT;
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
                String extendTimeString = section.getString(autoPriceName + ".extendTime");
                String maxRentTimeString = section.getString(autoPriceName + ".maxRentTime");
                long extendTime = ContractPrice.stringToTime(extendTimeString);
                long maxrenttime = ContractPrice.stringToTime(maxRentTimeString);
                AutoPriceCalculation autoPriceCalculation = AutoPriceCalculation.getAutoPriceType(section.getString(autoPriceName + ".autoPriceCalculation"));
                if(autoPriceCalculation == null) {
                    autoPriceCalculation = AutoPriceCalculation.STATIC;
                }

                AutoPrice.autoPrices.add(new AutoPrice(name, price, extendTime, maxrenttime, autoPriceCalculation));

            }
        }
    }

    public static void loadDefaultAutoPrice(ConfigurationSection section) {
        if(section == null) {
            return;
        }
        AutoPrice.DEFAULT.setPrice(section.getDouble("price"));
        AutoPrice.DEFAULT.setExtendtime(section.getLong("extendTime"));
        AutoPrice.DEFAULT.setMaxrenttime(section.getLong("maxRentTime"));
        AutoPriceCalculation autoPriceCalculation = AutoPriceCalculation.getAutoPriceType(section.getString("autoPriceCalculation"));
        if(autoPriceCalculation == null) {
            autoPriceCalculation = AutoPriceCalculation.STATIC;
        }
        AutoPrice.DEFAULT.setAutoPriceCalculation(autoPriceCalculation);
    }

    private static void addDefaults(ConfigurationSection section) {
        section.addDefault("price", 0);
        section.addDefault("extendTime", 0);
        section.addDefault("maxrenttime", 0);
        section.addDefault("autoPriceCalculation", "static");
    }

    public String getName() {
        return this.name;
    }

    public static List<AutoPrice> getAutoPrices() {
        return autoPrices;
    }

    public static List<String> tabCompleteAutoPrice(String string) {
        List<String> returnme = new ArrayList<>();
        for(AutoPrice ap : autoPrices) {
            if(ap.getName().toLowerCase().startsWith(string)) {
                returnme.add(ap.getName());
            }
        }
        return returnme;
    }

    private void setPrice(double price) {
        this.price = price;
    }

    private void setExtendtime(long extendtime) {
        this.extendtime = extendtime;
    }

    private void setMaxrenttime(long maxrenttime) {
        this.maxrenttime = maxrenttime;
    }

    private void setAutoPriceCalculation(AutoPriceCalculation autoPriceCalculation) {
        if(autoPriceCalculation == null) {
            this.autoPriceCalculation = AutoPriceCalculation.STATIC;
        } else {
            this.autoPriceCalculation = autoPriceCalculation;
        }
    }
}
