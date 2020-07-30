package com.uzykj.notice.controller;

import com.uzykj.notice.common.json.AjaxJson;
import com.uzykj.notice.domian.User;
import com.uzykj.notice.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;

/**
 * @author ghostxbh
 * @date 2020/7/28
 * @description
 */
@Controller
public class LoginController extends BaseController {
    @Resource(name = "userService")
    private UserService userService;

    @GetMapping("/index.html")
    public String index() {
        return "user/index";
    }
    @GetMapping("/login.html")
    public String goLogin() {
        return "user/login";
    }
    @GetMapping("/main.html")
    public String home(HttpSession session){
        return "user/main";
    }

    @PostMapping("/login")
    @ResponseBody
    public AjaxJson login(String username, String password, HttpSession session,
                          HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        log.debug("登陆，name: {}, pwd:{}", username, password);
        try {
            User byLoginName = userService.getByLoginName(username);
            if (byLoginName != null) {
                if (password.equals(byLoginName.getPassword())) {
                    j.setSuccess(true);
                    j.setMsg("ok");
                    LinkedHashMap<String, Object> body = j.getBody();
                    body.put("user", byLoginName);
                    j.setBody(body);
                    session.setAttribute(username, byLoginName);
                    response.addCookie(new Cookie("username", username));
                } else {
                    j.setSuccess(false);
                    j.setMsg("密码错误");
                }
            }
        } catch (Exception e) {
            log.error("登陆失败", e);
            j.setSuccess(false);
            j.setMsg("登陆失败");
        }
        return j;
    }

    @GetMapping("/logout")
    public String loginOut(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        session.removeAttribute("user");
        return "user/index";
    }
}
