package com.liwei.wiki.controller;

import com.liwei.wiki.domain.Test;
import com.liwei.wiki.service.TestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

// @Controller 返回一个页面
@RestController // 返回一个字符串
public class TestController {
    @Value("${test.hello:golang}") // 读取配置文件里面的配置项${}, golang 是缺省值的意思。
    private String testHello;

    @Resource
    private TestService testService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!" + testHello;
    }

    @PostMapping("/hello/post")
    public String helloPost(String name) {
       return "Hello World! post, " + name;
    }

    @GetMapping("/test/list")
    public List<Test> testList() {
        return testService.list();
    }
}
