package net.liggesmeyer.arm.Preseter;

import net.liggesmeyer.arm.Messages;
import net.liggesmeyer.arm.regions.RegionKind;
import net.liggesmeyer.arm.regions.RentRegion;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class RentPreset extends Preset {
    protected static final String SET_PRICE = " (?i)rentpreset (?i)price [+-]?([0-9]+[.])?[0-9]+";
    protected static final String REMOVE_PRICE = " (?i)rentpreset (?i)price remove";
    protected static final String SET_EXTEND_PER_CLICK = " (?i)rentpreset (?i)extendperclick ([0-9]+(s|m|h|d))";
    protected static final String REMOVE_EXTEND_PER_CLICK = " (?i)rentpreset (?i)extendperclick remove";
    protected static final String SET_MAX_RENT_TIME = " (?i)rentpreset (?i)maxrenttime ([0-9]+(s|m|h|d))";
    protected static final String SET_REGIONKIND = " (?i)rentpreset (?i)regionkind [^;\n ]+";
    protected static final String REMOVE_REGIONKIND = " (?i)rentpreset (?i)regionkind remove";
    protected static final String SET_AUTO_RESET = " (?i)rentpreset (?i)autoreset (false|true)";
    protected static final String REMOVE_AUTO_RESET = " (?i)rentpreset (?i)autoreset remove";
    protected static final String SET_HOTEL = " (?i)rentpreset (?i)hotel (false|true)";
    protected static final String REMOVE_HOTEL = " (?i)rentpreset (?i)hotel remove";
    protected static final String SET_DO_BLOCK_RESET = " (?i)rentpreset (?i)doblockreset (false|true)";
    protected static final String REMOVE_DO_BLOCK_RESET = " (?i)rentpreset (?i)doblockreset remove";
    protected static final String RESET = " (?i)rentpreset (?i)reset";
    protected static final String INFO = " (?i)rentpreset (?i)info";
    protected static ArrayList<RentPreset> list = new ArrayList<>();
    protected boolean hasMaxRentTime = false;
    protected long maxRentTime = 0;
    protected boolean hasExtendPerClick = false;
    protected long extendPerClick = 0;

    public RentPreset(Player player) {
        super(player);
    }

    public boolean hasExtendPerClick() {
        return hasExtendPerClick;
    }

    public void removeExtendPerClick(){
        this.hasExtendPerClick = false;
        this.extendPerClick = 0;
    }

    public long getMaxRentTime(){
        return this.maxRentTime;
    }

    public long getExtendPerClick(){
        return this.extendPerClick;
    }

    public void setExtendPerClick(String string) {
        this.hasExtendPerClick = true;
        this.extendPerClick = RentRegion.stringToTime(string);
    }

    public void setMaxRentTime(String string) {
        this.hasMaxRentTime = true;
        this.maxRentTime = RentRegion.stringToTime(string);
    }

    public boolean hasMaxRentTime() {
        return hasMaxRentTime;
    }

    public void removeMaxRentTime() {
        this.hasMaxRentTime = false;
        this.maxRentTime = 0;
    }

    public static ArrayList<RentPreset> getList(){
        return RentPreset.list;
    }

    public static boolean hasPreset(Player player){
        for(int i = 0; i < getList().size(); i++) {
            if(getList().get(i).getAssignedPlayer() == player) {
                return true;
            }
        }
        return false;
    }

    public static RentPreset getPreset(Player player) {
        for(int i = 0; i < getList().size(); i++) {
            if(getList().get(i).getAssignedPlayer() == player) {
                return getList().get(i);
            }
        }
        return null;
    }

    public static boolean removePreset(Player player){
        for(int i = 0; i < getList().size(); i++) {
            if(getList().get(i).getAssignedPlayer() == player) {
                getList().remove(i);
                return true;
            }
        }
        return false;
    }

    public String longToTime(long time){

        long remainingDays = TimeUnit.DAYS.convert(time, TimeUnit.MILLISECONDS);
        time = time - (remainingDays * 1000 * 60 * 60 *24);

        long remainingHours = TimeUnit.HOURS.convert(time, TimeUnit.MILLISECONDS);
        time = time - (remainingHours * 1000 * 60 * 60);

        long remainingMinutes = TimeUnit.MINUTES.convert(time, TimeUnit.MILLISECONDS);
        time = time - (remainingMinutes * 1000 * 60);

        long remainingSeconds = TimeUnit.SECONDS.convert(time, TimeUnit.MILLISECONDS);


        String timetoString = "";
        if(remainingDays != 0) {
            timetoString = timetoString + remainingDays + "d";
        }
        if(remainingHours != 0) {
            timetoString = timetoString + remainingHours + "h";
        }
        if(remainingMinutes != 0) {
            timetoString = timetoString + remainingMinutes + "m";
        }
        if(remainingSeconds != 0) {
            timetoString = timetoString + remainingSeconds + "s";
        }

        return timetoString;
    }

    @Override
    public void getPresetInfo(Player player) {
        String price = "not defined";
        if(this.hasPrice()) {
            price = this.getPrice() + "";
        }
        RegionKind regKind = RegionKind.DEFAULT;
        if(this.hasRegionKind()) {
            regKind = this.getRegionKind();
        }

        player.sendMessage(ChatColor.GOLD + "=========[RentPreset INFO]=========");
        player.sendMessage(Messages.REGION_INFO_PRICE + price);
        player.sendMessage(Messages.REGION_INFO_EXTEND_PER_CLICK + longToTime(this.extendPerClick));
        player.sendMessage(Messages.REGION_INFO_MAX_RENT_TIME + longToTime(this.maxRentTime));
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
                getList().add(new RentPreset(player));
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
                    getList().add(new RentPreset(player));
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
                getList().add(new RentPreset(player));
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
                getList().add(new RentPreset(player));
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
                getList().add(new RentPreset(player));
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

        } else if(command.matches(SET_EXTEND_PER_CLICK)) {
            if(hasPreset(player)) {
                getPreset(player).setExtendPerClick(args[2]);
                player.sendMessage(Messages.PRESET_SET);
                return true;
            } else {
                getList().add(new RentPreset(player));
                getPreset(player).setExtendPerClick(args[2]);
                player.sendMessage(Messages.PRESET_SET);
                return true;
            }
        } else if(command.matches(REMOVE_EXTEND_PER_CLICK)) {
            if(hasPreset(player)){
                getPreset(player).removeExtendPerClick();
                player.sendMessage(Messages.PRESET_REMOVED);
                return true;
            } else {
                player.sendMessage(Messages.PRESET_REMOVED);
                return true;
            }

        } else if(command.matches(SET_MAX_RENT_TIME)) {
            if(hasPreset(player)) {
                getPreset(player).setMaxRentTime(args[2]);
                player.sendMessage(Messages.PRESET_SET);
                return true;
            } else {
                getList().add(new RentPreset(player));
                getPreset(player).setMaxRentTime(args[2]);
                player.sendMessage(Messages.PRESET_SET);
                return true;
            }
        } else if(command.matches(REMOVE_EXTEND_PER_CLICK)) {
            if(hasPreset(player)){
                getPreset(player).removeMaxRentTime();
                player.sendMessage(Messages.PRESET_REMOVED);
                return true;
            } else {
                player.sendMessage(Messages.PRESET_REMOVED);
                return true;
            }

        }else {
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use:");
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "/arm sellpreset reset");
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "/arm sellpreset info");
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "/arm sellpreset price ([PRICE]/remove)");
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "/arm sellpreset regionkind ([REGIONKIND]/remove)");
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "/arm sellpreset autoreset (true/false/remove)");
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "/arm sellpreset hotel (true/false/remove)");
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "/arm sellpreset doblockreset (true/false/remove)");
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "/arm sellpreset extendperclick <ExtendPerClick (Ex.: 5d)>");
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "/arm sellpreset maxrenttime <MaxRentTime (Ex.: 5d)>");
        }
        return true;
    }

}
