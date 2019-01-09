package net.alex9849.arm.SubRegions;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.exceptions.LogicalException;
import net.alex9849.arm.minifeatures.ParticleBorder;
import net.alex9849.arm.regions.Region;
import net.alex9849.inter.WGRegion;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class SubRegionCreator {
    private static List<SubRegionCreator> subRegionCreatorList = new ArrayList<>();
    private Location pos1;
    private Location pos2;
    private Region parentRegion;
    private Player creator;
    private WGRegion subRegion;
    private SubSignCreationListener subSignCreationListener;
    private ParticleBorder particleBorder;

    public SubRegionCreator(Region parentRegion, Player creator) {
        this.parentRegion = parentRegion;
        this.creator = creator;
        this.particleBorder = null;
        for(int i = 0; i < subRegionCreatorList.size(); i++) {
            if(subRegionCreatorList.get(i).getCreator().getUniqueId() == creator.getUniqueId()) {
                subRegionCreatorList.get(i).remove();
                i--;
            }
        }
        subRegionCreatorList.add(this);
    }

    public void setPos1(Location pos1) throws InputException {
        if(!this.parentRegion.getRegion().contains(pos1.getBlockX(), pos1.getBlockY(), pos1.getBlockZ())) {
            //TODO change message
            throw new InputException(this.creator, "Position could not be set (outsinde of region)");
        }
        if(!this.parentRegion.getRegionworld().getName().equals(this.creator.getLocation().getWorld().getName())) {
            throw new InputException(this.creator, "Position could not be set (outsinde of region)");
        }
        for(Region subregion : this.parentRegion.getSubregions()) {
            if(subregion.getRegion().contains(pos1.getBlockX(), pos1.getBlockY(), pos1.getBlockZ())) {
                throw new InputException(this.creator, "Position could not be set (there already is a subregion at this position)");
            }
        }
        this.pos1 = pos1;

        if(this.pos1 != null && this.pos2 != null) {
            if(this.particleBorder != null) {
                this.particleBorder.removeBorder();
                this.particleBorder = null;
            }
            this.particleBorder = new ParticleBorder(this.pos1, this.pos2, this.creator);
            this.particleBorder.createParticleBorder(20 * 30);
        }

    }

    public void setPos2(Location pos2) throws InputException {
        if(!this.parentRegion.getRegion().contains(pos2.getBlockX(), pos2.getBlockY(), pos2.getBlockZ())) {
            throw new InputException(this.creator, "Position could not be set (outsinde of region)");
        }
        if(!this.parentRegion.getRegionworld().getName().equals(this.creator.getLocation().getWorld().getName())) {
            throw new InputException(this.creator, "Position could not be set (outsinde of region)");
        }
        for(Region subregion : this.parentRegion.getSubregions()) {
            if(subregion.getRegion().contains(pos2.getBlockX(), pos2.getBlockY(), pos2.getBlockZ())) {
                throw new InputException(this.creator, "Position could not be set (there already is a subregion at this position)");
            }
        }
        this.pos2 = pos2;
        if(this.pos1 != null && this.pos2 != null) {
            if(this.particleBorder != null) {
                this.particleBorder.removeBorder();
                this.particleBorder = null;
            }
            this.particleBorder = new ParticleBorder(this.pos1, this.pos2, this.creator);
            this.particleBorder.createParticleBorder(20 * 30);
        }

    }

    public void createWGRegion() throws InputException {
        if((this.pos1 == null) || (this.pos2 == null)) {
            //TODO Replace me
            throw new InputException(this.creator, "meep");
        }
        if(this.subSignCreationListener != null) {
            this.subSignCreationListener.unregister();
        }

        if(this.particleBorder != null) {
            this.particleBorder.removeBorder();
            this.particleBorder = null;
        }

        if(this.subRegion != null) {
            this.subRegion = null;
        }

        if(!this.getParentRegion().getRegion().hasOwner(creator.getUniqueId())) {
            throw new InputException(this.creator, "meep");
        }
        int subregionID = 0;
        boolean inUse = false;
        do {
            inUse = false;
            for(Region subregions : this.parentRegion.getSubregions()) {
                if(subregions.getRegion().getId().equalsIgnoreCase(this.parentRegion.getRegion().getId() + "-sub" + subregionID)) {
                    inUse = true;
                    subregionID++;
                }
            }
        } while(inUse);
        this.subRegion = AdvancedRegionMarket.getWorldGuardInterface().createRegion(this.parentRegion.getRegion(), this.parentRegion.getRegionworld(), this.parentRegion.getRegion().getId() + "-sub" + subregionID, this.pos1, this.pos2, AdvancedRegionMarket.getWorldGuard());
        this.subSignCreationListener = new SubSignCreationListener(this.creator, this);
        Bukkit.getServer().getPluginManager().registerEvents(this.subSignCreationListener, AdvancedRegionMarket.getARM());
        return;
    }

    public Player getCreator() {
        return this.creator;
    }

    public static SubRegionCreator getSubRegioncreator(Player player) {
        for(SubRegionCreator subRegionCreator: subRegionCreatorList) {
            if(subRegionCreator.getCreator().getUniqueId().equals(player.getUniqueId())) {
                return subRegionCreator;
            }
        }
        return null;
    }

    public void remove() {
        if(this.subSignCreationListener != null) {
            this.subSignCreationListener.unregister();
        }
        if(this.particleBorder != null) {
            this.particleBorder.removeBorder();
            this.particleBorder = null;
        }
        subRegionCreatorList.remove(this);
    }

    public void saveWorldGuardRegion() throws LogicalException {
        if(this.subRegion != null) {
            AdvancedRegionMarket.getWorldGuardInterface().addToRegionManager(this.subRegion, this.parentRegion.getRegionworld(), AdvancedRegionMarket.getWorldGuard());
        } else {
            throw new LogicalException("Could not save WorldGaurd Region! Subregion = null");
        }
    }

    public static void removeSubRegioncreator(Player player) {
        for(int i = 0; i < subRegionCreatorList.size(); i++) {
            if(subRegionCreatorList.get(i).getCreator().getUniqueId() == player.getUniqueId()) {
                SubSignCreationListener subSignCreationListener = subRegionCreatorList.get(i).getSubSignCreationListener();
                if(subSignCreationListener != null) {
                    subSignCreationListener.unregister();
                }
                subRegionCreatorList.remove(i);
                i--;
            }
        }
    }

    public Region getParentRegion() {
        return parentRegion;
    }

    public static void resetMarks() {
        for(int i = 0; i < subRegionCreatorList.size(); i++) {
            subRegionCreatorList.get(i).remove();
            i--;
        }
    }

    private SubSignCreationListener getSubSignCreationListener() {
        return this.subSignCreationListener;
    }

    protected WGRegion getSubRegion() {
        return this.subRegion;
    }

    /*

    private int createParticleBorders() {
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


        final Player player = this.creator;
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
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(AdvancedRegionMarket.getARM(), new Runnable() {

            @Override
            public void run() {
                for(Location location : particleSpawnPoints) {
                    player.spawnParticle(Particle.SPELL_WITCH, location.getX() + 0.5, location.getY() + 0.5, location.getBlockZ() + 0.5, 6,0, 0, 0);
                }
            }
        }, 0, 20);
    }

    private void removeParticleBorders() {
        if(this.particleBorderTaskID != null) {
            Bukkit.getScheduler().cancelTask(this.particleBorderTaskID);
        }
    }
    */
}
