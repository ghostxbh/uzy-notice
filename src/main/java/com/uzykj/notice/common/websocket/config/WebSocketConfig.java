package com.uzykj.notice.common.websocket.config;

import com.uzykj.notice.common.websocket.service.onchat.LayIMSocketHandler;
import com.uzykj.notice.common.websocket.service.onchat.LayIMSocketHandshakeInterceptor;
import com.uzykj.notice.common.websocket.service.system.SystemInfoSocketHandler;
import com.uzykj.notice.common.websocket.service.system.SystemInfoSocketHandshakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    	//注册layIM socket服务
        registry.addHandler(layImSocketHandler(),"/notice/layIMSocketServer").addInterceptors(new LayIMSocketHandshakeInterceptor());
        registry.addHandler(layImSocketHandler(), "/notice/sockjs/layIMSocketServer").addInterceptors(new LayIMSocketHandshakeInterceptor())
                .withSockJS();
        
      //注册 系统通知socket服务
        registry.addHandler(systemInfoSocketHandler(),"/notice/systemInfoSocketServer").addInterceptors(new SystemInfoSocketHandshakeInterceptor());
        registry.addHandler(systemInfoSocketHandler(), "/notice/sockjs/systemInfoSocketServer").addInterceptors(new SystemInfoSocketHandshakeInterceptor())
                .withSockJS();
    }

    @Bean
    public WebSocketHandler layImSocketHandler(){
        return new LayIMSocketHandler();
    }

    @Bean
    public WebSocketHandler systemInfoSocketHandler(){
        return new SystemInfoSocketHandler();
    }
}