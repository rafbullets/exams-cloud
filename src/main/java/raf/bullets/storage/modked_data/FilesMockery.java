package raf.bullets.storage.modked_data;

import raf.bullets.storage.dto.FileEntity;
import raf.bullets.storage.dto.FileType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilesMockery {

    private static String STORAGE_FOLDER = "./tmp";

    public static List<FileEntity> newFiles(List<File> files, String path) {

        ArrayList<FileEntity> fileEntities = new ArrayList<>(files.size());
        for (File file : files) {
            fileEntities.add(FilesMockery.newFile(file, path));
        }

        return fileEntities;
    }

    public static File findFile(String name, String path) {
        return new File(STORAGE_FOLDER+File.separator+path+File.separator+name);
    }

    public static FileEntity newFile(File file, String path) {
        new File("./tmp/"+path+"/").mkdirs();
        file.renameTo(new File(STORAGE_FOLDER+File.separator+path+File.separator+file.getName()));

        return new FileEntity(path, file.getName(), FileType.FILE);
    }

    public static ArrayList<FileEntity> findInPath(String path) {

        //If this pathname does not denote a directory, then listFiles() returns null.
        File[] files = new File(STORAGE_FOLDER+File.separator+path).listFiles();

        ArrayList<FileEntity> filesInPath = new ArrayList<>(files.length);

        for (File file : files) {
            filesInPath.add(new FileEntity(path, file.getName(), file.isFile()?FileType.FILE:FileType.FOLDER));
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
