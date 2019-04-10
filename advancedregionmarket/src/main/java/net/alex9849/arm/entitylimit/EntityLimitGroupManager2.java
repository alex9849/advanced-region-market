package net.alex9849.arm.entitylimit;

import net.alex9849.arm.util.YamlFileManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public class EntityLimitGroupManager2 extends YamlFileManager<EntityLimitGroup> {

    public EntityLimitGroupManager2(File savepath, InputStream resourceStream) {
        super(savepath, resourceStream);
    }

    @Override
    public List<EntityLimitGroup> loadSavedObjects(YamlConfiguration yamlConfiguration) {
        return null;
    }

    @Override
    public void saveObjectToYamlObject(EntityLimitGroup entityLimitGroup, YamlConfiguration yamlConfiguration) {
        yamlConfiguration.set("EntityLimits." + entityLimitGroup.getName(), entityLimitGroup.toConfigureationSection());
    }

    @Override
    public void removeObjectFromYamlObject(EntityLimitGroup entityLimitGroup, YamlConfiguration yamlConfiguration) {
        yamlConfiguration.set("EntityLimits." + entityLimitGroup.getName(), null);
    }

    @Override
    public void addStaticSettings(YamlConfiguration yamlConfiguration) {

    }
}
