package com.liwei.wiki.service;

import com.liwei.wiki.domain.Demo;
import com.liwei.wiki.mapper.DemoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service // 为了让 Spring 扫描到这个类
public class DemoService {
    @Resource
    private DemoMapper demoMapper;

    public List<Demo> list() {
       return demoMapper.selectByExample(null);
    }
}
