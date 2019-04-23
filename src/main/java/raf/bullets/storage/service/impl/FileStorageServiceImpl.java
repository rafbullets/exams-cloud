package raf.bullets.storage.service.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pak.DropboxFile;
import raf.bullets.LocalStorageOperations;
import raf.bullets.storage.dto.FileEntity;
import raf.bullets.storage.dto.FileType;
import raf.bullets.storage.helper.Helper;
import raf.bullets.storage.modked_data.FilesMockery;
import raf.bullets.storage.service.FileStorageService;
import specification.model.FileWrapper;
import specification.model.FolderResult;
import specification.model.FolderWrapper;
import specification.operations.CommonOperations;
import specification.operations.file.FileArchiveOperations;
import specification.operations.file.FileBasicOperations;
import specification.operations.folder.FolderBasicOperations;
import specification.storage.StorageOperations;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private StorageOperations storageOperations;
    private CommonOperations commonOperations;
    private FolderBasicOperations folderBasicOperations;
    private FileBasicOperations fileBasicOperations;
    private FileArchiveOperations fileArchiveOperations;

    private String rootPath;
    public FileStorageServiceImpl() {

//        loadLocal();
        loadDropbox();

        ArrayList<String> forbidden = new ArrayList<>();
        forbidden.add("bat");
        this.storageOperations.init(this.rootPath, forbidden);


    }

    private void loadDropbox() {
        DropboxFile dropboxFile= new DropboxFile();
        this.storageOperations = dropboxFile;
        this.commonOperations = dropboxFile;
        this.folderBasicOperations = dropboxFile;
        this.fileBasicOperations = dropboxFile;
        this.fileArchiveOperations = dropboxFile;
        this.rootPath = "init";
    }

    private void loadLocal() {
        LocalStorageOperations storageOperations = new LocalStorageOperations();
        this.storageOperations = storageOperations;
        this.commonOperations = storageOperations;
        this.folderBasicOperations = storageOperations;
        this.fileBasicOperations = storageOperations;
        this.fileArchiveOperations = storageOperations;
        this.rootPath = "./tmp";
    }

    @Override
    public Resource getFileAsResource(String name, String path) throws Exception {
        File file1 = this.fileBasicOperations.downloadFile(path, name, "./tmp/"+name).getFile();

        return new UrlResource(file1.toURI());
    }

    @Override
    public List<FileEntity> findInPath(String path) throws Exception {
        ArrayList<FileEntity> fileEntities = new ArrayList<>();
        FolderWrapper folderWrapper = this.folderBasicOperations.listFolder(path, "");
        for (FolderResult folderResult : folderWrapper.getFolderResults()) {
            fileEntities.add(new FileEntity(path, folderResult.getName(), folderResult.isDirectory()? FileType.FOLDER:FileType.FILE));
        }

        return fileEntities;
//        return FilesMockery.findInPath(path);
    }

    @Override
    public FileEntity rename(String path, String oldName, String newName) throws Exception {
        //TODO: Rename file in the component!
        throw new Exception("Not implemented");
//        return FilesMockery.rename(path, oldName, newName);
    }

    @Override
    public boolean delete(String path, String name) throws Exception {
        //TODO: Delete a file from storage
        throw new Exception("Not implemented");

//        FilesMockery.delete(path, name);
//        return true;
    }

    @Override
    public FileEntity newFolder(String path, String name) throws Exception {
        this.folderBasicOperations.createFolder(path, name);
        return new FileEntity(path, name, FileType.FOLDER);

//        return FilesMockery.newFolder(path, name);
    }

    @Override
    public FileEntity storeFile(MultipartFile multipartFile, String path) throws Exception {

        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(multipartFile.getBytes());
        fileOutputStream.close();

        this.fileBasicOperations.uploadFile(file, path, multipartFile.getOriginalFilename());
        return new FileEntity(path, multipartFile.getOriginalFilename(), FileType.FILE);

//        return FilesMockery.newFile(file, path);
    }

    @Override
    public List<FileEntity> storeFilesAsArchive(MultipartFile[] multipartFiles, String path, String archiveName) throws Exception {
        ArrayList<File> files = new ArrayList<>(multipartFiles.length);

        for (MultipartFile multipartFile : multipartFiles) {
            File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();

            files.add(file);
        }

        this.fileArchiveOperations.uploadAsZipFile(files, path, archiveName, "");

        ArrayList<FileEntity> zippedFileEntities = new ArrayList<>();
        zippedFileEntities.add(new FileEntity(path, archiveName, FileType.FILE));
        return zippedFileEntities;

//        return FilesMockery.newFiles(files, path);
    }

    @Override
    public List<FileEntity> storeFiles(MultipartFile[] multipartFiles, String path) throws Exception {
        //TODO: Store file using component and archive if it is requested

        ArrayList<FileEntity> files = new ArrayList<>(multipartFiles.length);

        for (MultipartFile multipartFile : multipartFiles) {
            File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
//            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();

            this.storeFile(multipartFile, path);
            files.add(new FileEntity(path, multipartFile.getOriginalFilename(), FileType.FILE));
        }

        return files;
    }

    @Override
    public String generateFolderLink(String sourcePath, String uploadDestinationPath) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("destination_path", uploadDestinationPath);
        this.folderBasicOperations.updateMetadata(Paths.get(sourcePath).getParent().toString(), Paths.get(sourcePath).getFileName().toString(), jsonObject.toString());
        return Helper.url("/link/"+Helper.base64Encode(sourcePath));
    }

    @Override
    public List<FileEntity> findInEncryptedPath(String encryptedPath) throws Exception {
        String decryptedPath = Helper.base64Decode(encryptedPath);

        return this.findInPath(decryptedPath);
    }

    @Override
    public FileEntity uploadToEncryptedPath(MultipartFile multipartFile, String encryptedPath, String studentData) throws Exception {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(encryptedPath);
        String decryptedPath = new String(decodedBytes);

//        System.out.println("parent: "+Paths.get(decryptedPath).getParent().toString());
//        System.out.println("name: "+Paths.get(decryptedPath).getFileName().toString());

        String metadata = this.folderBasicOperations.listFolder(Paths.get(decryptedPath).getParent().toString(), Paths.get(decryptedPath).getFileName().toString()).getMetadata();
        System.out.println("meta: "+metadata);

        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse(metadata).getAsJsonObject();

        String destinationPath = o.get("destination_path").getAsString();

        System.out.println("des: "+destinationPath);
        return this.storeFile(multipartFile, destinationPath);
    }


}
