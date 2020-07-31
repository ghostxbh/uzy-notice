package com.uzykj.notice.domian;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author ghostxbh
 * @date 2020/7/28
 * @description
 */
@Data
@Document(value = "notice")
public class Notice implements Serializable {
    @Id
    private String id;
    private String type;
    private String contents;
    private String headers;
    private String creator;
    private String createTime;
    private String updateTime;
    private Long updateStamp;
}
