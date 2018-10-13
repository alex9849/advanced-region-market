package net.alex9849.arm.minifeatures;

import java.util.ArrayList;
import java.util.List;

public class AutoPrice {
    private static List<AutoPrice> autoPrices = new ArrayList<>();
    private double pricepersquaremeter;
    private String name;

    public AutoPrice(String name, double pricepersquaremeter){
        this.pricepersquaremeter = pricepersquaremeter;
        this.name = name;
    }

    public boolean equals(String name){
        return this.name.equalsIgnoreCase(name);
    }

    @Override
    public String toString(){
        return this.name;
    }

    public double getPricePerSquareMeter(){
        return pricepersquaremeter;
    }

    public static List<AutoPrice> getAutoPrices(){
        return autoPrices;
    }

    public static void Reset(){
        autoPrices = new ArrayList<>();
    }
}
