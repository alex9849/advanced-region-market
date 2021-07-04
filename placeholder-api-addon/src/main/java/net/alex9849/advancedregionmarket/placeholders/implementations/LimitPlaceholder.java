package net.alex9849.advancedregionmarket.placeholders.implementations;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.limitgroups.LimitGroupManager;
import net.alex9849.advancedregionmarket.placeholders.AbstractOfflinePlayerPlaceholder;
import net.alex9849.arm.regionkind.LimitGroupElement;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class LimitPlaceholder extends AbstractOfflinePlayerPlaceholder {

    public LimitPlaceholder(AdvancedRegionMarket plugin) {
        super(plugin, "limit_(max|used)_[^;\n_ ]+");
    }

    @Override
    public String getReplacement(OfflinePlayer offlinePlayer, String[] arguments) {
        if(!(offlinePlayer instanceof Player)) {
            return "";
        }
        Player player = (Player) offlinePlayer;
        int value;

        if("total".equals(arguments[1])) {
            if("max".equals(arguments[0])) {
                value = plugin.getLimitGroupManager().getLimitTotal(player);
            } else {
                value = LimitGroupManager.getOwnedRegions(player);
            }
        } else {
            LimitGroupElement lge = plugin.getRegionKindManager().getRegionKind(arguments[1]);
            if(lge == null) {
                lge = plugin.getRegionKindGroupManager().getRegionKindGroup(arguments[1]);
            }
            if(lge == null) {
                return "";
            }

            if("max".equals(arguments[0])) {
                value = plugin.getLimitGroupManager().getLimit(player, lge);
            } else {
                value = LimitGroupManager.getOwnedRegions(player, lge);
            }
        }

        String returnMe;
        if(value == -1) {
            returnMe = ChatColor.stripColor(Messages.UNLIMITED);
        } else {
            returnMe = String.valueOf(value);
        }
        return returnMe;
    }
}
