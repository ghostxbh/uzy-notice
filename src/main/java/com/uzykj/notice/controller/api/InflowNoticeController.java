package com.uzykj.notice.controller.api;

import com.uzykj.notice.common.json.AjaxJson;
import com.uzykj.notice.common.websocket.service.system.SystemInfoSocketHandler;
import com.uzykj.notice.service.NoticeApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

    @Resource(name = "noticeApiService")
    private NoticeApiService noticeApiService;
    @Autowired
    private SystemInfoSocketHandler infoSocketHandler;

    @PostMapping("/in/json")
    @ApiOperation("通知流入/json")
    public AjaxJson inflowJson(HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            noticeApiService.inFrom(request);
            j.success(null, null);
        } catch (Exception e) {
            log.error("通知流入失败", e);
            j.error("流入失败");
        }
        return j;
    }

    @PostMapping("/in/from")
    @ApiOperation("通知流入/表单")
    public AjaxJson inflowFrom(HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            noticeApiService.inJson(request);
            j.success(null, null);
        } catch (Exception e) {
            log.error("通知流入", e);
            j.error("流入失败");
        }
        return j;
    }
}
