package net.alex9849.inter;

import org.bukkit.OfflinePlayer;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.UUID;

public abstract class WGRegion {

    public abstract int getMaxX();

    public abstract int getMaxY();

    public abstract int getMaxZ();

    public abstract int getMinX();

    public abstract int getMinY();

    public abstract int getMinZ();

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
}
