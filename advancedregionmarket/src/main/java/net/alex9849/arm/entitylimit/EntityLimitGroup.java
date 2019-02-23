package net.alex9849.arm.entitylimit;

import net.alex9849.arm.regions.Region;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class EntityLimitGroup {
    private List<EntityLimit> entityLimits;
    private String name;
    private int totalLimit;
    private boolean needsSave = false;
    public static EntityLimitGroup DEFAULT = new EntityLimitGroup(new ArrayList<>(), Integer.MAX_VALUE,"Default");
    public static EntityLimitGroup SUBREGION = new EntityLimitGroup(new ArrayList<>(), Integer.MAX_VALUE,"Subregion");


    public EntityLimitGroup(List<EntityLimit> entityLimits, int totalLimit, String name) {
        this.entityLimits = entityLimits;
        this.name = name;
        this.totalLimit = totalLimit;
        this.needsSave = false;
    }

    public boolean isLimitReached(Region region, EntityType entityType) {

        int maxEntitiesWithThisType = this.getLimit(entityType);

        int matchingEntities = 0;
        List<Entity> regionEntities = region.getInsideEntities(false);
        for(Entity entity : regionEntities) {
            if(entity.getType() == entityType) {
                matchingEntities++;
            }
        }

        if(this.totalLimit <= regionEntities.size()) {
            return true;
        }

        if(maxEntitiesWithThisType == Integer.MAX_VALUE) {
            return false;
        }

        return maxEntitiesWithThisType <= matchingEntities;
    }

    private int getLimit(EntityType entityType) {
        for(EntityLimit entityLimit : this.entityLimits) {
            if(entityLimit.getEntityType() == entityType) {
                return entityLimit.getAmount();
            }
        }
        return Integer.MAX_VALUE;
    }

    public String getName() {
        return this.name;
    }

    public List<EntityLimit> getEntityLimits() {
        return this.entityLimits;
    }

    public int getTotalLimit() {
        return this.totalLimit;
    }

    public boolean needsSave() {
        return this.needsSave;
    }

    public void queueSave() {
        this.needsSave = true;
    }

    protected void setSaved() {
        this.needsSave = false;
    }
}
