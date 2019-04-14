package raf.bullets.storage.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import raf.bullets.storage.dto.FileEntity;

import java.io.IOException;
import java.util.List;

@Service
public interface FileStorageService {

    public Resource getFileAsResource();

    public List<FileEntity> findInPath(String path);

    public FileEntity rename(String path, String oldName, String newName) throws Exception;

    public void delete(String path, String name) throws Exception;

    public FileEntity newFolder(String path, String name);

    public List<FileEntity> storeFiles(MultipartFile[] multipartFiles, String path, boolean asArchive) throws IOException;
}
