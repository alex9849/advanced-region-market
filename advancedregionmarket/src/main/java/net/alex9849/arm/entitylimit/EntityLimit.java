package net.alex9849.arm.entitylimit;

import org.bukkit.entity.EntityType;

public class EntityLimit {
    private EntityType entityType;
    private int softlimit;
    private int hardlimit;
    private int pricePerExtraEntity;

    public EntityLimit(EntityType entityType, int softlimit, int hardlimit, int pricePerExtraEntity) {
        if(softlimit < 0) {
            softlimit = softlimit * (-1);
        }
        if(hardlimit < 0) {
            hardlimit = hardlimit * (-1);
        }
        if(pricePerExtraEntity < 0) {
            pricePerExtraEntity = pricePerExtraEntity * (-1);
        }
        this.entityType = entityType;
        this.softlimit = softlimit;
        this.hardlimit = hardlimit;
        this.pricePerExtraEntity = pricePerExtraEntity;
    }

    public EntityType getEntityType() {
        return this.entityType;
    }

    public int getSoftLimit() {
        return this.softlimit;
    }

    public int getHardlimit() {
        return this.hardlimit;
    }

    public int getPricePerExtraEntity() {
        return this.pricePerExtraEntity;
    }
}
