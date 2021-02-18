package com.uzykj.notice.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uzykj.notice.common.Constants;
import com.uzykj.notice.common.websocket.service.system.SystemInfoSocketHandler;
import com.uzykj.notice.domian.Notice;
import com.uzykj.notice.enums.NoticeType;
import com.uzykj.notice.utils.HttpParseUtil;
import com.uzykj.notice.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @author ghostxbh
 * @date 2021/2/18
 * @description
 */
@Service
@Slf4j
public class NoticeApiService {
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private SystemInfoSocketHandler infoSocketHandler;

    public void inFrom(HttpServletRequest request) throws IOException {
        Notice notice = Notice.builder()
                .type(NoticeType.IN.toString().toLowerCase())
                .contents(JSON.toJSONString(StringUtil.getReqBody(request)))
                .headers(HttpParseUtil.getHeaders(request))
                .build();
        noticeService.save(notice);
        infoSocketHandler.sendMessageToAllUsers(Constants.NOTICE_NEW_MSG);
    }

    public void inJson(HttpServletRequest request) throws IOException {
        String body = StringUtil.getReqBody(request);
        Map<String, Object> parameter = StringUtil.getParameter(body);
        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(parameter);
        Notice notice = Notice.builder()
                .type(NoticeType.IN.toString().toLowerCase())
                .contents(JSONObject.toJSONString(valueAsString))
                .headers(HttpParseUtil.getHeaders(request))
                .build();
        noticeService.save(notice);
        infoSocketHandler.sendMessageToAllUsers(Constants.NOTICE_NEW_MSG);
    }
}
