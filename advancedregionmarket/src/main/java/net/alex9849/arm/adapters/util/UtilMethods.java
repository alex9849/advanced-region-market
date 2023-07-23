package net.alex9849.arm.adapters.util;

import java.io.File;

public class UtilMethods {

    public static void deleteFilesRec(File file) {
        if(file.isDirectory()) {
            for(File child : file.listFiles()) {
                deleteFilesRec(child);
            }
        }
        file.delete();
    }

}
