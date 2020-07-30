package com.uzykj.notice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

/**
 * @author ghostxbh
 * @date 2020/7/28
 * @description
 */
@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "home";
    }


}
