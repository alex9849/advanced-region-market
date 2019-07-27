package net.aex9849.armImporter.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zipper {

    public static void zipDir(File toZipPath, File outputFile) {
        List<File> files = getRecFiles(toZipPath);

        try {
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(outputFile));
            for(File file: files) {

                String insideZipPath = file.getAbsolutePath();
                insideZipPath = insideZipPath.substring(toZipPath.getAbsolutePath().length() + 1);
                ZipEntry zipEntry = new ZipEntry(insideZipPath);
                zipOut.putNextEntry(zipEntry);

                try {
                    FileInputStream fis = new FileInputStream(file);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zipOut.write(buffer, 0, length);
                    }

                    fis.close();
                    zipOut.closeEntry();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            zipOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static List<File> getRecFiles(File path) {
        List<File> allFiles = new LinkedList<File>();

        File[] pathFiles = path.listFiles();

        for(File file : pathFiles) {
            if(file.isFile()) {
                allFiles.add(file);
            } else {
                allFiles.addAll(getRecFiles(file));
            }
        }
        return allFiles;
    }
}
