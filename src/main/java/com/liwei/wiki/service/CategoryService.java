package com.liwei.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liwei.wiki.domain.Category;
import com.liwei.wiki.domain.CategoryExample;
import com.liwei.wiki.mapper.CategoryMapper;
import com.liwei.wiki.req.CategoryQueryReq;
import com.liwei.wiki.req.CategorySaveReq;
import com.liwei.wiki.resp.CategoryQueryResp;
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
public class CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<CategoryQueryResp> list(CategoryQueryReq req) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        // PageHelper分页注意事项:
        // 第一页从1开始
        // 并且只能第一个遇到的查询语句生效. 所以紧跟查询语句
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        // pageInfo.getTotal(); 总行数
        // pageInfo.getPages(); 总页数
        log.info("总行数:{}", pageInfo.getTotal());
        log.info("总页数:{}", pageInfo.getPages());




//        List<CategoryResp> respList = new ArrayList<>();
//        for (Category category: categoryList) {
//            // CategoryResp categoryResp = new CategoryResp();
//            // BeanUtils.copyProperties(category, categoryResp);
//            // 对象复制
//            CategoryResp categoryResp = CopyUtil.copy(category, CategoryResp.class);
//            respList.add(categoryResp);
//        }

        // 列表复制
        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        PageResp<CategoryQueryResp> categorysResp = new PageResp<>();
        categorysResp.setTotal(pageInfo.getTotal());
        categorysResp.setList(list);

        return categorysResp;
    }

    public List<CategoryQueryResp> all() {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        // 列表复制
        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);

        return list;
    }

    /**
     * 保存
     */
    public void save(CategorySaveReq req) {
        Category category = CopyUtil.copy(req, Category.class);
        if (ObjectUtils.isEmpty(category.getId())) {
            // 新增
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        } else {
            // 更新
            categoryMapper.updateByPrimaryKey(category);
        }
    }

    /**
     * 删除
     */
    public void delete(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }
}
