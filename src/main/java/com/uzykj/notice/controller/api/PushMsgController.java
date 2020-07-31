package com.uzykj.notice.controller.api;

import com.uzykj.notice.common.json.AjaxJson;
import com.uzykj.notice.common.websocket.service.system.SystemInfoSocketHandler;
import com.uzykj.notice.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ghostxbh
 * @date 2020/7/29
 * @description
 */
@RestController
@RequestMapping("/api/ws")
@Api("消息推送")
public class PushMsgController extends BaseController {

    @PostMapping("/pushAll")
    @ApiOperation("推送给所有人")
    public AjaxJson pushAll(@RequestParam("msg") String msg) {
        AjaxJson j = new AjaxJson();
        log.debug("推送消息, {}", msg);
        try {
            SystemInfoSocketHandler socketHandler = new SystemInfoSocketHandler();
            socketHandler.sendMessageToAllUsers(msg);
            j.success(null, null);
        } catch (Exception e) {
            log.error("推送失败", e);
            j.error("推送失败");
        }
        return j;
    }
}
