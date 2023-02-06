package com.liwei.wiki.controller;

import com.liwei.wiki.req.EbookQueryReq;
import com.liwei.wiki.req.EbookSaveReq;
import com.liwei.wiki.resp.CommonResp;
import com.liwei.wiki.resp.EbookQueryResp;
import com.liwei.wiki.resp.PageResp;
import com.liwei.wiki.service.EbookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

// @Controller 返回一个页面
@RestController // 返回一个字符串
@RequestMapping("/ebook") // 里面所有的路径都会拼上ebook这个路径
public class EbookController {
    @Resource
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp list(EbookQueryReq req) {
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody EbookSaveReq req) {
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }
}
