package net.alex9849.arm.minifeatures;

import net.alex9849.arm.Messages;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regions.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Diagram {

    public static boolean sendRegionStats(CommandSender sender) {
        sender.sendMessage(Messages.REGION_STATS);
        sendStatsForAllSellTypes(sender);
        for(RegionKind regionKind : RegionKind.getRegionKindList()) {
            sendStatsByRegionKind(sender, regionKind);
        }
        return true;
    }

    public static boolean sendRegionStats(CommandSender sender, String regiontype) throws InputException {
        SellType sellType = SellType.getSelltype(regiontype);
        RegionKind regionKind = RegionKind.getRegionKind(regiontype);

        if(sellType == null && regionKind == null) {
            throw new InputException(sender, Messages.REGIONKIND_DOES_NOT_EXIST);
        }
        sender.sendMessage(Messages.REGION_STATS);
        if(sellType != null) {
            sendStatsBySellType(sender, sellType);
        }

        if(regionKind != null) {
            sendStatsByRegionKind(sender, regionKind);
        }
        return true;
    }

    private static void sendStatsForAllSellTypes(CommandSender sender) {
        int sellregions = 0;
        int soldsellregions = 0;
        int rentregions = 0;
        int soldrentregions = 0;
        int contractregions = 0;
        int soldcontractregions = 0;

        for(Region region : Region.getRegionList()) {
            if(region instanceof SellRegion) {
                sellregions++;
                if(region.isSold()) {
                    soldsellregions++;
                }
            }
            if(region instanceof RentRegion) {
                rentregions++;
                if(region.isSold()) {
                    soldrentregions++;
                }
            }
            if(region instanceof ContractRegion) {
                contractregions++;
                if(region.isSold()) {
                    soldcontractregions++;
                }
            }
        }
        String regtypesting = Messages.REGION_STATS_PATTERN;
        sender.sendMessage(regtypesting.replace("%regionkind%", Messages.LIMIT_INFO_TOTAL));
        sender.sendMessage(generateDiagramm(soldsellregions + soldrentregions + soldcontractregions, sellregions + rentregions + contractregions));
        sender.sendMessage(regtypesting.replace("%regionkind%", Messages.SELL_REGION));
        sender.sendMessage(generateDiagramm(soldsellregions, sellregions));
        sender.sendMessage(regtypesting.replace("%regionkind%", Messages.RENT_REGION));
        sender.sendMessage(generateDiagramm(soldrentregions, rentregions));
        sender.sendMessage(regtypesting.replace("%regionkind%", Messages.CONTRACT_REGION));
        sender.sendMessage(generateDiagramm(soldcontractregions, contractregions));
    }

    private static void sendStatsByRegionKind(CommandSender sender, RegionKind regionKind) {
        int regions = 0;
        int soldregions = 0;

        for(Region region : Region.getRegionList()) {
            if(region.getRegionKind() == regionKind) {
                regions++;
                if(region.isSold()) {
                    soldregions++;
                }
            }
        }
        String regtypesting = Messages.REGION_STATS_PATTERN;
        sender.sendMessage(regtypesting.replace("%regionkind%", regionKind.getName()));
        sender.sendMessage(generateDiagramm(soldregions, regions));
    }

    private static void sendStatsBySellType(CommandSender sender, SellType sellType) {
        int regions = 0;
        int soldregions = 0;
        String selltypeName = "";

        if(sellType == SellType.SELL) {
            selltypeName = Messages.SELL_REGION;
            for(Region region : Region.getRegionList()) {
                if(region instanceof SellRegion) {
                    regions++;
                    if(region.isSold()) {
                        soldregions++;
                    }
                }
            }
        }

        if(sellType == SellType.RENT) {
            selltypeName = Messages.RENT_REGION;
            for(Region region : Region.getRegionList()) {
                if(region instanceof RentRegion) {
                    regions++;
                    if(region.isSold()) {
                        soldregions++;
                    }
                }
            }
        }

        if(sellType == SellType.CONTRACT) {
            selltypeName = Messages.CONTRACT_REGION;
            for(Region region : Region.getRegionList()) {
                if(region instanceof ContractRegion) {
                    regions++;
                    if(region.isSold()) {
                        soldregions++;
                    }
                }
            }
        }

        String regtypesting = Messages.REGION_STATS_PATTERN;
        sender.sendMessage(regtypesting.replace("%regionkind%", selltypeName));
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
        if(percent < 65) {
            color = ChatColor.GREEN;
        } else if (percent < 85) {
            color = ChatColor.YELLOW;
        } else {
            color = ChatColor.DARK_RED;
        }

        for(int i = 0; i < 20; i++) {
            if(i < neededhashtags) {
                diagram = diagram + color + "#";
            } else {
                diagram = diagram + ChatColor.WHITE + "-";
            }
        }
        diagram = diagram + ChatColor.GOLD + "] " + percent + "% (" + used + "/" + max + ")";
        return diagram;
    }
}
