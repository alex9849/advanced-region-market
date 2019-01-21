package net.alex9849.arm.Preseter.presets;

import net.alex9849.arm.Messages;
import net.alex9849.arm.regions.RegionKind;
import net.alex9849.arm.regions.RentRegion;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RentPreset extends Preset {
    private boolean hasMaxRentTime = false;
    private long maxRentTime = 0;
    private boolean hasExtendPerClick = false;
    private long extendPerClick = 0;

    public RentPreset(String name, boolean hasPrice, double price, RegionKind regionKind, boolean autoReset, boolean isHotel, boolean doBlockReset, boolean hasMaxRentTime,
                      long maxRentTime, boolean hasExtendPerClick, long extendPerClick, List<String> setupCommands){
        super(name, hasPrice, price, regionKind, autoReset, isHotel, doBlockReset, setupCommands);
        this.hasMaxRentTime = hasMaxRentTime;
        this.maxRentTime = maxRentTime;
        this.hasExtendPerClick = hasExtendPerClick;
        this.extendPerClick = extendPerClick;
    }

    public RentPreset getCopy(){
        List<String> newsetupCommands = new ArrayList<>();
        for(String cmd : setupCommands) {
            newsetupCommands.add(cmd);
        }
        return new RentPreset(this.name, this.hasPrice, this.price, this.regionKind, this.autoReset, this.isHotel, this.doBlockReset, this.hasMaxRentTime, this.maxRentTime, this.hasExtendPerClick, this.extendPerClick, newsetupCommands);
    }

    public boolean hasExtendPerClick() {
        return hasExtendPerClick;
    }

    public void removeExtendPerClick(){
        this.hasExtendPerClick = false;
        this.extendPerClick = 0;
    }

    public long getMaxRentTime(){
        return this.maxRentTime;
    }

    public long getExtendPerClick(){
        return this.extendPerClick;
    }

    public void setExtendPerClick(String string) {
        this.hasExtendPerClick = true;
        this.extendPerClick = RentRegion.stringToTime(string);
    }

    public void setExtendPerClick(long time) {
        this.hasExtendPerClick = true;
        this.extendPerClick = time;
    }

    public void setMaxRentTime(String string) {
        this.hasMaxRentTime = true;
        this.maxRentTime = RentRegion.stringToTime(string);
    }

    public void setMaxRentTime(Long time) {
        this.hasMaxRentTime = true;
        this.maxRentTime = time;
    }

    public boolean hasMaxRentTime() {
        return hasMaxRentTime;
    }

    public void removeMaxRentTime() {
        this.hasMaxRentTime = false;
        this.maxRentTime = 0;
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
        player.sendMessage(Messages.REGION_INFO_EXTEND_PER_CLICK + longToTime(this.extendPerClick));
        player.sendMessage(Messages.REGION_INFO_MAX_RENT_TIME + longToTime(this.maxRentTime));
    }

    @Override
    public PresetType getPresetType() {
        return PresetType.RENTPRESET;
    }

}
