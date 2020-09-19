package net.alex9849.arm.presets.presets;

import net.alex9849.arm.Messages;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.util.Saveable;
import net.alex9849.arm.util.stringreplacer.StringCreator;
import net.alex9849.arm.util.stringreplacer.StringReplacer;
import net.alex9849.inter.WGRegion;
import net.alex9849.signs.SignData;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Preset implements Saveable, Cloneable {
    private String name = "default";
    private Boolean inactivityReset;
    private Boolean isHotel;
    private Boolean autoRestore;
    private Boolean isUserRestorable;
    private Integer allowedSubregions;
    private Integer maxMembers;
    private Integer paybackPercentage;
    private Double price;
    private AutoPrice autoPrice;
    private RegionKind regionKind;
    private FlagGroup flagGroup;
    private EntityLimitGroup entityLimitGroup;
    private List<String> setupCommands = new ArrayList<>();
    private boolean needsSave = false;
    private StringReplacer stringReplacer = new StringReplacer(getVariableReplacements(), 50);

    /*#########################
    ######### Getter ##########
    #########################*/

    public String getName() {
        return this.name;
    }

    public Boolean isUserRestorable() {
        return this.isUserRestorable;
    }

    public Boolean isInactivityReset() {
        return this.inactivityReset;
    }

    public Boolean isHotel() {
        return isHotel;
    }

    public Boolean isAutoRestore() {
        return autoRestore;
    }

    public Integer getPaybackPercentage() {
        return paybackPercentage;
    }

    public Integer getAllowedSubregions() {
        return this.allowedSubregions;
    }

    public Integer getMaxMembers() {
        return maxMembers;
    }

    public Double getPrice() {
        return price;
    }

    public AutoPrice getAutoPrice() {
        return this.autoPrice;
    }

    public List<String> getCommands() {
        return this.setupCommands;
    }

    public FlagGroup getFlagGroup() {
        return this.flagGroup;
    }

    public EntityLimitGroup getEntityLimitGroup() {
        return this.entityLimitGroup;
    }

    public RegionKind getRegionKind() {
        return regionKind;
    }

    @Override
    public boolean needsSave() {
        return this.needsSave;
    }


    /*##########################
    ########## Setter ##########
    ###########################*/

    @Override
    public void queueSave() {
        this.needsSave = true;
    }

    @Override
    public void setSaved() {
        this.needsSave = false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPaybackPercentage(Integer paybackPercentage) {
        this.paybackPercentage = paybackPercentage;
    }

    public void setAllowedSubregions(Integer allowedSubregions) {
        this.allowedSubregions = allowedSubregions;
    }

    public void setMaxMembers(Integer maxMembers) {
        this.maxMembers = maxMembers;
    }

    public void setUserRestorable(Boolean isUserRestorable) {
        this.isUserRestorable = isUserRestorable;
    }

    public void setAutoRestore(Boolean bool) {
        this.autoRestore = bool;
    }

    public void setHotel(Boolean isHotel) {
        this.isHotel = isHotel;
    }

    public void setInactivityReset(Boolean InactivityReset) {
        this.inactivityReset = InactivityReset;
    }

    public void setPrice(Double price) {
        if(price != null) {
            this.autoPrice = null;
            if (price < 0) {
                price = price * (-1);
            }
        }
        this.price = price;
    }

    public void setAutoPrice(AutoPrice autoPrice) {
        this.autoPrice = autoPrice;
        if(autoPrice != null) {
            this.price = null;
        }
    }

    public void setRegionKind(RegionKind regionKind) {
        this.regionKind = regionKind;
    }

    public void setFlagGroup(FlagGroup flagGroup) {
        this.flagGroup = flagGroup;
    }

    public void setEntityLimitGroup(EntityLimitGroup entityLimitGroup) {
        this.entityLimitGroup = entityLimitGroup;
    }

    public void addCommand(String command) {
        this.setupCommands.add(command);
    }

    public void addCommand(List<String> command) {
        this.setupCommands.addAll(command);
    }

    public void removeCommand(int index) {

        if (index < 0) {
            return;
        }

        if (this.setupCommands.size() > index) {
            this.setupCommands.remove(index);
        }
    }


    /*##########################
    ##### Abstract methods #####
    ##########################*/

    public abstract void sendPresetInfo(CommandSender sender);

    public abstract PresetType getPresetType();

    public abstract boolean canPriceLineBeLetEmpty();

    /**
     * Generates a region Object, with the type of the preset and with default settings
     *
     * @param wgRegion The WorldGuard region
     * @param world The world of the WorldGuard region
     * @param signs The signs that should be linked to the region
     * @return The region
     */
    protected abstract Region generateBasicRegion(WGRegion wgRegion, World world, List<SignData> signs);


    /*##########################
    ####### Other stuff ########
    ##########################*/

    public Object clone() throws CloneNotSupportedException {
        Object obj = super.clone();
        if(obj instanceof Preset) {
            Preset preset = (Preset) obj;
            preset.setupCommands = new ArrayList<>();
            preset.setupCommands.addAll(this.getCommands());
            preset.stringReplacer = new StringReplacer(preset.getVariableReplacements(), 50);
        }
        return obj;
    }

    public HashMap<String, StringCreator> getVariableReplacements() {
        HashMap<String, StringCreator> variableReplacements = new HashMap<>();
        variableReplacements.put("%presetname%", this::getName);
        variableReplacements.put("%presetinactivityreset%", () -> Messages.getStringValue(this.isInactivityReset(), Messages::convertYesNo, Messages.NOT_DEFINED));
        variableReplacements.put("%presetishotel%", () -> Messages.getStringValue(this.isHotel(), Messages::convertYesNo, Messages.NOT_DEFINED));
        variableReplacements.put("%presetautorestore%", () -> Messages.getStringValue(this.isAutoRestore(), Messages::convertYesNo, Messages.NOT_DEFINED));
        variableReplacements.put("%presetisuserrestorable%", () -> Messages.getStringValue(this.isUserRestorable(), Messages::convertYesNo, Messages.NOT_DEFINED));
        variableReplacements.put("%presetallowedsubregions%", () -> Messages.getStringValue(this.getAllowedSubregions(), Object::toString, Messages.NOT_DEFINED));
        variableReplacements.put("%presetmaxmembers%", () -> Messages.getStringValue(this.getMaxMembers(), Object::toString, Messages.NOT_DEFINED));
        variableReplacements.put("%presetpaybackpercentage%", () -> Messages.getStringValue(this.getPaybackPercentage(), Object::toString, Messages.NOT_DEFINED));
        variableReplacements.put("%presetprice%", () -> Messages.getStringValue(this.getPrice(), Object::toString, Messages.NOT_DEFINED));
        variableReplacements.put("%presetautoprice%", () -> Messages.getStringValue(this.getAutoPrice(), AutoPrice::getName, Messages.NOT_DEFINED));
        variableReplacements.put("%presetregionkind%", () -> Messages.getStringValue(this.getRegionKind(), RegionKind::getName, Messages.NOT_DEFINED));
        variableReplacements.put("%presetflaggroup%", () -> Messages.getStringValue(this.getFlagGroup(), FlagGroup::getName, Messages.NOT_DEFINED));
        variableReplacements.put("%presetentitylimitgroup%", () -> Messages.getStringValue(this.getEntityLimitGroup(), EntityLimitGroup::getName, Messages.NOT_DEFINED));
        variableReplacements.put("%presetsetupcommands%", () -> Messages.getStringList(this.setupCommands.stream()
                .map(x -> (this.setupCommands.indexOf(x) + 1) + ". /" + x).collect(Collectors.toList()), x -> x, "\n"));
        return variableReplacements;
    }

    public void executeSetupCommands(CommandSender sender, Region region) {
        for (String command : this.setupCommands) {
            String cmd = region.replaceVariables(command);

            if (sender instanceof Player) {
                ((Player) sender).performCommand(cmd);
            } else {
                Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd);
            }
        }
    }

    /**
     * Generates a region with the settings of the preset
     * Does not create a schematic! Does not apply flaggroups!
     *
     * @param wgRegion The WorldGuard region
     * @param world    The world of the WorldGuard region
     * @param sender   The sender that executes the saved commands
     * @param signs    The signs that should be linked to the region
     * @param withSetupCommands If true setupcommands will be executed for the newly generated region
     * @return A Region with the given arguments
     */
    public Region generateRegion(WGRegion wgRegion, World world, CommandSender sender, boolean withSetupCommands, List<SignData> signs) {
        Region region = generateBasicRegion(wgRegion, world, signs);
        this.applyToRegion(region, sender, withSetupCommands);
        return region;
    }

    public void applyToRegion(Region region, CommandSender sender, boolean withSetupCommands) {
        this.applyToRegion(region);
        if(withSetupCommands) {
            this.executeSetupCommands(sender, region);
        }
    }

    /**
     * Applies settings of this preset to a region
     * @param region the region
     */
    public void applyToRegion(Region region) {
        if(this.autoPrice != null)
            region.setAutoPrice(this.getAutoPrice());
        if(this.inactivityReset != null)
            region.setInactivityReset(this.isInactivityReset());
        if(this.isHotel != null)
            region.setHotel(this.isHotel());
        if(this.autoRestore != null)
            region.setAutoRestore(this.isAutoRestore());
        if(this.regionKind != null)
            region.setRegionKind(this.getRegionKind());
        if(this.flagGroup != null)
            region.setFlagGroup(this.getFlagGroup());
        if(this.isUserRestorable != null)
            region.setUserRestorable(this.isUserRestorable());
        if(this.allowedSubregions != null)
            region.setAllowedSubregions(this.getAllowedSubregions());
        if(this.entityLimitGroup != null)
            region.setEntityLimitGroup(this.getEntityLimitGroup());
        if(this.maxMembers != null)
            region.setMaxMembers(this.getMaxMembers());
        if(this.paybackPercentage != null)
            region.setPaybackPercentage(this.getPaybackPercentage());
    }

    @Override
    public ConfigurationSection toConfigurationSection() {
        ConfigurationSection section = new YamlConfiguration();
        section.set("price", this.getPrice());
        section.set("userrestorable", this.isUserRestorable());
        section.set("allowedSubregions", this.getAllowedSubregions());
        section.set("isHotel", this.isHotel());
        section.set("autorestore", this.isAutoRestore());
        section.set("paybackPercentage", this.getPaybackPercentage());
        section.set("maxMembers", this.getMaxMembers());
        section.set("inactivityReset", this.isInactivityReset());
        if(this.getFlagGroup() != null)
            section.set("flaggroup", this.getFlagGroup().getName());
        if(this.getRegionKind() != null)
            section.set("regionKind", this.getRegionKind().getName());
        if(this.getEntityLimitGroup() != null)
            section.set("entityLimitGroup", this.getEntityLimitGroup().getName());
        if (this.autoPrice != null) {
            section.set("autoPrice", this.getAutoPrice().getName());
        }
        section.set("setupcommands", this.getCommands());
        return section;
    }

    public String replaceVariables(String message) {
        return this.stringReplacer.replace(message).toString();
    }

}
