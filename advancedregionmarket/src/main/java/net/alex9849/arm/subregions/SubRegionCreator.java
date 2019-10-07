package net.alex9849.arm.subregions;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.ParticleBorder;
import net.alex9849.arm.regions.Region;
import net.alex9849.inter.WGRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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

    public SubRegionCreator(Region parentRegion, Player creator) throws InputException {
        if (!parentRegion.isAllowSubregions()) {
            throw new InputException(creator, parentRegion.getConvertedMessage(Messages.SUB_REGION_LIMIT_REACHED));
        }
        this.parentRegion = parentRegion;
        this.creator = creator;
        this.particleBorder = null;
        for (int i = 0; i < subRegionCreatorList.size(); i++) {
            if (subRegionCreatorList.get(i).getCreator().getUniqueId() == creator.getUniqueId()) {
                subRegionCreatorList.get(i).remove();
                i--;
            }
        }
        subRegionCreatorList.add(this);
    }

    public static SubRegionCreator getSubRegioncreator(Player player) {
        for (SubRegionCreator subRegionCreator : subRegionCreatorList) {
            if (subRegionCreator.getCreator().getUniqueId().equals(player.getUniqueId())) {
                return subRegionCreator;
            }
        }
        return null;
    }

    public static void removeSubRegioncreator(Player player) {
        for (int i = 0; i < subRegionCreatorList.size(); i++) {
            if (subRegionCreatorList.get(i).getCreator().getUniqueId() == player.getUniqueId()) {
                SubSignCreationListener subSignCreationListener = subRegionCreatorList.get(i).getSubSignCreationListener();
                if (subSignCreationListener != null) {
                    subSignCreationListener.unregister();
                }
                subRegionCreatorList.remove(i);
                i--;
            }
        }
    }

    public static void resetMarks() {
        for (int i = 0; i < subRegionCreatorList.size(); i++) {
            subRegionCreatorList.get(i).remove();
            i--;
        }
    }

    public void setPos1(Location pos1) throws InputException {
        checkLegalPosition(pos1);
        this.pos1 = pos1;

        if (this.pos1 != null && this.pos2 != null) {
            if (this.particleBorder != null) {
                this.particleBorder.removeBorder();
                this.particleBorder = null;
            }
            this.particleBorder = new ParticleBorder(this.pos1.toVector(), this.pos2.toVector(), this.creator, this.parentRegion.getRegionworld());
            this.particleBorder.createParticleBorder(20 * 30);
        }

    }

    public void setPos2(Location pos2) throws InputException {
        checkLegalPosition(pos2);
        this.pos2 = pos2;
        if (this.pos1 != null && this.pos2 != null) {
            if (this.particleBorder != null) {
                this.particleBorder.removeBorder();
                this.particleBorder = null;
            }
            this.particleBorder = new ParticleBorder(this.pos1.toVector(), this.pos2.toVector(), this.creator, this.parentRegion.getRegionworld());
            this.particleBorder.createParticleBorder(20 * 30);
        }

    }

    private void checkLegalPosition(Location pos) throws InputException {
        if (!this.parentRegion.getRegion().contains(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ())) {
            throw new InputException(this.creator, Messages.POSITION_CLOUD_NOT_BE_SET_MARK_OUTSIDE_REGION);
        }
        if (!this.parentRegion.getRegionworld().getName().equals(this.creator.getLocation().getWorld().getName())) {
            throw new InputException(this.creator, Messages.POSITION_CLOUD_NOT_BE_SET_MARK_OUTSIDE_REGION);
        }
        if (!AdvancedRegionMarket.getInstance().getRegionManager().containsRegion(this.getParentRegion())) {
            this.remove();
            throw new InputException(this.creator, Messages.REGION_NOT_REGISTRED);
        }
        for (Region subregion : this.parentRegion.getSubregions()) {
            if (subregion.getRegion().contains(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ())) {
                throw new InputException(this.creator, Messages.ALREADY_SUB_REGION_AT_THIS_POSITION);
            }
        }
    }

    public void createWGRegion() throws InputException {
        if ((this.pos1 == null) || (this.pos2 == null)) {
            throw new InputException(this.creator, Messages.SELECTION_INVALID);
        }
        if (this.subSignCreationListener != null) {
            this.subSignCreationListener.unregister();
        }

        if (this.particleBorder != null) {
            this.particleBorder.removeBorder();
            this.particleBorder = null;
        }

        if (this.subRegion != null) {
            this.subRegion = null;
        }

        if (!(this.getParentRegion().getRegion().hasOwner(creator.getUniqueId()) || creator.hasPermission(Permission.ADMIN_SUBREGION_CREATE_ON_UNOWNED_REGIONS))) {
            this.remove();
            throw new InputException(this.creator, Messages.PARENT_REGION_NOT_OWN);
        }
        if (!AdvancedRegionMarket.getInstance().getRegionManager().containsRegion(this.getParentRegion())) {
            this.remove();
            throw new InputException(this.creator, Messages.REGION_NOT_REGISTRED);
        }
        for (Region region : this.getParentRegion().getSubregions()) {
            if (this.checkOverlap(region)) {
                throw new InputException(this.creator, Messages.ALREADY_SUB_REGION_AT_THIS_POSITION);
            }
        }
        if (this.getParentRegion().getSubregions().size() >= this.getParentRegion().getAllowedSubregions()) {
            throw new InputException(this.getCreator(), this.getParentRegion().getConvertedMessage(Messages.SUB_REGION_LIMIT_REACHED));
        }

        int subregionID = 1;
        boolean inUse = false;
        do {
            inUse = false;
            for (Region subregions : this.parentRegion.getSubregions()) {
                if (subregions.getRegion().getId().equalsIgnoreCase(this.parentRegion.getRegion().getId() + "-sub" + subregionID)) {
                    inUse = true;
                    subregionID++;
                }
            }
        } while (inUse);
        this.subRegion = AdvancedRegionMarket.getInstance().getWorldGuardInterface().createRegion(this.parentRegion.getRegion().getId() + "-sub" + subregionID, this.pos1, this.pos2, AdvancedRegionMarket.getInstance().getWorldGuard());
        if (AdvancedRegionMarket.getInstance().getPluginSettings().isAllowParentRegionOwnersBuildOnSubregions()) {
            this.subRegion.setParent(this.parentRegion.getRegion());
        } else {
            this.subRegion.setPriority(this.parentRegion.getRegion().getPriority() + 1);
        }
        this.subSignCreationListener = new SubSignCreationListener(this.creator, this);
        Bukkit.getServer().getPluginManager().registerEvents(this.subSignCreationListener, AdvancedRegionMarket.getInstance());
        return;
    }

    public Player getCreator() {
        return this.creator;
    }

    public void remove() {
        if (this.subSignCreationListener != null) {
            this.subSignCreationListener.unregister();
        }
        if (this.particleBorder != null) {
            this.particleBorder.removeBorder();
            this.particleBorder = null;
        }
        subRegionCreatorList.remove(this);
    }

    public void saveWorldGuardRegion() {
        if (this.subRegion == null) {
            throw new NullPointerException("Subregion not created!");
        }
        AdvancedRegionMarket.getInstance().getWorldGuardInterface().addToRegionManager(this.subRegion, this.parentRegion.getRegionworld(), AdvancedRegionMarket.getInstance().getWorldGuard());
    }

    public Region getParentRegion() {
        return parentRegion;
    }

    private SubSignCreationListener getSubSignCreationListener() {
        return this.subSignCreationListener;
    }

    protected WGRegion getSubRegion() {
        return this.subRegion;
    }

    private boolean checkOverlap(Region region) {
        if (this.pos2 == null || this.pos1 == null) {
            return false;
        }
        int minX;
        int minY;
        int minZ;
        int maxX;
        int maxY;
        int maxZ;
        if (this.pos1.getBlockX() < this.pos2.getBlockX()) {
            minX = this.pos1.getBlockX();
            maxX = this.pos2.getBlockX();
        } else {
            minX = this.pos2.getBlockX();
            maxX = this.pos1.getBlockX();
        }
        if (this.pos1.getBlockY() < this.pos2.getBlockY()) {
            minY = this.pos1.getBlockY();
            maxY = this.pos2.getBlockY();
        } else {
            minY = this.pos2.getBlockY();
            maxY = this.pos1.getBlockY();
        }
        if (this.pos1.getBlockZ() < this.pos2.getBlockZ()) {
            minZ = this.pos1.getBlockZ();
            maxZ = this.pos2.getBlockZ();
        } else {
            minZ = this.pos2.getBlockZ();
            maxZ = this.pos1.getBlockZ();
        }
        int parentMinX = region.getRegion().getMinPoint().getBlockX();
        int parentMinY = region.getRegion().getMinPoint().getBlockY();
        int parentMinZ = region.getRegion().getMinPoint().getBlockZ();
        int parentMaxX = region.getRegion().getMaxPoint().getBlockX();
        int parentMaxY = region.getRegion().getMaxPoint().getBlockY();
        int parentMaxZ = region.getRegion().getMaxPoint().getBlockZ();

        if ((maxX < parentMinX) || (maxY < parentMinY) || (maxZ < parentMinZ)) {
            return false;
        }
        if ((minX > parentMaxX) || (minY > parentMaxY) || (minZ > parentMaxZ)) {
            return false;
        }
        return true;
    }
}
