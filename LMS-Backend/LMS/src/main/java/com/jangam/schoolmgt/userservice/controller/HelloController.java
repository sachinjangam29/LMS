package com.jangam.schoolmgt.userservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String getPrintedMessage(){
        return "Hello World!";
    }
}
