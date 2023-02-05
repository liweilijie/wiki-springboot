package com.liwei.wiki.controller;

import com.liwei.wiki.req.EbookReq;
import com.liwei.wiki.resp.CommonResp;
import com.liwei.wiki.resp.EbookResp;
import com.liwei.wiki.resp.PageResp;
import com.liwei.wiki.service.EbookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

// @Controller 返回一个页面
@RestController // 返回一个字符串
@RequestMapping("/ebook") // 里面所有的路径都会拼上ebook这个路径
public class EbookController {
    @Resource
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp list(EbookReq req) {
        CommonResp<PageResp<EbookResp>> resp = new CommonResp<>();
        PageResp<EbookResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }
}
