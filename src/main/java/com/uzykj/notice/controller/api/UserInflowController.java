package com.uzykj.notice.controller.api;

import com.uzykj.notice.common.json.AjaxJson;
import com.uzykj.notice.controller.BaseController;
import com.uzykj.notice.domian.User;
import com.uzykj.notice.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ghostxbh
 * @date 2020/7/29
 * @description
 */
@RestController
@RequestMapping("/api/u")
@Api("用户接口管理")
public class UserInflowController extends BaseController {
    @Resource
    private UserService userService;

    @PostMapping("add")
    @ApiOperation("添加用户")
    public AjaxJson add(@RequestBody User user) {
        log.info("添加用户, {}", user);
        AjaxJson j = new AjaxJson();
        try {
            userService.save(user);
            j.success(user, "user");
        } catch (Exception e) {
            log.error("添加用户失败", e);
            j.error("添加用户失败");
        }
        return j;
    }
}
