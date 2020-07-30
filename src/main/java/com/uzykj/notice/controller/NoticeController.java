package com.uzykj.notice.controller;

import com.uzykj.notice.common.json.AjaxJson;
import com.uzykj.notice.domian.Notice;
import com.uzykj.notice.service.NoticeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.beans.Encoder;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author ghostxbh
 * @date 2020/7/29
 * @description
 */
@Controller
@RequestMapping("/n")
public class NoticeController extends BaseController {

    @Resource
    private NoticeService noticeService;

    @RequestMapping(value = "/list")
    @ResponseBody
    public AjaxJson find(Integer pageNo, Integer pageSize) {
        AjaxJson j = new AjaxJson();
        try {
            List<Notice> noticeList = noticeService.findByPage(pageNo, pageSize);
            long count = noticeService.findCount();
            LinkedHashMap<String, Object> body = j.getBody();
            body.put("list", noticeList);
            body.put("count", count);
            j.setSuccess(true);
            j.setMsg("ok");
            j.setBody(body);
        } catch (Exception e) {
            log.error("获取列表失败", e);
            j.error("获取列表失败");
        }
        return j;
    }

    @RequestMapping(value = "/{id}")
    @ResponseBody
    public String findOne(@PathVariable String id) {
        try {
            Notice notice = noticeService.get(id);
            return notice.getContents();
        } catch (Exception e) {
            log.error("获取列表失败", e);
        }
        return "";
    }
}
