package raf.bullets.storage.modked_data;

import raf.bullets.storage.dto.FileEntity;
import raf.bullets.storage.dto.FileType;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class FilesMockery {
    private static FileEntity[] fileEntitiesArray = {
        new FileEntity(File.separator, "UUP", FileType.FOLDER),
        new FileEntity(File.separator, "subfolder", FileType.FOLDER),
        new FileEntity(File.separator+"subfolder"+File.separator, "test.txt", FileType.FILE),
        new FileEntity(File.separator, "bobek.go", FileType.FILE),
        new FileEntity(File.separator, "antole.php", FileType.FILE),
        new FileEntity(File.separator, "tudu.php", FileType.FILE)
    };

    private static ArrayList<FileEntity> fileEntities = new ArrayList<>(Arrays.asList(fileEntitiesArray));

    public static synchronized void addFile(FileEntity fileEntity){
        fileEntities.add(fileEntity);
    }

    public static ArrayList<FileEntity> findInPath(String path) {
        ArrayList<FileEntity> filesInPath = new ArrayList<>();
        for (FileEntity fileEntity : fileEntities) {
            if(fileEntity.getPath().equals(path)) {
                filesInPath.add(fileEntity);
            }
        }

        return filesInPath;
    }

    public static synchronized FileEntity rename(String path, String oldName, String newName) throws Exception {
        FileEntity newFileEntity = null;
        for (FileEntity fileEntity:fileEntities) {
            if(fileEntity.getPath().equals(path) && fileEntity.getName().equals(oldName)) {
                fileEntity.setName(newName);
                newFileEntity = fileEntity;
                break;
            }
        }

        if (newFileEntity == null) {
            throw new Exception("Can not find the file on the given path.");
        }

        return newFileEntity;
    }

    public static synchronized void delete(String path, String name) throws Exception {
        FileEntity deleteFileEntity = null;
        for (FileEntity fileEntity : fileEntities) {
            if(fileEntity.getPath().equals(path) && fileEntity.getName().equals(name)) {
                deleteFileEntity = fileEntity;
            }
        }

        if(deleteFileEntity == null) {
            throw new Exception("Can not find the file on the given path.");
        }

        fileEntities.remove(deleteFileEntity);
    }

    public static synchronized FileEntity newFolder(String path, String name) {
        FileEntity fileEntity = new FileEntity(path, name, FileType.FOLDER);
        fileEntities.add(fileEntity);

        return fileEntity;
    }
}
