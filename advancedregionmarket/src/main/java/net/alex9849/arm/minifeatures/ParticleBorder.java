package net.alex9849.arm.minifeatures;

import net.alex9849.arm.AdvancedRegionMarket;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ParticleBorder {
    private Location pos1;
    private Location pos2;
    private Player player;
    private Integer taskID;
    private Integer cancelerID;

    public ParticleBorder(Location pos1, Location pos2, Player player) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.player = player;
    }

    public void createParticleBorder(int ticks) {

        int minX;
        int minY;
        int minZ;
        int maxX;
        int maxY;
        int maxZ;

        if(this.pos1.getBlockX() < this.pos2.getBlockX()) {
            minX = this.pos1.getBlockX();
            maxX = this.pos2.getBlockX();
        } else {
            minX = this.pos2.getBlockX();
            maxX = this.pos1.getBlockX();
        }

        if(this.pos1.getBlockY() < this.pos2.getBlockY()) {
            minY = this.pos1.getBlockY();
            maxY = this.pos2.getBlockY();
        } else {
            minY = this.pos2.getBlockY();
            maxY = this.pos1.getBlockY();
        }

        if(this.pos1.getBlockZ() < this.pos2.getBlockZ()) {
            minZ = this.pos1.getBlockZ();
            maxZ = this.pos2.getBlockZ();
        } else {
            minZ = this.pos2.getBlockZ();
            maxZ = this.pos1.getBlockZ();
        }


        final Player player = this.player;
        final Location minPos = new Location(player.getWorld(), minX, minY, minZ);
        final Location maxPos = new Location(player.getWorld(), maxX, maxY, maxZ);

        List<Location> particleSpawnPoints = new ArrayList<>();
        for(int i = minPos.getBlockX(); i <= maxPos.getBlockX(); i++) {
            particleSpawnPoints.add(new Location(player.getWorld(), i, minPos.getBlockY(), minPos.getBlockZ()));
            particleSpawnPoints.add(new Location(player.getWorld(), i, minPos.getBlockY(), maxPos.getBlockZ()));
            particleSpawnPoints.add(new Location(player.getWorld(), i, maxPos.getBlockY(), minPos.getBlockZ()));
            particleSpawnPoints.add(new Location(player.getWorld(), i, maxPos.getBlockY(), maxPos.getBlockZ()));
        }
        for(int i = minPos.getBlockY(); i <= maxPos.getBlockY(); i++) {
            particleSpawnPoints.add(new Location(player.getWorld(), minPos.getBlockX(), i, minPos.getBlockZ()));
            particleSpawnPoints.add(new Location(player.getWorld(), minPos.getBlockX(), i, maxPos.getBlockZ()));
            particleSpawnPoints.add(new Location(player.getWorld(), maxPos.getBlockX(), i, minPos.getBlockZ()));
            particleSpawnPoints.add(new Location(player.getWorld(), maxPos.getBlockX(), i, maxPos.getBlockZ()));
        }
        for(int i = minPos.getBlockZ(); i <= maxPos.getBlockZ(); i++) {
            particleSpawnPoints.add(new Location(player.getWorld(), minPos.getBlockX(), minPos.getBlockY(), i));
            particleSpawnPoints.add(new Location(player.getWorld(), minPos.getBlockX(), maxPos.getBlockY(), i));
            particleSpawnPoints.add(new Location(player.getWorld(), maxPos.getBlockX(), minPos.getBlockY(), i));
            particleSpawnPoints.add(new Location(player.getWorld(), maxPos.getBlockX(), maxPos.getBlockY(), i));
        }
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(AdvancedRegionMarket.getARM(), new Runnable() {

            @Override
            public void run() {
                if(player.getWorld().getName().equals(pos1.getWorld().getName())) {
                    for(Location location : particleSpawnPoints) {
                        player.spawnParticle(Particle.SPELL_WITCH, location.getX() + 0.5, location.getY() + 0.5, location.getBlockZ() + 0.5, 6,0, 0, 0);
                    }
                }
            }
        }, 0, 20);
        this.cancelerID = Bukkit.getScheduler().scheduleSyncDelayedTask(AdvancedRegionMarket.getARM(), new Runnable() {
            @Override
            public void run() {
                if(taskID != null) {
                    Bukkit.getScheduler().cancelTask(taskID);
                }
                cancelerID = null;
                taskID = null;
            }
        }, ticks);
    }

    public void removeBorder() {
        if((this.taskID == null) || (this.cancelerID == null)) {
            return;
        }
        Bukkit.getScheduler().cancelTask(cancelerID);
        Bukkit.getScheduler().cancelTask(taskID);
        cancelerID = null;
        taskID = null;
    }
}
