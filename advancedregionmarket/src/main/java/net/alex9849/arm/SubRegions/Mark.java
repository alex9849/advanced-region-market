package net.alex9849.arm.SubRegions;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regions.Region;
import net.alex9849.inter.WGRegion;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Mark {
    private static List<Mark> markList = new ArrayList<>();
    private Location pos1;
    private Location pos2;
    private Region parentRegion;
    private Player creator;

    public Mark(Region parentRegion, Player creator) {
        this.parentRegion = parentRegion;
        this.creator = creator;
    }

    public void setPos1(Location pos1) throws InputException {
        if(!this.parentRegion.getRegionworld().equals(pos1.getWorld().getName())) {
            //TODO Replace me
            throw new InputException(this.creator, "meep");
        }
        if(!this.parentRegion.getRegion().contains(pos1.getBlockX(), pos1.getBlockY(), pos1.getBlockZ())) {
            //TODO Replace me
            throw new InputException(this.creator, "meep");
        }

        this.pos1 = pos1;

    }

    public void setPos2(Location pos2) throws InputException {
        if(!this.parentRegion.getRegionworld().equals(pos2.getWorld().getName())) {
            //TODO Replace me
            throw new InputException(this.creator, "meep");
        }
        if(!this.parentRegion.getRegion().contains(pos2.getBlockX(), pos2.getBlockY(), pos2.getBlockZ())) {
            //TODO Replace me
            throw new InputException(this.creator, "meep");
        }

        this.pos2 = pos2;

    }


    public WGRegion createWGRegion() throws InputException {
        if((pos1 == null) || (pos2 == null)) {
            //TODO Replace me
            throw new InputException(this.creator, "meep");
        }

        WGRegion wgRegion = AdvancedRegionMarket.getWorldGuardInterface().createRegion(this.parentRegion.getRegion(), this.pos1, this.pos2);
        Mark.markList.remove(this);
        return wgRegion;
    }

    public Player getCreator() {
        return this.creator;
    }

    public static Mark getMark(Player player) {
        for(Mark mark : markList) {
            if(mark.getCreator().getUniqueId().equals(player.getUniqueId())) {
                return mark;
            }
        }
        return null;
    }

}
