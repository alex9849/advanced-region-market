package net.alex9849.arm.entitylimit;

import net.alex9849.arm.Messages;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.util.Saveable;
import net.alex9849.arm.util.stringreplacer.StringCreator;
import net.alex9849.arm.util.stringreplacer.StringReplacer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntityLimitGroup implements Saveable {
    public static EntityLimitGroup DEFAULT = new EntityLimitGroup(new ArrayList<>(), Integer.MAX_VALUE, Integer.MAX_VALUE, 0, "Default");
    public static EntityLimitGroup SUBREGION = new EntityLimitGroup(new ArrayList<>(), Integer.MAX_VALUE, Integer.MAX_VALUE, 0, "Subregion");
    private List<EntityLimit> entityLimits;
    private String name;
    private int softTotal;
    private int hardTotal;
    private int pricePerExtraEntity;
    private boolean needsSave = false;
    private StringReplacer stringReplacer;

    {
        HashMap<String, StringCreator> variableReplacements = new HashMap<>();
        variableReplacements.put("%entitylimitgroup%", () -> {
            return this.getName();
        });
        variableReplacements.put("%priceperextraentity%", () -> {
            return this.getPricePerExtraEntity() + "";
        });
        variableReplacements.put("%currency%", () -> {
            return Messages.CURRENCY;
        });
        variableReplacements.put("%entitytype%", () -> {
            return Messages.ENTITYLIMIT_TOTAL;
        });
        variableReplacements.put("%hardlimitentities%", () -> {
            return EntityLimitGroup.intToLimitString(this.getHardLimit());
        });

        this.stringReplacer = new StringReplacer(variableReplacements, 20);
    }


    public EntityLimitGroup(List<EntityLimit> entityLimits, int softTotal, int hardTotal, int pricePerExtraEntity, String name) {
        if (softTotal == -1) {
            softTotal = Integer.MAX_VALUE;
        }
        if (softTotal < 0) {
            softTotal = softTotal * (-1);
        }
        if (hardTotal == -1) {
            hardTotal = Integer.MAX_VALUE;
        }
        if (hardTotal < 0) {
            hardTotal = hardTotal * (-1);
        }
        if (hardTotal < softTotal) {
            hardTotal = softTotal;
        }
        if (pricePerExtraEntity < 0) {
            pricePerExtraEntity = 0;
        }
        this.entityLimits = entityLimits;
        this.name = name;
        this.softTotal = softTotal;
        this.hardTotal = hardTotal;
        this.pricePerExtraEntity = pricePerExtraEntity;
        this.needsSave = false;
    }

    public static List<Entity> filterEntitys(List<Entity> inputlist, EntityLimit.LimitableEntityType limitableEntityType) {
        List<Entity> result = new ArrayList<>();

        for (Entity entity : inputlist) {
            if (limitableEntityType.isAssingnable(entity.getType().getEntityClass())) {
                result.add(entity);
            }
        }

        return result;
    }

    public static String intToLimitString(int number) {
        if (number == Integer.MAX_VALUE) {
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

    public boolean isLimitReached(Region region, EntityType entityType, int totalExpansion) {
        EntityLimit.LimitableEntityType limitableEntityType = EntityLimit.toLimitableEntityType(entityType);
        if (limitableEntityType == null) {
            return false;
        }

        List<Entity> regionEntities = region.getFilteredInsideEntities(false, true,
                true, true, true, true,
                false, false, false);

        if ((this.softTotal + totalExpansion) <= regionEntities.size()) {
            return true;
        }

        for (EntityLimit entityLimit : this.entityLimits) {
            if (entityLimit.isLimitReached(regionEntities, limitableEntityType, region)) {
                return true;
            }
        }

        return false;
    }

    public String getName() {
        return this.name;
    }

    public List<EntityLimit> getEntityLimits() {
        return this.entityLimits;
    }

    public int getSoftLimit(int expansion) {
        if ((this.softTotal + expansion) >= 0) {
            return this.softTotal + expansion;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    public int getSoftLimit(EntityLimit.LimitableEntityType limitableEntityType, int expansion) {
        for (EntityLimit entityLimit : entityLimits) {
            if (entityLimit.getLimitableEntityType() == limitableEntityType) {
                return entityLimit.getSoftLimit(expansion);
            }
        }
        return Integer.MAX_VALUE;
    }

    public int getLimit(EntityLimit.LimitableEntityType limitableEntityType, Region region) {
        int actualLimit = Integer.MAX_VALUE;
        int expansion = region.getExtraEntityAmount(limitableEntityType);

        for (EntityLimit entityLimit : entityLimits) {
            if (entityLimit.getLimitableEntityType().isAssingnable(limitableEntityType.getClazz())) {
                if (entityLimit.getSoftLimit(expansion) < actualLimit) {
                    actualLimit = entityLimit.getSoftLimit(expansion);
                }
            }
        }
        return actualLimit;
    }

    public int getHardLimit() {
        return this.hardTotal;
    }

    public void setHardLimit(int limit) {
        this.hardTotal = limit;
        this.queueSave();
    }

    public int getHardLimit(EntityLimit.LimitableEntityType limitableEntityType) {
        for (EntityLimit entityLimit : entityLimits) {
            if (entityLimit.getLimitableEntityType() == limitableEntityType) {
                return entityLimit.getHardLimit();
            }
        }
        return Integer.MAX_VALUE;
    }

    public int getPricePerExtraEntity() {
        return this.pricePerExtraEntity;
    }

    public void setPricePerExtraEntity(int limit) {
        this.pricePerExtraEntity = limit;
        this.queueSave();
    }

    public int getPricePerExtraEntity(EntityLimit.LimitableEntityType limitableEntityType) {
        for (EntityLimit entityLimit : entityLimits) {
            if (entityLimit.getLimitableEntityType() == limitableEntityType) {
                return entityLimit.getPricePerExtraEntity();
            }
        }
        return 0;
    }

    public boolean needsSave() {
        return this.needsSave;
    }

    public void queueSave() {
        this.needsSave = true;
    }

    public void setSaved() {
        this.needsSave = false;
    }

    public void setSoftLimit(int limit) {
        this.softTotal = limit;
        this.queueSave();
    }

    public String getConvertedMessage(String message, List<Entity> entities, int entityExpansion) {
        String result = message;
        result = result.replace("%softlimitentities%", EntityLimitGroup.intToLimitString(this.getSoftLimit(entityExpansion)));
        result = result.replace("%actualentities%", entities.size() + "");
        result = getConvertedMessage(result);
        return result;
    }

    public boolean containsLimit(EntityLimit.LimitableEntityType limitableEntityType) {

        for (EntityLimit entityLimit : entityLimits) {
            if (entityLimit.getLimitableEntityType() == limitableEntityType) {
                return true;
            }
        }
        return false;
    }

    public EntityLimit getEntityLimit(EntityLimit.LimitableEntityType limitableEntityType) {

        for (EntityLimit entityLimit : this.entityLimits) {
            if (entityLimit.getLimitableEntityType() == limitableEntityType) {
                return entityLimit;
            }
        }
        return null;
    }

    @Override
    public ConfigurationSection toConfigurationSection() {
        YamlConfiguration confSection = new YamlConfiguration();
        int softLimit = this.getSoftLimit(0);
        if (softLimit == Integer.MAX_VALUE) {
            softLimit = -1;
        }
        int hardLimit = this.getHardLimit();
        if (hardLimit == Integer.MAX_VALUE) {
            hardLimit = -1;
        }
        confSection.set("softtotal", softLimit);
        confSection.set("hardtotal", hardLimit);
        confSection.set("pricePerExtraEntity", this.getPricePerExtraEntity());
        for (int i = 0; i < this.getEntityLimits().size(); i++) {
            EntityLimit entityLimit = this.getEntityLimits().get(i);
            confSection.set(i + ".entityType", entityLimit.getLimitableEntityType().getName());
            int softLimitEntity = entityLimit.getSoftLimit(0);
            if (softLimitEntity == Integer.MAX_VALUE) {
                softLimitEntity = -1;
            }
            int hardLimitEntity = entityLimit.getHardLimit();
            if (hardLimitEntity == Integer.MAX_VALUE) {
                hardLimitEntity = -1;
            }
            confSection.set(i + ".softLimit", softLimitEntity);
            confSection.set(i + ".hardLimit", hardLimitEntity);
            confSection.set(i + ".pricePerExtraEntity", entityLimit.getPricePerExtraEntity());
        }
        return confSection;
    }

    public String getConvertedMessage(String message) {
        return this.stringReplacer.replace(message).toString();
    }
}
