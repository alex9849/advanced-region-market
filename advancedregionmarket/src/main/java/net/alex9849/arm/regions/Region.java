package net.alex9849.arm.regions;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.ArmSettings;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.entitylimit.EntityLimit;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.events.PreBuyEvent;
import net.alex9849.arm.events.RestoreRegionEvent;
import net.alex9849.arm.events.UnsellRegionEvent;
import net.alex9849.arm.events.UpdateRegionEvent;
import net.alex9849.arm.exceptions.*;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.inactivityexpiration.InactivityExpirationGroup;
import net.alex9849.arm.inactivityexpiration.PlayerInactivityGroupMapper;
import net.alex9849.arm.minifeatures.ParticleBorder;
import net.alex9849.arm.minifeatures.teleporter.Teleporter;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.arm.util.Saveable;
import net.alex9849.arm.util.TimeUtil;
import net.alex9849.arm.util.stringreplacer.StringCreator;
import net.alex9849.arm.util.stringreplacer.StringReplacer;
import net.alex9849.inter.WGRegion;
import net.alex9849.signs.SignData;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

public abstract class Region implements Saveable {
    private Integer m2Amount;
    private WGRegion region;
    private World regionworld;
    private ArrayList<SignData> sellsign;
    private Region parentRegion;
    private Set<Region> subregions;
    private Location teleportLocation;
    private StringReplacer stringReplacer;
    private boolean sold;
    private boolean inactivityReset = true;
    private boolean isHotel = false;
    private boolean isUserRestorable = true;
    private boolean needsSave = false;
    private boolean isAutoRestore = true;
    private long lastreset = 0;
    private long lastLogin = 0;
    private int maxMembers = -1;
    private int paybackPercentage = 0;
    private int extraTotalEntitys = 0;
    private int allowedSubregions = 0;
    private UUID landlord = null;
    private RegionKind regionKind = RegionKind.DEFAULT;
    private FlagGroup flagGroup = FlagGroup.DEFAULT;
    private EntityLimitGroup entityLimitGroup = EntityLimitGroup.DEFAULT;
    private HashSet<Integer> builtblocks = new HashSet<>();
    private HashMap<EntityLimit.LimitableEntityType, Integer> extraEntitys = new HashMap<>();

    {
        HashMap<String, StringCreator> variableReplacements = new HashMap<>();
        variableReplacements.put("%prefix%", () -> {
            return Messages.PREFIX;
        });
        variableReplacements.put("%regionid%", () -> {
            return this.getRegion().getId();
        });
        variableReplacements.put("%maxmembers%", () -> {
            return (this.getMaxMembers() < 0) ? Messages.UNLIMITED : this.getMaxMembers() + "";
        });
        variableReplacements.put("%region%", () -> {
            return this.getRegion().getId();
        });
        variableReplacements.put("%price%", () -> {
            return Price.formatPrice(this.getPricePerPeriod());
        });
        variableReplacements.put("%dimensions%", () -> {
            return this.getDimensions();
        });
        variableReplacements.put("%priceperm2%", () -> {
            return Price.formatPrice(this.getPricePerM2());
        });
        variableReplacements.put("%priceperm3%", () -> {
            return Price.formatPrice(this.getPricePerM3());
        });
        variableReplacements.put("%remaininguserresetcooldown-date%", () -> {
            return TimeUtil.getDate(AdvancedRegionMarket.getInstance().getPluginSettings().getUserResetCooldown() + this.getLastreset(),
                    true, Messages.INFO_NOW, AdvancedRegionMarket.getInstance().getPluginSettings().getDateTimeformat());
        });
        variableReplacements.put("%remaininguserresetcooldown-countdown-short%", () -> {
            return TimeUtil.getCountdown(AdvancedRegionMarket.getInstance().getPluginSettings().getUserResetCooldown() + this.getLastreset(),
                    false, false, false, "");
        });
        variableReplacements.put("%remaininguserresetcooldown-countdown-short-cutted%", () -> {
            return TimeUtil.getCountdown(AdvancedRegionMarket.getInstance().getPluginSettings().getUserResetCooldown() + this.getLastreset(),
                    false, true, false, "");
        });
        variableReplacements.put("%remaininguserresetcooldown-countdown-writtenout%", () -> {
            return TimeUtil.getCountdown(AdvancedRegionMarket.getInstance().getPluginSettings().getUserResetCooldown() + this.getLastreset(),
                    true, false, false, "");
        });
        variableReplacements.put("%remaininguserresetcooldown-countdown-writtenout-cutted%", () -> {
            return TimeUtil.getCountdown(AdvancedRegionMarket.getInstance().getPluginSettings().getUserResetCooldown() + this.getLastreset(),
                    true, true, false, "");
        });
        variableReplacements.put("%paybackmoney%", () -> {
            return Price.formatPrice(this.getPaybackMoney());
        });
        variableReplacements.put("%paypackpercentage%", () -> {
            return this.getPaybackPercentage() + "";
        });
        variableReplacements.put("%currency%", () -> {
            return Messages.CURRENCY;
        });
        variableReplacements.put("%world%", () -> {
            return this.getRegionworld().getName();
        });
        variableReplacements.put("%subregionlimit%", () -> {
            return this.getAllowedSubregions() + "";
        });
        variableReplacements.put("%hotelfunctionstatus%", () -> {
            return Messages.convertEnabledDisabled(this.isHotel);
        });
        variableReplacements.put("%soldstatus%", () -> {
            if (this.isSold()) {
                return Messages.SOLD;
            } else {
                return Messages.AVAILABLE;
            }
        });
        variableReplacements.put("%issold%", () -> {
            return Messages.convertYesNo(this.isSold());
        });
        variableReplacements.put("%selltype%", () -> {
            return this.getSellType().getName();
        });
        variableReplacements.put("%ishotel%", () -> {
            return Messages.convertYesNo(this.isHotel());
        });
        variableReplacements.put("%isuserrestorable%", () -> {
            return Messages.convertYesNo(this.isUserRestorable());
        });
        variableReplacements.put("%isautorestore%", () -> {
            return Messages.convertYesNo(this.isAutoRestore());
        });
        variableReplacements.put("%isinactivityreset%", () -> {
            return Messages.convertYesNo(this.isInactivityReset());
        });
        variableReplacements.put("%landlord%", () -> {
            return Messages.getStringValue(this.getLandlord(), x -> Messages.getStringValue(Bukkit.getOfflinePlayer(x).getName(), y -> y, Messages.UNKNOWN_UUID), Messages.LANDLORD_SERVER);
        });
        variableReplacements.put("%lastownerlogin%", () -> {
            if(this.getLastLogin() == 0) {
                return Messages.NEVER;
            }
            return TimeUtil.getDate(this.getLastLogin(), false, "",
                    AdvancedRegionMarket.getInstance().getPluginSettings().getDateTimeformat());
        });
        variableReplacements.put("%owner%", () -> {
            if(this.getRegion().getOwners().isEmpty()) {
                return "";
            }
            return Messages.getStringValue(Bukkit.getOfflinePlayer(this.getRegion().getOwners().get(0)).getName(), x -> x, Messages.UNKNOWN_UUID);
        });
        variableReplacements.put("%autoprice%", () -> {
            if (this.getPriceObject().isAutoPrice()) {
                return this.getPriceObject().getAutoPrice().getName();
            }
            return Messages.convertYesNo(this.getPriceObject().isAutoPrice());
        });
        variableReplacements.put("%subregions%", () -> {
            return Messages.getStringList(this.subregions, x -> x.getRegion().getId(), ", ");
        });
        variableReplacements.put("%members%", () -> {
            return Messages.getStringList(this.getRegion().getMembers(), x -> Messages.getStringValue(Bukkit.getOfflinePlayer(x).getName(), y -> y, Messages.UNKNOWN_UUID), ", ");
        });
        variableReplacements.put("%takeoverin-date%", () -> {
            return this.getTakeoverCountdown(true, false, false);
        });
        variableReplacements.put("%takeoverin-countdown-short%", () -> {
            return this.getTakeoverCountdown(false, false, false);
        });
        variableReplacements.put("%takeoverin-countdown-short-cutted%", () -> {
            return this.getTakeoverCountdown(false, false, true);
        });
        variableReplacements.put("%takeoverin-countdown-writtenout%", () -> {
            return this.getTakeoverCountdown(false, true, false);
        });
        variableReplacements.put("%takeoverin-countdown-writtenout-cutted%", () -> {
            return this.getTakeoverCountdown(false, true, true);
        });
        variableReplacements.put("%inactivityresetin-date%", () -> {
            return this.getInactivityResetCountdown(true, false, false);
        });
        variableReplacements.put("%inactivityresetin-countdown-short%", () -> {
            return this.getInactivityResetCountdown(false, false, false);
        });
        variableReplacements.put("%inactivityresetin-countdown-short-cutted%", () -> {
            return this.getInactivityResetCountdown(false, false, true);
        });
        variableReplacements.put("%inactivityresetin-countdown-writtenout%", () -> {
            return this.getInactivityResetCountdown(false, true, false);
        });
        variableReplacements.put("%inactivityresetin-countdown-writtenout-cutted%", () -> {
            return this.getInactivityResetCountdown(false, true, true);
        });

        this.stringReplacer = new StringReplacer(variableReplacements, 50);

    }

