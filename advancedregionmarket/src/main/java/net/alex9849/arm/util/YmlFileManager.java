package net.alex9849.arm.util;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class YmlFileManager<ManagedObject extends Saveable> {
    private List<ManagedObject> objectList;
    private YamlConfiguration yamlConfiguration;
    private File savepath;


    public YmlFileManager(File savepath, InputStream resourceStream, Plugin plugin) {
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
        this.objectList.addAll(loadSavedObjects());
    }

    public void add(ManagedObject managedObject) {
        if(!this.objectList.contains(managedObject)) {
            this.objectList.add(managedObject);
            managedObject.queueSave();
            saveObjects();
        }
    }

    public void remove(ManagedObject managedObject) {
        if(this.objectList.remove(managedObject)) {
            for(ManagedObject saveObject : this.objectList) {
                saveObject(saveObject, this.yamlConfiguration);
                saveObject.setSaved();
            }
            this.saveFile();
        }
    }

    private void saveFile() {
        try {
            this.yamlConfiguration.save(this.savepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract List<ManagedObject> loadSavedObjects();

    public void saveObjects() {
        boolean savedSomething = false;
        for(ManagedObject managedObject : this.objectList) {
            if(managedObject.needSave()) {
                saveObject(managedObject, this.yamlConfiguration);
                managedObject.setSaved();
                savedSomething = true;
            }
        }

        if(savedSomething) {
            saveFile();
        }
    }

    public abstract void saveObject(ManagedObject object, YamlConfiguration yamlConfiguration);

    public List<ManagedObject> getObjectList() {
        return this.objectList;
    }
}
