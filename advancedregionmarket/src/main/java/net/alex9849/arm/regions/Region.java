package net.alex9849.arm.regions;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.ArmSettings;
import net.alex9849.arm.Messages;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.events.ResetBlocksEvent;
import net.alex9849.arm.events.UnsellRegionEvent;
import net.alex9849.arm.events.UpdateRegionEvent;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.minifeatures.ParticleBorder;
import net.alex9849.arm.minifeatures.teleporter.Teleporter;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.arm.util.Saveable;
import net.alex9849.exceptions.InputException;
import net.alex9849.exceptions.SchematicNotFoundException;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Level;

public abstract class Region implements Saveable {
    public static boolean completeTabRegions;

    protected WGRegion region;
    protected World regionworld;
    protected ArrayList<SignData> sellsign;
    protected HashSet<Integer> builtblocks;
    protected Price price;
    protected boolean sold;
    protected boolean autoreset;
    protected boolean isHotel;
    protected long lastreset;
    protected static int resetcooldown;
    protected RegionKind regionKind;
    protected Location teleportLocation;
    protected boolean isDoBlockReset;
    protected List<Region> subregions;
    protected int allowedSubregions;
    protected Region parentRegion;
    protected boolean isUserResettable;
    protected FlagGroup flagGroup;
    protected EntityLimitGroup entityLimitGroup;
    private boolean needsSave;
    private HashMap<EntityType, Integer> extraEntitys;
    private int extraTotalEntitys;
    Integer m2Amount;

