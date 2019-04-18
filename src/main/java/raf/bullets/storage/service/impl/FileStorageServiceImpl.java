package raf.bullets.storage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import raf.bullets.storage.dto.FileEntity;
import raf.bullets.storage.helper.Helper;
import raf.bullets.storage.modked_data.FilesMockery;
import raf.bullets.storage.service.FileStorageService;

import java.io.*;
import java.net.MalformedURLException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Autowired
    private Environment environment;

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

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(multipartFile.getBytes());
        fileOutputStream.close();

        if(asArchive) {
            File fileForZip = new File(file.getName()+".zip");
            FileOutputStream fos = new FileOutputStream(fileForZip);
            ZipOutputStream zipOut = new ZipOutputStream(fos);

            FileInputStream fis = new FileInputStream(file);
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            zipOut.close();
            fis.close();
            fos.close();

            file = fileForZip;
        }

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

        if(asArchive) {
            File fileForZip = new File("multiCompressed.zip");
            FileOutputStream fos = new FileOutputStream(fileForZip);
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            for (File file : files) {
                FileInputStream fis = new FileInputStream(file);
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zipOut.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                fis.close();
            }
            zipOut.close();
            fos.close();

            files.clear();
            files.add(fileForZip);
        }

        return FilesMockery.newFiles(files, path);
    }

    @Override
    public String generateFolderLink(String sourcePath, String uploadDestinationPath) {
        //TODO: store uploadDestinationPath as metadata

        return Helper.url("/link/"+Helper.base64Encode(sourcePath));
    }

    @Override
    public List<FileEntity> findInEncryptedPath(String encryptedPath) {
        String decryptedPath = Helper.base64Decode(encryptedPath);

        return this.findInPath(decryptedPath);
    }

    @Override
    public FileEntity uploadToEncryptedPath(MultipartFile multipartFile, String encryptedPath) throws IOException {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(encryptedPath);
        String decryptedPath = new String(decodedBytes);
        //TODO: get destination path from file's metadata
        String destinationPath = "upload_destination";

        return this.storeFile(multipartFile, destinationPath, false);
    }

}
