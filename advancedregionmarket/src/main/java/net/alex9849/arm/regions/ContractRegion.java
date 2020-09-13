package net.alex9849.arm.regions;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.events.PreExtendEvent;
import net.alex9849.arm.exceptions.*;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.ContractPrice;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.arm.util.stringreplacer.StringCreator;
import net.alex9849.arm.util.stringreplacer.StringReplacer;
import net.alex9849.inter.WGRegion;
import net.alex9849.signs.SignData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class ContractRegion extends CountdownRegion {
    private ContractPrice contractPrice;
    private boolean terminated = false;
    private StringReplacer stringReplacer;

    {
        HashMap<String, StringCreator> variableReplacements = new HashMap<>();
        variableReplacements.put("%status%", () -> {
            return this.terminated? Messages.CONTRACT_REGION_STATUS_TERMINATED : Messages.CONTRACT_REGION_STATUS_ACTIVE;
        });
        variableReplacements.put("%isterminated%", () -> {
            return Messages.convertYesNo(this.isTerminated());
        });
        this.stringReplacer = new StringReplacer(variableReplacements, 50);
    }

    public ContractRegion(WGRegion region, List<SignData> sellsigns, ContractPrice contractPrice, boolean sold, Region parentRegion) {
        super(region, sellsigns, sold, parentRegion);
        this.contractPrice = contractPrice;
    }

    public ContractRegion(WGRegion region, World regionworld, List<SignData> sellsigns, ContractPrice contractPrice, boolean sold) {
        super(region, regionworld, sellsigns, sold);
        this.contractPrice = contractPrice;
    }

    @Override
    public void regionInfo(CommandSender sender) {
        super.regionInfo(sender);
        List<String> msg;

        if (sender.hasPermission(Permission.ADMIN_INFO)) {
            msg = Messages.REGION_INFO_CONTRACTREGION_ADMIN;
        } else {
            msg = Messages.REGION_INFO_CONTRACTREGION;
        }

        if (this.isSubregion()) {
            msg = Messages.REGION_INFO_CONTRACTREGION_SUBREGION;
        }

        for (String s : msg) {
            sender.sendMessage(this.replaceVariables(s));
        }
    }

    @Override
    public void updateRegion() {

        try {
            if (this.isSold()) {
                GregorianCalendar actualtime = new GregorianCalendar();

                //If region expired and terminated
                if (this.getPayedTill() < actualtime.getTimeInMillis()) {
                    if (this.isTerminated()) {
                        this.automaticResetRegion(ActionReason.EXPIRED, true);
                    } else {
                        UUID owner = this.getOwner();
                        if (owner == null) {
                            this.extend();
                        } else {
                            OfflinePlayer oplayer = Bukkit.getOfflinePlayer(owner);
                            if (AdvancedRegionMarket.getInstance().getEcon().hasAccount(oplayer)) {
                                PreExtendEvent preExtendEvent = new PreExtendEvent(this);
                                Bukkit.getServer().getPluginManager().callEvent(preExtendEvent);
                                if(preExtendEvent.isCancelled()) {
                                    return;
                                }
                                boolean isNoMoneyTransfer = preExtendEvent.isNoMoneyTransfer();
                                if (!isNoMoneyTransfer && AdvancedRegionMarket.getInstance().getEcon().getBalance(oplayer) < this.getPricePerPeriod()) {
                                    this.automaticResetRegion(ActionReason.INSUFFICIENT_MONEY, true);
                                } else {
                                    if(!isNoMoneyTransfer) {
                                        AdvancedRegionMarket.getInstance().getEcon().withdrawPlayer(oplayer, this.getPricePerPeriod());
                                        this.giveLandlordMoney(this.getPricePerPeriod());
                                    }
                                    this.extend();
                                    if (oplayer.isOnline() && AdvancedRegionMarket.getInstance().getPluginSettings().isSendContractRegionExtendMessage()) {
                                        String sendmessage = this.replaceVariables(Messages.CONTRACT_REGION_EXTENDED);
                                        oplayer.getPlayer().sendMessage(Messages.PREFIX + sendmessage);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (SchematicNotFoundException e) {
            AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, this.replaceVariables(Messages.COULD_NOT_FIND_OR_LOAD_SCHEMATIC_LOG));
        }

        super.updateRegion();
    }

    @Override
    public Price getPriceObject() {
        return this.contractPrice;
    }

    public void setContractPrice(ContractPrice contractPrice) {
        this.contractPrice = contractPrice;
        this.queueSave();
        this.updateSigns();
    }

    @Override
    public void setSold(OfflinePlayer player) {
        this.terminated = false;
        super.setSold(player);
        this.queueSave();
    }

    @Override
    protected void updateSignText(SignData signData) {
        if (this.isSold()) {
            String[] lines = new String[4];
            lines[0] = this.replaceVariables(Messages.CONTRACT_SOLD_SIGN1);
            lines[1] = this.replaceVariables(Messages.CONTRACT_SOLD_SIGN2);
            lines[2] = this.replaceVariables(Messages.CONTRACT_SOLD_SIGN3);
            lines[3] = this.replaceVariables(Messages.CONTRACT_SOLD_SIGN4);
            signData.writeLines(lines);
        } else {
            String[] lines = new String[4];
            lines[0] = this.replaceVariables(Messages.CONTRACT_SIGN1);
            lines[1] = this.replaceVariables(Messages.CONTRACT_SIGN2);
            lines[2] = this.replaceVariables(Messages.CONTRACT_SIGN3);
            lines[3] = this.replaceVariables(Messages.CONTRACT_SIGN4);
            signData.writeLines(lines);
        }
    }

    public void signClickAction(Player player) throws OutOfLimitExeption, AlreadySoldException, NotSoldException, NoPermissionException, NotEnoughMoneyException, RegionNotOwnException {
        if(this.isSold()) {
            this.changeTerminated(player);
        } else {
            this.buy(player);
        }
    }

    public void changeTerminated(Player player) throws OutOfLimitExeption, NoPermissionException, NotSoldException, RegionNotOwnException {
        if (!player.hasPermission(Permission.MEMBER_BUY)) {
            throw new NoPermissionException(Messages.NO_PERMISSION);
        }

        if(!this.isSold()) {
            throw new NotSoldException(Messages.REGION_NOT_SOLD);
        }

        if(!this.getRegion().hasOwner(player.getUniqueId()) && !player.hasPermission(Permission.ADMIN_TERMINATE_CONTRACT)) {
            throw new RegionNotOwnException(Messages.REGION_NOT_OWN);
        }

        if (this.isTerminated()) {
            if (!AdvancedRegionMarket.getInstance().getLimitGroupManager().isInLimit(player, this.getRegionKind())) {
                throw new OutOfLimitExeption(this.replaceVariables(Messages.REGION_BUY_OUT_OF_LIMIT));
            } else {
                this.setTerminated(false, player);
            }
        } else {
            this.setTerminated(true, player);
        }
    }

    public void setTerminated(Boolean bool, Player player) {
        this.terminated = bool;
        this.queueSave();
        if (player != null) {
            String sendmessage;
            if(bool) {
                sendmessage = Messages.CONTRACTREGION_TERMINATED;
            } else {
                sendmessage = Messages.CONTRACTREGION_REACTIVATED;
            }
            player.sendMessage(Messages.PREFIX + this.replaceVariables(sendmessage));
        }
    }

    public boolean isTerminated() {
        return this.terminated;
    }

    public void setTerminated(Boolean bool) {
        this.setTerminated(bool, null);
    }

    @Override
    public long getExtendTime() {
        return this.contractPrice.getExtendTime();
    }

    public double getPricePerM2PerWeek() {
        if (this.getExtendTime() == 0) {
            return Integer.MAX_VALUE;
        }
        double pricePerM2 = this.getPricePerM2();
        double msPerWeek = 1000 * 60 * 60 * 24 * 7;
        return (msPerWeek / this.getExtendTime()) * pricePerM2;
    }

    public double getPricePerM3PerWeek() {
        if (this.getExtendTime() == 0) {
            return Integer.MAX_VALUE;
        }
        double pricePerM2 = this.getPricePerM3();
        double msPerWeek = 1000 * 60 * 60 * 24 * 7;
        return (msPerWeek / this.getExtendTime()) * pricePerM2;
    }

    public String replaceVariables(String message) {
        message = this.stringReplacer.replace(message).toString();
        return super.replaceVariables(message);
    }

    public SellType getSellType() {
        return SellType.CONTRACT;
    }

    @Override
    public void setAutoPrice(AutoPrice autoPrice) {
        this.setContractPrice(new ContractPrice(autoPrice));
    }

    public ConfigurationSection toConfigurationSection() {
        ConfigurationSection yamlConfiguration = super.toConfigurationSection();
        yamlConfiguration.set("terminated", this.isTerminated());
        return yamlConfiguration;
    }
}
