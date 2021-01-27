package net.alex9849.adapters;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.function.mask.Masks;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.RegionGroupFlag;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionType;
import net.alex9849.inter.WGRegion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.IntStream;

public class WG7Region extends WGRegion {

    private ProtectedRegion region;
    private int Volume;

    WG7Region(ProtectedRegion region) {
        this.region = region;
        if(region instanceof ProtectedPolygonalRegion){
            Volume = polyArea(region.getPoints()) * (region.getMaximumPoint().getBlockY()-region.getMinimumPoint().getBlockY());
        } else {
            Volume = region.volume();
        }
    }

    public Vector getMaxPoint() {
        BlockVector3 wgVector = this.region.getMaximumPoint();
        return new Vector(wgVector.getBlockX(), wgVector.getBlockY(), wgVector.getBlockZ());
    }

    public Vector getMinPoint() {
        BlockVector3 wgVector = this.region.getMinimumPoint();
        return new Vector(wgVector.getBlockX(), wgVector.getBlockY(), wgVector.getBlockZ());
    }

    public String getId() {
        return this.region.getId();
    }

    public boolean hasMember(UUID uuid) {
        return this.region.getMembers().contains(uuid);
    }

    public void addMember(UUID uuid) {
        this.region.getMembers().addPlayer(uuid);
    }

    public void deleteMembers() {
        DefaultDomain defaultDomain = new DefaultDomain();
        this.region.setMembers(defaultDomain);
    }

    public void removeMember(UUID uuid) {
        this.region.getMembers().removePlayer(uuid);
    }

    public ArrayList<UUID> getMembers() {
        return new ArrayList<UUID>(this.region.getMembers().getUniqueIds());
    }

    public void setOwner(OfflinePlayer player) {
        DefaultDomain newOwner = new DefaultDomain();
        newOwner.addPlayer(player.getUniqueId());
        this.region.setOwners(newOwner);
    }

    public boolean hasOwner(UUID uuid) {
        return this.region.getOwners().contains(uuid);
    }

    public void deleteOwners() {
        DefaultDomain defaultDomain = new DefaultDomain();
        this.region.setOwners(defaultDomain);
    }

    public void removeOwner(UUID uuid) {
        this.region.getOwners().removePlayer(uuid);
    }

    public ArrayList<UUID> getOwners() {
        return new ArrayList<UUID>(this.region.getOwners().getUniqueIds());
    }

    public boolean contains(int x, int y, int z) {
        return this.region.contains(x, y, z);
    }

    protected ProtectedRegion getRegion() {
        return this.region;
    }

    @Override
    public boolean equals(WGRegion wgRegion) {
        WG7Region wg7Region = (WG7Region) wgRegion;
        return wg7Region.getRegion() == this.region;
    }

    @Override
    public WGRegion getParent() {
        if (this.getRegion().getParent() != null) {
            return new WG7Region(this.getRegion().getParent());
        } else {
            return null;
        }

    }

    @Override
    public void setParent(WGRegion wgRegion) {
        WG7Region wg7Region = (WG7Region) wgRegion;
        this.getRegion().setPriority(wg7Region.getPriority());
        try {
            this.getRegion().setParent(wg7Region.getRegion());
        } catch (ProtectedRegion.CircularInheritanceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getPriority() {
        return this.getRegion().getPriority();
    }

    @Override
    public void setPriority(int priority) {
        this.getRegion().clearParent();
        this.getRegion().setPriority(priority);
    }

    @Override
    public int getVolume() {
        return this.Volume;
    }

    @Override
    public List<Vector> getPoints() {
        List<Vector> points = new ArrayList<Vector>();
        List<BlockVector2> points2D = this.region.getPoints();
        int minY = this.region.getMinimumPoint().getBlockY();

        for (BlockVector2 vector2D : points2D) {
            points.add(new Vector(vector2D.getBlockX(), minY, vector2D.getBlockZ()));
        }
        return points;
    }

    public <T extends Flag<V>, V> void setFlag(Flag<V> flag, V value) {
        this.region.setFlag(flag, value);
    }

    public void deleteFlags(Flag... flags) {
        if (flags == null) {
            return;
        }
        for (Flag<?> flag : flags) {
            RegionGroupFlag groupFlag = flag.getRegionGroupFlag();
            if (this.region.getFlag(flag) != null) {
                this.region.setFlag(flag, null);
            }
            if (groupFlag != null && this.region.getFlag(groupFlag) != null) {
                this.region.setFlag(groupFlag, null);
            }
        }
    }

    public void deleteAllFlags() {
        Flag[] flagarr = new Flag[0];
        deleteFlags(this.region.getFlags().keySet().toArray(flagarr));
    }

    public Object getFlagSetting(Flag flag) {
        return this.region.getFlag(flag);
    }

    public boolean isCuboid() {
        return this.region.getType() == RegionType.CUBOID;
    }

    public static int polyArea(List<BlockVector2> points){
        Polygonal2DRegion region = new Polygonal2DRegion(new BukkitWorld(Bukkit.getWorld("world")),points,0,0);
        EditSession es = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(Bukkit.getWorld("world")), Integer.MAX_VALUE);
        return es.countBlocks(region, Masks.alwaysTrue()) + 1; //this function reliably miscounts by one in all my testing. I don't know why.
    }
}
