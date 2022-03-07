package net.alex9849.arm.regions;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.*;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.inter.WGRegion;
import net.alex9849.signs.SignData;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SellRegion extends Region {
    private Price price;


    public SellRegion(String regionId, List<SignData> sellSigns, Price price, boolean sold, Region parentRegion) {
        super(regionId, sellSigns, sold, parentRegion);
        this.price = price;
    }

    public SellRegion(String regionId, World regionWorld, List<SignData> sellSigns, Price price, boolean sold) {
        super(regionId, regionWorld, sellSigns, sold);
        this.price = price;
    }

    @Override
    public void signClickAction(Player player) throws OutOfLimitExeption, AlreadySoldException, NotEnoughMoneyException, NoPermissionException, ProtectionOfContinuanceException {
        this.buy(player);
    }

    @Override
    public Price getPriceObject() {
        return this.price;
    }

    @Override
    public void regionInfo(CommandSender sender) {
        super.regionInfo(sender);
        List<String> msg;

        if (sender.hasPermission(Permission.ADMIN_INFO)) {
            msg = Messages.REGION_INFO_SELLREGION_ADMIN;
        } else {
            msg = Messages.REGION_INFO_SELLREGION;
        }

        if (this.isSubregion()) {
            msg = Messages.REGION_INFO_SELLREGION_SUBREGION;
        }

        for (String s : msg) {
            sender.sendMessage(this.replaceVariables(s));
        }
    }

    public void setSellPrice(Price price) {
        this.price = price;
        this.queueSave();
        this.updateSigns();
    }

    @Override
    public double getPricePerM2PerWeek() {
        return this.getPricePerM2();
    }

    @Override
    public double getPricePerM3PerWeek() {
        return this.getPricePerM2();
    }

    @Override
    public double getPaybackMoney() {
        double money = (this.getPricePerPeriod() * this.getPaybackPercentage()) / 100;
        if (money > 0) {
            return money;
        } else {
            return 0;
        }
    }

    @Override
    protected void updateSignText(SignData signData) {
        String[] lines = new String[4];

        if (this.isSold()) {
            lines[0] = this.replaceVariables(Messages.SOLD_SIGN1);
            lines[1] = this.replaceVariables(Messages.SOLD_SIGN2);
            lines[2] = this.replaceVariables(Messages.SOLD_SIGN3);
            lines[3] = this.replaceVariables(Messages.SOLD_SIGN4);

        } else if(this.isProtectionOfContinuance()) {
            lines[0] = this.replaceVariables(Messages.PROTECTION_OF_CONTINUANCE_SIGN1);
            lines[1] = this.replaceVariables(Messages.PROTECTION_OF_CONTINUANCE_SIGN2);
            lines[2] = this.replaceVariables(Messages.PROTECTION_OF_CONTINUANCE_SIGN3);
            lines[3] = this.replaceVariables(Messages.PROTECTION_OF_CONTINUANCE_SIGN4);

        } else {
            lines[0] = this.replaceVariables(Messages.SELL_SIGN1);
            lines[1] = this.replaceVariables(Messages.SELL_SIGN2);
            lines[2] = this.replaceVariables(Messages.SELL_SIGN3);
            lines[3] = this.replaceVariables(Messages.SELL_SIGN4);
        }
        signData.writeLines(lines);
    }

    public SellType getSellType() {
        return SellType.SELL;
    }

    @Override
    public void setAutoPrice(AutoPrice autoPrice) {
        this.setSellPrice(new Price(autoPrice));
    }
}
