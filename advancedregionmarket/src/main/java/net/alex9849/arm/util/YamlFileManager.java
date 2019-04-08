package net.alex9849.arm.util;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class YamlFileManager<ManagedObject extends Saveable> {
    private List<ManagedObject> objectList;
    private YamlConfiguration yamlConfiguration;
    private File savepath;


    public YamlFileManager(File savepath, InputStream resourceStream) {
        this.objectList = new ArrayList<>();
        this.savepath = savepath;
        if(!this.savepath.exists()) {
            try {
                byte[] buffer = new byte[resourceStream.available()];
                resourceStream.read(buffer);
                OutputStream output = new FileOutputStream(savepath);
                output.write(buffer);
                output.close();
                resourceStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(this.savepath);
        this.objectList.addAll(loadSavedObjects(this.yamlConfiguration));
    }

    public void add(ManagedObject managedObject) {
        if(!this.objectList.contains(managedObject)) {
            this.objectList.add(managedObject);
            managedObject.queueSave();
            updateFile(false);
        }
    }

    public void remove(ManagedObject managedObject) {
        if(this.objectList.remove(managedObject)) {
            updateFile(true);
        }
    }

    private void saveFile() {
        try {
            this.yamlConfiguration.save(this.savepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract List<ManagedObject> loadSavedObjects(YamlConfiguration yamlConfiguration);

    /**
     *
     * @param deleteDeleted if true deleted Objects will be deleted from the file
     */
    public void updateFile(boolean deleteDeleted) {
        boolean savedSomething = false;

        if(deleteDeleted) {
            this.yamlConfiguration = new YamlConfiguration();
        }

        for(ManagedObject managedObject : this.objectList) {
            if(managedObject.needSave() || deleteDeleted) {
                saveObjectToYamlObject(managedObject, this.yamlConfiguration);
                managedObject.setSaved();
                savedSomething = true;
            }
        }

        if(savedSomething) {
            saveFile();
        }
    }

    public abstract void saveObjectToYamlObject(ManagedObject object, YamlConfiguration yamlConfiguration);

    public List<ManagedObject> getObjectListCopy() {
        return new ArrayList<>(this.objectList);
    }
}
