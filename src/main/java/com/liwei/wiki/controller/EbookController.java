package com.liwei.wiki.controller;

import com.liwei.wiki.domain.Ebook;
import com.liwei.wiki.service.EbookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

// @Controller 返回一个页面
@RestController // 返回一个字符串
@RequestMapping("/ebook") // 里面所有的路径都会拼上ebook这个路径
public class EbookController {
    @Resource
    private EbookService ebookService;

    @GetMapping("/list")
    public List<Ebook> list() {
        return ebookService.list();
    }
}
