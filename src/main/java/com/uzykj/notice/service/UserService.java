package com.uzykj.notice.service;

import com.uzykj.notice.common.GlobCache;
import com.uzykj.notice.domian.User;
import com.uzykj.notice.enums.Opear;
import com.uzykj.notice.utils.StringUtil;
import com.uzykj.notice.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author ghostxbh
 * @date 2020/7/28
 * @description
 */
@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Resource
    private MongoTemplate mongoTemplate;

    public void save(User user) {
        user.setId(StringUtil.generateId());
        user.setCreateTime(TimeUtils.getCurrentTime());
        user.setUpdateTime(TimeUtils.getCurrentTime());
        mongoTemplate.save(user);
        GlobCache.opearUser(user, Opear.ADD);
    }

    public void del(String id) {
        mongoTemplate.remove(id);
        GlobCache.opearUser(new User(id), Opear.DEL);
    }

    public void update(User user) {
        user.setUpdateTime(TimeUtils.getCurrentTime());
        mongoTemplate.save(user);
        GlobCache.opearUser(user, Opear.UPD);
    }

    public List<User> findAll() {
        List<User> all = GlobCache.opearUserList(null, Opear.GET);
        if (all != null) return all;
        else {
            List<User> userList = mongoTemplate.findAll(User.class);
            GlobCache.opearUserList(userList, Opear.ADD);
            return userList;
        }
    }

    public User get(String id) {
        User user = GlobCache.opearUser(new User(id), Opear.GET);
        if (user != null) return user;
        else {
            User byId = mongoTemplate.findById(id, User.class);
            GlobCache.opearUser(byId, Opear.ADD);
            return byId;
        }
    }

    public User getByLoginName(String loginName) {
        List<User> userList = (List<User>) GlobCache.cacheMap.get(GlobCache.USER_CACHE);
        if (userList != null) {
            Optional<User> first = userList.stream().filter(user -> loginName.equals(user.getLoginName())).findFirst();
            return first.orElse(null);
        } else {
            Query query = new Query();
            query.addCriteria(Criteria.where("loginName").is(loginName));
            User user = mongoTemplate.findOne(query, User.class);
            return Optional.ofNullable(user).orElse(null);
        }
    }
}
