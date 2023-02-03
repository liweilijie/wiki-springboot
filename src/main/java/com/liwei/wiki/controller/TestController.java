package com.liwei.wiki.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// @Controller 返回一个页面
@RestController // 返回一个字符串
public class TestController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }
}
