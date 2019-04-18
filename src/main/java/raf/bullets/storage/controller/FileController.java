package raf.bullets.storage.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import raf.bullets.storage.dto.FileEntity;
import raf.bullets.storage.service.FileStorageService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    private FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @RequestMapping(value = "", method = { RequestMethod.GET, RequestMethod.POST })
    public ResponseEntity<List<FileEntity>> getFiles(@RequestParam("path") String path) {
        return new ResponseEntity<>(this.fileStorageService.findInPath(path), HttpStatus.OK);
    }

    @RequestMapping(value = "/download", method = { RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Resource> downloadFile(@RequestParam("name") String name,
                                                 @RequestParam("path") String path,
                                                 HttpServletRequest request) throws IOException {
        // Load file as Resource
        Resource resource = this.fileStorageService.getFileAsResource(name, path);


        String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping(value =  "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<List<FileEntity>> uploadFiles(@RequestParam("files") MultipartFile[] multipartFiles,
                                                        @RequestParam("path") String path,
                                                        @RequestParam(value = "archive", required = false) boolean asArchive,
                                                        @RequestParam(value = "archive_name", required = false) String archiveName) throws IOException {

        if (asArchive) {
            return new ResponseEntity<>(this.fileStorageService.storeFilesAsArchive(multipartFiles, path, archiveName), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(this.fileStorageService.storeFiles(multipartFiles, path), HttpStatus.CREATED);
    }

    @PostMapping("/rename")
    public ResponseEntity<FileEntity> renameFile(@RequestParam("path") String path,
                                                 @RequestParam("old_name") String oldName,
                                                 @RequestParam("new_name") String newName) throws Exception {

        return new ResponseEntity<>(this.fileStorageService.rename(path, oldName, newName), HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity delete(@RequestParam("path") String path,@RequestParam("name") String name) throws Exception {
        this.fileStorageService.delete(path, name);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/folder")
    public ResponseEntity<FileEntity> newFolder(@RequestParam("path") String path, @RequestParam("name") String name) {
        return new ResponseEntity<>(this.fileStorageService.newFolder(path, name), HttpStatus.CREATED);
    }
}
