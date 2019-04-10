package raf.bullets.storage.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.bullets.storage.dto.FileEntity;
import raf.bullets.storage.service.FileStorageService;

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
