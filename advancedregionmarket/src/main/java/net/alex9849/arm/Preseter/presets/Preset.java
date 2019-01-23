package net.alex9849.arm.Preseter.presets;

import net.alex9849.arm.Messages;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.RegionKind;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Preset {
    protected String name = "default";
    protected boolean hasPrice = false;
    protected double price = 0;
    protected RegionKind regionKind = RegionKind.DEFAULT;
    protected boolean autoReset = true;
    protected boolean isHotel = false;
    protected boolean doBlockReset = true;
    protected boolean isUserResettable = true;
    protected int allowedSubregions = 0;
    protected AutoPrice autoPrice;
    protected List<String> setupCommands = new ArrayList<>();

    public Preset(String name, boolean hasPrice, double price, RegionKind regionKind, boolean autoReset, boolean isHotel, boolean doBlockReset, boolean isUserResettable, int allowedSubregions, AutoPrice autoPrice, List<String> setupCommands){
        this.name = name;
        this.hasPrice = hasPrice;
        this.price = price;
        this.regionKind = regionKind;
        this.autoReset = autoReset;
        this.isHotel = isHotel;
        this.doBlockReset = doBlockReset;
        this.isUserResettable = isUserResettable;
        this.allowedSubregions = allowedSubregions;
        this.setupCommands = setupCommands;
        this.autoPrice = autoPrice;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setUserResettable(boolean isUserResettable) {
        this.isUserResettable = isUserResettable;
    }

    public boolean isUserResettable() {
        return this.isUserResettable;
    }

    public void setAllowedSubregions(int allowedSubregions) {
        this.allowedSubregions = allowedSubregions;
    }

    public int getAllowedSubregions() {
        return this.allowedSubregions;
    }

    public void addCommand(String command) {
        this.setupCommands.add(command);
    }

    public void addCommand(List<String> command) {
        this.setupCommands.addAll(command);
    }

    public List<String> getCommands() {
        return this.setupCommands;
    }

    public void executeSavedCommands(Player player, Region region) {
        for(String command : this.setupCommands) {
            String cmd = region.getConvertedMessage(command);
            cmd = cmd.replace("%regionkind%", region.getRegionKind().getName());
            cmd = cmd.replace("%regionkinddisplay%", region.getRegionKind().getDisplayName());

            player.performCommand(cmd);
        }
    }

    public void removeCommand(int index) {

        if(index < 0) {
            return;
        }

        if(this.setupCommands.size() > index) {
            this.setupCommands.remove(index);
        }
    }

    public void removeAutoPrice() {
        this.autoPrice = null;
    }

    public void setAutoPrice(AutoPrice autoPrice) {
        this.autoPrice = autoPrice;
        this.removePrice();
    }

    public boolean hasAutoPrice() {
        return this.autoPrice != null;
    }

    public AutoPrice getAutoPrice() {
        return this.autoPrice;
    }

    public void setPrice(double price){
        if(price < 0){
            price = price * (-1);
        }
        this.hasPrice = true;
        this.price = price;
        this.removeAutoPrice();
    }

    public void getPresetInfo(Player player) {
        String price = "not defined";
        String autoPrice = Messages.convertYesNo(this.hasAutoPrice());
        if(this.hasPrice()) {
            price = this.getPrice() + "";
        }
        if(this.hasAutoPrice()) {
            autoPrice = this.getAutoPrice().getName();
        }
        RegionKind regKind = this.getRegionKind();

        player.sendMessage(ChatColor.GOLD + "=========[Preset INFO]=========");
        player.sendMessage(Messages.REGION_INFO_AUTOPRICE + autoPrice);
        player.sendMessage(Messages.REGION_INFO_PRICE + price);
        this.getAdditionalInfo(player);
        player.sendMessage(Messages.REGION_INFO_TYPE + regKind.getName());
        player.sendMessage(Messages.REGION_INFO_AUTORESET + this.isAutoReset());
        player.sendMessage(Messages.REGION_INFO_HOTEL + this.isHotel());
        player.sendMessage(Messages.REGION_INFO_DO_BLOCK_RESET + this.isDoBlockReset());
        player.sendMessage(Messages.REGION_INFO_IS_USER_RESETTABLE + this.isUserResettable());
        player.sendMessage(Messages.REGION_INFO_ALLOWED_SUBREGIONS + this.getAllowedSubregions());
        player.sendMessage(Messages.PRESET_SETUP_COMMANDS);
        for(int i = 0; i < this.setupCommands.size(); i++) {
            String message = (i + 1) +". /" + this.setupCommands.get(i);
            player.sendMessage(ChatColor.GOLD + message);
        }
    }

    public abstract void getAdditionalInfo(Player player);

    public void removePrice(){
        this.hasPrice = false;
        this.price = 0;
    }

    public boolean hasPrice() {
        return hasPrice;
    }

    public void setRegionKind(RegionKind regionKind){
        if(regionKind == null) {
            regionKind = RegionKind.DEFAULT;
        }
        this.regionKind = regionKind;
    }

    public void setDoBlockReset(Boolean bool){
        this.doBlockReset = bool;
    }

    public void setAutoReset(Boolean autoReset) {
        this.autoReset = autoReset;
    }

    public void setHotel(Boolean isHotel){
        this.isHotel = isHotel;
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

    public abstract PresetType getPresetType();

    public abstract Preset getCopy();

    public abstract boolean canPriceLineBeLetEmpty();
}
