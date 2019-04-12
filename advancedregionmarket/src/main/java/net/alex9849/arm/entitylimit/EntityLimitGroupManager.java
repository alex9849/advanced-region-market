package net.alex9849.arm.entitylimit;

import net.alex9849.arm.util.YamlFileManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class EntityLimitGroupManager extends YamlFileManager<EntityLimitGroup> {

    public EntityLimitGroupManager(File savepath) {
        super(savepath);
    }

    @Override
    public List<EntityLimitGroup> loadSavedObjects(YamlConfiguration yamlConfiguration) {
        ArrayList<EntityLimitGroup> entityLimitGroups = new ArrayList<>();
        yamlConfiguration.options().copyDefaults(true);

        if(yamlConfiguration.get("DefaultEntityLimit") != null) {
            ConfigurationSection entityLimitDEFAULTSection = yamlConfiguration.getConfigurationSection("DefaultEntityLimit");
            updateDefaults(entityLimitDEFAULTSection);
            EntityLimitGroup.setDEFAULT(parseEntityLimitGroup(entityLimitDEFAULTSection, "default"));
        }

        if(yamlConfiguration.get("SubregionEntityLimit") != null) {
            ConfigurationSection entityLimitSUBREGIONSection = yamlConfiguration.getConfigurationSection("SubregionEntityLimit");
            updateDefaults(entityLimitSUBREGIONSection);
            EntityLimitGroup.setSUBREGION(parseEntityLimitGroup(entityLimitSUBREGIONSection, "subregion"));
        }

        if(yamlConfiguration.get("EntityLimits") == null) {
            return  entityLimitGroups;
        }
        ConfigurationSection entityLimitsSection = yamlConfiguration.getConfigurationSection("EntityLimits");
        List<String> limitnames = new ArrayList<>(entityLimitsSection.getKeys(false));

        for(String limitname : limitnames) {
            if(entityLimitsSection.get(limitname) != null) {
                ConfigurationSection limitSection = entityLimitsSection.getConfigurationSection(limitname);
                updateDefaults(limitSection);
                entityLimitGroups.add(parseEntityLimitGroup(limitSection, limitname));
            }
        }

        this.saveFile();
        yamlConfiguration.options().copyDefaults(false);

        return entityLimitGroups;
    }

    @Override
    public void saveObjectToYamlObject(EntityLimitGroup entityLimitGroup, YamlConfiguration yamlConfiguration) {
        yamlConfiguration.set("EntityLimits." + entityLimitGroup.getName(), entityLimitGroup.toConfigureationSection());
    }

    @Override
    public void writeStaticSettings(YamlConfiguration yamlConfiguration) {
        yamlConfiguration.set("DefaultEntityLimit", EntityLimitGroup.DEFAULT.toConfigureationSection());
        yamlConfiguration.set("SubregionEntityLimit", EntityLimitGroup.SUBREGION.toConfigureationSection());
    }

    private static EntityLimitGroup parseEntityLimitGroup(ConfigurationSection section, String id) {
        List<String> entityNumbers = new ArrayList<>(section.getKeys(false));
        List<EntityLimit> entityLimits = new ArrayList<>();
        int softtotal = section.getInt("softtotal");
        int hardtotal = section.getInt("hardtotal");
        int pricePerExtraEntityTotal = section.getInt("pricePerExtraEntity");
        for(String entityNumber : entityNumbers) {
            if(!(entityNumber.equalsIgnoreCase("softtotal") || entityNumber.equalsIgnoreCase("hardtotal") ||
                    entityNumber.equalsIgnoreCase("pricePerExtraEntity"))) {
                String entityTypeName = section.getString(entityNumber + "." + "entityType");
                try {
                    EntityType entityType = EntityType.valueOf(entityTypeName);
                    if(entityType != null) {
                        int softLimit = section.getInt(entityNumber + "." + "softLimit");
                        if(softLimit == -1) {
                            softLimit = Integer.MAX_VALUE;
                        }
                        int hardLimit = section.getInt(entityNumber + "." + "hardLimit");
                        if(hardLimit == -1) {
                            hardLimit = Integer.MAX_VALUE;
                        }
                        int pricePerExtraEntity = section.getInt(entityNumber + "." + "pricePerExtraEntity");
                        entityLimits.add(new EntityLimit(entityType, softLimit, hardLimit, pricePerExtraEntity));
                    }
                } catch (IllegalArgumentException e) {
                    Bukkit.getLogger().log(Level.WARNING, "[AdvancedRegionMarket] Could not find EntityType " + entityTypeName + " for EntityLimitGroup " + id + "! Ignoring it");
                }
            }
        }
        if(softtotal == -1) {
            softtotal = Integer.MAX_VALUE;
        }
        if(hardtotal == -1) {
            hardtotal = Integer.MAX_VALUE;
        }
        if(pricePerExtraEntityTotal < 0) {
            pricePerExtraEntityTotal = 0;
        }

        return new EntityLimitGroup(entityLimits, softtotal, hardtotal, pricePerExtraEntityTotal, id);
    }

    public List<String> tabCompleteEntityLimitGroups(String name) {
        List<String> returnme = new ArrayList<>();

        for(EntityLimitGroup entityLimitGroup : this.getObjectListCopy()) {
            if(entityLimitGroup.getName().startsWith(name)) {
                returnme.add(entityLimitGroup.getName());
            }
        }

        if("default".startsWith(name)) {
            returnme.add("Default");
        }

        if("subregion".startsWith(name)) {
            returnme.add("Subregion");
        }

        return returnme;
    }

    public EntityLimitGroup getEntityLimitGroup(String name) {
        for(EntityLimitGroup entityLimitGroup : this.getObjectListCopy()) {
            if(entityLimitGroup.getName().equalsIgnoreCase(name)) {
                return entityLimitGroup;
            }
        }
        if(EntityLimitGroup.DEFAULT.getName().equalsIgnoreCase(name) || "default".equalsIgnoreCase(name)) {
            return EntityLimitGroup.DEFAULT;
        }

        if(EntityLimitGroup.SUBREGION.getName().equalsIgnoreCase(name) || "subregion".equalsIgnoreCase(name)) {
            return EntityLimitGroup.SUBREGION;
        }

        return null;
    }

    private static void updateDefaults(ConfigurationSection section) {
        section.addDefault("softtotal", -1);
        section.addDefault("hardtotal", -1);
        section.addDefault("pricePerExtraEntity", 0);

        List<String> entityNumbers = new ArrayList<>(section.getKeys(false));
        for(String entityNumber : entityNumbers) {
            if(!(entityNumber.equalsIgnoreCase("softtotal") || entityNumber.equalsIgnoreCase("hardtotal") || entityNumber.equalsIgnoreCase("pricePerExtraEntity"))) {
                section.addDefault(entityNumber + ".entityType", EntityType.ARMOR_STAND.name());
                section.addDefault(entityNumber + ".softLimit", 1);
                section.addDefault(entityNumber + ".hardLimit", 1);
                section.addDefault(entityNumber + ".pricePerExtraEntity", 1);
            }
        }
    }
}
