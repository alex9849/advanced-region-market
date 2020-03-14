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

    @Override
    public boolean canPriceLineBeLetEmpty() {
        return this.hasPrice() || this.hasAutoPrice();
    }

    @Override
    protected SellRegion generateBasicRegion(WGRegion wgRegion, World world, List<SignData> signs) {
        return new SellRegion(wgRegion, world, signs, new Price(AutoPrice.DEFAULT), false, new ArrayList<>());
    }

    @Override
    public void applyToRegion(Region region) {
        super.applyToRegion(region);
        if(this.hasPrice()) {
            region.setPrice(new Price(this.getPrice()));
        }
    }

}
