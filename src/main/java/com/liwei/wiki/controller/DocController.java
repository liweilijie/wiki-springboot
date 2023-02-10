package com.liwei.wiki.controller;

import com.liwei.wiki.req.DocQueryReq;
import com.liwei.wiki.req.DocSaveReq;
import com.liwei.wiki.resp.CommonResp;
import com.liwei.wiki.resp.DocQueryResp;
import com.liwei.wiki.resp.PageResp;
import com.liwei.wiki.service.DocService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// @Controller 返回一个页面
@RestController // 返回一个字符串
@RequestMapping("/doc") // 里面所有的路径都会拼上doc这个路径
public class DocController {
    @Resource
    private DocService docService;

    @GetMapping("/list")
    public CommonResp list(@Valid DocQueryReq req) {
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.list(req);
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/all/{ebookId}")
    public CommonResp all(@PathVariable Long ebookId) {
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.all(ebookId);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq req) {
        CommonResp resp = new CommonResp<>();
        docService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{idStr}")
    public CommonResp delete(@PathVariable String idStr) {
        CommonResp resp = new CommonResp<>();
        List<Long> list = Arrays.asList(idStr.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());

        docService.delete(list);
        return resp;
    }

    @GetMapping("/find-content/{id}")
    public CommonResp findContent(@PathVariable Long id) {
        CommonResp<String> resp = new CommonResp<>();
        String content = docService.findContent(id);
        resp.setContent(content);
        return resp;
    }

    @GetMapping("/vote/{id}")
    public CommonResp vote(@PathVariable Long id) {
        CommonResp<String> resp = new CommonResp<>();
        docService.vote(id);
        return resp;
    }
}