    /* ######################################
    ############### Constructors ############
    #########################################*/
    public Region(WGRegion region, List<SignData> sellsigns, boolean sold, Region parentRegion) {
        this(region, parentRegion.getRegionworld(), sellsigns, sold);
        if (parentRegion.isSubregion()) {
            throw new IllegalArgumentException("Subregions can't be parent regions!");
        }
        ArmSettings pluginsSettings = AdvancedRegionMarket.getInstance().getPluginSettings();
        this.inactivityReset = pluginsSettings.isSubregionInactivityReset();
        this.isAutoRestore = pluginsSettings.isSubregionAutoRestore();
        this.regionKind = RegionKind.SUBREGION;
        this.flagGroup = FlagGroup.SUBREGION;
        this.isUserRestorable = pluginsSettings.isAllowSubRegionUserRestore();
        this.entityLimitGroup = EntityLimitGroup.SUBREGION;
        this.maxMembers = pluginsSettings.getMaxSubRegionMembers();
        this.paybackPercentage = pluginsSettings.getSubRegionPaybackPercentage();
        this.allowedSubregions = 0;
        this.parentRegion = parentRegion;
        parentRegion.addSubRegion(this);
    }

    public Region(WGRegion region, World regionworld, List<SignData> sellsigns, boolean sold) {
        this.region = region;
        this.sellsign = new ArrayList<>(sellsigns);
        this.sold = sold;
        this.regionworld = regionworld;
        this.subregions = new HashSet<>();

        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File builtblocksdic = new File(pluginfolder + "/schematics/" + this.regionworld.getName() + "/" + region.getId() + "/builtblocks.builtblocks");
        if (builtblocksdic.exists()) {
            try {
                FileReader filereader = new FileReader(builtblocksdic);
                BufferedReader reader = new BufferedReader(filereader);
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] lines = line.split(";", 3);
                    int x = Integer.parseInt(lines[0]);
                    int y = Integer.parseInt(lines[1]);
                    int z = Integer.parseInt(lines[2]);
                    Location loc = new Location(this.regionworld, x, y, z);
                    builtblocks.add(loc.hashCode());
                }
                reader.close();
                filereader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.builtblocks = new HashSet<>();
        }
    }

    /*#######################################
    ################# Getter ################
    ########################################*/

    public RegionKind getRegionKind() {
        return this.regionKind;
    }

    public boolean isSold() {
        return sold;
    }

    public boolean isUserRestorable() {
        return this.isUserRestorable;
    }

    public boolean isSubregion() {
        return this.parentRegion != null;
    }

    public boolean isInactivityReset() {
        return this.inactivityReset;
    }

    public boolean isAutoRestore() {
        return isAutoRestore;
    }

    public boolean isHotel() {
        return isHotel;
    }

    public boolean isAllowSubregions() {
        return (this.allowedSubregions > 0);
    }

    public boolean needsSave() {
        if (this.needsSave) {
            return true;
        }
        for (Region subregion : this.subregions) {
            if (subregion.needsSave()) {
                return true;
            }
        }
        return false;
    }

    public boolean isInactive() {
        if (!this.isInactivityReset()) {
            return false;
        }
        if (!this.isSold()) {
            return false;
        }
        long actualTime = new GregorianCalendar().getTimeInMillis();
        List<UUID> owners = this.getRegion().getOwners();
        if (owners.size() == 0) {
            if (InactivityExpirationGroup.DEFAULT.isResetDisabled()) {
                return false;
            }
            return this.getLastLogin() + InactivityExpirationGroup.DEFAULT.getResetAfterMs() < actualTime;
        }
        UUID ownerID = owners.get(0);
        InactivityExpirationGroup ieGroup = PlayerInactivityGroupMapper.getBestResetAfterMs(this.getRegionworld(), ownerID);
        if (ieGroup.isResetDisabled()) {
            return false;
        }
        return (this.getLastLogin() + ieGroup.getResetAfterMs() < actualTime);
    }

    public boolean isTakeOverReady() {
        if (!this.isInactivityReset()) {
            return false;
        }
        long actualTime = new GregorianCalendar().getTimeInMillis();
        List<UUID> owners = this.getRegion().getOwners();
        if (owners.size() == 0) {
            if (InactivityExpirationGroup.DEFAULT.isTakeOverDisabled()) {
                return false;
            }
            return this.getLastLogin() + InactivityExpirationGroup.DEFAULT.getTakeOverAfterMs() < actualTime;
        }
        UUID ownerID = owners.get(0);
        InactivityExpirationGroup ieGroup = PlayerInactivityGroupMapper.getBestTakeoverAfterMs(this.getRegionworld(), ownerID);
        if (ieGroup.isTakeOverDisabled()) {
            return false;
        }
        return (this.getLastLogin() + ieGroup.getTakeOverAfterMs() < actualTime);
    }

    /**
     *
     * @return -1 means infinite
     */
    public int getMaxMembers() {
        return maxMembers;
    }

    public int getAllowedSubregions() {
        return this.allowedSubregions;
    }

    public int getPaybackPercentage() {
        return paybackPercentage;
    }

    public UUID getLandlord() {
        if(this.isSubregion()) {
            if(this.getParentRegion().getRegion().getOwners().isEmpty()) {
                return null;
            } else {
                return this.getParentRegion().getRegion().getOwners().get(0);
            }
        }
        return landlord;
    }

    public int getExtraEntityAmount(EntityLimit.LimitableEntityType entityType) {
        Integer amount = this.extraEntitys.get(entityType);
        if (amount == null) {
            return 0;
        } else {
            return amount;
        }
    }

    protected int getM2Amount() {
        if (this.m2Amount == null) {
            int hight = ((this.getRegion().getMaxPoint().getBlockY() - this.getRegion().getMinPoint().getBlockY()) + 1);
            this.m2Amount = this.getRegion().getVolume() / hight;
        }
        return this.m2Amount;
    }

    public int getExtraTotalEntitys() {
        return this.extraTotalEntitys;
    }

    public int getNumberOfSigns() {
        return this.sellsign.size();
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public long getLastreset() {
        return this.lastreset;
    }

    public double getPricePerPeriod() {
        return this.getPriceObject().calcPrice(this.getRegion());
    }

    public double getPricePerM2() {
        double m2 = this.getM2Amount();
        return this.getPriceObject().calcPrice(this.getRegion()) / m2;
    }

    public double getPricePerM3() {
        double pricePerM2 = this.getPricePerM2();
        double hight = ((this.getRegion().getMaxPoint().getBlockY() - this.getRegion().getMinPoint().getBlockY()) + 1);
        return pricePerM2 / hight;
    }

    public String getDimensions() {
        Vector min = this.getRegion().getMinPoint();
        Vector max = this.getRegion().getMaxPoint();
        int maxX = max.getBlockX();
        int maxY = max.getBlockY();
        int maxZ = max.getBlockZ();
        int minX = min.getBlockX();
        int minY = min.getBlockY();
        int minZ = min.getBlockZ();
        return Math.abs((maxX - minX) + 1) + "x" + (Math.abs(maxY - minY) + 1) + "x" + (Math.abs(maxZ - minZ) + 1);

    }

    public Location getTeleportLocation() {
        return this.teleportLocation;
    }

    public abstract Price getPriceObject();

    public EntityLimitGroup getEntityLimitGroup() {
        return this.entityLimitGroup;
    }

    public FlagGroup getFlagGroup() {
        return this.flagGroup;
    }

    public Region getParentRegion() {
        return this.parentRegion;
    }

    public World getRegionworld() {
        return regionworld;
    }

    public WGRegion getRegion() {
        return region;
    }

    public Set<Region> getSubregions() {
        return subregions;
    }

    protected List<SignData> getSellSigns() {
        return this.sellsign;
    }

    protected HashMap<EntityLimit.LimitableEntityType, Integer> getExtraEntitys() {
        return this.extraEntitys;
    }

    /*#############################
    ########### Setter ############
    #############################*/

    public void queueSave() {
        UpdateRegionEvent updateRegionEvent = new UpdateRegionEvent(this);
        Bukkit.getServer().getPluginManager().callEvent(updateRegionEvent);
        if (!updateRegionEvent.isCancelled()) {
            this.needsSave = true;
        }
    }

    public void setSaved() {
        this.needsSave = false;
        for (Region subregion : this.subregions) {
            subregion.setSaved();
        }
    }

    public void setLastLogin() {
        this.setLastLogin(new GregorianCalendar().getTimeInMillis());
    }

    public void setUserRestorable(boolean bool) {
        if (this.isSubregion())
            throw new IllegalArgumentException("Can't change this option for a Subregion!");
        this.isUserRestorable = bool;
        this.queueSave();
    }

    public void setAutoRestore(boolean bool) {
        if (this.isSubregion())
            throw new IllegalArgumentException("Can't change this option for a Subregion!");
        this.isAutoRestore = bool;
        this.queueSave();
    }

    public void setSold(boolean sold) {
        boolean isSold = this.isSold();
        this.sold = sold;
        if (!isSold && sold || isSold && !sold) {
            if (sold) {
                this.setLastLogin();
            }
            try {
                this.getFlagGroup().applyToRegion(this, FlagGroup.ResetMode.COMPLETE, false);
            } catch (FeatureDisabledException e) {
                //Ignore exception
            }
        }
        this.queueSave();
    }

    public void setHotel(boolean bool) {
        this.isHotel = bool;
        this.queueSave();
    }

    public void setInactivityReset(boolean state) {
        if (this.isSubregion())
            throw new IllegalArgumentException("Can't change this option for a Subregion!");
        this.inactivityReset = state;
        this.queueSave();
    }

    public void setAllowedSubregions(int allowedSubregions) {
        if (this.isSubregion())
            throw new IllegalArgumentException("Can't change this option for a Subregion!");
        this.allowedSubregions = allowedSubregions;
        this.queueSave();
    }

    public void setPaybackPercentage(int paybackPercentage) {
        if (this.isSubregion())
            throw new IllegalArgumentException("Can't change this option for a Subregion!");
        this.paybackPercentage = paybackPercentage;
        this.queueSave();
    }

    public void setLandlord(UUID landlord) {
        if (this.isSubregion())
            throw new IllegalArgumentException("Can't change this option for a Subregion!");
        this.landlord = landlord;
        this.queueSave();
    }

    /**
     *
     * @param maxMembers -1 means infinite. Smaller values than -1 will set the limit to 0 automatically
     */
    public void setMaxMembers(int maxMembers) {
        if (this.isSubregion())
            throw new IllegalArgumentException("Can't change this option for a Subregion!");
        if (maxMembers < -1) {
            this.maxMembers = 0;
        } else {
            this.maxMembers = maxMembers;
        }
        this.queueSave();
    }

    public void setLastReset(long lastReset) {
        this.lastreset = lastReset;
        this.queueSave();
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
        this.queueSave();
    }

    public void setRegionKind(RegionKind regionKind) {
        if (this.isSubregion())
            throw new IllegalArgumentException("Can't change this option for a Subregion!");
        this.regionKind = regionKind;
        this.queueSave();
    }

    /**
     * Sets the region to sold and sets a players a an owner
     *
     * @param player The player that should own the region
     */
    public void setSold(OfflinePlayer player) {
        this.setOwner(player);
        this.setSold(true);
        this.queueSave();
        this.updateSigns();
    }

    public void setOwner(OfflinePlayer oPlayer) {
        this.getRegion().setOwner(oPlayer);
        this.setLastLogin();
    }

    public void setEntityLimitGroup(EntityLimitGroup entityLimitGroup) {
        if (this.isSubregion())
            throw new IllegalArgumentException("Can't change this option for a Subregion!");
        this.entityLimitGroup = entityLimitGroup;
        this.queueSave();
    }

    public void setFlagGroup(FlagGroup flagGroup) {
        if (this.isSubregion())
            throw new IllegalArgumentException("Can't change this option for a Subregion!");
        this.flagGroup = flagGroup;
        this.queueSave();
    }

    public void setTeleportLocation(Location loc) {
        if (this.isSubregion())
            throw new IllegalArgumentException("Can't change this option for a Subregion!");
        this.teleportLocation = loc;
        this.queueSave();
    }

    public void setExtraTotalEntitys(int extraTotalEntitys) {
        if (this.isSubregion())
            throw new IllegalArgumentException("Can't change this option for a Subregion!");
        if (extraTotalEntitys < 0) {
            this.extraTotalEntitys = 0;
        } else {
            this.extraTotalEntitys = extraTotalEntitys;
        }

        this.queueSave();
    }

    public void setExtraEntityAmount(EntityLimit.LimitableEntityType entityType, int amount) {
        if (this.isSubregion())
            throw new IllegalArgumentException("Can't change this option for a Subregion!");
        this.extraEntitys.remove(entityType);
        if (amount > 0) {
            this.extraEntitys.put(entityType, amount);
        }
        this.queueSave();
    }

    /*##################################
    ####### Abstract Methods ###########
    ##################################*/

    public abstract void signClickAction(Player player) throws OutOfLimitExeption, AlreadySoldException, NotSoldException, NoPermissionException, NotEnoughMoneyException, RegionNotOwnException;

    public abstract double getPricePerM2PerWeek();

    public abstract double getPricePerM3PerWeek();

    protected abstract void updateSignText(SignData signData);

    public abstract double getPaybackMoney();

    public abstract SellType getSellType();

    public abstract void setAutoPrice(AutoPrice autoPrice);

    /*##################################
    ######### Other Methods ############
    ##################################*/

    public void buy(Player player) throws NoPermissionException, OutOfLimitExeption, NotEnoughMoneyException, AlreadySoldException {
        if (!player.hasPermission(Permission.MEMBER_BUY)) {
            throw new NoPermissionException(Messages.NO_PERMISSION);
        }

        if (this.isSold()) {
            throw new AlreadySoldException(Messages.REGION_ALREADY_SOLD);
        }

        boolean isPlayerInLimit = AdvancedRegionMarket.getInstance().getLimitGroupManager().isCanBuyAnother(player, this.getRegionKind());
        PreBuyEvent preBuyEvent = new PreBuyEvent(this, player, isPlayerInLimit);
        Bukkit.getServer().getPluginManager().callEvent(preBuyEvent);
        if(preBuyEvent.isCancelled()) {
            return;
        }
        isPlayerInLimit = preBuyEvent.isPlayerInLimit();
        boolean isNoMoneyTransfer = preBuyEvent.isNoMoneyTransfer();

        if (!isPlayerInLimit) {
            throw new OutOfLimitExeption(this.replaceVariables(Messages.REGION_BUY_OUT_OF_LIMIT));
        }

        if (!isNoMoneyTransfer && AdvancedRegionMarket.getInstance().getEcon().getBalance(player) < this.getPricePerPeriod()) {
            throw new NotEnoughMoneyException(this.replaceVariables(Messages.NOT_ENOUGH_MONEY));
        }

        this.setSold(player);
        if (AdvancedRegionMarket.getInstance().getPluginSettings().isTeleportAfterRentRegionBought()) {
            try {
                Teleporter.teleport(player, this, "", AdvancedRegionMarket.getInstance().getConfig().getBoolean("Other.TeleportAfterRegionBoughtCountdown"));
            } catch (NoSaveLocationException e) {
                if (e.hasMessage()) {
                    player.sendMessage(Messages.PREFIX + e.getMessage());
                }
            }
        }
        player.sendMessage(Messages.PREFIX + Messages.REGION_BUYMESSAGE);

        if(!isNoMoneyTransfer) {
            AdvancedRegionMarket.getInstance().getEcon().withdrawPlayer(player, this.getPricePerPeriod());
            this.giveLandlordMoney(this.getPricePerPeriod());
        }
    }

    private void addSubRegion(Region region) {
        this.subregions.add(region);
        this.queueSave();
    }

    public void deleteSigns() {
        for (int i = 0; i < this.sellsign.size(); i++) {
            Location loc = this.sellsign.get(i).getLocation();
            loc.getBlock().setType(Material.AIR);
            this.removeSign(loc);
            i--;
        }
    }

    /**
     * This will remove all signs, flags and subregions from the region
     * If the region is registred in a regionManager, it still needs to be removed from
     * there in order to be deleted completely
     */
    public void delete() {
        this.deleteSigns();
        if(FlagGroup.isFeatureEnabled())
            this.getRegion().deleteAllFlags();
        if (this.isSubregion()) {
            AdvancedRegionMarket.getInstance().getWorldGuardInterface().removeFromRegionManager(this.getRegion(), this.getRegionworld());
            this.getParentRegion().getSubregions().remove(this);
            this.getParentRegion().queueSave();
        } else {
            for(Region subregion : this.getSubregions()) {
                subregion.delete();
            }
        }
    }

    public void addBuiltBlock(Location loc) {
        if (this.builtblocks.add(loc.hashCode())) {
            try {
                File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
                File builtblocksdic = new File(pluginfolder + "/schematics/" + this.regionworld.getName() + "/" + region.getId() + "/builtblocks.builtblocks");
                if (!builtblocksdic.exists()) {
                    File builtblocksfolder = new File(pluginfolder + "/schematics/" + this.regionworld.getName() + "/" + region.getId());
                    builtblocksfolder.mkdirs();
                    builtblocksdic.createNewFile();
                }

                FileWriter fileWriter = new FileWriter(builtblocksdic, true);
                BufferedWriter writer = new BufferedWriter(fileWriter);
                loc.getBlock().getType();
                writer.write(loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ());
                writer.newLine();
                writer.close();
                fileWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void giveLandlordMoney(double amount) {
        UUID landlordUUID = this.getLandlord();
        if(landlordUUID == null) {
            return;
        }
        OfflinePlayer subRegionOwner = Bukkit.getOfflinePlayer(landlordUUID);
        AdvancedRegionMarket.getInstance().getEcon().depositPlayer(subRegionOwner, amount);
    }

    public void addSign(SignData signData) {
        if (signData == null) {
            return;
        }

        sellsign.add(signData);
        this.queueSave();
        this.updateSignText(signData);
        this.getRegionworld().save();

    }

    public boolean removeSign(Location loc) {
        for(SignData sd : this.sellsign) {
            if (sd.getLocation().getWorld().getName().equals(loc.getWorld().getName())
                    && sd.getLocation().distance(loc) == 0) {
                this.sellsign.remove(sd);
                this.queueSave();
                return true;
            }
        }
        return false;
    }

    public void applyFlagGroup(FlagGroup.ResetMode resetMode, boolean forceApply) throws FeatureDisabledException {
        this.flagGroup.applyToRegion(this, resetMode, forceApply);
    }

    public void updateSigns() {

        for (SignData signData : this.sellsign) {
            if (signData.isChunkLoaded()) {
                if (!signData.isPlaced()) {
                    signData.placeSign();
                }
                this.updateSignText(signData);
            }
        }
    }

    public boolean hasSign(Sign sign) {
        for(SignData sd : this.sellsign) {
            if (sd.getLocation().getWorld().getName().equalsIgnoreCase(sign.getWorld().getName())
                    && sd.getLocation().distance(sign.getLocation()) == 0) {
                return true;
            }
        }
        return false;
    }

    private String getTakeoverCountdown(boolean date, boolean writeOut, boolean returnOnlyHighestUnit) {
        if (!this.isInactivityReset()) {
            return Messages.INFO_DEACTIVATED;
        }
        if (!this.isSold()) {
            return Messages.INFO_REGION_NOT_SOLD;
        }
        List<UUID> ownerslist = this.getRegion().getOwners();
        UUID ownerID = null;
        if (ownerslist.size() > 0) {
            ownerID = ownerslist.get(0);
        }
        InactivityExpirationGroup ieGroup = PlayerInactivityGroupMapper.getBestTakeoverAfterMs(this.getRegionworld(), ownerID);
        if (ieGroup.isNotCalculated()) {
            return Messages.INFO_NOT_CALCULATED;
        } else if (ieGroup.isTakeOverDisabled()) {
            return Messages.INFO_DEACTIVATED;
        } else {
            if (date) {
                return TimeUtil.getDate(ieGroup.getTakeOverAfterMs() + this.getLastLogin(), true,
                        Messages.INFO_NOW, AdvancedRegionMarket.getInstance().getPluginSettings().getDateTimeformat());
            } else {
                return TimeUtil.getCountdown(ieGroup.getTakeOverAfterMs() + this.getLastLogin(), writeOut,
                        returnOnlyHighestUnit, true, Messages.INFO_NOW);
            }
        }
    }

    private String getInactivityResetCountdown(boolean date, boolean writeOut, boolean returnOnlyHighestUnit) {
        if (!this.isInactivityReset()) {
            return Messages.INFO_DEACTIVATED;
        }
        if (!this.isSold()) {
            return Messages.INFO_REGION_NOT_SOLD;
        }
        List<UUID> ownerslist = this.getRegion().getOwners();
        UUID ownerID = null;
        if (ownerslist.size() > 0) {
            ownerID = ownerslist.get(0);
        }
        InactivityExpirationGroup ieGroup = PlayerInactivityGroupMapper.getBestResetAfterMs(this.getRegionworld(), ownerID);
        if (ieGroup.isNotCalculated()) {
            return Messages.INFO_NOT_CALCULATED;
        } else if (ieGroup.isResetDisabled()) {
            return Messages.INFO_DEACTIVATED;
        } else {
            if (date) {
                return TimeUtil.getDate(ieGroup.getResetAfterMs() + this.getLastLogin(), true,
                        Messages.INFO_NOW, AdvancedRegionMarket.getInstance().getPluginSettings().getDateTimeformat());
            } else {
                return TimeUtil.getCountdown(ieGroup.getResetAfterMs() + this.getLastLogin(), writeOut,
                        returnOnlyHighestUnit, true, Messages.INFO_NOW);
            }
        }
    }

    public void createBackup() {
        String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSSS").format(new Date());
        File schematicFolder = new File(AdvancedRegionMarket.getInstance().getDataFolder() + "/schematics");
        File regionsSchematicFolder = new File(schematicFolder + "/" + this.getRegionworld().getName() + "/" + this.getRegion().getId() + "/Backups");
        AdvancedRegionMarket.getInstance().getWorldEditInterface().createSchematic(this.getRegion(), this.getRegionworld(), regionsSchematicFolder, fileName);
    }

    public void loadBackup(String name) throws SchematicNotFoundException {
        File schematicFolder = new File(AdvancedRegionMarket.getInstance().getDataFolder() + "/schematics");
        File regionsSchematicPathWithoutFileEnding = new File(schematicFolder + "/" + this.getRegionworld().getName() + "/" + this.getRegion().getId() + "/Backups/" + name);
        AdvancedRegionMarket.getInstance().getWorldEditInterface().restoreSchematic(this.getRegion(), this.getRegionworld(), regionsSchematicPathWithoutFileEnding);
    }

    public void createSchematic() {
        File schematicFolder = new File(AdvancedRegionMarket.getInstance().getDataFolder() + "/schematics");
        File regionsSchematicFolder = new File(schematicFolder + "/" + this.getRegionworld().getName() + "/" + this.getRegion().getId());
        AdvancedRegionMarket.getInstance().getWorldEditInterface().createSchematic(this.getRegion(), this.getRegionworld(), regionsSchematicFolder, "schematic");
    }

    public void restoreRegion(ActionReason actionReason, boolean logToConsole, boolean preventBackup) throws SchematicNotFoundException {

        RestoreRegionEvent resetBlocksEvent = new RestoreRegionEvent(this);
        Bukkit.getServer().getPluginManager().callEvent(resetBlocksEvent);
        if (resetBlocksEvent.isCancelled()) {
            return;
        }

        if (AdvancedRegionMarket.getInstance().getPluginSettings().isCreateBackupOnRegionRestore() && !preventBackup) {
            this.createBackup();
        }

        if (AdvancedRegionMarket.getInstance().getPluginSettings().isRemoveEntitiesOnRegionBlockReset()) {
            this.killEntitys();
        }

        File schematicFolder = new File(AdvancedRegionMarket.getInstance().getDataFolder() + "/schematics");
        File regionsSchematicPathWithoutFileEnding = new File(schematicFolder + "/" + this.getRegionworld().getName() + "/" + this.getRegion().getId() + "/schematic");

        AdvancedRegionMarket.getInstance().getWorldEditInterface().restoreSchematic(this.getRegion(), this.getRegionworld(), regionsSchematicPathWithoutFileEnding);

        if (AdvancedRegionMarket.getInstance().getPluginSettings().isDeleteSubregionsOnParentRegionBlockReset()) {
            Iterator<Region> subRegions = this.subregions.iterator();
            while (subRegions.hasNext()) {
                Region subRegion = subRegions.next();
                subRegions.remove();
                subRegion.delete();
            }
        }
        this.resetBuiltBlocks();

        //TODO Add to messages.yml
        if (logToConsole) {
            AdvancedRegionMarket.getInstance().getLogger().log(Level.INFO,
                    actionReason.replaceVariables(this.replaceVariables("Region %region% has been restored! Reason: %resetreason%")));
        }

        return;
    }

    public void killEntitys() {
        List<Entity> entities = this.getInsideEntities(false);
        for (Entity entity : entities) {
            entity.remove();
        }
    }

    public void resetBuiltBlocks() {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File builtblocksdic = new File(pluginfolder + "/schematics/" + this.regionworld.getName() + "/" + region.getId() + "/builtblocks.builtblocks");
        if (builtblocksdic.exists()) {
            builtblocksdic.delete();
            this.builtblocks = new HashSet<>();
        }
    }

    public void regionInfo(CommandSender sender) {
        if (sender instanceof Player
                && AdvancedRegionMarket.getInstance().getPluginSettings().isRegionInfoParticleBorder()) {
            Player player = (Player) sender;
            new ParticleBorder(this.getRegion().getPoints(), this.getRegion().getMinPoint().getBlockY(), this.getRegion().getMaxPoint().getBlockY(), player, this.getRegionworld()).createParticleBorder(20 * 30);
            for (Region subregion : this.getSubregions()) {
                Location lpos1 = new Location(subregion.getRegionworld(), subregion.getRegion().getMinPoint().getX(), subregion.getRegion().getMinPoint().getY(), subregion.getRegion().getMinPoint().getZ());
                Location lPos2 = new Location(subregion.getRegionworld(), subregion.getRegion().getMaxPoint().getX(), subregion.getRegion().getMaxPoint().getY(), subregion.getRegion().getMaxPoint().getZ());
                new ParticleBorder(lpos1.toVector(), lPos2.toVector(), player, subregion.getRegionworld()).createParticleBorder(20 * 30);
            }
        }
    }

    public void updateRegion() {
        this.updateSigns();
    }

    public void teleport(Player player, boolean teleportToSign) throws NoSaveLocationException {
        if (teleportToSign) {
            for (SignData signData : this.sellsign) {
                if (Teleporter.teleport(player, signData)) {
                    return;
                }
            }
            throw new NoSaveLocationException(Messages.TELEPORTER_NO_SAVE_LOCATION_FOUND);
        } else {
            Teleporter.teleport(player, this);
        }
    }

    public void userRestore(Player player) {
        try {
            this.restoreRegion(ActionReason.USER_RESTORE, true, false);
            GregorianCalendar calendar = new GregorianCalendar();
            this.lastreset = calendar.getTimeInMillis();
            this.queueSave();
            player.sendMessage(Messages.PREFIX + Messages.RESET_COMPLETE);
        } catch (SchematicNotFoundException e) {
            player.sendMessage(Messages.PREFIX + Messages.SCHEMATIC_NOT_FOUND_ERROR_USER.replace("%regionid%", e.getRegion().getId()));
            AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, this.replaceVariables(Messages.COULD_NOT_FIND_OR_LOAD_SCHEMATIC_LOG));
        }
    }

    public void writeSigns() {
        for (SignData signData : this.sellsign) {
            this.updateSignText(signData);
        }
    }

    public UUID getOwner() {
        if(this.getRegion().getOwners().isEmpty()) {
            return null;
        }
        return this.getRegion().getOwners().get(0);
    }

    public void userSell(Player player, boolean noMoney) throws SchematicNotFoundException, NotEnoughMoneyException {
        List<UUID> owners = this.getRegion().getOwners();
        double amount = this.getPaybackMoney();
        UUID owner = getOwner();
        UUID landlord = getLandlord();

        if (!noMoney && amount > 0 && owner != null) {
            if(landlord != null) {
                if(AdvancedRegionMarket.getInstance().getEcon().getBalance(Bukkit.getOfflinePlayer(landlord)) < amount) {
                    throw new NotEnoughMoneyException(this.replaceVariables(Messages.SELLBACK_LANDLORD_NOT_ENOUGH_MONEY));
                }
                AdvancedRegionMarket.getInstance().getEcon().withdrawPlayer(Bukkit.getOfflinePlayer(landlord), amount);
            }
            AdvancedRegionMarket.getInstance().getEcon().depositPlayer(Bukkit.getOfflinePlayer(owner), amount);
        }

        this.automaticResetRegion(ActionReason.USER_SELL, true);
    }

    public void resetRegion(ActionReason actionReason, boolean logToConsole) throws SchematicNotFoundException {
        this.unsell(actionReason, logToConsole, false);
        this.extraEntitys.clear();
        this.extraTotalEntitys = 0;
        this.queueSave();
        this.restoreRegion(actionReason, logToConsole, true);
    }

    /**
     * @param actionReason An ActionReason
     * @param logToConsole If true, this will be logged to the console with the give ActionReason
     * @throws SchematicNotFoundException if the schematic file of the region could not be found. Nevertheless
     *                                    if the execption gets thrown the region will be unsold
     */
    public void automaticResetRegion(ActionReason actionReason, boolean logToConsole) throws SchematicNotFoundException {
        this.unsell(actionReason, logToConsole, false);
        if (this.isAutoRestore()) {
            this.extraEntitys.clear();
            this.extraTotalEntitys = 0;
            try {
                this.restoreRegion(actionReason, logToConsole, true);
            } finally {
                this.queueSave();
            }
        }
        this.queueSave();
    }

    public boolean allowBlockBreak(Location breakloc) {
        if (this.isHotel) {
            return this.builtblocks.contains(breakloc.hashCode());
        }
        return true;
    }

    public void unsell(ActionReason actionReason, boolean logToConsole, boolean preventBackupCreation) {
        UnsellRegionEvent unsellRegionEvent = new UnsellRegionEvent(this);
        Bukkit.getServer().getPluginManager().callEvent(unsellRegionEvent);
        if (unsellRegionEvent.isCancelled()) {
            return;
        }

        if (AdvancedRegionMarket.getInstance().getPluginSettings().isCreateBackupOnRegionUnsell() && !preventBackupCreation) {
            this.createBackup();
        }

        this.getRegion().deleteMembers();
        this.getRegion().deleteOwners();
        this.setSold(false);
        this.lastreset = 0;
        this.lastLogin = 0;

        if (AdvancedRegionMarket.getInstance().getPluginSettings().isDeleteSubregionsOnParentRegionUnsell()) {
            Iterator<Region> subRegions = this.subregions.iterator();
            while (subRegions.hasNext()) {
                Region subRegion = subRegions.next();
                subRegions.remove();
                subRegion.delete();
            }
        }

        if (logToConsole) {
            AdvancedRegionMarket.getInstance().getLogger().log(Level.INFO,
                    actionReason.replaceVariables(this.replaceVariables("Region %region% has been unsold! Reason: %resetreason%")));
        }

        this.updateSigns();
        this.queueSave();
    }

    public List<String> replaceVariables(List<String> messages) {
        List<String> aList = new ArrayList<>();
        for(String message : messages) {
            aList.add(replaceVariables(message));
        }
        return aList;
    }

    public String replaceVariables(String message) {
        message = this.stringReplacer.replace(message).toString();
        message = this.getRegionKind().replaceVariables(message).toString();
        message = this.getEntityLimitGroup().replaceVariables(message).toString();
        return this.getFlagGroup().replaceVariables(message).toString();
    }

    public List<Entity> getInsideEntities(boolean includePlayers) {
        List<Entity> entities;
        List<Entity> result = new ArrayList<>();
        Vector minPoint = this.getRegion().getMinPoint();
        Vector maxPoint = this.getRegion().getMaxPoint();

        double minX = (minPoint.getX() + maxPoint.getX()) / 2;
        double minY = (minPoint.getY() + maxPoint.getY()) / 2;
        double minZ = (minPoint.getZ() + maxPoint.getZ()) / 2;
        Location midLocation = new Location(this.getRegionworld(), minX, minY, minZ);

        double xAxis = (maxPoint.getX() + 1 - minPoint.getX()) / 2d;
        double yAxis = (maxPoint.getY() + 1 - minPoint.getY()) / 2d;
        double zAxis = (maxPoint.getZ() + 1 - minPoint.getZ()) / 2d;

        xAxis += 1;
        yAxis += 1;
        zAxis += 1;

        entities = new ArrayList<>(this.getRegionworld().getNearbyEntities(midLocation, xAxis, yAxis, zAxis));

        for (Entity entity : entities) {
            Location entityLoc = entity.getLocation();
            boolean insideRegion = false;
            boolean add = true;

            if (this.getRegion().contains(entityLoc.getBlockX(), entityLoc.getBlockY(), entityLoc.getBlockZ())) {
                insideRegion = true;
            }

            if ((entity.getType() == EntityType.PLAYER) && !includePlayers) {
                add = false;
            }

            if (insideRegion && add) {
                result.add(entity);
            }

        }
        return result;
    }

    public List<Entity> getFilteredInsideEntities(boolean includePlayers, boolean includeHanging, boolean includeMonsters,
                                                  boolean includeAnimals, boolean includeVehicles,
                                                  boolean includeProjectiles, boolean includeAreaEffectCloud,
                                                  boolean includeItemFrames, boolean includePaintings) {

        List<Entity> insideEntitys = this.getInsideEntities(includePlayers);
        List<Entity> result = new ArrayList<>();

        for (Entity selectedEntity : insideEntitys) {
            boolean add = false;

            if ((selectedEntity instanceof Hanging) && includeHanging) {
                add = true;
            }

            if ((selectedEntity instanceof Monster) && includeMonsters) {
                add = true;
            }

            if ((selectedEntity instanceof Animals) && includeAnimals) {
                add = true;
            }

            if ((selectedEntity instanceof Vehicle) && includeVehicles) {
                add = true;
            }

            if ((selectedEntity instanceof Projectile) && includeProjectiles) {
                add = true;
            }

            if ((selectedEntity instanceof AreaEffectCloud) && includeAreaEffectCloud) {
                add = true;
            }

            if ((selectedEntity instanceof ItemFrame) && includeItemFrames) {
                add = true;
            }

            if ((selectedEntity instanceof Painting) && includePaintings) {
                add = true;
            }

            if (add) {
                result.add(selectedEntity);
            }
        }

        return result;
    }

    public ConfigurationSection toConfigurationSection() {
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.set("sold", this.isSold());
        yamlConfiguration.set("paybackPercentage", this.getPaybackPercentage());
        yamlConfiguration.set("isHotel", this.isHotel());
        yamlConfiguration.set("lastreset", this.getLastreset());
        yamlConfiguration.set("lastLogin", this.getLastLogin());
        yamlConfiguration.set("maxMembers", this.getMaxMembers());
        yamlConfiguration.set("regiontype", this.getSellType().getInternalName());
        if (this.getPriceObject().isAutoPrice()) {
            yamlConfiguration.set("autoprice", this.getPriceObject().getAutoPrice().getName());
            yamlConfiguration.set("price", null);
        } else {
            yamlConfiguration.set("price", this.getPricePerPeriod());
        }
        List<String> signs = new ArrayList<>();
        for (SignData signData : this.getSellSigns()) {
            signs.add(signData.toString());
        }
        yamlConfiguration.set("signs", signs);

        if (!this.isSubregion()) {
            yamlConfiguration.set("landlord", this.getLandlord() == null? null : this.getLandlord().toString());
            yamlConfiguration.set("kind", this.getRegionKind().getName());
            yamlConfiguration.set("flagGroup", this.flagGroup.getName());
            yamlConfiguration.set("inactivityReset", this.isInactivityReset());
            yamlConfiguration.set("entityLimitGroup", this.getEntityLimitGroup().getName());
            yamlConfiguration.set("autorestore", this.isAutoRestore());
            yamlConfiguration.set("allowedSubregions", this.getAllowedSubregions());
            yamlConfiguration.set("userrestorable", this.isUserRestorable());
            yamlConfiguration.set("boughtExtraTotalEntitys", this.getExtraTotalEntitys());
            List<String> boughtExtraEntitysStringList = new ArrayList<>();
            for (Map.Entry<EntityLimit.LimitableEntityType, Integer> entry : this.getExtraEntitys().entrySet()) {
                boughtExtraEntitysStringList.add(entry.getKey().getName() + ": " + entry.getValue());
            }
            yamlConfiguration.set("boughtExtraEntitys", boughtExtraEntitysStringList);
            if (this.getTeleportLocation() != null) {
                String teleportloc = this.getTeleportLocation().getWorld().getName() + ";" + this.getTeleportLocation().getBlockX() + ";" +
                        this.getTeleportLocation().getBlockY() + ";" + this.getTeleportLocation().getBlockZ() + ";" +
                        this.getTeleportLocation().getPitch() + ";" + this.getTeleportLocation().getYaw();
                yamlConfiguration.set("teleportLoc", teleportloc);
            } else {
                yamlConfiguration.set("teleportLoc", null);
            }
            for (Region subregion : this.getSubregions()) {
                yamlConfiguration.set("subregions." + subregion.getRegion().getId(), subregion.toConfigurationSection());
            }
        }
        return yamlConfiguration;
    }

    public List<String> tabCompleteRegionMembers(String args) {
        List<String> returnme = new ArrayList<>();

        List<UUID> uuidList = this.getRegion().getMembers();
        for (UUID uuids : uuidList) {
            OfflinePlayer oplayer = Bukkit.getOfflinePlayer(uuids);
            if (oplayer != null && oplayer.getName().toLowerCase().startsWith(args)) {
                returnme.add(oplayer.getName());
            }
        }
        return returnme;
    }

    public static enum ActionReason {
        USER_SELL, USER_RESTORE, EXPIRED, INACTIVITY, MANUALLY_BY_ADMIN,
        INSUFFICIENT_MONEY, DELETE, NONE, MANUALLY_BY_PARENT_REGION_OWNER;

        public String replaceVariables(String message) {
            String conMessage = message;
            if (conMessage.contains("%resetreason%")) conMessage = conMessage.replace("%resetreason%", this.name());
            return conMessage;
        }
    }
}
