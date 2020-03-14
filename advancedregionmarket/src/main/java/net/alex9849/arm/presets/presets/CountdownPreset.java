package net.alex9849.arm.presets.presets;

import net.alex9849.arm.Messages;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.ContractPrice;
import net.alex9849.arm.regions.price.RentPrice;
import net.alex9849.arm.util.TimeUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

public abstract class CountdownPreset extends Preset {
    private Long extendTime;

    public Long getExtendTime() {
        return this.extendTime;
    }

    public void setExtendTime(String string) {
        this.setExtendTime(RentPrice.stringToTime(string));
    }

    public void setExtendTime(Long time) {
        this.extendTime = time;
        if(time != null) {
            this.setAutoPrice(null);
        }
    }

    @Override
    public void setAutoPrice(AutoPrice autoPrice) {
        super.setAutoPrice(autoPrice);
        if(autoPrice != null) {
            this.extendTime = null;
        }
    }

    @Override
    public void getAdditionalInfo(CommandSender sender) {
        String extendtime = "not defined";
        if (this.extendTime != null) {
            extendtime = TimeUtil.timeInMsToString(this.extendTime, false, false);
        }
        sender.sendMessage(Messages.REGION_INFO_AUTO_EXTEND_TIME + extendtime);
    }

    @Override
    public boolean canPriceLineBeLetEmpty() {
        return (this.getPrice() != null && this.getExtendTime() != null) || this.getAutoPrice() != null;
    }

    @Override
    public void applyToRegion(Region region) {
        super.applyToRegion(region);
        if (this.getPrice() != null && this.getExtendTime() != null) {
            region.setPrice(new ContractPrice(this.getPrice(), this.getExtendTime()));
        }
    }

    @Override
    public ConfigurationSection toConfigurationSection() {
        ConfigurationSection section = super.toConfigurationSection();
        section.set("extendTime", this.getExtendTime());
        return section;
    }
}
