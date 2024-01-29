package net.alex9849.arm.adapters;

import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector2;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.RegionGroupFlag;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionType;
import org.bukkit.OfflinePlayer;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WG7Region implements WGRegion {

    private ProtectedRegion region;

    WG7Region(ProtectedRegion region) {
        this.region = region;
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
    public Object unwrap() {
        return region;
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

    @Override
    public boolean isPolygonal() {
        return region instanceof ProtectedPolygonalRegion;
    }

    public static double calculatePoly2DRegionVolume(List<Vector2> polygonPoints) {
        int numPoints = polygonPoints.size();
        if (numPoints < 3) {
            return 0D;
        }

        double volume = 0D;
        Vector2 baseVector = polygonPoints.get(0);

        for (int i = 1; i < numPoints - 1; i++) {
            Vector2 vector1 = polygonPoints.get(i).subtract(baseVector);
            Vector2 vector2 = polygonPoints.get(i + 1).subtract(baseVector);

            double crossProduct = vector1.getX() * vector2.getZ() - vector1.getZ() * vector2.getX();
            double triangleArea = crossProduct / 2F;
            double sideLength = vector1.length();

            volume += triangleArea * sideLength;
        }

        return Math.abs(volume);
    }

    @Override
    public int getVolume() {
        if (region.getPoints().size() > 4) {
            ArrayList<Vector2> points = new ArrayList<>();
            region.getPoints().forEach(blockVector2 -> {
                points.add(Vector2.at(blockVector2.getX(), blockVector2.getZ()));
            });

            float surface = (float) calculatePoly2DRegionVolume(points);
            //int height = Math.abs(region.getMaximumPoint().getBlockY() - region.getMinimumPoint().getBlockY());
            return (int) (surface);
        }
        return region.volume();
    }
}
