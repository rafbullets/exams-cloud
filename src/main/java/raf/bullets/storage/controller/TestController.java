package raf.bullets.storage.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {

    @RequestMapping("/test")
    public List<String> say() {
        List<String> list = new ArrayList<>();
        List<String> arrayList = new ArrayList<>();
        list.add("test1");
        list.add("test2");
        return list;
    }
}
