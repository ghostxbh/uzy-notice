package com.uzykj.notice.controller;

import com.uzykj.notice.common.Constants;
import com.uzykj.notice.common.json.AjaxJson;
import com.uzykj.notice.domian.Notice;
import com.uzykj.notice.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ghostxbh
 */
@Slf4j
@RestController
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    @PostMapping(value = "/list")
    public AjaxJson find(Integer pageNo, Integer pageSize) {
        AjaxJson json = new AjaxJson();
        try {
            List<Notice> noticeList = noticeService.findByPage(pageNo, pageSize);
            long count = noticeService.findCount();
            json.getBody().put("list", noticeList);
            json.getBody().put("count", count);
            json.setSuccess(true);
        } catch (Exception e) {
            log.error("获取列表失败", e);
            json.error("获取列表失败");
        }
        return json;
    }

    @PostMapping(value = "/list/{id}")
    public String findOne(@PathVariable String id) {
        try {
            Notice notice = noticeService.get(id);
            return notice.getContents();
        } catch (Exception e) {
            log.error("获取列表失败", e);
        }
        return "";
    }

    @GetMapping(value = "/remove")
    public AjaxJson remove(@RequestParam String virify) {
        if (StringUtils.isEmpty(virify) || !virify.equals(Constants.DEL_COMMAND)) {
            return AjaxJson.builder()
                    .success(false)
                    .msg("验证失败")
                    .build();
        }

        try {
            long count = noticeService.findCount();
            noticeService.delAll();
            return AjaxJson.builder()
                    .success(true)
                    .msg("ok! 总共删除 " + count + " 条")
                    .build();
        } catch (Exception e) {
            log.error("获取列表失败", e);
            return AjaxJson.builder()
                    .success(false)
                    .msg(e.getMessage())
                    .build();
        }
    }
}
