package com.uzykj.notice.common.websocket.service.system;


import com.uzykj.notice.common.websocket.utils.Constants;
import com.uzykj.notice.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@Configuration
public class SystemInfoSocketHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object
            > attributes) {
        log.info("socket connect beforeHandshake..");
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession(false);
            if (session != null) {
                // 使用loginName区分WebSocketHandler，以便定向发送消息
                // String loginName = UserUtils.getUser().getLoginName();//获取当前用户登录名
                String loginName = StringUtil.getRandomString(8);
                attributes.put(Constants.WEBSOCKET_LOGINNAME, loginName);
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        log.info("socket connect beforeHandshake..");
    }
}