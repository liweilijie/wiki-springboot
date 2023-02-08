package com.liwei.wiki.controller;

import com.liwei.wiki.req.UserQueryReq;
import com.liwei.wiki.req.UserSaveReq;
import com.liwei.wiki.resp.CommonResp;
import com.liwei.wiki.resp.UserQueryResp;
import com.liwei.wiki.resp.PageResp;
import com.liwei.wiki.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

// @Controller 返回一个页面
@RestController // 返回一个字符串
@RequestMapping("/user") // 里面所有的路径都会拼上user这个路径
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/list")
    public CommonResp list(@Valid UserQueryReq req) {
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        PageResp<UserQueryResp> list = userService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody UserSaveReq req) {
        CommonResp resp = new CommonResp<>();
        userService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>();
        userService.delete(id);
        return resp;
    }
}
