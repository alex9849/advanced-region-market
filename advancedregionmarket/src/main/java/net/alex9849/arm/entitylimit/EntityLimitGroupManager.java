package net.alex9849.arm.entitylimit;

import net.alex9849.arm.util.YamlFileManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class EntityLimitGroupManager extends YamlFileManager<EntityLimitGroup> {

    public EntityLimitGroupManager(File savepath) {
        super(savepath);
    }

    private static EntityLimitGroup parseEntityLimitGroup(ConfigurationSection section, String id) {
        List<String> entityNumbers = new ArrayList<>(section.getKeys(false));
        List<EntityLimit> entityLimits = new ArrayList<>();
        int softtotal = section.getInt("softtotal");
        int hardtotal = section.getInt("hardtotal");
        int pricePerExtraEntityTotal = section.getInt("pricePerExtraEntity");
        for (String entityNumber : entityNumbers) {
            if (!(entityNumber.equalsIgnoreCase("softtotal") || entityNumber.equalsIgnoreCase("hardtotal") ||
                    entityNumber.equalsIgnoreCase("pricePerExtraEntity"))) {
                String entityTypeName = section.getString(entityNumber + "." + "entityType");
                EntityLimit.LimitableEntityType limitableEntityType = null;
                for (EntityLimit.LimitableEntityType entityType : EntityLimit.entityTypes) {
                    if (entityType.getName().equalsIgnoreCase(entityTypeName)) {
                        limitableEntityType = entityType;
                    }
                }
                if (limitableEntityType != null) {
                    int softLimit = section.getInt(entityNumber + "." + "softLimit");
                    if (softLimit == -1) {
                        softLimit = Integer.MAX_VALUE;
                    }
                    int hardLimit = section.getInt(entityNumber + "." + "hardLimit");
                    if (hardLimit == -1) {
                        hardLimit = Integer.MAX_VALUE;
                    }
                    int pricePerExtraEntity = section.getInt(entityNumber + "." + "pricePerExtraEntity");
                    entityLimits.add(new EntityLimit(limitableEntityType, softLimit, hardLimit, pricePerExtraEntity));
                } else {
                    Bukkit.getLogger().log(Level.WARNING, "Could not find EntityType " + entityTypeName + " for EntityLimitGroup " + id + "! Ignoring it");
                }
            }
        }
        if (softtotal == -1) {
            softtotal = Integer.MAX_VALUE;
        }
        if (hardtotal == -1) {
            hardtotal = Integer.MAX_VALUE;
        }
        if (pricePerExtraEntityTotal < 0) {
            pricePerExtraEntityTotal = 0;
        }

        return new EntityLimitGroup(entityLimits, softtotal, hardtotal, pricePerExtraEntityTotal, id);
    }

    @Override
    public List<EntityLimitGroup> loadSavedObjects(YamlConfiguration yamlConfiguration) {
        ArrayList<EntityLimitGroup> entityLimitGroups = new ArrayList<>();
        yamlConfiguration.options().copyDefaults(true);
        boolean fileupdated = false;

        if (yamlConfiguration.get("DefaultEntityLimit") != null) {
            ConfigurationSection entityLimitDEFAULTSection = yamlConfiguration.getConfigurationSection("DefaultEntityLimit");
            fileupdated |= updateDefaults(entityLimitDEFAULTSection);
            EntityLimitGroup.setDEFAULT(parseEntityLimitGroup(entityLimitDEFAULTSection, "Default"));
        }

        if (yamlConfiguration.get("SubregionEntityLimit") != null) {
            ConfigurationSection entityLimitSUBREGIONSection = yamlConfiguration.getConfigurationSection("SubregionEntityLimit");
            fileupdated |= updateDefaults(entityLimitSUBREGIONSection);
            EntityLimitGroup.setSUBREGION(parseEntityLimitGroup(entityLimitSUBREGIONSection, "Subregion"));
        }

        if (yamlConfiguration.get("EntityLimits") == null) {
            return entityLimitGroups;
        }
        ConfigurationSection entityLimitsSection = yamlConfiguration.getConfigurationSection("EntityLimits");
        List<String> limitnames = new ArrayList<>(entityLimitsSection.getKeys(false));

        for (String limitname : limitnames) {
            if (entityLimitsSection.get(limitname) != null) {
                ConfigurationSection limitSection = entityLimitsSection.getConfigurationSection(limitname);
                fileupdated |= updateDefaults(limitSection);
                entityLimitGroups.add(parseEntityLimitGroup(limitSection, limitname));
            }
        }

        if (fileupdated) {
            this.saveFile();
        }
        yamlConfiguration.options().copyDefaults(false);

        return entityLimitGroups;
    }

    @Override
    public boolean staticSaveQuenued() {
        return EntityLimitGroup.DEFAULT.needsSave() || EntityLimitGroup.SUBREGION.needsSave();
    }

    @Override
    public void saveObjectToYamlObject(EntityLimitGroup entityLimitGroup, YamlConfiguration yamlConfiguration) {
        yamlConfiguration.set("EntityLimits." + entityLimitGroup.getName(), entityLimitGroup.toConfigurationSection());
    }

    @Override
    public void writeStaticSettings(YamlConfiguration yamlConfiguration) {
        yamlConfiguration.set("DefaultEntityLimit", EntityLimitGroup.DEFAULT.toConfigurationSection());
        EntityLimitGroup.DEFAULT.setSaved();
        yamlConfiguration.set("SubregionEntityLimit", EntityLimitGroup.SUBREGION.toConfigurationSection());
        EntityLimitGroup.SUBREGION.setSaved();
    }

    public List<String> tabCompleteEntityLimitGroups(String name) {
        List<String> returnme = new ArrayList<>();

        for (EntityLimitGroup entityLimitGroup : this) {
            if (entityLimitGroup.getName().startsWith(name)) {
                returnme.add(entityLimitGroup.getName());
            }
        }

        if ("default".startsWith(name)) {
            returnme.add("Default");
        }

        if ("subregion".startsWith(name)) {
            returnme.add("Subregion");
        }

        return returnme;
    }

    public EntityLimitGroup getEntityLimitGroup(String name) {
        for (EntityLimitGroup entityLimitGroup : this) {
            if (entityLimitGroup.getName().equalsIgnoreCase(name)) {
                return entityLimitGroup;
            }
        }
        if (EntityLimitGroup.DEFAULT.getName().equalsIgnoreCase(name) || "default".equalsIgnoreCase(name)) {
            return EntityLimitGroup.DEFAULT;
        }

        if (EntityLimitGroup.SUBREGION.getName().equalsIgnoreCase(name) || "subregion".equalsIgnoreCase(name)) {
            return EntityLimitGroup.SUBREGION;
        }

        return null;
    }

    private boolean updateDefaults(ConfigurationSection section) {
        boolean updated = false;
        updated |= this.addDefault(section, "softtotal", -1);
        updated |= this.addDefault(section, "hardtotal", -1);
        updated |= this.addDefault(section, "pricePerExtraEntity", 0);

        List<String> entityNumbers = new ArrayList<>(section.getKeys(false));
        for (String entityNumber : entityNumbers) {
            if (!(entityNumber.equalsIgnoreCase("softtotal") || entityNumber.equalsIgnoreCase("hardtotal") || entityNumber.equalsIgnoreCase("pricePerExtraEntity"))) {
                updated |= this.addDefault(section, entityNumber + ".entityType", EntityType.ARMOR_STAND.name());
                updated |= this.addDefault(section, entityNumber + ".softLimit", 1);
                updated |= this.addDefault(section, entityNumber + ".hardLimit", 1);
                updated |= this.addDefault(section, entityNumber + ".pricePerExtraEntity", 1);
            }
        }
        return updated;
    }
}
