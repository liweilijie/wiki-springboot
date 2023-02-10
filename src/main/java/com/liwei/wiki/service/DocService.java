package com.liwei.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liwei.wiki.domain.Content;
import com.liwei.wiki.domain.Doc;
import com.liwei.wiki.domain.DocExample;
import com.liwei.wiki.exception.BusinessException;
import com.liwei.wiki.exception.BusinessExceptionCode;
import com.liwei.wiki.mapper.ContentMapper;
import com.liwei.wiki.mapper.DocMapper;
import com.liwei.wiki.mapper.DocMapperCust;
import com.liwei.wiki.req.DocQueryReq;
import com.liwei.wiki.req.DocSaveReq;
import com.liwei.wiki.resp.DocQueryResp;
import com.liwei.wiki.resp.PageResp;
import com.liwei.wiki.util.CopyUtil;
import com.liwei.wiki.util.RedisUtil;
import com.liwei.wiki.util.RequestContext;
import com.liwei.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service // 为了让 Spring 扫描到这个类
public class DocService {

    private static final Logger log = LoggerFactory.getLogger(DocService.class);

    @Resource
    private DocMapper docMapper;

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private DocMapperCust docMapperCust;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    public WsService wsService;

    public PageResp<DocQueryResp> list(DocQueryReq req) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        // PageHelper分页注意事项:
        // 第一页从1开始
        // 并且只能第一个遇到的查询语句生效. 所以紧跟查询语句
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        // pageInfo.getTotal(); 总行数
        // pageInfo.getPages(); 总页数
        log.info("总行数:{}", pageInfo.getTotal());
        log.info("总页数:{}", pageInfo.getPages());




//        List<DocResp> respList = new ArrayList<>();
//        for (Doc doc: docList) {
//            // DocResp docResp = new DocResp();
//            // BeanUtils.copyProperties(doc, docResp);
//            // 对象复制
//            DocResp docResp = CopyUtil.copy(doc, DocResp.class);
//            respList.add(docResp);
//        }

        // 列表复制
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);
        PageResp<DocQueryResp> docsResp = new PageResp<>();
        docsResp.setTotal(pageInfo.getTotal());
        docsResp.setList(list);

        return docsResp;
    }

    public List<DocQueryResp> all(Long ebookId) {
        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);

        // 列表复制
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);

        return list;
    }

    /**
     * 保存
     */
    @Transactional // 操作两张表需要做事务操作，增加这个就能事务，不能在同一个类里面调用。
    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content c = CopyUtil.copy(req, Content.class);
        if (ObjectUtils.isEmpty(doc.getId())) {
            // 新增
            doc.setId(snowFlake.nextId());
            doc.setViewCount(0); // 不然会有 null 的情况
            doc.setVoteCount(0); // 有null 的情况会在 update 的时候失败
            docMapper.insert(doc);

            // 将上面生成的 id 赋值给 content 再插入
            c.setId(doc.getId());
            contentMapper.insert(c);

        } else {
            // 更新
            docMapper.updateByPrimaryKey(doc);
            // 这里如果更新失败即说明数据库里面还没有，需要 insert
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(c);
            if (count == 0) {
                contentMapper.insert(c);
            }
        }
    }

    /**
     * 删除
     */
    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }

    public void delete(List<Long> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }

    public String findContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);

        Integer updateCount = docMapperCust.increaseViewCount(id);
        log.info("更新阅读数：{}", updateCount);

        if (ObjectUtils.isEmpty(content)) {
            return "";
        } else {
            return content.getContent();
        }
    }

    /**
     * 点赞
     */
    public void vote(Long id) {
        // docMapperCust.increaseVoteCount(id);
        // 远程IP+doc.id作为key，24小时内不能重复
        String ip = RequestContext.getRemoteAddr();
        if (redisUtil.validateRepeat("DOC_VOTE_" + id + "_" + ip, 5000)) {
            docMapperCust.increaseVoteCount(id);
        } else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }

        // 推送消息
        Doc docDb = docMapper.selectByPrimaryKey(id);
        String logId = MDC.get("LOG_ID");
        wsService.sendInfo("【" + docDb.getName() + "】被点赞！", logId);
        // rocketMQTemplate.convertAndSend("VOTE_TOPIC", "【" + docDb.getName() + "】被点赞！");
    }

    public void updateEbookInfo() {
        docMapperCust.updateEbookInfo();
    }
}
