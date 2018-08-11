package net.liggesmeyer.arm.Preseter;

import net.liggesmeyer.arm.regions.RegionKind;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SellPreset {
    private static ArrayList<SellPreset> list = new ArrayList<>();
    private Player assignedPlayer;
    private boolean hasPrice = false;
    private double price = 0;
    private boolean hasRegionKind = false;
    private RegionKind regionKind = RegionKind.DEFAULT;
    private boolean hasAutoReset = false;
    private boolean autoReset = false;
    private boolean hasIsHotel = false;
    private boolean isHotel = false;

    public SellPreset(Player player){
        assignedPlayer = player;
    }

    public static ArrayList<SellPreset> getList(){
        return SellPreset.list;
    }

    public Player getAssignedPlayer(){
        return this.assignedPlayer;
    }

    public void setPrice(double price){
        if(price < 0){
            price = price * (-1);
        }
        this.hasPrice = true;
        this.price = price;
    }

    public void removePrice(){
        this.hasPrice = false;
        this.price = 0;
    }

    public void setRegionKind(RegionKind regionKind){
        if(regionKind == null) {
            regionKind = RegionKind.DEFAULT;
        }
        this.hasRegionKind = true;
        this.regionKind = regionKind;
    }

    public void removeRegionKind(){
        this.hasRegionKind = false;
        this.regionKind = RegionKind.DEFAULT;
    }

    public void setAutoReset(Boolean autoReset) {
        this.hasAutoReset = true;
        this.autoReset = autoReset;
    }

    public void removeAutoReset(){
        this.hasAutoReset = false;
        this.autoReset = true;
    }

    public void setHotel(Boolean isHotel){
        this.hasIsHotel = true;
        this.isHotel = isHotel;
    }

    public void removeIsHotel(){
        this.hasIsHotel = false;
        this.isHotel = false;
    }

    public static boolean hasPreset(Player player){
        for(int i = 0; i < SellPreset.getList().size(); i++) {
            if(SellPreset.getList().get(i).getAssignedPlayer() == player) {
                return true;
            }
        }
        return false;
    }

    public static boolean removePreset(Player player){
        for(int i = 0; i < SellPreset.getList().size(); i++) {
            if(SellPreset.getList().get(i).getAssignedPlayer() == player) {
                SellPreset.getList().remove(i);
                return true;
            }
        }
        return false;
    }

}
