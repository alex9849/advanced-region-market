package net.alex9849.arm.entitylimit;

import org.bukkit.entity.EntityType;

public class EntityLimit {
    private EntityType entityType;
    private int amount;

    public EntityLimit(EntityType entityType, int amount) {
        if(amount < 0) {
            amount = amount * (-1);
        }
        this.entityType = entityType;
        this.amount = amount;
    }

    public EntityType getEntityType() {
        return this.entityType;
    }

    public int getAmount() {
        return this.amount;
    }
}
