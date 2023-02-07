package com.liwei.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liwei.wiki.domain.Ebook;
import com.liwei.wiki.domain.EbookExample;
import com.liwei.wiki.mapper.EbookMapper;
import com.liwei.wiki.req.EbookQueryReq;
import com.liwei.wiki.req.EbookSaveReq;
import com.liwei.wiki.resp.EbookQueryResp;
import com.liwei.wiki.resp.PageResp;
import com.liwei.wiki.util.CopyUtil;
import com.liwei.wiki.util.SnowFlake;
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

    @Resource
    private SnowFlake snowFlake;

    public PageResp<EbookQueryResp> list(EbookQueryReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        // 如果有 name 条件查询则进行 like 查询
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        // 如果有 二级分类 条件查询则进行 equal 查询, 在首页的时候需要用到。
        if (!ObjectUtils.isEmpty(req.getCategory2Id())) {
            criteria.andCategory2IdEqualTo(req.getCategory2Id());
        }
        // PageHelper分页注意事项:
        // 第一页从1开始
        // 并且只能第一个遇到的查询语句生效. 所以紧跟查询语句
        PageHelper.startPage(req.getPage(), req.getSize());
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
        List<EbookQueryResp> list = CopyUtil.copyList(ebookList, EbookQueryResp.class);
        PageResp<EbookQueryResp> ebooksResp = new PageResp<>();
        ebooksResp.setTotal(pageInfo.getTotal());
        ebooksResp.setList(list);

        return ebooksResp;
    }

    /**
     * 保存
     */
    public void save(EbookSaveReq req) {
        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        if (ObjectUtils.isEmpty(ebook.getId())) {
            // 新增
            ebook.setId(snowFlake.nextId());
            if (ObjectUtils.isEmpty(ebook.getDocCount())) {
                ebook.setDocCount(0);
            }
            if (ObjectUtils.isEmpty(ebook.getViewCount())) {
                ebook.setViewCount(0);
            }
            if (ObjectUtils.isEmpty(ebook.getVoteCount())) {
                ebook.setVoteCount(0);
            }
            ebookMapper.insert(ebook);
        } else {
            // 更新
            ebookMapper.updateByPrimaryKey(ebook);
        }
    }

    /**
     * 删除
     */
    public void delete(Long id) {
        ebookMapper.deleteByPrimaryKey(id);
    }
}
