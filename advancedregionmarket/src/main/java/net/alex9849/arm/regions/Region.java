package net.alex9849.arm.regions;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.ArmSettings;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.events.RegionEvent;
import net.alex9849.arm.events.ResetBlocksEvent;
import net.alex9849.arm.minifeatures.ParticleBorder;
import net.alex9849.arm.minifeatures.teleporter.Teleporter;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.arm.util.Saveable;
import net.alex9849.exceptions.InputException;
import net.alex9849.exceptions.SchematicNotFoundException;
import net.alex9849.inter.WGRegion;
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
    protected ArrayList<Sign> sellsign;
    protected ArrayList<Location> builtblocks;
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
    protected EntityLimitGroup entityLimitGroup;
    private boolean needsSave;
    private HashMap<EntityType, Integer> extraEntitys;
    private int extraTotalEntitys;
    Integer m2Amount;

    public Region(WGRegion region, World regionworld, List<Sign> sellsign, Price price, Boolean sold, Boolean autoreset,
                  Boolean isHotel, Boolean doBlockReset, RegionKind regionKind, Location teleportLoc, long lastreset,
                  boolean isUserResettable, List<Region> subregions, int allowedSubregions, EntityLimitGroup entityLimitGroup,
                  HashMap<EntityType, Integer> extraEntitys, int boughtExtraTotalEntitys){
        this.region = region;
        this.sellsign = new ArrayList<Sign>(sellsign);
        this.sold = sold;
        this.price = price;
        this.regionworld = regionworld;
        this.regionKind = regionKind;
        this.autoreset = autoreset;
        this.isDoBlockReset = doBlockReset;
        this.lastreset = lastreset;
        this.builtblocks = new ArrayList<Location>();
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
                    builtblocks.add(loc);
                }
                reader.close();
                filereader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.builtblocks = new ArrayList<Location>();
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
        boolean contains = false;
        for(Location bbloc : this.builtblocks) {
            if((bbloc.getBlockX() == loc.getBlockX()) && (bbloc.getBlockY() == loc.getBlockY()) && (bbloc.getBlockZ() == loc.getBlockZ())) {
                contains = true;
                break;
            }
        }
        if(!contains) {
            this.builtblocks.add(loc);
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

    public void addSign(Location loc){
        Sign newsign = (Sign) loc.getBlock().getState();
        sellsign.add(newsign);
        this.queueSave();
        this.updateSignText(newsign);
        this.getRegionworld().save();

    }

    public boolean removeSign(Location loc){
        return this.removeSign(loc, null);
    }

    public boolean removeSign(Location loc, Player destroyer){
        for(int i = 0; i < this.sellsign.size(); i++){
            if(this.sellsign.get(i).getWorld().getName().equals(loc.getWorld().getName())){
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

    public void updateSigns() {
        for (int i = 0; i < this.sellsign.size(); i++) {
            Location loc = new Location(this.sellsign.get(i).getLocation().getWorld(), this.sellsign.get(i).getLocation().getBlockX(), this.sellsign.get(i).getLocation().getBlockY(), this.sellsign.get(i).getLocation().getBlockZ());
            if (loc.getBlock().getType() != this.sellsign.get(i).getType()) {
                loc.getBlock().setType(this.sellsign.get(i).getType());
                loc.getBlock().setBlockData(this.sellsign.get(i).getBlockData());
                this.sellsign.set(i, (Sign) loc.getBlock().getState());
            }

            this.updateSignText(this.sellsign.get(i));
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
            if(this.sellsign.get(i).getWorld().getName().equalsIgnoreCase(sign.getWorld().getName())){
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
            this.builtblocks = new ArrayList<>();
        }
    }

    public void regionInfo(CommandSender sender){
        String owners = "";
        String members = "";
        String subregions = "";
        List<UUID> ownerslist = this.getRegion().getOwners();
        List<UUID> memberslist = this.getRegion().getMembers();
        for(int i = 0; i < ownerslist.size() - 1; i++){
            owners = owners + Bukkit.getOfflinePlayer(ownerslist.get(i)).getName() + ", ";
        }
        if(ownerslist.size() != 0){
            owners = owners + Bukkit.getOfflinePlayer(ownerslist.get(ownerslist.size() - 1)).getName();
        }
        for(int i = 0; i < memberslist.size() - 1; i++){
            members = members + Bukkit.getOfflinePlayer(memberslist.get(i)).getName() + ", ";
        }
        if(memberslist.size() != 0){
            members = members + Bukkit.getOfflinePlayer(memberslist.get(memberslist.size() - 1)).getName();
        }
        for (int i = 0; i < this.getSubregions().size() - 1; i++) {
            subregions = subregions + this.getSubregions().get(i).getRegion().getId() + ", ";
        }
        if(this.getSubregions().size() != 0){
            subregions = subregions + this.getSubregions().get(this.getSubregions().size() - 1).getRegion().getId();
        }



        sender.sendMessage(Messages.REGION_INFO);
        sender.sendMessage(Messages.REGION_INFO_ID + this.getRegion().getId());
        sender.sendMessage(Messages.REGION_INFO_SOLD + Messages.convertYesNo(this.isSold()));
        sender.sendMessage(Messages.REGION_INFO_PRICE + this.price.calcPrice(this.getRegion()) + " " + Messages.CURRENCY);
        sender.sendMessage(Messages.REGION_INFO_TYPE + this.getRegionKind().getDisplayName());
        sender.sendMessage(Messages.REGION_INFO_ENTITYLIMITGROUP + this.getEntityLimitGroup().getName());
        sender.sendMessage(Messages.REGION_INFO_OWNER + owners);
        sender.sendMessage(Messages.REGION_INFO_MEMBERS + members);
        sender.sendMessage(Messages.REGION_INFO_HOTEL + Messages.convertYesNo(this.isHotel));
        if(sender.hasPermission(Permission.ADMIN_INFO)){
            String autoresetmsg = Messages.REGION_INFO_AUTORESET + Messages.convertYesNo(this.autoreset);
            if((!ArmSettings.isEnableAutoReset()) && this.autoreset){
                autoresetmsg = autoresetmsg + " (but globally disabled)";
            }
            sender.sendMessage(autoresetmsg);
            sender.sendMessage(Messages.REGION_INFO_DO_BLOCK_RESET + Messages.convertYesNo(this.isDoBlockReset));
            sender.sendMessage(Messages.REGION_INFO_IS_USER_RESETTABLE + Messages.convertYesNo(this.isUserResettable));
            String isAutoPriceInfo = "";
            if(this.getPriceObject().isAutoPrice()) {
                isAutoPriceInfo = this.getPriceObject().getAutoPrice().getName();
            } else {
                isAutoPriceInfo = Messages.convertYesNo(this.getPriceObject().isAutoPrice());
            }
            sender.sendMessage(Messages.REGION_INFO_AUTOPRICE + isAutoPriceInfo);
        }
        this.displayExtraInfo(sender);
        if(!this.isSubregion()) {
            sender.sendMessage(Messages.REGION_INFO_ALLOWED_SUBREGIONS + this.getAllowedSubregions());
            sender.sendMessage(Messages.REGION_INFO_SUBREGIONS + subregions);
        }


        if(sender instanceof Player) {
            if(ArmSettings.isRegionInfoParticleBorder()) {
                Player player = (Player) sender;
                new ParticleBorder(this.getRegion().getPoints(), this.getRegion().getMinY(), this.getRegion().getMaxY(), player, this.getRegionworld()).createParticleBorder(20 * 30);
                for(Region subregion : this.getSubregions()) {
                    Location lpos1 = new Location(subregion.getRegionworld(), subregion.getRegion().getMinPoint().getX(), subregion.getRegion().getMinPoint().getY(), subregion.getRegion().getMinPoint().getZ());
                    Location lPos2 = new Location(subregion.getRegionworld(), subregion.getRegion().getMaxPoint().getX(), subregion.getRegion().getMaxPoint().getY(), subregion.getRegion().getMaxPoint().getZ());
                    new ParticleBorder(lpos1.toVector(), lPos2.toVector(), player, subregion.getRegionworld()).createParticleBorder(20 * 30);
                }
            }
        }
    }

    public abstract void displayExtraInfo(CommandSender sender);

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
            for(Sign sign : this.sellsign) {
                if(Teleporter.teleport(player, sign)) {
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
        for (int i = 0; i < this.sellsign.size(); i++) {
            this.updateSignText(this.sellsign.get(i));
        }
    }

    public abstract void setSold(OfflinePlayer player);
    protected abstract void updateSignText(Sign mysign);
    public abstract void buy(Player player) throws InputException;
    public abstract void userSell(Player player);
    public abstract double getPaybackMoney();

    public void resetRegion() throws SchematicNotFoundException {

        this.unsell();

        this.resetBlocks();
        return;
    }

    public void automaticResetRegion(){
        this.automaticResetRegion(null);
    }

    public void automaticResetRegion(Player player){

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
            for (int i = 0; i < this.builtblocks.size(); i++) {
                if (this.builtblocks.get(i).distance(breakloc) < 1) {
                    return true;
                }
            }
            return false;
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

        for(int i = 0; i < this.sellsign.size(); i++){
            this.updateSignText(this.sellsign.get(i));
        }
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

    protected List<Sign> getSellSigns() {
        return this.sellsign;
    }

    protected long getLastreset() {
        return this.lastreset;
    }

    protected boolean isAutoreset() {
        return this.autoreset;
    }

    private String getOwnerName() {
        LinkedList<UUID> ownerlist = new LinkedList<>(this.getRegion().getOwners());
        String ownername;
        if(ownerlist.size() < 1){
            ownername = "Unknown";
        } else {
            OfflinePlayer owner = Bukkit.getOfflinePlayer(ownerlist.get(0));
            ownername = owner.getName();
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
        message = message.replace("%regionid%", this.getRegion().getId());
        message = message.replace("%region%", this.getRegion().getId());
        message = message.replace("%price%", Price.formatPrice(this.getPrice()) + "");
        message = message.replace("%dimensions%", this.getDimensions());
        message = message.replace("%priceperm2%", Price.formatPrice(this.getPricePerM2()) + "");
        message = message.replace("%priceperm3%", Price.formatPrice(this.getPricePerM3()) + "");
        message = message.replace("%remainingdays%", (Region.getResetCooldown() - this.timeSinceLastReset()) + "");
        message = message.replace("%paybackmoney%", Price.formatPrice(this.getPaybackMoney()));
        message = message.replace("%currency%", Messages.CURRENCY);
        message = message.replace("%owner%", this.getOwnerName());
        message = message.replace("%world%", this.getRegionworld().getName());
        message = message.replace("%subregionlimit%", this.getAllowedSubregions() + "");
        message = message.replace("%hotelfunctionstatus%", Messages.convertEnabledDisabled(this.isHotel));
        message = message.replace("%soldstatus%", this.getSoldStringStatus());
        message = message.replace("%selltype%", this.getSellType().getName());
        message = this.getRegionKind().getConvertedMessage(message);
        return message;
    }

    public void queueSave() {
        this.needsSave = true;
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

    public ConfigurationSection toConfigureationSection() {
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
        for(Sign sign : this.getSellSigns()) {
            Location signloc = sign.getLocation();
            signs.add(signloc.getWorld().getName() + ";" + signloc.getX() + ";" + signloc.getY() + ";" + signloc.getZ());
        }
        yamlConfiguration.set("signs", signs);

        if(!this.isSubregion()) {
            yamlConfiguration.set("kind", this.getRegionKind().getName());
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
                yamlConfiguration.set("subregions." + subregion.getRegion().getId(), subregion.toConfigureationSection());
            }
        }
        return yamlConfiguration;
    }


}
