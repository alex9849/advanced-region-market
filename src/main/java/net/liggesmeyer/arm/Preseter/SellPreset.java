package net.liggesmeyer.arm.Preseter;

import net.liggesmeyer.arm.Messages;
import net.liggesmeyer.arm.regions.RegionKind;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SellPreset {
    private static final String SET_PRICE = " (?i)preset price [+-]?([0-9]+[.])?[0-9]+";
    private static final String REMOVE_PRICE = " (?i)preset price remove";
    private static final String SET_REGIONKIND = " (?i)preset regionkind [^;\n ]+";
    private static final String REMOVE_REGIONKIND = " (?i)preset regionkind remove";
    private static final String SET_AUTO_RESET = " (?i)preset autoreset (false|true)";
    private static final String REMOVE_AUTO_RESET = " (?i)preset autoreset remove";
    private static final String SET_HOTEL = " (?i)preset hotel (false|true)";
    private static final String REMOVE_HOTEL = " (?i)preset hotel remove";
    private static final String SET_DO_BLOCK_RESET = " (?i)preset doblockreset (false|true)";
    private static final String REMOVE_DO_BLOCK_RESET = " (?i)preset doblockreset remove";
    private static final String RESET = " (?i)preset reset";
    private static final String INFO = " (?i)preset info";
    private static ArrayList<SellPreset> list = new ArrayList<>();
    private Player assignedPlayer;
    private boolean hasPrice = false;
    private double price = 0;
    private boolean hasRegionKind = false;
    private RegionKind regionKind = RegionKind.DEFAULT;
    private boolean hasAutoReset = false;
    private boolean autoReset = false;
    private boolean hasIsHotel = false;
    private boolean isHotel = false;
    private boolean hasDoBlockReset = false;
    private boolean doBlockReset = true;

    public SellPreset(Player player){
        assignedPlayer = player;
    }

    public static ArrayList<SellPreset> getList(){
        return SellPreset.list;
    }

    public Player getAssignedPlayer(){
        return this.assignedPlayer;
    }

    public void setPrice(double price){
        if(price < 0){
            price = price * (-1);
        }
        this.hasPrice = true;
        this.price = price;
    }

    public void removePrice(){
        this.hasPrice = false;
        this.price = 0;
    }

    public boolean hasPrice() {
        return hasPrice;
    }

    public boolean hasRegionKind() {
        return hasRegionKind;
    }

    public boolean hasAutoReset() {
        return hasAutoReset;
    }

    public boolean hasIsHotel() {
        return hasIsHotel;
    }

    public boolean isHasDoBlockReset() {
        return hasDoBlockReset;
    }

    public void setRegionKind(RegionKind regionKind){
        if(regionKind == null) {
            regionKind = RegionKind.DEFAULT;
        }
        this.hasRegionKind = true;
        this.regionKind = regionKind;
    }

    public void setDoBlockReset(Boolean bool){
        this.hasDoBlockReset = true;
        this.doBlockReset = bool;
    }

    public void removeDoBlockReset(){
        this.hasDoBlockReset = false;
        this.doBlockReset = true;
    }

    public void removeRegionKind(){
        this.hasRegionKind = false;
        this.regionKind = RegionKind.DEFAULT;
    }

    public void setAutoReset(Boolean autoReset) {
        this.hasAutoReset = true;
        this.autoReset = autoReset;
    }

    public void removeAutoReset(){
        this.hasAutoReset = false;
        this.autoReset = true;
    }

    public void setHotel(Boolean isHotel){
        this.hasIsHotel = true;
        this.isHotel = isHotel;
    }

    public void removeHotel(){
        this.hasIsHotel = false;
        this.isHotel = false;
    }

    public static boolean hasPreset(Player player){
        for(int i = 0; i < SellPreset.getList().size(); i++) {
            if(SellPreset.getList().get(i).getAssignedPlayer() == player) {
                return true;
            }
        }
        return false;
    }

    public static SellPreset getPreset(Player player) {
        for(int i = 0; i < SellPreset.getList().size(); i++) {
            if(SellPreset.getList().get(i).getAssignedPlayer() == player) {
                return SellPreset.getList().get(i);
            }
        }
        return null;
    }

    public static boolean removePreset(Player player){
        for(int i = 0; i < SellPreset.getList().size(); i++) {
            if(SellPreset.getList().get(i).getAssignedPlayer() == player) {
                SellPreset.getList().remove(i);
                return true;
            }
        }
        return false;
    }

    public double getPrice() {
        return price;
    }

    public RegionKind getRegionKind() {
        return regionKind;
    }

    public boolean isAutoReset() {
        return autoReset;
    }

    public boolean isDoBlockReset() {
        return doBlockReset;
    }

    public boolean isHotel() {
        return isHotel;
    }

    public void getPresetInfo(Player player) {
        String price = "not defined";
        if(this.hasPrice()) {
            price = this.getPrice() + "";
        }
        RegionKind regKind = RegionKind.DEFAULT;
        if(this.hasRegionKind()) {
            regKind = this.getRegionKind();
        }

        player.sendMessage(ChatColor.GOLD + "===========[Info]===========");
        player.sendMessage(Messages.REGION_INFO_PRICE + price);
        player.sendMessage(Messages.REGION_INFO_TYPE + regKind.getName());
        player.sendMessage(Messages.REGION_INFO_AUTORESET + this.isAutoReset());
        player.sendMessage(Messages.REGION_INFO_HOTEL + this.isHotel());
        player.sendMessage(Messages.REGION_INFO_DO_BLOCK_RESET + this.isDoBlockReset());
    }

    public static boolean onCommand(CommandSender sender, String command, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Messages.PREFIX + Messages.COMMAND_ONLY_INGAME);
            return true;
        }
        Player player = (Player) sender;
        if(command.matches(SET_PRICE)) {
            if(hasPreset(player)) {
                getPreset(player).setPrice(Double.parseDouble(args[2]));
                player.sendMessage(Messages.PRESET_SET);
                return true;
            } else {
                getList().add(new SellPreset(player));
                getPreset(player).setPrice(Double.parseDouble(args[2]));
                player.sendMessage(Messages.PRESET_SET);
                return true;
            }
        } else if(command.matches(REMOVE_PRICE)) {
            if(hasPreset(player)){
                getPreset(player).removePrice();
                player.sendMessage(Messages.PRESET_REMOVED);
                return true;
            } else {
                player.sendMessage(Messages.PRESET_REMOVED);
                return true;
            }
        } else if(command.matches(SET_REGIONKIND)) {
            if(RegionKind.kindExists(args[2]) || args[2].equalsIgnoreCase(RegionKind.DEFAULT.getName())){
                RegionKind regkind = RegionKind.getRegionKind(args[2]);
                if(hasPreset(player)) {
                    getPreset(player).setRegionKind(regkind);
                    player.sendMessage(Messages.PRESET_SET);
                } else {
                    getList().add(new SellPreset(player));
                    getPreset(player).setRegionKind(regkind);
                    player.sendMessage(Messages.PRESET_SET);
                }
            } else {
                player.sendMessage(Messages.PREFIX + Messages.REGIONKIND_DOES_NOT_EXIST);
                return true;
            }
        } else if(command.matches(REMOVE_REGIONKIND)) {
            if(hasPreset(player)){
                getPreset(player).removeRegionKind();
                player.sendMessage(Messages.PRESET_REMOVED);
                return true;
            } else {
                player.sendMessage(Messages.PRESET_REMOVED);
                return true;
            }
        } else if(command.matches(SET_AUTO_RESET)) {
            if(hasPreset(player)) {
                getPreset(player).setAutoReset(Boolean.parseBoolean(args[2]));
                player.sendMessage(Messages.PRESET_SET);
                return true;
            } else {
                getList().add(new SellPreset(player));
                getPreset(player).setAutoReset(Boolean.parseBoolean(args[2]));
                player.sendMessage(Messages.PRESET_SET);
                return true;
            }
        } else if(command.matches(REMOVE_AUTO_RESET)) {
            if(hasPreset(player)){
                getPreset(player).removeAutoReset();
                player.sendMessage(Messages.PRESET_REMOVED);
                return true;
            } else {
                player.sendMessage(Messages.PRESET_REMOVED);
                return true;
            }
        }else if(command.matches(SET_HOTEL)) {
            if(hasPreset(player)) {
                getPreset(player).setHotel(Boolean.parseBoolean(args[2]));
                player.sendMessage(Messages.PRESET_SET);
                return true;
            } else {
                getList().add(new SellPreset(player));
                getPreset(player).setHotel(Boolean.parseBoolean(args[2]));
                player.sendMessage(Messages.PRESET_SET);
                return true;
            }
        } else if(command.matches(REMOVE_HOTEL)) {
            if(hasPreset(player)){
                getPreset(player).removeHotel();
                player.sendMessage(Messages.PRESET_REMOVED);
                return true;
            } else {
                player.sendMessage(Messages.PRESET_REMOVED);
                return true;
            }
        } else if(command.matches(RESET)) {
            if(removePreset(player)){
                player.sendMessage(Messages.PRESET_REMOVED);
                return true;
            } else {
                player.sendMessage(Messages.PRESET_NOT_EXISTING);
                return true;
            }
        } else if(command.matches(INFO)) {
            if(hasPreset(player)){
                getPreset(player).getPresetInfo(player);
                return true;
            } else {
                player.sendMessage(Messages.PRESET_NOT_EXISTING);
                return true;
            }
        } else if(command.matches(SET_DO_BLOCK_RESET)) {
            if(hasPreset(player)) {
                getPreset(player).setDoBlockReset(Boolean.parseBoolean(args[2]));
                player.sendMessage(Messages.PRESET_SET);
                return true;
            } else {
                getList().add(new SellPreset(player));
                getPreset(player).setDoBlockReset(Boolean.parseBoolean(args[2]));
                player.sendMessage(Messages.PRESET_SET);
                return true;
            }
        } else if(command.matches(REMOVE_DO_BLOCK_RESET)) {
            if(hasPreset(player)){
                getPreset(player).removeDoBlockReset();
                player.sendMessage(Messages.PRESET_REMOVED);
                return true;
            } else {
                player.sendMessage(Messages.PRESET_REMOVED);
                return true;
            }
        } else {
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use:");
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "/arm preset reset");
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "/arm preset info");
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "/arm preset price ([PRICE]/remove)");
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "/arm preset regionkind ([REGIONKIND]/remove)");
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "/arm preset autoreset (true/false/remove)");
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "/arm preset hotel (true/false/remove)");
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "/arm preset doblockreset (true/false/remove)");
        }
        return true;
    }
}
