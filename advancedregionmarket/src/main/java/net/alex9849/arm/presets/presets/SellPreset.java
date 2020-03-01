package net.alex9849.arm.presets.presets;

import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.SellRegion;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.inter.WGRegion;
import net.alex9849.signs.SignData;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class SellPreset extends Preset {

    public SellPreset(String name, boolean hasPrice, double price, RegionKind regionKind, FlagGroup flagGroup,
                      boolean inactivityReset, boolean isHotel, boolean doBlockReset, boolean isUserRestorable,
                      int allowedSubregions, AutoPrice autoPrice, EntityLimitGroup entityLimitGroup,
                      List<String> setupCommands, int maxMembers, int paybackPercentage) {
        super(name, hasPrice, price, regionKind, flagGroup, inactivityReset, isHotel, doBlockReset, isUserRestorable,
                allowedSubregions, autoPrice, entityLimitGroup, setupCommands, maxMembers, paybackPercentage);
    }

    @Override
    public void getAdditionalInfo(CommandSender sender) {
    }

    @Override
    public PresetType getPresetType() {
        return PresetType.SELLPRESET;
    }

    public SellPreset getCopy() {
        List<String> newsetupCommands = new ArrayList<>();
        for (String cmd : getCommands()) {
            newsetupCommands.add(cmd);
        }
        return new SellPreset(this.getName(), this.hasPrice(), this.getPrice(), this.getRegionKind(),
                this.getFlagGroup(), this.isInactivityReset(), this.isHotel(), this.isAutoRestore(),
                this.isUserRestorable(), this.getAllowedSubregions(), this.getAutoPrice(),
                this.getEntityLimitGroup(), newsetupCommands, this.getMaxMembers(), this.getPaybackPercentage());
    }

    @Override
    public boolean canPriceLineBeLetEmpty() {
        return this.hasPrice() || this.hasAutoPrice();
    }

    @Override
    public Region generateRegion(WGRegion wgRegion, World world, List<SignData> signs) {

        SellRegion sellRegion = new SellRegion(wgRegion, world, signs, new Price(AutoPrice.DEFAULT),
                false, this.isInactivityReset(), this.isHotel(), this.isAutoRestore(),
                this.getRegionKind(), this.getFlagGroup(), null, 0,
                new GregorianCalendar().getTimeInMillis(), this.isUserRestorable(), new ArrayList<>(),
                this.getAllowedSubregions(), this.getEntityLimitGroup(), new HashMap<>(), 0,
                this.getMaxMembers(), this.getPaybackPercentage());

        if (this.hasAutoPrice()) {
            sellRegion.setPrice(new Price(this.getAutoPrice()));
        } else if (this.hasPrice()) {
            sellRegion.setPrice(new Price(this.getPrice()));
        }
        return sellRegion;
    }

}
