package net.alex9849.arm.minifeatures;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.SellType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Diagram {

    public static boolean sendRegionStats(CommandSender sender) {
        sender.sendMessage(Messages.REGION_STATS);
        sendStatsForAllSellTypes(sender);
        sendStatsByRegionKind(sender, RegionKind.DEFAULT);
        for (RegionKind regionKind : AdvancedRegionMarket.getInstance().getRegionKindManager()) {
            sendStatsByRegionKind(sender, regionKind);
        }
        sendStatsByRegionKind(sender, RegionKind.SUBREGION);
        return true;
    }

    public static boolean sendRegionStats(CommandSender sender, String regiontype) throws InputException {
        SellType sellType = SellType.getSelltype(regiontype);
        RegionKind regionKind = AdvancedRegionMarket.getInstance().getRegionKindManager().getRegionKind(regiontype);

        if (sellType == null && regionKind == null) {
            throw new InputException(sender, Messages.REGIONKIND_DOES_NOT_EXIST);
        }
        sender.sendMessage(Messages.REGION_STATS);
        if (sellType != null) {
            sendStatsBySellType(sender, sellType);
        }

        if (regionKind != null) {
            sendStatsByRegionKind(sender, regionKind);
        }
        return true;
    }

    private static void sendStatsForAllSellTypes(CommandSender sender) {

        int allRegions = 0;
        int allSoldRegions = 0;

        for (Region region : AdvancedRegionMarket.getInstance().getRegionManager()) {
            allRegions++;
            if (region.isSold()) {
                allSoldRegions++;
            }
        }

        String regtypesting = Messages.REGION_STATS_PATTERN;
        sender.sendMessage(regtypesting.replace("%regionkind%", Messages.LIMIT_INFO_TOTAL)
                .replace("%regionkinddisplay%", Messages.LIMIT_INFO_TOTAL));
        sender.sendMessage(generateDiagramm(allSoldRegions, allRegions));
        sendStatsBySellType(sender, SellType.SELL);
        sendStatsBySellType(sender, SellType.CONTRACT);
        sendStatsBySellType(sender, SellType.RENT);
    }

    private static void sendStatsByRegionKind(CommandSender sender, RegionKind regionKind) {
        int regions = 0;
        int soldregions = 0;

        for (Region region : AdvancedRegionMarket.getInstance().getRegionManager().getRegionsByRegionKind(regionKind)) {
            if (region.getRegionKind() == regionKind) {
                regions++;
                if (region.isSold()) {
                    soldregions++;
                }
            }
        }
        sender.sendMessage(regionKind.getConvertedMessage(Messages.REGION_STATS_PATTERN));
        sender.sendMessage(generateDiagramm(soldregions, regions));
    }

    private static void sendStatsBySellType(CommandSender sender, SellType sellType) {
        int regions = 0;
        int soldregions = 0;

        for (Region region : AdvancedRegionMarket.getInstance().getRegionManager().getRegionsBySelltype(sellType)) {
            regions++;
            if (region.isSold()) {
                soldregions++;
            }
        }

        String regtypesting = Messages.REGION_STATS_PATTERN;
        sender.sendMessage(regtypesting.replace("%regionkind%", sellType.getName())
                .replace("%regionkinddisplay%", sellType.getName()));
        sender.sendMessage(generateDiagramm(soldregions, regions));
    }

    private static String generateDiagramm(int used, int max) {
        double percent = 100;
        percent = percent / ((double) max);
        percent = percent * used * 100;
        percent = Math.round(percent);
        percent = percent / 100;
        int neededhashtags = 20;
        neededhashtags = (int) Math.round(neededhashtags * (percent / 100));
        String diagram = ChatColor.GOLD + "[";
        ChatColor color = null;
        if (percent < 65) {
            color = ChatColor.GREEN;
        } else if (percent < 85) {
            color = ChatColor.YELLOW;
        } else {
            color = ChatColor.DARK_RED;
        }

        for (int i = 0; i < 20; i++) {
            if (i < neededhashtags) {
                diagram = diagram + color + "#";
            } else {
                diagram = diagram + ChatColor.WHITE + "-";
            }
        }
        diagram = diagram + ChatColor.GOLD + "] " + percent + "% (" + used + "/" + max + ")";
        return diagram;
    }
}
