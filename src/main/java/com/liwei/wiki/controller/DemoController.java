package com.liwei.wiki.controller;

import com.liwei.wiki.domain.Demo;
import com.liwei.wiki.service.DemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

// @Controller 返回一个页面
@RestController // 返回一个字符串
@RequestMapping("/demo") // 里面所有的路径都会拼上demo这个路径
public class DemoController {
    @Resource
    private DemoService demoService;

    @GetMapping("/list")
    public List<Demo> list() {
        return demoService.list();
    }
}
