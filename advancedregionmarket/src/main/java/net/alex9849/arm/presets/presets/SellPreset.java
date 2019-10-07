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
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class SellPreset extends Preset {

    public SellPreset(String name, boolean hasPrice, double price, RegionKind regionKind, FlagGroup flagGroup, boolean inactivityReset, boolean isHotel, boolean doBlockReset, boolean isUserResettable, int allowedSubregions, AutoPrice autoPrice, EntityLimitGroup entityLimitGroup, List<String> setupCommands) {
        super(name, hasPrice, price, regionKind, flagGroup, inactivityReset, isHotel, doBlockReset, isUserResettable, allowedSubregions, autoPrice, entityLimitGroup, setupCommands);
    }

    @Override
    public void getAdditionalInfo(Player player) {
    }

    @Override
    public PresetType getPresetType() {
        return PresetType.SELLPRESET;
    }

    public SellPreset getCopy() {
        List<String> newsetupCommands = new ArrayList<>();
        for (String cmd : setupCommands) {
            newsetupCommands.add(cmd);
        }
        return new SellPreset(this.name, this.hasPrice, this.price, this.regionKind, this.flagGroup, this.inactivityReset, this.isHotel, this.doBlockReset, this.isUserResettable, this.allowedSubregions, this.autoPrice, this.entityLimitGroup, newsetupCommands);
    }

    @Override
    public boolean canPriceLineBeLetEmpty() {
        return this.hasPrice() || this.hasAutoPrice();
    }

    @Override
    public Region generateRegion(WGRegion wgRegion, World world, List<SignData> signs) {

        SellRegion sellRegion = new SellRegion(wgRegion, world, signs, new Price(AutoPrice.DEFAULT), false, this.isInactivityReset(), this.isHotel(), this.isDoBlockReset(), this.getRegionKind(), this.getFlagGroup(), null, 0, new GregorianCalendar().getTimeInMillis(), this.isUserResettable(), new ArrayList<>(), this.getAllowedSubregions(), this.entityLimitGroup, new HashMap<>(), 0);
        if (this.hasAutoPrice()) {
            sellRegion.setPrice(new Price(this.getAutoPrice()));
        } else if (this.hasPrice()) {
            sellRegion.setPrice(new Price(this.getPrice()));
        }
        return sellRegion;
    }

}
