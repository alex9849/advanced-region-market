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

    public EntityLimitGroupManager(File savepath, InputStream resourceStream) {
        super(savepath, resourceStream);
    }

    @Override
    public List<EntityLimitGroup> loadSavedObjects(YamlConfiguration yamlConfiguration) {
        ArrayList<EntityLimitGroup> entityLimitGroups = new ArrayList<>();

        ConfigurationSection entityLimitDEFAULTSection = yamlConfiguration.getConfigurationSection("DefaultEntityLimit");
        EntityLimitGroup.setDEFAULT(parseEntityLimitGroup(entityLimitDEFAULTSection, "default"));

        ConfigurationSection entityLimitSUBREGIONSection = yamlConfiguration.getConfigurationSection("SubregionEntityLimit");
        EntityLimitGroup.setSUBREGION(parseEntityLimitGroup(entityLimitSUBREGIONSection, "subregion"));

        if(yamlConfiguration.get("EntityLimits") == null) {
            return  entityLimitGroups;
        }
        ConfigurationSection entityLimitsSection = yamlConfiguration.getConfigurationSection("EntityLimits");
        List<String> limitnames = new ArrayList<>(entityLimitsSection.getKeys(false));

        for(String limitname : limitnames) {
            if(entityLimitsSection.get(limitname) != null) {
                ConfigurationSection limitSection = entityLimitsSection.getConfigurationSection(limitname);
                entityLimitGroups.add(parseEntityLimitGroup(limitSection, limitname));
            }
        }
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
                        int hardLimit = section.getInt(entityNumber + "." + "hardLimit");
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

    //Todo maybe needed later
    /*
    private static void updateConfig() {
        if(entityLimitConf.get("EntityLimits") == null) {
            return;
        }
        List<String> groups = new ArrayList<>(entityLimitConf.getConfigurationSection("EntityLimits").getKeys(false));
        ConfigurationSection entityLimitsSection = entityLimitConf.getConfigurationSection("EntityLimits");

        for(String limitName : groups) {
            entityLimitConf.addDefault("EntityLimits." + limitName + ".softtotal", -1);
            entityLimitConf.addDefault("EntityLimits." + limitName + ".hardtotal", -1);
            entityLimitConf.addDefault("EntityLimits." + limitName + ".pricePerExtraEntity", 0);
            if(entityLimitsSection.get(limitName) != null) {
                List<String> limts = new ArrayList<>(entityLimitsSection.getConfigurationSection(limitName).getKeys(false));
                for(String limit : limts) {
                    entityLimitConf.addDefault("EntityLimits." + limitName + "." + limit + ".entityType", EntityType.ARMOR_STAND.name());
                    entityLimitConf.addDefault("EntityLimits." + limitName + "." + limit + ".softLimit", 1);
                    entityLimitConf.addDefault("EntityLimits." + limitName + "." + limit + ".hardLimit", 1);
                    entityLimitConf.addDefault("EntityLimits." + limitName + "." + limit + ".pricePerExtraEntity", 1);
                }
            }

        }
        entityLimitConf.addDefault("DefaultEntityLimit.softtotal", -1);
        entityLimitConf.addDefault("DefaultEntityLimit.hardtotal", -1);
        entityLimitConf.addDefault("SubregionEntityLimit.softtotal", -1);
        entityLimitConf.addDefault("SubregionEntityLimit.hardtotal", -1);

        entityLimitConf.options().copyDefaults(true);
        saveEntityLimitsConf();
        entityLimitConf.options().copyDefaults(false);
    }
    */



}
