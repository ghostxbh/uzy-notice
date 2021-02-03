package com.uzykj.notice.controller.api;

import com.alibaba.fastjson.JSON;
import com.uzykj.notice.common.json.AjaxJson;
import com.uzykj.notice.common.websocket.service.system.SystemInfoSocketHandler;
import com.uzykj.notice.domian.Notice;
import com.uzykj.notice.enums.NoticeType;
import com.uzykj.notice.service.NoticeService;
import com.uzykj.notice.utils.HttpParseUtil;
import com.uzykj.notice.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ghostxbh
 * @date 2020/7/28
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/api")
@Api("通知流入")
public class InflowNoticeController {

    @Resource(name = "noticeService")
    private NoticeService noticeService;
    @Autowired
    private SystemInfoSocketHandler infoSocketHandler;

    @PostMapping("/in/json")
    @ApiOperation("通知流入/json")
    public AjaxJson inflowJson(HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Notice notice = new Notice();
            notice.setType(NoticeType.IN.toString().toLowerCase());
            notice.setContents(JSON.toJSONString(StringUtil.getReqBody(request)));
            notice.setHeaders(HttpParseUtil.getHeaders(request));
            noticeService.save(notice);
            infoSocketHandler.sendMessageToAllUsers("您有新的流入数据");
            j.success(null, null);
        } catch (Exception e) {
            log.error("通知流入失败", e);
            j.error("流入失败");
        }
        return j;
    }

    @PostMapping("/in/from")
    @ApiOperation("通知流入/表单")
    public AjaxJson inflowFrom(HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Notice notice = new Notice();
            notice.setType(NoticeType.IN.toString().toLowerCase());
            notice.setContents(JSON.toJSONString(StringUtil.getReqBody(request)));
            notice.setHeaders(HttpParseUtil.getHeaders(request));
            noticeService.save(notice);
            infoSocketHandler.sendMessageToAllUsers("您有新的流入数据");
            j.success(null, null);
        } catch (Exception e) {
            log.error("通知流入", e);
            j.error("流入失败");
        }
        return j;
    }
}
