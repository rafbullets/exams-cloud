package raf.bullets.storage.service.impl;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import raf.bullets.storage.dto.FileEntity;
import raf.bullets.storage.modked_data.FilesMockery;
import raf.bullets.storage.service.FileStorageService;

import java.io.*;
import java.net.MalformedURLException;
import java.util.*;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Override
    public Resource getFileAsResource(String name, String path) throws MalformedURLException {
        File file1 = FilesMockery.findFile(name, path);

        return new UrlResource(file1.toURI());
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
    public boolean delete(String path, String name) throws Exception {
        //TODO: Delete a file from storage

        FilesMockery.delete(path, name);
        return true;
    }

    @Override
    public FileEntity newFolder(String path, String name) {
        //TODO: Create new folder in storage!

        return FilesMockery.newFolder(path, name);
    }

    @Override
    public FileEntity storeFile(MultipartFile multipartFile, String path, boolean asArchive) throws IOException {
        //TODO: Store file using component and archive if it is requested

        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
//            convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(multipartFile.getBytes());
        fos.close();

        return FilesMockery.newFile(file, path);
    }

    @Override
    public List<FileEntity> storeFiles(MultipartFile[] multipartFiles, String path, boolean asArchive) throws IOException {
        //TODO: Store file using component and archive if it is requested

        ArrayList<File> files = new ArrayList<>(multipartFiles.length);

        for (MultipartFile multipartFile : multipartFiles) {
            File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
//            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();


            files.add(file);
        }


        return FilesMockery.newFiles(files, path);
    }

    @Override
    public String generateLink(String sourcePath, String uploadDestinationPath) {
        //TODO: store uploadDestinationPath as metadata

        //TODO: get base url from config.
        return "http://localhost:8080/files/link/"+Base64.getUrlEncoder().encodeToString(sourcePath.getBytes());
    }

    @Override
    public List<FileEntity> findInEncryptedPath(String encryptedPath) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(encryptedPath);
        String decryptedPath = new String(decodedBytes);

        return this.findInPath(decryptedPath);
    }

    @Override
    public FileEntity uploadToEncryptedPath(MultipartFile multipartFile, String encryptedPath) throws IOException {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(encryptedPath);
        String decryptedPath = new String(decodedBytes);
        //TODO: get destination path from file metadata
        String destinationPath = "upload_destination";

        return this.storeFile(multipartFile, destinationPath, false);
    }

}
