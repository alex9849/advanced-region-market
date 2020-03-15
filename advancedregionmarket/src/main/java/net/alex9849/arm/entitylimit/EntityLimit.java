package net.alex9849.arm.entitylimit;

import net.alex9849.arm.Messages;
import net.alex9849.arm.regions.Region;
import org.bukkit.entity.*;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class EntityLimit {
    public static final Set<LimitableEntityType> entityTypes;
    private static HashMap<EntityType, LimitableEntityType> toLimitableEntityTypeMap = new HashMap<>();

    static {
        Set<LimitableEntityType> entityTypeSet = new LinkedHashSet<>();

        for (EntityType entityType : EntityType.values()) {
            if (entityType.getEntityClass() == null) {
                continue;
            }
            if (Animals.class.isAssignableFrom(entityType.getEntityClass())) {
                entityTypeSet.add(LimitableEntityType.getUniqueLimitableEntityType(entityType.name(), entityType.getEntityClass()));
            }
            if (Monster.class.isAssignableFrom(entityType.getEntityClass())) {
                entityTypeSet.add(LimitableEntityType.getUniqueLimitableEntityType(entityType.name(), entityType.getEntityClass()));
            }
            if (Creature.class.isAssignableFrom(entityType.getEntityClass())) {
                entityTypeSet.add(LimitableEntityType.getUniqueLimitableEntityType(entityType.name(), entityType.getEntityClass()));
            }
            if (Vehicle.class.isAssignableFrom(entityType.getEntityClass())) {
                entityTypeSet.add(LimitableEntityType.getUniqueLimitableEntityType(entityType.name(), entityType.getEntityClass()));
            }
            if (Projectile.class.isAssignableFrom(entityType.getEntityClass())) {
                entityTypeSet.add(LimitableEntityType.getUniqueLimitableEntityType(entityType.name(), entityType.getEntityClass()));
            }
            if (Hanging.class.isAssignableFrom(entityType.getEntityClass())) {
                entityTypeSet.add(LimitableEntityType.getUniqueLimitableEntityType(entityType.name(), entityType.getEntityClass()));
            }
        }
        entityTypeSet.add(LimitableEntityType.getUniqueLimitableEntityType("HANGING", Hanging.class));
        entityTypeSet.add(LimitableEntityType.getUniqueLimitableEntityType("ANIMALS", Animals.class));
        entityTypeSet.add(LimitableEntityType.getUniqueLimitableEntityType("CREATURE", Creature.class));
        entityTypeSet.add(LimitableEntityType.getUniqueLimitableEntityType("MONSTER", Monster.class));
        entityTypeSet.add(LimitableEntityType.getUniqueLimitableEntityType("VEHICLE", Vehicle.class));
        entityTypeSet.add(LimitableEntityType.getUniqueLimitableEntityType("MINECART", Minecart.class));
        entityTypeSet.add(LimitableEntityType.getUniqueLimitableEntityType("PROJECTILE", Projectile.class));
        entityTypes = entityTypeSet;
    }

    private LimitableEntityType limitableEntityType;
    private int softlimit;
    private int hardlimit;
    private int pricePerExtraEntity;
    public EntityLimit(LimitableEntityType limitableEntityType, int softlimit, int hardlimit, int pricePerExtraEntity) {
        if (softlimit < 0) {
            softlimit = softlimit * (-1);
        }
        if (hardlimit < 0) {
            hardlimit = hardlimit * (-1);
        }
        if (hardlimit < softlimit) {
            hardlimit = softlimit;
        }
        if (pricePerExtraEntity < 0) {
            pricePerExtraEntity = 0;
        }
        this.limitableEntityType = limitableEntityType;
        this.softlimit = softlimit;
        this.hardlimit = hardlimit;
        this.pricePerExtraEntity = pricePerExtraEntity;
    }

    public static LimitableEntityType toLimitableEntityType(EntityType entityType) {
        if (toLimitableEntityTypeMap.get(entityType) != null) {
            return toLimitableEntityTypeMap.get(entityType);
        }

        for (LimitableEntityType limitableEntityType : entityTypes) {
            if (limitableEntityType.getClazz().equals(entityType.getEntityClass())) {
                toLimitableEntityTypeMap.put(entityType, limitableEntityType);
                return limitableEntityType;
            }
        }
        return null;
    }

    public static LimitableEntityType getLimitableEntityType(String name) {
        for (LimitableEntityType limitableEntityType : entityTypes) {
            if (limitableEntityType.getName().equalsIgnoreCase(name)) {
                return limitableEntityType;
            }
        }
        return null;
    }

    public LimitableEntityType getLimitableEntityType() {
        return this.limitableEntityType;
    }

    public int getSoftLimit(int expansion) {
        if ((this.softlimit + expansion) >= 0) {
            return this.softlimit + expansion;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    public int getHardLimit() {
        return this.hardlimit;
    }

    public int getPricePerExtraEntity() {
        return this.pricePerExtraEntity;
    }

    public boolean isLimitReached(List<Entity> entities, LimitableEntityType limitableEntityType, Region region) {
        if (!this.limitableEntityType.isAssingnable(limitableEntityType.getClazz())) {
            return false;
        }

        int extraEntities = region.getExtraEntityAmount(this.limitableEntityType);
        int affectedEntities = this.affectedEntityAmount(entities);
        return extraEntities + this.softlimit <= affectedEntities;
    }

    public int affectedEntityAmount(List<Entity> inputlist) {
        int affected = 0;

        for (Entity entity : inputlist) {
            if (this.limitableEntityType.isAssingnable(entity.getType().getEntityClass())) {
                affected++;
            }
        }
        return affected;
    }

    public String replaceVariables(String message, List<Entity> entities, int entityExpansion) {
        String result = message;
        if (result.contains("%entitytype%"))
            result = result.replace("%entitytype%", this.getLimitableEntityType().getName());
        if (result.contains("%actualentities%"))
            result = result.replace("%actualentities%", EntityLimitGroup.filterEntitys(entities, this.getLimitableEntityType()).size() + "");
        if (result.contains("%softlimitentities%"))
            result = result.replace("%softlimitentities%", EntityLimitGroup.intToLimitString(this.getSoftLimit(entityExpansion)));
        if (result.contains("%hardlimitentities%"))
            result = result.replace("%hardlimitentities%", EntityLimitGroup.intToLimitString(this.getHardLimit()));
        if (result.contains("%priceperextraentity%"))
            result = result.replace("%priceperextraentity%", this.getPricePerExtraEntity() + "");
        if (result.contains("%currency%")) result = result.replace("%currency%", Messages.CURRENCY);
        return result;
    }

    public static class LimitableEntityType {
        private static HashMap<Class, LimitableEntityType> limitableEntityTypes = new HashMap<>();
        private final Class clazz;
        private final String name;

        private LimitableEntityType(String name, Class clazz) {
            this.clazz = clazz;
            this.name = name;
        }

        private static LimitableEntityType getUniqueLimitableEntityType(String name, Class clazz) {
            if (limitableEntityTypes.get(clazz) == null) {
                limitableEntityTypes.put(clazz, new LimitableEntityType(name, clazz));
            }
            return limitableEntityTypes.get(clazz);
        }

        public boolean isAssingnable(Class clazz) {
            if (clazz == null) {
                return false;
            }
            return this.clazz.isAssignableFrom(clazz);
        }

        public String getName() {
            return this.name;
        }

        public Class getClazz() {
            return this.clazz;
        }

        @Override
        public String toString() {
            return this.getName();
        }
    }
}
