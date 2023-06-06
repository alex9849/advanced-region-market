package net.alex9849.inter;

import com.sk89q.worldguard.protection.flags.Flag;
import org.bukkit.OfflinePlayer;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface WGRegion {
    Vector getMaxPoint();

    Vector getMinPoint();

    String getId();

    boolean hasMember(UUID uuid);

    void addMember(UUID uuid);

    void deleteMembers();

    void removeMember(UUID uuid);

    ArrayList<UUID> getMembers();

    void setOwner(OfflinePlayer player);

    boolean hasOwner(UUID uuid);

    void deleteOwners();

    void removeOwner(UUID uuid);

    ArrayList<UUID> getOwners();

    boolean contains(int x, int y, int z);

    WGRegion getParent();

    void setParent(WGRegion wgRegion);

    int getPriority();

    Object unwrap();

    void setPriority(int priority);

    List<Vector> getPoints();

    <T extends Flag<V>, V> void setFlag(Flag<V> flag, V value);

    void deleteFlags(Flag... flags);

    void deleteAllFlags();

    Object getFlagSetting(Flag flag);

    boolean isCuboid();

    boolean isPolygonal();

    int getVolume();
}
