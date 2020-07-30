package com.uzykj.notice.controller.api;

import com.alibaba.fastjson.JSON;
import com.uzykj.notice.controller.BaseController;
import com.uzykj.notice.common.json.AjaxJson;
import com.uzykj.notice.common.websocket.service.system.SystemInfoSocketHandler;
import com.uzykj.notice.domian.Notice;
import com.uzykj.notice.enums.NoticeType;
import com.uzykj.notice.service.NoticeService;
import com.uzykj.notice.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @author ghostxbh
 * @date 2020/7/28
 * @description
 */
@RestController
@RequestMapping("/api/n")
@Api("通知流入")
public class InflowNoticeController extends BaseController {

    @Resource(name = "noticeService")
    private NoticeService noticeService;

    @PostMapping("/in/json")
    @ApiOperation("通知流入/json")
    public AjaxJson inflow(HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Notice notice = new Notice();
            notice.setType(NoticeType.IN.toString().toLowerCase());
            notice.setContents(JSON.toJSONString(StringUtil.getReqBody(request)));
            noticeService.save(notice);
            SystemInfoSocketHandler socketHandler = new SystemInfoSocketHandler();
            socketHandler.sendMessageToAllUsers("您有新的流入数据");
            j.success(notice, "notice");
        } catch (Exception e) {
            log.error("通知流入失败", e);
            j.error("流入失败");
        }
        return j;
    }

    @PostMapping("/in/from")
    @ApiOperation("通知流入/表单")
    public AjaxJson inflow(@RequestParam("contents") String contents,
                           HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        Notice notice = new Notice();
        notice.setContents(contents);
        try {
            notice.setType(NoticeType.IN.toString().toLowerCase());
            noticeService.save(notice);
            SystemInfoSocketHandler socketHandler = new SystemInfoSocketHandler();
            socketHandler.sendMessageToAllUsers("您有新的流入数据");
            j.success(notice, "notice");
        } catch (Exception e) {
            log.error("通知流入", e);
            j.error("流入失败");
        }
        return j;
    }
}
