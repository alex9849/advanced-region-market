package net.alex9849.arm.inactivityexpiration;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Permission;
import net.alex9849.arm.regions.CountdownRegion;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class InactivityExpirationGroup {
    public static final InactivityExpirationGroup UNLIMITED = new InactivityExpirationGroup("Unlimited", -1, -1);
    public static final InactivityExpirationGroup NOT_CALCULATED = new InactivityExpirationGroup("Not calculated", -1, -1);
    public static InactivityExpirationGroup DEFAULT = new InactivityExpirationGroup("Default", -1, -1);
    private static Set<InactivityExpirationGroup> inactivityExpirationGroupSet = new HashSet<>();
    private String name;
    private long resetAfterMs;
    private long takeOverAfterMs;

    public InactivityExpirationGroup(String name, long resetAfterMs, long takeOverAfterMs) {
        this.name = name;
        this.resetAfterMs = resetAfterMs;
        this.takeOverAfterMs = takeOverAfterMs;
    }

    public static void reset() {
        inactivityExpirationGroupSet = new HashSet<>();
    }

    public static void add(InactivityExpirationGroup inactivityExpirationGroup) {
        inactivityExpirationGroupSet.add(inactivityExpirationGroup);
    }

    public static InactivityExpirationGroup getBestResetAfterMs(OfflinePlayer oPlayer, World world) {
        //Workaround to prevent Luckperms from blocking permissionchecks for offlineplayers
        //use PlayerInactivityGroupMapper for a more performant group check
        GetBestGroupThread getBestGroupThread = new GetBestGroupThread(oPlayer, world) {
            @Override
            public void run() {
                if (!AdvancedRegionMarket.getInstance().getVaultPerms().isEnabled()) {
                    setResult(UNLIMITED);
                    return;
                }
                if (oPlayer == null || oPlayer.getName() == null) {
                    setResult(DEFAULT);
                    return;
                }
                if (AdvancedRegionMarket.getInstance().getVaultPerms().playerHas(world.getName(), oPlayer, Permission.ARM_INACTIVITY_EXPIRATION + "unlimited")) {
                    setResult(UNLIMITED);
                    return;
                }
                InactivityExpirationGroup selectedGroup = DEFAULT;

                for (InactivityExpirationGroup ieGroup : inactivityExpirationGroupSet) {
                    if (AdvancedRegionMarket.getInstance().getVaultPerms().playerHas(world.getName(), oPlayer, Permission.ARM_INACTIVITY_EXPIRATION + ieGroup.getName())) {
                        if (selectedGroup == DEFAULT || ((!selectedGroup.isResetDisabled() && (selectedGroup.getResetAfterMs() < ieGroup.getResetAfterMs())) || ieGroup.isResetDisabled())) {
                            selectedGroup = ieGroup;
                        }
                    }
                }
                setResult(selectedGroup);
                return;
            }
        };
        getBestGroupThread.start();
        try {
            getBestGroupThread.join();
            return getBestGroupThread.getResult();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return UNLIMITED;
        }
    }

    public static InactivityExpirationGroup getBestTakeOverAfterMs(OfflinePlayer oPlayer, World world) {
        //Workaround to prevent Luckperms from blocking permissionchecks for offlineplayers
        //use PlayerInactivityGroupMapper for a more performant group check
        GetBestGroupThread getBestGroupThread = new GetBestGroupThread(oPlayer, world) {
            @Override
            public void run() {
                if (!AdvancedRegionMarket.getInstance().getVaultPerms().isEnabled()) {
                    setResult(UNLIMITED);
                    return;
                }
                if (oPlayer == null || oPlayer.getName() == null) {
                    setResult(DEFAULT);
                    return;
                }
                if (AdvancedRegionMarket.getInstance().getVaultPerms().playerHas(world.getName(), oPlayer, Permission.ARM_INACTIVITY_EXPIRATION + "unlimited")) {
                    setResult(UNLIMITED);
                    return;
                }
                InactivityExpirationGroup selectedGroup = DEFAULT;

                for (InactivityExpirationGroup ieGroup : inactivityExpirationGroupSet) {
                    if (AdvancedRegionMarket.getInstance().getVaultPerms().playerHas(world.getName(), oPlayer, Permission.ARM_INACTIVITY_EXPIRATION + ieGroup.getName())) {
                        if (selectedGroup == DEFAULT || ((!selectedGroup.isTakeOverDisabled() && (selectedGroup.getTakeOverAfterMs() < ieGroup.getTakeOverAfterMs())) || ieGroup.isTakeOverDisabled())) {
                            selectedGroup = ieGroup;
                        }
                    }
                }
                setResult(selectedGroup);
                return;
            }
        };
        getBestGroupThread.start();
        try {
            getBestGroupThread.join();
            return getBestGroupThread.getResult();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return UNLIMITED;
        }
    }

    public static InactivityExpirationGroup parse(ConfigurationSection configurationSection, String name) {
        long resetAfterMs;
        if (configurationSection.getString("resetAfter").equalsIgnoreCase("none")) {
            resetAfterMs = -1;
        } else {
            try {
                resetAfterMs = CountdownRegion.stringToTime(configurationSection.getString("resetAfter"));
            } catch (IllegalArgumentException e) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Could parse resetAfter for " +
                        "InactivityExpirationGroup " + name + "! Please check! ResetAfter has been set to unlimited to prevent unwanted region-resets");
                resetAfterMs = -1;
            }

        }
        long takeOverAfterMs;
        if (configurationSection.getString("takeOverAfter").equalsIgnoreCase("none")) {
            takeOverAfterMs = -1;
        } else {
            try {
                takeOverAfterMs = CountdownRegion.stringToTime(configurationSection.getString("takeOverAfter"));
            } catch (IllegalArgumentException e) {
                AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, "Could parse resetAfter for " +
                        "InactivityExpirationGroup " + name + "! Please check! ResetAfter has been set to unlimited to prevent unwanted region-resets");
                takeOverAfterMs = -1;
            }

        }

        return new InactivityExpirationGroup(name, resetAfterMs, takeOverAfterMs);
    }

    public String getName() {
        return this.name;
    }

    public long getResetAfterMs() {
        return resetAfterMs;
    }

    public boolean isResetDisabled() {
        return this.resetAfterMs <= 0;
    }

    public boolean isNotCalculated() {
        return this == NOT_CALCULATED;
    }

    public boolean isTakeOverDisabled() {
        return this.takeOverAfterMs <= 0;
    }

    public long getTakeOverAfterMs() {
        return takeOverAfterMs;
    }

    //Workaround to prevent Luckperms from blocking permissionchecks for offlineplayers
    private static abstract class GetBestGroupThread extends Thread {
        InactivityExpirationGroup result = null;
        private OfflinePlayer oPlayer;
        private World world;

        GetBestGroupThread(OfflinePlayer offlinePlayer, World world) {
            this.oPlayer = offlinePlayer;
            this.world = world;
        }

        OfflinePlayer getOfflinePlayer() {
            return this.oPlayer;
        }

        World getWorld() {
            return this.world;
        }

        public abstract void run();

        public InactivityExpirationGroup getResult() {
            return result;
        }

        void setResult(InactivityExpirationGroup ieGroup) {
            this.result = ieGroup;
        }
    }
}
