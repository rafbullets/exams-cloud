package raf.bullets.storage.service.impl;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import raf.bullets.storage.dto.FileEntity;
import raf.bullets.storage.dto.FileType;
import raf.bullets.storage.modked_data.FilesMockery;
import raf.bullets.storage.service.FileStorageService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Override
    public Resource getFileAsResource() {
        return null;
    }

    @Override
    public List<FileEntity> findInPath(String path) {
        //TODO: Get files from component!

        return FilesMockery.findInPath(path);
    }

    @Override
    public FileEntity rename(String path, String oldName, String newName) throws Exception {
        //TODO: Rename file in the component!

        return FilesMockery.rename(path, oldName, newName);
    }

    @Override
    public void delete(String path, String name) throws Exception {
        //TODO: Delete a file from storage

        FilesMockery.delete(path, name);
    }

    @Override
    public FileEntity newFolder(String path, String name) {
        //TODO: Create new folder in storage!

        return FilesMockery.newFolder(path, name);
    }

    @Override
    public List<FileEntity> storeFiles(MultipartFile[] multipartFiles, String path, boolean asArchive) throws IOException {
        //TODO: Store file using component and archive if it is requested

        ArrayList<File> files = new ArrayList<>(multipartFiles.length);

        for (MultipartFile multipartFile : multipartFiles) {
            File convFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
//            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(multipartFile.getBytes());
            fos.close();

            files.add(convFile);
        }

        return FilesMockery.newFiles(files, path);
    }


}
