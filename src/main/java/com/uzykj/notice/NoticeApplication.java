package com.uzykj.notice;

import com.uzykj.notice.common.websocket.service.system.SystemInfoSocketHandler;
import com.uzykj.notice.common.websocket.service.system.SystemInfoSocketHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@SpringBootApplication
@EnableWebSocket
@ComponentScan("com.uzykj")
@Controller
public class NoticeApplication implements WebSocketConfigurer, WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(NoticeApplication.class, args);
    }

    @GetMapping("/")
    public String index() {
        return "home";
    }

    @Autowired
    private SystemInfoSocketHandler socketHandler;

    /**
     * 注册系统通知socket服务
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketHandler, "/systemInfoSocketServer").addInterceptors(new SystemInfoSocketHandshakeInterceptor());
        registry.addHandler(socketHandler, "/sockjs/systemInfoSocketServer").addInterceptors(new SystemInfoSocketHandshakeInterceptor())
                .withSockJS();
    }

    private String baseUrl;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String baseUrl = StringUtils.trimTrailingCharacter(this.baseUrl, '/');
//        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
//        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
//        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
//        registry.addResourceHandler("/layui/**").addResourceLocations("classpath:/static/layui/");
        registry.addResourceHandler(baseUrl + "/swagger-ui/**")
                .addResourceLocations("classpath:META-INF/resource/wenjars/springfox-swagger-ui/")
                .resourceChain(false);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(baseUrl + "/swagger-ui/")
                .setViewName("forward:" + baseUrl + "/swagger-ui/index.html");
    }
}
