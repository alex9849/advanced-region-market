package net.alex9849.arm.entitylimit;

import org.bukkit.entity.EntityType;

public class EntityLimit {
    private EntityType entityType;
    private int softlimit;

    public EntityLimit(EntityType entityType, int softlimit) {
        if(softlimit < 0) {
            softlimit = softlimit * (-1);
        }
        this.entityType = entityType;
        this.softlimit = softlimit;
    }

    public EntityType getEntityType() {
        return this.entityType;
    }

    public int getSoftLimit() {
        return this.softlimit;
    }
}
