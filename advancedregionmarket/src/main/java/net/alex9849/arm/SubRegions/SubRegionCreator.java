package net.alex9849.arm.SubRegions;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.exceptions.LogicalException;
import net.alex9849.arm.regions.Region;
import net.alex9849.inter.WGRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

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

    public SubRegionCreator(Region parentRegion, Player creator) {
        this.parentRegion = parentRegion;
        this.creator = creator;
        for(int i = 0; i < subRegionCreatorList.size(); i++) {
            if(subRegionCreatorList.get(i).getCreator().getUniqueId() == creator.getUniqueId()) {
                subRegionCreatorList.remove(i);
                i--;
            }
        }
        subRegionCreatorList.add(this);
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public void createWGRegion() throws InputException {
        if((this.pos1 == null) || (this.pos2 == null)) {
            //TODO Replace me
            throw new InputException(this.creator, "meep");
        }
        if(this.subSignCreationListener != null) {
            this.subSignCreationListener.unregister();
        }

        if(this.subRegion != null) {
            this.subRegion = null;
        }

        if(!this.getParentRegion().getRegion().hasOwner(creator.getUniqueId())) {
            throw new InputException(this.creator, "meep");
        }
        this.getCreator().sendMessage("Test");
        this.subRegion = AdvancedRegionMarket.getWorldGuardInterface().createRegion(this.parentRegion.getRegion(), Bukkit.getWorld(this.parentRegion.getRegionworld()), this.parentRegion.getRegion().getId() + "-sub" + this.parentRegion.getSubregions().size(), this.pos1, this.pos2, AdvancedRegionMarket.getWorldGuard());
        Bukkit.getServer().getPluginManager().registerEvents(new SubSignCreationListener(this.creator, this), AdvancedRegionMarket.getARM());
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
        subRegionCreatorList.remove(this);
    }

    public void saveWorldGuardRegion() throws LogicalException {
        if(this.subRegion != null) {
            AdvancedRegionMarket.getWorldGuardInterface().addToRegionManager(this.subRegion, Bukkit.getWorld(this.parentRegion.getRegionworld()), AdvancedRegionMarket.getWorldGuard());
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
        subRegionCreatorList = new ArrayList<>();
    }

    private SubSignCreationListener getSubSignCreationListener() {
        return this.subSignCreationListener;
    }

    protected WGRegion getSubRegion() {
        return this.subRegion;
    }
}
