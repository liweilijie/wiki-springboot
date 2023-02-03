package com.liwei.wiki.service;

import com.liwei.wiki.domain.Test;
import com.liwei.wiki.mapper.TestMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service // 为了让 Spring 扫描到这个类
public class TestService {
    @Resource
    private TestMapper testMapper;

    public List<Test> list() {
       return testMapper.list();
    }
}
