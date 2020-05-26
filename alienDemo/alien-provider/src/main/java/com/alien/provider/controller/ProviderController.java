package com.alien.provider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProviderController {
    @GetMapping("/test/{id}")
    public String test(@PathVariable("id") String id) {
        System.out.println("服务：他终于来了"+id);
        return  id;
    }
}
