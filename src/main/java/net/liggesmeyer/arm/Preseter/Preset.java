package net.liggesmeyer.arm.Preseter;

import net.liggesmeyer.arm.Messages;
import net.liggesmeyer.arm.regions.RegionKind;
import net.liggesmeyer.arm.regions.RentRegion;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public abstract class Preset {
    protected Player assignedPlayer;
    protected boolean hasPrice = false;
    protected double price = 0;
    protected boolean hasRegionKind = false;
    protected RegionKind regionKind = RegionKind.DEFAULT;
    protected boolean hasAutoReset = false;
    protected boolean autoReset = false;
    protected boolean hasIsHotel = false;
    protected boolean isHotel = false;
    protected boolean hasDoBlockReset = false;
    protected boolean doBlockReset = true;

    public Preset(Player player){
        assignedPlayer = player;
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

    public abstract void getPresetInfo(Player player);

    public void removePrice(){
        this.hasPrice = false;
        this.price = 0;
    }

    public boolean hasPrice() {
        return hasPrice;
    }

    public boolean hasRegionKind() {
        return hasRegionKind;
    }

    public boolean hasAutoReset() {
        return hasAutoReset;
    }

    public boolean hasIsHotel() {
        return hasIsHotel;
    }

    public boolean isHasDoBlockReset() {
        return hasDoBlockReset;
    }

    public void setRegionKind(RegionKind regionKind){
        if(regionKind == null) {
            regionKind = RegionKind.DEFAULT;
        }
        this.hasRegionKind = true;
        this.regionKind = regionKind;
    }

    public void setDoBlockReset(Boolean bool){
        this.hasDoBlockReset = true;
        this.doBlockReset = bool;
    }

    public void removeDoBlockReset(){
        this.hasDoBlockReset = false;
        this.doBlockReset = true;
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

    public void removeHotel(){
        this.hasIsHotel = false;
        this.isHotel = false;
    }

    public double getPrice() {
        return price;
    }

    public RegionKind getRegionKind() {
        return regionKind;
    }

    public boolean isAutoReset() {
        return autoReset;
    }

    public boolean isDoBlockReset() {
        return doBlockReset;
    }

    public boolean isHotel() {
        return isHotel;
    }
}
