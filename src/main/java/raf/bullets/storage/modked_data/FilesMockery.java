package raf.bullets.storage.modked_data;

import raf.bullets.storage.dto.FileEntity;
import raf.bullets.storage.dto.FileType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilesMockery {

    private static String STORAGE_FOLDER = "./tmp";

    public static ArrayList<FileEntity> findInPath(String path) {
        ArrayList<FileEntity> filesInPath = new ArrayList<>();

        //If this pathname does not denote a directory, then listFiles() returns null.
        File[] files = new File(STORAGE_FOLDER+File.separator+path).listFiles();

        if (files != null) {
            for (File file : files) {
                filesInPath.add(new FileEntity(path, file.getName(), file.isFile()?FileType.FILE:FileType.FOLDER));
            }
        }

        return filesInPath;
    }

    public static synchronized FileEntity rename(String path, String oldName, String newName) throws Exception {

        File file = new File(STORAGE_FOLDER+File.separator+path+File.separator+oldName);

        if(!file.renameTo(new File(STORAGE_FOLDER+File.separator+path+File.separator+newName))) {
            throw new Exception("Can not find the file on the given path.");
        }

        return new FileEntity(path, newName, file.isFile()?FileType.FILE:FileType.FOLDER);

    }

    public static synchronized void delete(String path, String name) throws Exception {

        File file = new File(STORAGE_FOLDER+File.separator+path+File.separator+name);

        if(!file.delete()) {
            throw new Exception("Can not find the file on the given path.");
        }
    }

    public static synchronized FileEntity newFolder(String path, String name) {
        new File(STORAGE_FOLDER+File.separator+path+File.separator+name).mkdirs();

        return new FileEntity(path, name, FileType.FOLDER);
    }
}
