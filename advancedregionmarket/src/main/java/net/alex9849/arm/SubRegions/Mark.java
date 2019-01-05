package net.alex9849.arm.SubRegions;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regions.Region;
import net.alex9849.inter.WGRegion;
import org.bukkit.Bukkit;
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
        for(int i = 0; i < markList.size(); i++) {
            if(markList.get(i).getCreator().getUniqueId() == creator.getUniqueId()) {
                markList.remove(i);
                i--;
            }
        }
        markList.add(this);
    }

    public void setPos1(Location pos1) throws InputException {
        this.pos1 = pos1;
    }

    public void setPos2(Location pos2) throws InputException {
        this.pos2 = pos2;
    }

    public WGRegion createWGRegion() throws InputException {
        if((pos1 == null) || (pos2 == null)) {
            //TODO Replace me
            throw new InputException(this.creator, "meep");
        }

        if(!this.getParentRegion().getRegion().hasOwner(creator.getUniqueId())) {
            throw new InputException(this.creator, "meep");
        }

        WGRegion wgRegion = AdvancedRegionMarket.getWorldGuardInterface().createRegion(this.parentRegion.getRegion(), Bukkit.getWorld(this.parentRegion.getRegionworld()), this.parentRegion.getRegion().getId() + "-" + this.parentRegion.getSubregions().size(), this.pos1, this.pos2, AdvancedRegionMarket.getWorldGuard());
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

    public static void removeMark(Player player) {
        for(int i = 0; i < markList.size(); i++) {
            if(markList.get(i).getCreator().getUniqueId() == player.getUniqueId()) {
                markList.remove(i);
                i--;
            }
        }
    }

    public Region getParentRegion() {
        return parentRegion;
    }

    public static void leftClick() {

    }

    public static void rightClick() {

    }

    public static void resetMarks() {
        markList = new ArrayList<>();
    }
}
