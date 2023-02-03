package com.liwei.wiki.service;

import com.liwei.wiki.domain.Ebook;
import com.liwei.wiki.mapper.EbookMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service // 为了让 Spring 扫描到这个类
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    public List<Ebook> list() {
       return ebookMapper.selectByExample(null);
    }
}
