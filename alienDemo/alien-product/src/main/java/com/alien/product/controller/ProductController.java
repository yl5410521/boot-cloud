package com.alien.product.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {


    @GetMapping("/login/{type}")
    public String login(@PathVariable String type){

        return  type+"test for me.hello!";
    }
}
