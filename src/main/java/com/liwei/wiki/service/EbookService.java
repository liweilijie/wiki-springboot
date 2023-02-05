package com.liwei.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liwei.wiki.domain.Ebook;
import com.liwei.wiki.domain.EbookExample;
import com.liwei.wiki.mapper.EbookMapper;
import com.liwei.wiki.req.EbookReq;
import com.liwei.wiki.resp.EbookResp;
import com.liwei.wiki.util.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service // 为了让 Spring 扫描到这个类
public class EbookService {

    private static final Logger log = LoggerFactory.getLogger(EbookService.class);

    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        // 如果有 name 条件查询则进行 like 查询
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        // PageHelper分页注意事项:
        // 第一页从1开始
        // 并且只能第一个遇到的查询语句生效. 所以紧跟查询语句
        PageHelper.startPage(1, 3);
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        // pageInfo.getTotal(); 总行数
        // pageInfo.getPages(); 总页数
        log.info("总行数:{}", pageInfo.getTotal());
        log.info("总页数:{}", pageInfo.getPages());




//        List<EbookResp> respList = new ArrayList<>();
//        for (Ebook ebook: ebookList) {
//            // EbookResp ebookResp = new EbookResp();
//            // BeanUtils.copyProperties(ebook, ebookResp);
//            // 对象复制
//            EbookResp ebookResp = CopyUtil.copy(ebook, EbookResp.class);
//            respList.add(ebookResp);
//        }

        // 列表复制
        List<EbookResp> list = CopyUtil.copyList(ebookList, EbookResp.class);

        return list;
    }
}
