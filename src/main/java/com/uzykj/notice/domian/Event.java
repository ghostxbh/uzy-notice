package com.uzykj.notice.domian;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author ghostxbh
 * @date 2020/7/28
 * @description
 */
@Data
@AllArgsConstructor
public class Event {
    private String userAgent;
    private String host;
    private String accept;
    private String connection;
    private String contentType;
    private String contentLength;
}
