package net.alex9849.arm.regions.price.Autoprice;

import net.alex9849.arm.regions.price.ContractPrice;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class AutoPrice {
    public static AutoPrice DEFAULT = new AutoPrice("default", 0, 1000, 1000, AutoPriceCalculation.STATIC);
    private static List<AutoPrice> autoPrices = new ArrayList<>();
    private AutoPriceCalculation autoPriceCalculation;
    private double price;
    private long extendtime;
    private long maxrenttime;
    private String name;

    public AutoPrice(@Nonnull String name, double price, long extendtime, long maxrenttime,
                     @Nonnull AutoPriceCalculation autoPriceCalculation) {
        if(price < 0)
            throw new IllegalArgumentException("AutoPrice price needs to be positive!");
        if(extendtime < 1000)
            throw new IllegalArgumentException("AutoPrice extendTime needs to be at least one second!");
        if(maxrenttime < 1000)
            throw new IllegalArgumentException("AutoPrice maxRentTime needs to be at least one second!");
        this.price = price;
        this.name = name;
        this.extendtime = extendtime;
        this.maxrenttime = maxrenttime;
        this.autoPriceCalculation = autoPriceCalculation;
    }

    public double getPrice() {
        return this.price;
    }

    public long getExtendtime() {
        return this.extendtime;
    }

    public long getMaxrenttime() {
        return this.maxrenttime;
    }

    public String getName() {
        return this.name;
    }

    public double getCalculatedPrice(int m2, int m3) {
        if (this.autoPriceCalculation == AutoPriceCalculation.STATIC) {
            return price;
        }
        if (this.autoPriceCalculation == AutoPriceCalculation.PER_M2) {
            return price * m2;
        }
        if (this.autoPriceCalculation == AutoPriceCalculation.PER_M3) {
            return price * m3;
        } else {
            throw new RuntimeException("Calculationmethod for AutoPriceCalculation " + this.autoPriceCalculation.name() + " not found!");
        }
    }

    public static AutoPrice getAutoprice(String name) {
        for (AutoPrice autoPrice : autoPrices) {
            if (autoPrice.getName().equals(name)) {
                return autoPrice;
            }
        }
        if (name.equalsIgnoreCase("default")) {
            return AutoPrice.DEFAULT;
        }
        return null;
    }

    public static void reset() {
        autoPrices = new ArrayList<>();
    }

    public static void loadAutoprices(ConfigurationSection section) {
        AutoPrice.reset();
        if (section == null) {
            return;
        }

        List<String> autoPrices = new ArrayList<>(section.getKeys(false));
        for (String autoPriceName : autoPrices) {
            AutoPrice.autoPrices.add(loadAutoPrice(section.getConfigurationSection(autoPriceName), autoPriceName));
        }
    }

    public static void loadDefaultAutoPrice(ConfigurationSection section) {
        if (section == null) {
            return;
        }
        AutoPrice.DEFAULT = loadAutoPrice(section, "default");
    }

    private static AutoPrice loadAutoPrice(ConfigurationSection section, String autoPriceName) {
        addDefaults(section);
        double price = section.getDouble("price");
        long extendTime = ContractPrice.stringToTime(section.getString("extendTime"));
        long maxrenttime = ContractPrice.stringToTime(section.getString("maxRentTime"));
        AutoPriceCalculation autoPriceCalculation = AutoPriceCalculation.getAutoPriceType(section.getString("autoPriceCalculation"));
        if (autoPriceCalculation == null) {
            autoPriceCalculation = AutoPriceCalculation.STATIC;
        }
        return new AutoPrice(autoPriceName, price, extendTime, maxrenttime, autoPriceCalculation);
    }

    private static void addDefaults(ConfigurationSection section) {
        section.addDefault("price", 0);
        section.addDefault("extendTime", 0);
        section.addDefault("maxrenttime", 0);
        section.addDefault("autoPriceCalculation", "static");
    }

    public static List<AutoPrice> getAutoPrices() {
        return autoPrices;
    }

    public static List<String> tabCompleteAutoPrice(String string) {
        List<String> returnme = new ArrayList<>();
        for (AutoPrice ap : autoPrices) {
            if (ap.getName().toLowerCase().startsWith(string)) {
                returnme.add(ap.getName());
            }
        }
        return returnme;
    }
}
