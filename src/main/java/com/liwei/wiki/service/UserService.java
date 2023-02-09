package com.liwei.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liwei.wiki.domain.User;
import com.liwei.wiki.domain.UserExample;
import com.liwei.wiki.exception.BusinessException;
import com.liwei.wiki.exception.BusinessExceptionCode;
import com.liwei.wiki.mapper.UserMapper;
import com.liwei.wiki.req.UserQueryReq;
import com.liwei.wiki.req.UserResetPasswordReq;
import com.liwei.wiki.req.UserSaveReq;
import com.liwei.wiki.resp.UserQueryResp;
import com.liwei.wiki.resp.PageResp;
import com.liwei.wiki.util.CopyUtil;
import com.liwei.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service // 为了让 Spring 扫描到这个类
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<UserQueryResp> list(UserQueryReq req) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        // 如果有 name 条件查询则进行 like 查询
        if (!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andLoginNameEqualTo(req.getLoginName());
        }
        // PageHelper分页注意事项:
        // 第一页从1开始
        // 并且只能第一个遇到的查询语句生效. 所以紧跟查询语句
        PageHelper.startPage(req.getPage(), req.getSize());
        List<User> userList = userMapper.selectByExample(userExample);

        PageInfo<User> pageInfo = new PageInfo<>(userList);
        // pageInfo.getTotal(); 总行数
        // pageInfo.getPages(); 总页数
        log.info("总行数:{}", pageInfo.getTotal());
        log.info("总页数:{}", pageInfo.getPages());




//        List<UserResp> respList = new ArrayList<>();
//        for (User user: userList) {
//            // UserResp userResp = new UserResp();
//            // BeanUtils.copyProperties(user, userResp);
//            // 对象复制
//            UserResp userResp = CopyUtil.copy(user, UserResp.class);
//            respList.add(userResp);
//        }

        // 列表复制
        List<UserQueryResp> list = CopyUtil.copyList(userList, UserQueryResp.class);
        PageResp<UserQueryResp> usersResp = new PageResp<>();
        usersResp.setTotal(pageInfo.getTotal());
        usersResp.setList(list);

        return usersResp;
    }

    /**
     * 保存
     */
    public void save(UserSaveReq req) {
        User user = CopyUtil.copy(req, User.class);
        if (ObjectUtils.isEmpty(user.getId())) {
            // 新增
            if (ObjectUtils.isEmpty(selectUserByLoginName(req.getLoginName()))) {
                user.setId(snowFlake.nextId());
                userMapper.insert(user);
            } else {
                // 登陆名已经存在
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        } else {
            // 更新, 不能更新用户名和密码
            user.setLoginName(null);
            user.setPassword(null);
            // userMapper.updateByPrimaryKey(user); // 不能用这个方法
            userMapper.updateByPrimaryKeySelective(user); // 只能用这个方法
            // update user SET `name` = ? where id = ? 只会更新昵称
        }
    }

    public void resetPassword(UserResetPasswordReq req) {
        User user = CopyUtil.copy(req, User.class);
        userMapper.updateByPrimaryKeySelective(user); // 只能用这个方法
    }

    /**
     * 删除
     */
    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }

    public User selectUserByLoginName(String loginName) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(loginName);
        List<User> users = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(users)) {
            return null;
        } else {
            return users.get(0);
        }

    }
}
