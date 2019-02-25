package net.alex9849.arm.entitylimit;

import net.alex9849.arm.Messages;
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
        if(totalLimit == -1) {
            totalLimit = Integer.MAX_VALUE;
        }
        if(totalLimit < 0) {
            totalLimit = totalLimit * (-1);
        }
        this.entityLimits = entityLimits;
        this.name = name;
        this.totalLimit = totalLimit;
        this.needsSave = false;
    }

    public boolean isLimitReached(Region region, EntityType entityType) {

        int maxEntitiesWithThisType = this.getLimit(entityType);

        List<Entity> regionEntities = region.getFilteredInsideEntities(false, true, true, false, false, true, true);

        int matchingEntities = EntityLimitGroup.filterEntitys(regionEntities, entityType).size();

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
                return entityLimit.getSoftLimit();
            }
        }
        return Integer.MAX_VALUE;
    }

    public static List<Entity> filterEntitys(List<Entity> inputlist, EntityType entityType) {
        List<Entity> result = new ArrayList<>();

        for(Entity entity : inputlist) {
            if(entity.getType() == entityType) {
                result.add(entity);
            }
        }

        return result;
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

    public void setTotalLimit(int limit) {
        this.totalLimit = limit;
        this.queueSave();
    }

    public static String intToLimitString(int number) {
        if(number == Integer.MAX_VALUE) {
            return Messages.UNLIMITED;
        } else {
            return number + "";
        }
    }

    protected static void setDEFAULT(EntityLimitGroup entityLimitGroup) {
        EntityLimitGroup.DEFAULT = entityLimitGroup;
    }

    protected static void setSUBREGION(EntityLimitGroup entityLimitGroup) {
        EntityLimitGroup.SUBREGION = entityLimitGroup;
    }
}
