package com.ecom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    @Autowired
    TestRepository repository;

    @GetMapping("/test")
    public String test(){
        return "Hello World";
    }

    @PostMapping("/test")
    public TestObj saveObj(@RequestBody TestObj obj){
        System.out.println(obj);
        return repository.save(obj);
    }
}
