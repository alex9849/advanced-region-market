package net.alex9849.arm.minifeatures;

import net.alex9849.arm.AdvancedRegionMarket;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ParticleBorder {
    private Player player;
    private Integer taskID;
    private Integer cancelerID;
    private World world;
    private List<Vector> points;
    private int depth;
    private int height;

    public ParticleBorder(Vector pos1, Vector pos2, Player player, World world) {
        this.points = new ArrayList<>();
        this.points.add(pos1);
        this.points.add(pos2);
        this.player = player;
        this.world = world;
        this.height = this.getMaxVector(pos1, pos2).getBlockY();
        this.depth = this.getMinVector(pos1, pos2).getBlockY();
    }

    public ParticleBorder(List<Vector> points, int depth, int height, Player player, World world) {
        this.points = points;
        this.depth = 255;
        this.height = 0;
        this.height = height;
        this.depth = depth;
        this.player = player;
        this.world = world;
    }

    private List<Location> getParticleLocations() {
        if (this.points.size() == 2) {
            return this.getCuboidParticleLocations();
        } else if (this.points.size() > 2) {
            return this.get2DParticleLocations();
        } else {
            return new ArrayList<>();
        }
    }

    private List<Location> getCuboidParticleLocations() {
        List<Location> particleLoc = new ArrayList<>();

        final Vector minPos = this.getMinVector(this.points.get(0), this.points.get(1));
        final Vector maxPos = this.getMaxVector(this.points.get(0), this.points.get(1));

        for (int i = minPos.getBlockX(); i <= maxPos.getBlockX(); i++) {
            particleLoc.add(new Location(player.getWorld(), i, minPos.getBlockY(), minPos.getBlockZ()));
            particleLoc.add(new Location(player.getWorld(), i, minPos.getBlockY(), maxPos.getBlockZ()));
            particleLoc.add(new Location(player.getWorld(), i, maxPos.getBlockY(), minPos.getBlockZ()));
            particleLoc.add(new Location(player.getWorld(), i, maxPos.getBlockY(), maxPos.getBlockZ()));
        }
        for (int i = minPos.getBlockY(); i <= maxPos.getBlockY(); i++) {
            particleLoc.add(new Location(player.getWorld(), minPos.getBlockX(), i, minPos.getBlockZ()));
            particleLoc.add(new Location(player.getWorld(), minPos.getBlockX(), i, maxPos.getBlockZ()));
            particleLoc.add(new Location(player.getWorld(), maxPos.getBlockX(), i, minPos.getBlockZ()));
            particleLoc.add(new Location(player.getWorld(), maxPos.getBlockX(), i, maxPos.getBlockZ()));
        }
        for (int i = minPos.getBlockZ(); i <= maxPos.getBlockZ(); i++) {
            particleLoc.add(new Location(player.getWorld(), minPos.getBlockX(), minPos.getBlockY(), i));
            particleLoc.add(new Location(player.getWorld(), minPos.getBlockX(), maxPos.getBlockY(), i));
            particleLoc.add(new Location(player.getWorld(), maxPos.getBlockX(), minPos.getBlockY(), i));
            particleLoc.add(new Location(player.getWorld(), maxPos.getBlockX(), maxPos.getBlockY(), i));
        }

        return particleLoc;
    }

    private List<Location> get2DParticleLocations() {
        List<Location> particleLoc = new ArrayList<>();

        for (int i = 0; i < this.points.size(); i++) {
            Vector point1bottem = new Vector(this.points.get(i).getBlockX(), this.depth, this.points.get(i).getBlockZ());
            Vector point2bottem = new Vector(this.points.get(Math.floorMod(i + 1, this.points.size())).getBlockX(), this.depth, this.points.get(Math.floorMod(i + 1, this.points.size())).getBlockZ());
            Vector point1top = new Vector(this.points.get(i).getBlockX(), this.height, this.points.get(i).getBlockZ());
            Vector point2top = new Vector(this.points.get(Math.floorMod(i + 1, this.points.size())).getBlockX(), this.height, this.points.get(Math.floorMod(i + 1, this.points.size())).getBlockZ());
            particleLoc.addAll(this.plotLine(point1bottem, point2bottem, this.world));
            particleLoc.addAll(this.plotLine(point1top, point2top, this.world));
            int pointX = this.points.get(i).getBlockX();
            int pointZ = this.points.get(i).getBlockZ();
            Vector pointMin = new Vector(pointX, this.depth, pointZ);
            Vector pointMax = new Vector(pointX, this.height, pointZ);
            particleLoc.addAll(this.plotLine(pointMin, pointMax, this.world));
        }
        return particleLoc;
    }

    private List<Location> plotLine(Vector p1, Vector p2, World world) {
        List<Location> line = new ArrayList<>();
        Vector vectorP1 = p1.clone();
        Vector vectorP2 = p2.clone();
        Vector vectorP1Copy = p1.clone();
        Vector vector = vectorP1Copy.subtract(vectorP2).normalize();
        int points = (int) vectorP1.distance(vectorP2);

        for (int i = 0; i < points; i++) {
            Vector multiplier = vector.clone();
            Vector vectorP2Copy = vectorP2.clone();
            Vector newPoint = vectorP2Copy.add(multiplier.multiply(i));
            line.add(new Location(world, newPoint.getBlockX(), newPoint.getBlockY(), newPoint.getBlockZ()));
        }

        return line;
    }

    private Vector getMaxVector(Vector p1, Vector p2) {
        int x = 0;
        int y = 0;
        int z = 0;

        if (p1.getBlockX() < p2.getBlockX()) {
            x = p2.getBlockX();
        } else {
            x = p1.getBlockX();
        }
        if (p1.getBlockY() < p2.getBlockY()) {
            y = p2.getBlockY();
        } else {
            y = p1.getBlockY();
        }
        if (p1.getBlockZ() < p2.getBlockZ()) {
            z = p2.getBlockZ();
        } else {
            z = p1.getBlockZ();
        }
        return new Vector(x, y, z);
    }

    private Vector getMinVector(Vector p1, Vector p2) {
        int x = 0;
        int y = 0;
        int z = 0;

        if (p1.getBlockX() > p2.getBlockX()) {
            x = p2.getBlockX();
        } else {
            x = p1.getBlockX();
        }
        if (p1.getBlockY() > p2.getBlockY()) {
            y = p2.getBlockY();
        } else {
            y = p1.getBlockY();
        }
        if (p1.getBlockZ() > p2.getBlockZ()) {
            z = p2.getBlockZ();
        } else {
            z = p1.getBlockZ();
        }
        return new Vector(x, y, z);
    }

    public void createParticleBorder(int ticks) {

        final Player player = this.player;
        final List<Location> particleSpawnPoints = this.getParticleLocations();

        final World finalWorld = this.world;

        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(AdvancedRegionMarket.getInstance(), new Runnable() {

            @Override
            public void run() {
                if (finalWorld == null) {
                    return;
                }
                if (player.getWorld().getName().equals(finalWorld.getName())) {
                    for (Location location : particleSpawnPoints) {
                        player.spawnParticle(Particle.SPELL_WITCH, location.getX() + 0.5, location.getY() + 0.5, location.getBlockZ() + 0.5, 6, 0, 0, 0);
                    }
                }
            }
        }, 0, 20);
        this.cancelerID = Bukkit.getScheduler().scheduleSyncDelayedTask(AdvancedRegionMarket.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (taskID != null) {
                    Bukkit.getScheduler().cancelTask(taskID);
                }
                cancelerID = null;
                taskID = null;
            }
        }, ticks);
    }

    public void removeBorder() {
        if ((this.taskID == null) || (this.cancelerID == null)) {
            return;
        }
        Bukkit.getScheduler().cancelTask(cancelerID);
        Bukkit.getScheduler().cancelTask(taskID);
        cancelerID = null;
        taskID = null;
    }
}
