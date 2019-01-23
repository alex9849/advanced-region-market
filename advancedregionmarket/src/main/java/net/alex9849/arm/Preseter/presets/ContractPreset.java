package net.alex9849.arm.Preseter.presets;

import net.alex9849.arm.Messages;
import net.alex9849.arm.regions.RentRegion;
import net.alex9849.arm.regions.RegionKind;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ContractPreset extends Preset {
    private boolean hasExtend = false;
    private long extend = 0;

    public ContractPreset(String name, boolean hasPrice, double price, RegionKind regionKind, boolean autoReset, boolean isHotel, boolean doBlockReset, boolean hasExtend, long extend, boolean isUserResettable, int allowedSubregions, AutoPrice autoPrice, List<String> setupCommands){
        super(name, hasPrice, price, regionKind, autoReset, isHotel, doBlockReset, isUserResettable, allowedSubregions, autoPrice, setupCommands);
        this.hasExtend = hasExtend;
        this.extend = extend;
    }

    public ContractPreset getCopy(){
        List<String> newsetupCommands = new ArrayList<>();
        for(String cmd : setupCommands) {
            newsetupCommands.add(cmd);
        }
        return new ContractPreset(this.name, this.hasPrice, this.price, this.regionKind, this.autoReset, this.isHotel, this.doBlockReset, this.hasExtend, this.extend, this.isUserResettable, this.allowedSubregions, this.autoPrice, newsetupCommands);
    }

    public boolean hasExtend() {
        return hasExtend;
    }

    public void removeExtend(){
        this.hasExtend = false;
        this.extend = 0;
    }

    public long getExtend(){
        return this.extend;
    }

    public void setExtend(String string) {
        this.hasExtend = true;
        this.extend = RentRegion.stringToTime(string);
        this.removeAutoPrice();
    }

    public void setExtend(long time) {
        this.hasExtend = true;
        this.extend = time;
        this.removeAutoPrice();
    }

    @Override
    public void setAutoPrice(AutoPrice autoPrice) {
        super.setAutoPrice(autoPrice);
        this.removeExtend();
    }

    private String longToTime(long time){

        long remainingDays = TimeUnit.DAYS.convert(time, TimeUnit.MILLISECONDS);
        time = time - (remainingDays * 1000 * 60 * 60 *24);

        long remainingHours = TimeUnit.HOURS.convert(time, TimeUnit.MILLISECONDS);
        time = time - (remainingHours * 1000 * 60 * 60);

        long remainingMinutes = TimeUnit.MINUTES.convert(time, TimeUnit.MILLISECONDS);
        time = time - (remainingMinutes * 1000 * 60);

        long remainingSeconds = TimeUnit.SECONDS.convert(time, TimeUnit.MILLISECONDS);


        String timetoString = "";
        if(remainingDays != 0) {
            timetoString = timetoString + remainingDays + "d";
        }
        if(remainingHours != 0) {
            timetoString = timetoString + remainingHours + "h";
        }
        if(remainingMinutes != 0) {
            timetoString = timetoString + remainingMinutes + "m";
        }
        if(remainingSeconds != 0) {
            timetoString = timetoString + remainingSeconds + "s";
        }

        return timetoString;
    }

    @Override
    public void getAdditionalInfo(Player player) {
        String extendtime = "not defined";
        if(this.hasExtend()) {
            extendtime = longToTime(this.extend);
        }
        player.sendMessage(Messages.REGION_INFO_AUTO_EXTEND_TIME + extendtime);
    }

    @Override
    public PresetType getPresetType() {
        return PresetType.CONTRACTPRESET;
    }

    @Override
    public boolean canPriceLineBeLetEmpty() {
        return (this.hasPrice() && this.hasExtend()) || this.hasAutoPrice();
    }

}