    public Region(WGRegion region, World regionworld, List<SignData> sellsign, Price price, Boolean sold, Boolean autoreset,
                  Boolean isHotel, Boolean doBlockReset, RegionKind regionKind, FlagGroup flagGroup, Location teleportLoc, long lastreset,
                  boolean isUserResettable, List<Region> subregions, int allowedSubregions, EntityLimitGroup entityLimitGroup,
                  HashMap<EntityType, Integer> extraEntitys, int boughtExtraTotalEntitys){
        this.region = region;
        this.sellsign = new ArrayList<SignData>(sellsign);
        this.sold = sold;
        this.price = price;
        this.regionworld = regionworld;
        this.regionKind = regionKind;
        this.flagGroup = flagGroup;
        this.autoreset = autoreset;
        this.isDoBlockReset = doBlockReset;
        this.lastreset = lastreset;
        this.builtblocks = new HashSet<>();
        this.isHotel = isHotel;
        this.teleportLocation = teleportLoc;
        this.subregions = subregions;
        this.allowedSubregions = allowedSubregions;
        this.isUserResettable = isUserResettable;
        this.needsSave = false;
        this.entityLimitGroup = entityLimitGroup;
        this.extraEntitys = extraEntitys;
        this.extraTotalEntitys = boughtExtraTotalEntitys;

        for(Region subregion : subregions) {
            subregion.setParentRegion(this);
        }

        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File builtblocksdic = new File(pluginfolder + "/schematics/" + this.regionworld.getName() + "/" + region.getId() + "--builtblocks.schematic");
        if(builtblocksdic.exists()){
            try {
                FileReader filereader = new FileReader(builtblocksdic);
                BufferedReader reader = new BufferedReader(filereader);
                String line;
                while ((line = reader.readLine()) != null){
                    String[] lines = line.split(";", 4);
                    int x = Integer.parseInt(lines[1]);
                    int y = Integer.parseInt(lines[2]);
                    int z = Integer.parseInt(lines[3]);
                    Location loc = new Location(Bukkit.getWorld(lines[0]), x, y, z);
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

    protected void giveParentRegionOwnerMoney(double amount) {
        if(this.isSubregion()) {
            if(this.getParentRegion() != null) {
                if(this.getParentRegion().isSold()) {
                    List<UUID> parentRegionOwners = this.getParentRegion().getRegion().getOwners();
                    for(UUID uuid : parentRegionOwners) {
                        OfflinePlayer subRegionOwner = Bukkit.getOfflinePlayer(uuid);
                        if(subRegionOwner != null) {
                            AdvancedRegionMarket.getEcon().depositPlayer(subRegionOwner, amount);
                        }
                    }
                }
            }
        }
    }

    public void setAllowedSubregions(int allowedSubregions) {
        this.allowedSubregions = allowedSubregions;
        this.queueSave();
    }

    public boolean isUserResettable() {
        return this.isUserResettable;
    }

    public boolean isSubregion() {
        return (this.parentRegion != null);
    }

    public void setTeleportLocation(Location loc) {
        this.teleportLocation = loc;
        this.queueSave();
    }

    public Region getParentRegion() {
        return this.parentRegion;
    }

    public void setFlagGroup(FlagGroup flagGroup) {
        this.flagGroup = flagGroup;
        this.queueSave();
    }

    private void setParentRegion(Region region) {
        this.parentRegion = region;
    }

    public int getAllowedSubregions() {
        return this.allowedSubregions;
    }

    public boolean isAllowSubregions() {
        return (this.allowedSubregions > 0);
    }

    public void addSubRegion(Region region) {
        region.setParentRegion(this);
        this.subregions.add(region);
        this.queueSave();
    }

    public void delete() {
        for(int i = 0; i < this.sellsign.size(); i++) {
            Location loc = this.sellsign.get(i).getLocation();
            loc.getBlock().setType(Material.AIR);
            this.removeSign(loc, null);
            i--;
        }
    }

    public void addBuiltBlock(Location loc) {
        if(this.builtblocks.add(loc.hashCode())) {
            try {
                File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
                File builtblocksdic = new File(pluginfolder + "/schematics/" + this.regionworld.getName() + "/" + region.getId() + "--builtblocks.schematic");
                if(!builtblocksdic.exists()){
                    File builtblocksfolder = new File(pluginfolder + "/schematics/" + this.regionworld.getName());
                    builtblocksfolder.mkdirs();
                    builtblocksdic.createNewFile();
                }

                FileWriter fileWriter = new FileWriter(builtblocksdic, true);
                BufferedWriter writer = new BufferedWriter(fileWriter);
                loc.getBlock().getType();
                writer.write(loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ());
                writer.newLine();
                writer.close();
                fileWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public EntityLimitGroup getEntityLimitGroup() {
        return this.entityLimitGroup;
    }

    public void setEntityLimitGroup(EntityLimitGroup entityLimitGroup) {
        this.entityLimitGroup = entityLimitGroup;
        this.queueSave();
    }

    public void addSign(SignData signData){
        if(signData == null) {
            return;
        }

        sellsign.add(signData);
        this.queueSave();
        this.updateSignText(signData);
        this.getRegionworld().save();

    }

    public boolean removeSign(Location loc){
        return this.removeSign(loc, null);
    }

    public boolean removeSign(Location loc, Player destroyer){
        for(int i = 0; i < this.sellsign.size(); i++){
            if(this.sellsign.get(i).getLocation().getWorld().getName().equals(loc.getWorld().getName())){
                if(this.sellsign.get(i).getLocation().distance(loc) == 0){
                    this.sellsign.remove(i);
                    if(destroyer != null) {
                        String message = Messages.SIGN_REMOVED_FROM_REGION.replace("%remaining%", this.getNumberOfSigns() + "");
                        destroyer.sendMessage(Messages.PREFIX + message);
                    }
                    if(this.sellsign.size() == 0) {
                        for(int y = 0; i < this.getSubregions().size();) {
                            this.getSubregions().get(y).delete();
                        }
                        if(this.isSubregion()) {
                            AdvancedRegionMarket.getWorldGuardInterface().removeFromRegionManager(this.getRegion(), this.getRegionworld(), AdvancedRegionMarket.getWorldGuard());
                            this.getParentRegion().getSubregions().remove(this);
                            this.getParentRegion().queueSave();
                        } else {
                            AdvancedRegionMarket.getRegionManager().remove(this);
                        }
                        if(destroyer != null) {
                            destroyer.sendMessage(Messages.PREFIX + Messages.REGION_REMOVED_FROM_ARM);
                        }
                    }
                    this.queueSave();
                    return true;
                }
            }
        }
        return false;
    }

    public WGRegion getRegion() {
        return region;
    }

    public void applyFlagGroup(FlagGroup.ResetMode resetMode) {
        this.flagGroup.applyToRegion(this, resetMode);
    }

    public void updateSigns() {

        for(SignData signData : this.sellsign) {
            if(signData.isChunkLoaded()) {
                if (!signData.isPlaced()) {
                    signData.placeSign();
                }
                this.updateSignText(signData);
            }
        }
    }

    public World getRegionworld() {
        return regionworld;
    }

    public double getPrice(){
        return this.price.calcPrice(this.getRegion());
    }

    public boolean hasSign(Sign sign){
        for(int i = 0; i < this.sellsign.size(); i++){
            if(this.sellsign.get(i).getLocation().getWorld().getName().equalsIgnoreCase(sign.getWorld().getName())){
                if(this.sellsign.get(i).getLocation().distance(sign.getLocation()) == 0){
                    return true;
                }
            }
        }
        return false;
    }

    public int getNumberOfSigns(){
        return this.sellsign.size();
    }

    public boolean setKind(RegionKind kind){
        if(kind == null) {
            return false;
        }
        this.regionKind = kind;
        this.queueSave();
        return true;
    }

    public void setIsUserResettable(boolean bool) {
        this.isUserResettable = bool;
        this.queueSave();
    }

    public RegionKind getRegionKind(){
        return this.regionKind;
    }

    public boolean isSold() {
        return sold;
    }

    public void createSchematic(){
        AdvancedRegionMarket.getWorldEditInterface().createSchematic(this.getRegion(), this.getRegionworld(), AdvancedRegionMarket.getWorldedit().getWorldEdit());
    }

    public void resetBlocks() throws SchematicNotFoundException {

        ResetBlocksEvent resetBlocksEvent = new ResetBlocksEvent(this);
        Bukkit.getServer().getPluginManager().callEvent(resetBlocksEvent);
        if(resetBlocksEvent.isCancelled()) {
            return;
        }

        try {
            if(ArmSettings.isRemoveEntitiesOnRegionBlockReset()) {
                this.killEntitys();
            }
            AdvancedRegionMarket.getWorldEditInterface().resetBlocks(this.getRegion(), this.getRegionworld(), AdvancedRegionMarket.getWorldedit().getWorldEdit());
            if(ArmSettings.isDeleteSubregionsOnParentRegionBlockReset()) {
                for(int i = 0; i < this.getSubregions().size();) {
                    this.getSubregions().get(i).delete();
                }
            }
            this.resetBuiltBlocks();
            this.flagGroup.applyToRegion(this, FlagGroup.ResetMode.COMPLETE);

        } catch (IOException e) {
            if(e instanceof SchematicNotFoundException) {
                throw new SchematicNotFoundException(((SchematicNotFoundException) e).getRegion());
            } else {
                e.printStackTrace();
            }
        }

        return;
    }

    public void killEntitys() {
        List<Entity> entities = this.getInsideEntities(false);
        for(Entity entity : entities) {
            entity.remove();
        }
    }

    public void resetBuiltBlocks() {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File builtblocksdic = new File(pluginfolder + "/schematics/" + this.regionworld.getName() + "/" + region.getId() + "--builtblocks.schematic");
        if(builtblocksdic.exists()){
            builtblocksdic.delete();
            this.builtblocks = new HashSet<>();
        }
    }

    public void regionInfo(CommandSender sender) {
        if(sender instanceof Player) {
            if(ArmSettings.isRegionInfoParticleBorder()) {
                Player player = (Player) sender;
                new ParticleBorder(this.getRegion().getPoints(), this.getRegion().getMinPoint().getBlockY(), this.getRegion().getMaxPoint().getBlockY(), player, this.getRegionworld()).createParticleBorder(20 * 30);
                for(Region subregion : this.getSubregions()) {
                    Location lpos1 = new Location(subregion.getRegionworld(), subregion.getRegion().getMinPoint().getX(), subregion.getRegion().getMinPoint().getY(), subregion.getRegion().getMinPoint().getZ());
                    Location lPos2 = new Location(subregion.getRegionworld(), subregion.getRegion().getMaxPoint().getX(), subregion.getRegion().getMaxPoint().getY(), subregion.getRegion().getMaxPoint().getZ());
                    new ParticleBorder(lpos1.toVector(), lPos2.toVector(), player, subregion.getRegionworld()).createParticleBorder(20 * 30);
                }
            }
        }
    }

    public abstract void updateRegion();

    public boolean getAutoreset() {
        return autoreset;
    }

    public void setAutoreset(Boolean state){
        this.autoreset = state;
        this.queueSave();
    }

    public Material getLogo() {
        return this.getRegionKind().getMaterial();
    }

    public Location getTeleportLocation() {
        return this.teleportLocation;
    }

    public void teleport(Player player, boolean teleportToSign) throws InputException {
        if(teleportToSign) {
            for(SignData signData : this.sellsign) {
                if(Teleporter.teleport(player, signData)) {
                    return;
                }
            }
            throw new InputException(player, Messages.TELEPORTER_NO_SAVE_LOCATION_FOUND);
        } else {
            Teleporter.teleport(player, this);
        }
    }

    public void setNewOwner(OfflinePlayer member){
        ArrayList<UUID> owner = this.getRegion().getOwners();
        for (int i = 0; i < owner.size(); i++) {
            this.getRegion().addMember(owner.get(i));
        }
        this.getRegion().setOwner(member);
    }

    public String getDimensions(){
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

    public int timeSinceLastReset(){
        GregorianCalendar calendar = new GregorianCalendar();
        long result = calendar.getTimeInMillis() - this.lastreset;
        int days = (int) (result / (1000 * 60 * 60 * 24));
        return days;
    }

    public void userBlockReset(Player player){
        try {
            this.resetBlocks();
            GregorianCalendar calendar = new GregorianCalendar();
            this.lastreset = calendar.getTimeInMillis();
            this.queueSave();
        } catch (SchematicNotFoundException e) {
            player.sendMessage(Messages.PREFIX + Messages.SCHEMATIC_NOT_FOUND_ERROR_USER.replace("%regionid%", e.getRegion().getId()));
            Bukkit.getLogger().log(Level.WARNING, "Could not find schematic file for region " + this.getRegion().getId() + "in world " + this.getRegionworld().getName());
        }
        player.sendMessage(Messages.PREFIX + Messages.RESET_COMPLETE);
    }

    public static void setResetcooldown(int cooldown){
        Region.resetcooldown = cooldown;
    }

    public static int getResetCooldown(){
        return Region.resetcooldown;
    }

    public int getRemainingDaysTillReset(){
        ArrayList<UUID> ownerlist = this.getRegion().getOwners();
        if(ownerlist.size() > 0) {
            UUID owner = ownerlist.get(0);
            if(ArmSettings.isEnableTakeOver() ||ArmSettings.isEnableAutoReset()) {
                try{
                    ResultSet rs = ArmSettings.getStmt().executeQuery("SELECT * FROM `" + ArmSettings.getSqlPrefix() + "lastlogin` WHERE `uuid` = '" + owner.toString() + "'");

                    if(rs.next()){
                        Timestamp lastlogin = rs.getTimestamp("lastlogin");
                        GregorianCalendar calendar = new GregorianCalendar();

                        long time = lastlogin.getTime();
                        long result = calendar.getTimeInMillis() - time;
                        int days = (int)  (ArmSettings.getAutoResetAfter() - (result / (1000 * 60 * 60 * 24)));
                        return days;
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return 0;
    }

    public void writeSigns(){
        for (SignData signData : this.sellsign) {
            this.updateSignText(signData);
        }
    }

    public abstract void setSold(OfflinePlayer player);
    protected abstract void updateSignText(SignData signData);
    public abstract void buy(Player player) throws InputException;
    public abstract void userSell(Player player);
    public abstract double getPaybackMoney();

    public void resetRegion() throws SchematicNotFoundException {
        UnsellRegionEvent unsellRegionEvent = new UnsellRegionEvent(this);
        Bukkit.getServer().getPluginManager().callEvent(unsellRegionEvent);
        if(unsellRegionEvent.isCancelled()) {
            return;
        }

        this.unsell();
        this.resetBlocks();
        return;
    }

    public void automaticResetRegion(){
        this.automaticResetRegion(null);
    }

    public FlagGroup getFlagGroup() {
        return this.flagGroup;
    }

    public void automaticResetRegion(Player player){
        UnsellRegionEvent unsellRegionEvent = new UnsellRegionEvent(this);
        Bukkit.getServer().getPluginManager().callEvent(unsellRegionEvent);
        if(unsellRegionEvent.isCancelled()) {
            return;
        }

        this.unsell();
        if(this.isDoBlockReset()){
            try {
                this.resetBlocks();
            } catch (SchematicNotFoundException e) {
                Bukkit.getLogger().log(Level.WARNING, "Could not find schematic file for region " + this.getRegion().getId() + "in world " + this.getRegionworld().getName());
            }
        }
        if(player != null) {
            player.sendMessage(Messages.PREFIX + Messages.RESET_COMPLETE);
        }
    }

    public boolean isHotel() {
        return isHotel;
    }

    public boolean allowBlockBreak(Location breakloc) {
        if(this.isHotel) {
            return this.builtblocks.contains(breakloc.hashCode());
        }
        return true;
    }

    public void setHotel(Boolean bool) {
        this.isHotel = bool;
        this.queueSave();
    }

    public void unsell(){
        this.getRegion().deleteMembers();
        this.getRegion().deleteOwners();
        this.sold = false;
        this.lastreset = 1;

        if(ArmSettings.isDeleteSubregionsOnParentRegionUnsell()) {
            for(int i = 0; i < this.getSubregions().size();) {
                this.getSubregions().get(i).delete();
            }
        }

        this.extraEntitys.clear();
        this.extraTotalEntitys = 0;
        this.updateSigns();
        this.flagGroup.applyToRegion(this, FlagGroup.ResetMode.COMPLETE);
        this.queueSave();
    }

    public boolean isDoBlockReset() {
        return isDoBlockReset;
    }

    public void setDoBlockReset(Boolean bool) {
        this.isDoBlockReset = bool;
        this.queueSave();
    }

    public static void setCompleteTabRegions(Boolean bool) {
        Region.completeTabRegions = bool;
    }

    public List<Region> getSubregions() {
        return subregions;
    }

    protected List<SignData> getSellSigns() {
        return this.sellsign;
    }

    protected long getLastreset() {
        return this.lastreset;
    }

    protected boolean isAutoreset() {
        return this.autoreset;
    }

    private String getOwnerName() {
        List<UUID> ownerlist = new ArrayList<>(this.getRegion().getOwners());
        String ownername;
        if(ownerlist.size() < 1){
            ownername = "Unknown";
        } else {
            OfflinePlayer owner = Bukkit.getOfflinePlayer(ownerlist.get(0));
            ownername = owner.getName();

            if(ownername == null) {
                ownername = "Unknown";
            }
        }
        return ownername;
    }

    public abstract double getPricePerM2PerWeek();

    public abstract double getPricePerM3PerWeek();

    public double getPricePerM2() {
        double m2 = this.getM2Amount();
        return this.price.calcPrice(this.getRegion()) / m2;
    }

    public double getPricePerM3() {
        double pricePerM2 = this.getPricePerM2();
        double hight = ((this.getRegion().getMaxPoint().getBlockY() - this.getRegion().getMinPoint().getBlockY()) + 1);
        return pricePerM2 / hight;
    }

    private String getSoldStringStatus() {
        if(this.isSold()) {
            return Messages.SOLD;
        } else {
            return Messages.AVAILABLE;
        }
    }

    public Price getPriceObject() {
        return this.price;
    }

    public abstract SellType getSellType();

    public abstract void setPrice(Price price);

    public String getConvertedMessage(String message) {
        if(message.contains("%regionid%")) message = message.replace("%regionid%", this.getRegion().getId());
        if(message.contains("%region%")) message = message.replace("%region%", this.getRegion().getId());
        if(message.contains("%price%")) message = message.replace("%price%", Price.formatPrice(this.getPrice()) + "");
        if(message.contains("%dimensions%")) message = message.replace("%dimensions%", this.getDimensions());
        if(message.contains("%priceperm2%")) message = message.replace("%priceperm2%", Price.formatPrice(this.getPricePerM2()) + "");
        if(message.contains("%priceperm3%")) message = message.replace("%priceperm3%", Price.formatPrice(this.getPricePerM3()) + "");
        if(message.contains("%remainingdays%")) message = message.replace("%remainingdays%", (Region.getResetCooldown() - this.timeSinceLastReset()) + "");
        if(message.contains("%paybackmoney%")) message = message.replace("%paybackmoney%", Price.formatPrice(this.getPaybackMoney()));
        if(message.contains("%currency%")) message = message.replace("%currency%", Messages.CURRENCY);
        if(message.contains("%world%")) message = message.replace("%world%", this.getRegionworld().getName());
        if(message.contains("%subregionlimit%")) message = message.replace("%subregionlimit%", this.getAllowedSubregions() + "");
        if(message.contains("%hotelfunctionstatus%")) message = message.replace("%hotelfunctionstatus%", Messages.convertEnabledDisabled(this.isHotel));
        if(message.contains("%soldstatus%")) message = message.replace("%soldstatus%", this.getSoldStringStatus());
        if(message.contains("%issold%")) message = message.replace("%issold%", Messages.convertYesNo(this.isSold()));
        if(message.contains("%selltype%")) message = message.replace("%selltype%", this.getSellType().getName());
        if(message.contains("%ishotel%")) message = message.replace("%ishotel%", Messages.convertYesNo(this.isHotel()));
        if(message.contains("%isuserresettable%")) message = message.replace("%isuserresettable%", Messages.convertYesNo(this.isUserResettable()));
        if(message.contains("%isdoblockreset%")) message = message.replace("%isdoblockreset%", Messages.convertYesNo(this.isDoBlockReset()));
        if(message.contains("%autoprice%")) {
            String autopriceInfo = "";
            if(this.getPriceObject().isAutoPrice()) {
                autopriceInfo = this.getPriceObject().getAutoPrice().getName();
            } else {
                autopriceInfo = Messages.convertYesNo(this.getPriceObject().isAutoPrice());
            }
            message = message.replace("%autoprice%", autopriceInfo);
        }
        if(message.contains("%isautoreset%")) {
            String autoresetInfo = Messages.convertYesNo(this.isAutoreset());
            if(this.isAutoreset() && !ArmSettings.isEnableAutoReset()) {
                autoresetInfo += " (but globally disabled)";
            }
            message = message.replace("%isautoreset%", autoresetInfo);
        }
        if(message.contains("%subregions%")) {
            String subregions = "";
            for (int i = 0; i < this.getSubregions().size() - 1; i++) {
                subregions = subregions + this.getSubregions().get(i).getRegion().getId() + ", ";
            }
            if(this.getSubregions().size() != 0){
                subregions = subregions + this.getSubregions().get(this.getSubregions().size() - 1).getRegion().getId();
            }
            message = message.replace("%subregions%", subregions);
        }
        if(message.contains("%owner%")) {
            String ownersInfo = "";
            List<UUID> ownerslist = this.getRegion().getOwners();
            for(int i = 0; i < ownerslist.size() - 1; i++){
                ownersInfo = ownersInfo + Bukkit.getOfflinePlayer(ownerslist.get(i)).getName() + ", ";
            }
            if(ownerslist.size() != 0){
                ownersInfo = ownersInfo + Bukkit.getOfflinePlayer(ownerslist.get(ownerslist.size() - 1)).getName();
            }
            message = message.replace("%owner%", ownersInfo);
        }
        if(message.contains("%members%")) {
            String membersInfo = "";
            List<UUID> memberslist = this.getRegion().getMembers();
            for(int i = 0; i < memberslist.size() - 1; i++){
                membersInfo = membersInfo + Bukkit.getOfflinePlayer(memberslist.get(i)).getName() + ", ";
            }
            if(memberslist.size() != 0){
                membersInfo = membersInfo + Bukkit.getOfflinePlayer(memberslist.get(memberslist.size() - 1)).getName();
            }
            message = message.replace("%members%", membersInfo);
        }
        message = this.getRegionKind().getConvertedMessage(message);
        message = this.getEntityLimitGroup().getConvertedMessage(message);
        message = this.flagGroup.getConvertedMessage(message);
        return message;
    }

    public void queueSave() {
        UpdateRegionEvent updateRegionEvent = new UpdateRegionEvent(this);
        Bukkit.getServer().getPluginManager().callEvent(updateRegionEvent);
        if(!updateRegionEvent.isCancelled()) {
            this.needsSave = true;
        }
    }

    public void setSaved() {
        this.needsSave = false;
    }

    public boolean needsSave() {
        if(this.needsSave) {
            return true;
        }
        for(Region subregion : this.subregions) {
            if(subregion.needsSave()) {
                return true;
            }
        }
        return false;
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

        for(int i = 0; i < entities.size(); i++) {
            Location entityLoc = entities.get(i).getLocation();
            boolean insideRegion = false;
            boolean add = true;

            if(this.getRegion().contains(entityLoc.getBlockX(), entityLoc.getBlockY(), entityLoc.getBlockZ())) {
                insideRegion = true;
            }

            if((entities.get(i).getType() == EntityType.PLAYER) && !includePlayers) {
                add = false;
            }

            if(insideRegion && add) {
                result.add(entities.get(i));
            }

        }
        return result;
    }

    public List<Entity> getFilteredInsideEntities(boolean includePlayers, boolean includeLivingEntity, boolean includeVehicles, boolean includeProjectiles, boolean includeAreaEffectCloud, boolean includeItemFrames, boolean includePaintings) {

        List<Entity> insideEntitys = this.getInsideEntities(includePlayers);
        List<Entity> result = new ArrayList<>();

        for(Entity selectedEntity : insideEntitys) {
            boolean add = false;

            if((selectedEntity instanceof LivingEntity) && includeLivingEntity && (selectedEntity.getType() != EntityType.PLAYER)) {
                add = true;
            }

            if((selectedEntity instanceof Vehicle) && includeVehicles) {
                add = true;
            }

            if((selectedEntity instanceof Projectile) && includeProjectiles) {
                add = true;
            }

            if((selectedEntity instanceof AreaEffectCloud) && includeAreaEffectCloud) {
                add = true;
            }

            if((selectedEntity instanceof ItemFrame) && includeItemFrames) {
                add = true;
            }

            if((selectedEntity instanceof Painting) && includePaintings) {
                add = true;
            }

            if(add) {
                result.add(selectedEntity);
            }
        }

        return result;
    }

    public int getExtraEntityAmount(EntityType entityType) {
        Integer amount = this.extraEntitys.get(entityType);
        if(amount == null) {
            return 0;
        } else {
            return amount;
        }
    }

    protected int getM2Amount() {
        if(this.m2Amount == null) {
            int hight = ((this.getRegion().getMaxPoint().getBlockY() - this.getRegion().getMinPoint().getBlockY()) + 1);
            this.m2Amount = this.getRegion().getVolume() / hight;
        }
        return this.m2Amount;
    }

    public int getExtraTotalEntitys() {
        return this.extraTotalEntitys;
    }

    public void setExtraTotalEntitys(int extraTotalEntitys) {
        if(extraTotalEntitys < 0) {
            this.extraTotalEntitys = 0;
        } else {
            this.extraTotalEntitys = extraTotalEntitys;
        }

        this.queueSave();
    }

    public void setExtraEntityAmount(EntityType entityType, int amount) {
        this.extraEntitys.remove(entityType);
        if(amount > 0) {
            this.extraEntitys.put(entityType, amount);
        }
        this.queueSave();
    }

    public void setRegionKind(RegionKind regionKind) {
        this.regionKind = regionKind;
        this.queueSave();
    }

    protected HashMap<EntityType, Integer> getExtraEntitys() {
        return this.extraEntitys;
    }

    public ConfigurationSection toConfigurationSection() {
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.set("sold", this.isSold());
        yamlConfiguration.set("isHotel", this.isHotel());
        yamlConfiguration.set("lastreset", this.getLastreset());
        yamlConfiguration.set("regiontype", this.getSellType().getInternalName());
        if(this.getPriceObject().isAutoPrice()) {
            yamlConfiguration.set("autoprice", this.getPriceObject().getAutoPrice().getName());
            yamlConfiguration.set("price", null);
        } else {
            yamlConfiguration.set("price", this.getPrice());
        }
        List<String> signs = new ArrayList<>();
        for(SignData signData : this.getSellSigns()) {
            signs.add(signData.toString());
        }
        yamlConfiguration.set("signs", signs);

        if(!this.isSubregion()) {
            yamlConfiguration.set("kind", this.getRegionKind().getName());
            yamlConfiguration.set("flagGroup", this.flagGroup.getName());
            yamlConfiguration.set("autoreset", this.isAutoreset());
            yamlConfiguration.set("entityLimitGroup", this.getEntityLimitGroup().getName());
            yamlConfiguration.set("doBlockReset", this.isDoBlockReset());
            yamlConfiguration.set("allowedSubregions", this.getAllowedSubregions());
            yamlConfiguration.set("isUserResettable", this.isUserResettable());
            yamlConfiguration.set("boughtExtraTotalEntitys", this.getExtraTotalEntitys());
            List<String> boughtExtraEntitysStringList = new ArrayList<>();
            for(Map.Entry<EntityType, Integer> entry : this.getExtraEntitys().entrySet()) {
                boughtExtraEntitysStringList.add(entry.getKey().name() + ": " + entry.getValue());
            }
            yamlConfiguration.set("boughtExtraEntitys", boughtExtraEntitysStringList);
            if(this.getTeleportLocation() != null) {
                String teleportloc = this.getTeleportLocation().getWorld().getName() + ";" + this.getTeleportLocation().getBlockX() + ";" +
                        this.getTeleportLocation().getBlockY() + ";" + this.getTeleportLocation().getBlockZ() + ";" +
                        this.getTeleportLocation().getPitch() + ";" + this.getTeleportLocation().getYaw();
                yamlConfiguration.set("teleportLoc", teleportloc);
            } else {
                yamlConfiguration.set("teleportLoc", null);
            }
            for(Region subregion : this.getSubregions()) {
                yamlConfiguration.set("subregions." + subregion.getRegion().getId(), subregion.toConfigurationSection());
            }
        }
        return yamlConfiguration;
    }

    public List<String> tabCompleteRegionMembers(String args) {
        List<String> returnme = new ArrayList<>();

        List<UUID> uuidList = this.getRegion().getMembers();
        for(UUID uuids: uuidList) {
            OfflinePlayer oplayer = Bukkit.getOfflinePlayer(uuids);
            if(oplayer != null) {
                if (oplayer.getName().toLowerCase().startsWith(args)) {
                    returnme.add(oplayer.getName());
                }
            }
        }
        return  returnme;
    }

    public Set<Chunk> getChunks() {
        Set<Chunk> chunkSet = new HashSet<>();
        int maxX = this.getRegion().getMaxPoint().getBlockX();
        int maxZ = this.getRegion().getMaxPoint().getBlockZ();

        for(int x = this.getRegion().getMinPoint().getBlockX(); x <= maxX + 16; x += 16) {
            for(int z = this.getRegion().getMinPoint().getBlockZ(); z <= maxZ + 16; z += 16) {
                chunkSet.add(this.getRegionworld().getChunkAt(x >> 4, z >> 4));
            }
        }
        return chunkSet;
    }
}
