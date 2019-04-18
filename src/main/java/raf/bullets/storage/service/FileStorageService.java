package raf.bullets.storage.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import raf.bullets.storage.dto.FileEntity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Service
public interface FileStorageService {

    public List<FileEntity> findInPath(String path);

    public FileEntity rename(String path, String oldName, String newName) throws Exception;

    public boolean delete(String path, String name) throws Exception;

    public FileEntity newFolder(String path, String name);

    public FileEntity storeFile(MultipartFile multipartFile, String path, boolean asArchive) throws IOException;

    public List<FileEntity> storeFiles(MultipartFile[] multipartFiles, String path) throws IOException;

    public List<FileEntity> storeFilesAsArchive(MultipartFile[] multipartFiles, String path, String archiveName) throws IOException;

    public Resource getFileAsResource(String name, String path) throws MalformedURLException;

    public String generateFolderLink(String sourcePath, String uploadDestinationPath);

    public List<FileEntity> findInEncryptedPath(String encryptedPath);

    public FileEntity uploadToEncryptedPath(MultipartFile multipartFile, String encryptedPath) throws IOException;

}
