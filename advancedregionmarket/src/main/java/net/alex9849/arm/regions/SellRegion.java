package net.alex9849.arm.regions;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.AlreadySoldException;
import net.alex9849.arm.exceptions.NoPermissionException;
import net.alex9849.arm.exceptions.NotEnoughMoneyException;
import net.alex9849.arm.exceptions.OutOfLimitExeption;
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


    public SellRegion(WGRegion region, List<SignData> sellsigns, Price price, boolean sold, Region parentRegion) {
        super(region, sellsigns, sold, parentRegion);
        this.price = price;
    }

    public SellRegion(WGRegion region, World regionworld, List<SignData> sellsigns, Price price, boolean sold) {
        super(region, regionworld, sellsigns, sold);
        this.price = price;
    }

    @Override
    public void signClickAction(Player player) throws OutOfLimitExeption, AlreadySoldException, NotEnoughMoneyException, NoPermissionException {
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
        if (this.isSold()) {
            String[] lines = new String[4];
            lines[0] = this.replaceVariables(Messages.SOLD_SIGN1);
            lines[1] = this.replaceVariables(Messages.SOLD_SIGN2);
            lines[2] = this.replaceVariables(Messages.SOLD_SIGN3);
            lines[3] = this.replaceVariables(Messages.SOLD_SIGN4);
            signData.writeLines(lines);
        } else {
            String[] lines = new String[4];
            lines[0] = this.replaceVariables(Messages.SELL_SIGN1);
            lines[1] = this.replaceVariables(Messages.SELL_SIGN2);
            lines[2] = this.replaceVariables(Messages.SELL_SIGN3);
            lines[3] = this.replaceVariables(Messages.SELL_SIGN4);
            signData.writeLines(lines);
        }

    }

    public SellType getSellType() {
        return SellType.SELL;
    }

    @Override
    public void setAutoPrice(AutoPrice autoPrice) {
        this.setSellPrice(new Price(autoPrice));
    }
}
