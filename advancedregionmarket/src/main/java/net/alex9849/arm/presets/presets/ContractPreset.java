package net.alex9849.arm.presets.presets;

import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.ContractRegion;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.ContractPrice;
import net.alex9849.inter.WGRegion;
import net.alex9849.signs.SignData;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class ContractPreset extends CountdownPreset {


    public ContractPreset(String name, boolean hasPrice, double price, RegionKind regionKind, FlagGroup flagGroup,
                          boolean inactivityReset, boolean isHotel, boolean doBlockReset, boolean hasExtend,
                          long extend, boolean isUserRestorable, int allowedSubregions, AutoPrice autoPrice,
                          EntityLimitGroup entityLimitGroup, List<String> setupCommands, int maxMembers, int paybackPercentage) {
        super(name, hasPrice, price, regionKind, flagGroup, inactivityReset, isHotel, doBlockReset, hasExtend,
                extend, isUserRestorable, allowedSubregions, autoPrice, entityLimitGroup, setupCommands,
                maxMembers, paybackPercentage);
    }

    @Override
    public PresetType getPresetType() {
        return PresetType.CONTRACTPRESET;
    }

    @Override
    protected ContractRegion generateBasicRegion(WGRegion wgRegion, World world, List<SignData> signs) {
        return new ContractRegion(wgRegion, world, signs, new ContractPrice(AutoPrice.DEFAULT), false, new ArrayList<>());
    }
}
