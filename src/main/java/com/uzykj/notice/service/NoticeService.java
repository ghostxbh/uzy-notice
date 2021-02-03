package com.uzykj.notice.service;

import com.uzykj.notice.domian.Notice;
import com.uzykj.notice.utils.StringUtil;
import com.uzykj.notice.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * @author ghostxbh
 * @date 2020/7/28
 * @description
 */
@Service
public class NoticeService {
    private static final Logger log = LoggerFactory.getLogger(NoticeService.class);
    @Resource
    private MongoTemplate mongoTemplate;

    public Notice get(String id) {
        log.debug("获取详情, ID: {}", id);
        return mongoTemplate.findById(id, Notice.class);
    }

    public void save(Notice notice) {
        notice.setCreateTime(TimeUtils.getCurrentTime());
        notice.setUpdateTime(TimeUtils.getCurrentTime());
        notice.setUpdateStamp(TimeUtils.getCurrentTimeStamp());
        log.debug("保存通知, {}", notice.toString());
        mongoTemplate.save(notice);
    }

    public void del(String id) {
        log.debug("删除通知, {}", id);
        mongoTemplate.remove(id);
    }

    public Notice update(Notice notice) {
        log.debug("修改通知, {}", notice);
        notice.setUpdateTime(TimeUtils.getCurrentTime());
        notice.setUpdateStamp(TimeUtils.getCurrentTimeStamp());
        mongoTemplate.save(notice);
        return notice;
    }

    public List<Notice> findByPage(int pageNo, int pageSize){
        log.debug("查找通知，pageNo: {}, pageSize: {}", pageNo, pageSize);
        long skip = (pageNo - 1) * pageSize;
        Query query = new Query();
        query.skip(skip);
        query.limit(pageSize);
        query.with(Sort.by(Sort.Direction.DESC, "createTime"));
        return mongoTemplate.find(query, Notice.class);
    }

    public long findCount(){
        log.debug("查找通知总数");
        return mongoTemplate.count(new Query(), Notice.class);
    }
}
