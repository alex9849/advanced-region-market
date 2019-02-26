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
    private int softTotal;
    private int hardTotal;
    private int pricePerExtraEntity;
    private boolean needsSave = false;
    public static EntityLimitGroup DEFAULT = new EntityLimitGroup(new ArrayList<>(), Integer.MAX_VALUE, Integer.MAX_VALUE, 0, "Default");
    public static EntityLimitGroup SUBREGION = new EntityLimitGroup(new ArrayList<>(), Integer.MAX_VALUE, Integer.MAX_VALUE, 0,"Subregion");


    public EntityLimitGroup(List<EntityLimit> entityLimits, int softTotal, int hardTotal, int pricePerExtraEntity, String name) {
        if(softTotal == -1) {
            softTotal = Integer.MAX_VALUE;
        }
        if(softTotal < 0) {
            softTotal = softTotal * (-1);
        }
        if(hardTotal == -1) {
            hardTotal = Integer.MAX_VALUE;
        }
        if(hardTotal < 0) {
            hardTotal = hardTotal * (-1);
        }
        if(hardTotal < softTotal) {
            hardTotal = softTotal;
        }
        if(pricePerExtraEntity < 0) {
            pricePerExtraEntity = 0;
        }
        this.entityLimits = entityLimits;
        this.name = name;
        this.softTotal = softTotal;
        this.hardTotal = hardTotal;
        this.pricePerExtraEntity = pricePerExtraEntity;
        this.needsSave = false;
    }

    public boolean isLimitReached(Region region, EntityType entityType, int entityExpansion) {

        int maxEntitiesWithThisType = this.getLimit(entityType, entityExpansion);

        List<Entity> regionEntities = region.getFilteredInsideEntities(false, true, true, false, false, true, true);

        int matchingEntities = EntityLimitGroup.filterEntitys(regionEntities, entityType).size();

        if(this.softTotal <= regionEntities.size()) {
            return true;
        }

        if(maxEntitiesWithThisType == Integer.MAX_VALUE) {
            return false;
        }

        return maxEntitiesWithThisType <= matchingEntities;
    }

    private int getLimit(EntityType entityType, int entityExpansion) {
        for(EntityLimit entityLimit : this.entityLimits) {
            if(entityLimit.getEntityType() == entityType) {
                return entityLimit.getSoftLimit(entityExpansion);
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

    public int getSoftLimit() {
        return this.softTotal;
    }

    public int getHardLimit() {
        return this.hardTotal;
    }

    public int getPricePerExtraEntity() {
        return this.pricePerExtraEntity;
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

    public void setSoftLimit(int limit) {
        this.softTotal = limit;
        this.queueSave();
    }

    public void setHardLimit(int limit) {
        this.hardTotal = limit;
        this.queueSave();
    }

    public void setPricePerExtraEntity(int limit) {
        this.pricePerExtraEntity = limit;
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

    public String getConvertedMessage(String message, List<Entity> entities) {
        message = message.replace("%entitytype%", Messages.ENTITYLIMIT_TOTAL);
        message = message.replace("%actualentities%", entities.size() + "");
        message = message.replace("%softlimitentities%", EntityLimitGroup.intToLimitString(this.getSoftLimit()));
        message = message.replace("%hardlimitentities%", EntityLimitGroup.intToLimitString(this.getHardLimit()));
        message = message.replace("%priceperextraentity%", this.getPricePerExtraEntity() + "");
        message = message.replace("%currency%", Messages.CURRENCY);
        return message;
    }
}
