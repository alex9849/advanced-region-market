package net.alex9849.arm.regions;

import net.alex9849.arm.util.YamlFileManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class RegionManager2 extends YamlFileManager<Region> {

    public RegionManager2(File savepath) {
        super(savepath);
    }

    @Override
    public List<Region> loadSavedObjects(YamlConfiguration yamlConfiguration) {
        return null;
    }

    @Override
    public void saveObjectToYamlObject(Region region, YamlConfiguration yamlConfiguration) {
        yamlConfiguration.set("Regions." + region.getRegionworld().getName() + "." + region.getRegion().getId(), region.toConfigureationSection());
    }

    @Override
    public void writeStaticSettings(YamlConfiguration yamlConfiguration) {

    }
}
