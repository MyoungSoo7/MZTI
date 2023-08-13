package com.example.mzti_server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home(){
        return "<h1>MZTI 어플리케이션 서버입니다</h1>";
    }
}
