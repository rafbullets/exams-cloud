package raf.bullets.storage.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import raf.bullets.storage.dto.FileEntity;
import raf.bullets.storage.dto.responses.GeneratedLinkResponse;
import raf.bullets.storage.service.FileStorageService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/link")
public class EncryptedLinkController {

    private FileStorageService fileStorageService;

    public EncryptedLinkController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<GeneratedLinkResponse> generateLink(@RequestParam("source_path") String sourcePath,
                                                              @RequestParam("upload_destination_path") String uploadDestinationPath) {

        return new ResponseEntity<>(new GeneratedLinkResponse(this.fileStorageService.generateFolderLink(sourcePath, uploadDestinationPath)), HttpStatus.CREATED);
    }

    @GetMapping("/{encrypted_path}")
    public ResponseEntity<List<FileEntity>> listFilesFromLinkedFolder(@PathVariable("encrypted_path") String encryptedPath) throws Exception {

        return new ResponseEntity<>(this.fileStorageService.findInEncryptedPath(encryptedPath), HttpStatus.OK);
    }

    @PostMapping("/{encrypted_path}")
    public ResponseEntity uploadFileForLinkedFolder(@RequestParam("file") MultipartFile multipartFile,
                                                    @PathVariable("encrypted_path") String encryptedPath) throws Exception {

        this.fileStorageService.uploadToEncryptedPath(multipartFile, encryptedPath);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
}
