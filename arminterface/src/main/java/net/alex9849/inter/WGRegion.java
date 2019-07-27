package net.alex9849.inter;

import com.sk89q.worldguard.protection.flags.Flag;
import org.bukkit.OfflinePlayer;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class WGRegion {

    public abstract Vector getMaxPoint();

    public abstract Vector getMinPoint();

    public abstract String getId();

    public abstract boolean hasMember(UUID uuid);

    public abstract void addMember(UUID uuid);

    public abstract void deleteMembers();

    public abstract void removeMember(UUID uuid);

    public abstract ArrayList<UUID> getMembers();

    public abstract void setOwner(OfflinePlayer player);

    public abstract boolean hasOwner(UUID uuid);

    public abstract void deleteOwners();

    public abstract void removeOwner(UUID uuid);

    public abstract ArrayList<UUID> getOwners();

    public abstract boolean contains(int x, int y, int z);

    public abstract boolean equals(WGRegion wgRegion);

    public abstract WGRegion getParent();

    public abstract void setParent(WGRegion wgRegion);

    public abstract int getPriority();

    public abstract void setPriority(int priority);

    public abstract int getVolume();

    public abstract List<Vector> getPoints();

    public abstract  <T extends Flag<V>, V> void setFlag(Flag<V> flag, V value);

    public abstract void deleteFlags(Flag... flags);

    public abstract void deleteAllFlags();

    public abstract Object getFlagSetting(Flag flag);
}
