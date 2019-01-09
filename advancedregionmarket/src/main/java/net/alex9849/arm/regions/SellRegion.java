package net.alex9849.arm.regions;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.Group.LimitGroup;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.teleporter.Teleporter;
import net.alex9849.inter.WGRegion;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class SellRegion extends Region {


    public SellRegion(WGRegion region, World regionworld, List<Sign> sellsign, double price, Boolean sold, Boolean autoreset, Boolean allowOnlyNewBlocks, Boolean doBlockReset, RegionKind regionKind, Location teleportLoc, long lastreset, boolean isUserResettable, List<Region> subregions, boolean isTown, boolean isSubregion) {
        super(region, regionworld, sellsign, price, sold, autoreset,allowOnlyNewBlocks, doBlockReset, regionKind, teleportLoc, lastreset, isUserResettable, subregions, isTown, isSubregion);

        this.updateSigns();
    }

    @Override
    public void displayExtraInfo(CommandSender sender) {
        return;
    }

    @Override
    public void updateRegion() {
        this.updateSigns();
    }

    @Override
    protected void updateSignText(Sign mysign){
        if(this.sold){

            LinkedList<UUID> ownerlist = new LinkedList<>(this.getRegion().getOwners());
            String ownername;
            if(ownerlist.size() == 0){
                ownername = "Unknown";
            } else {
                OfflinePlayer owner = Bukkit.getOfflinePlayer(ownerlist.get(0));
                ownername = owner.getName();
            }

            String line1 = Messages.SOLD_SIGN1.replace("%regionid%", this.getRegion().getId());
            line1 = line1.replace("%price%", this.price + "");
            line1 = line1.replace("%currency%", Messages.CURRENCY);
            line1 = line1.replace("%dimensions%", this.getDimensions());
            line1 = line1.replace("%owner%", ownername);

            String line2 = Messages.SOLD_SIGN2.replace("%regionid%", this.getRegion().getId());
            line2 = line2.replace("%price%", this.price + "");
            line2 = line2.replace("%currency%", Messages.CURRENCY);
            line2 = line2.replace("%dimensions%", this.getDimensions());
            line2 = line2.replace("%owner%", ownername);

            String line3 = Messages.SOLD_SIGN3.replace("%regionid%", this.getRegion().getId());
            line3 = line3.replace("%price%", this.price + "");
            line3 = line3.replace("%currency%", Messages.CURRENCY);
            line3 = line3.replace("%dimensions%", this.getDimensions());
            line3 = line3.replace("%owner%", ownername);

            String line4 = Messages.SOLD_SIGN4.replace("%regionid%", this.getRegion().getId());
            line4 = line4.replace("%price%", this.price + "");
            line4 = line4.replace("%currency%", Messages.CURRENCY);
            line4 = line4.replace("%dimensions%", this.getDimensions());
            line4 = line4.replace("%owner%", ownername);

            mysign.setLine(0, line1);
            mysign.setLine(1, line2);
            mysign.setLine(2, line3);
            mysign.setLine(3, line4);
            mysign.update();

        } else {

            String line1 = Messages.SELL_SIGN1.replace("%regionid%", this.getRegion().getId());
            line1 = line1.replace("%price%", this.price + "");
            line1 = line1.replace("%currency%", Messages.CURRENCY);
            line1 = line1.replace("%dimensions%", this.getDimensions());

            String line2 = Messages.SELL_SIGN2.replace("%regionid%", this.getRegion().getId());
            line2 = line2.replace("%price%", this.price + "");
            line2 = line2.replace("%currency%", Messages.CURRENCY);
            line2 = line2.replace("%dimensions%", this.getDimensions());

            String line3 = Messages.SELL_SIGN3.replace("%regionid%", this.getRegion().getId());
            line3 = line3.replace("%price%", this.price + "");
            line3 = line3.replace("%currency%", Messages.CURRENCY);
            line3 = line3.replace("%dimensions%", this.getDimensions());

            String line4 = Messages.SELL_SIGN4.replace("%regionid%", this.getRegion().getId());
            line4 = line4.replace("%price%", this.price + "");
            line4 = line4.replace("%currency%", Messages.CURRENCY);
            line4 = line4.replace("%dimensions%", this.getDimensions());

            mysign.setLine(0, line1);
            mysign.setLine(1, line2);
            mysign.setLine(2, line3);
            mysign.setLine(3, line4);
            mysign.update();

        }

    }

    @Override
    public void buy(Player player) throws InputException {

        if(!Permission.hasAnyBuyPermission(player)) {
            throw new InputException(player, Messages.NO_PERMISSION);
        }
        if(this.sold) {
            throw new InputException(player, Messages.REGION_ALREADY_SOLD);
        }
        if (this.regionKind != RegionKind.DEFAULT){
            if(!player.hasPermission(Permission.ARM_BUYKIND + this.regionKind.getName())){
                throw new InputException(player, Messages.NO_PERMISSIONS_TO_BUY_THIS_KIND_OF_REGION);
            }
        }

        if(!LimitGroup.isCanBuyAnother(player, this)){
            throw new InputException(player, LimitGroup.getRegionBuyOutOfLimitMessage(player, this.regionKind));
        }

        if(AdvancedRegionMarket.getEcon().getBalance(player) < this.price) {
            throw new InputException(player, Messages.NOT_ENOUGHT_MONEY);
        }
        AdvancedRegionMarket.getEcon().withdrawPlayer(player, price);

        this.setSold(player);
        this.resetBuiltBlocks();
        if(AdvancedRegionMarket.isTeleportAfterSellRegionBought()){
            Teleporter.teleport(player, this, "", AdvancedRegionMarket.getARM().getConfig().getBoolean("Other.TeleportAfterRegionBoughtCountdown"));
        }
        player.sendMessage(Messages.PREFIX + Messages.REGION_BUYMESSAGE);
    }

    @Override
    public void setSold(OfflinePlayer player){
        this.sold = true;
        this.getRegion().deleteMembers();
        this.getRegion().setOwner(player);

        this.updateSigns();

        RegionManager.writeRegionsToConfig();
    }

    @Override
    public void userSell(Player player){
        List<UUID> defdomain = this.getRegion().getOwners();
        double amount = this.getPaybackMoney();

        if(amount > 0){
            for(int i = 0; i < defdomain.size(); i++) {
                AdvancedRegionMarket.getEcon().depositPlayer(Bukkit.getOfflinePlayer(defdomain.get(i)), amount);
            }
        }

        this.automaticResetRegion(player);
    }

    @Override
    public double getPaybackMoney() {
        double money = (this.getPrice() * this.getRegionKind().getPaybackPercentage())/100;
        if (money > 0) {
            return money;
        } else {
            return 0;
        }
    }

}
