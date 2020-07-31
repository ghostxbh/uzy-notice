package com.uzykj.notice.utils;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author ghostxbh
 * @date 2020/7/30
 * @description
 */
public class HttpParseUtil {

    public static String getHeaders(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String nextkey = headerNames.nextElement();
            String headerValue = request.getHeader(nextkey);
            json.put(nextkey, headerValue);
        }
        return json.toJSONString();
    }
}
