package net.alex9849.arm.inactivityexpiration;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Permission;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import java.util.HashSet;
import java.util.Set;

public class InactivityExpirationGroup {
    public static InactivityExpirationGroup DEFAULT = new InactivityExpirationGroup("Default", 0, 0);
    private static Set<InactivityExpirationGroup> inactivityExpirationGroupSet = new HashSet<>();
    private String name;
    private long resetAfterMs;
    private long takeOverAfterMs;

    public InactivityExpirationGroup(String name, long resetAfterMs, long takeOverAfterMs) {
        this.name = name;
        this.resetAfterMs = resetAfterMs;
        this.takeOverAfterMs = takeOverAfterMs;
    }

    public String getName() {
        return this.name;
    }

    public long getResetAfterMs() {
        return resetAfterMs;
    }

    public long getTakeOverAfterMs() {
        return takeOverAfterMs;
    }

    public static void reset() {
        inactivityExpirationGroupSet = new HashSet<>();
    }

    public void add(InactivityExpirationGroup inactivityExpirationGroup) {
        inactivityExpirationGroup.add(inactivityExpirationGroup);
    }

    public static long getResetAfterMsTime(OfflinePlayer oPlayer, World world) {
        if(AdvancedRegionMarket.getVaultPerms().isEnabled()) {
            return 0;
        }

        long resetAfterMs = DEFAULT.getResetAfterMs();

        for(InactivityExpirationGroup ieGroup : inactivityExpirationGroupSet) {
            if(AdvancedRegionMarket.getVaultPerms().playerHas(world.getName(), oPlayer, Permission.ARM_INACTIVITY_EXPIRATION + ieGroup.getName())) {
                if(resetAfterMs < ieGroup.getResetAfterMs()) {
                    resetAfterMs = ieGroup.getResetAfterMs();
                }
            }
        }
        return resetAfterMs;
    }
}
