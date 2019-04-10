package raf.bullets.storage.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@RestController
public class TestController {


    @RequestMapping("/test")
    public ResponseEntity<Resource> test(HttpServletRequest request) throws IOException {
        File file1 = new File("src/main/resources/static/opinodo_screen.jpg");

        Resource resource = new UrlResource(file1.toURI());
        String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());


        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        System.out.println("Content type" + contentType);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file1.getName() + "\"")
                .body(resource);
    }
}
