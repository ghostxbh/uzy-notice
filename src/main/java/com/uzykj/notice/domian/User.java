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
@Document(value = "user")
public class User implements Serializable {
    @Id
    private String id;
    private String loginName;
    private String name;
    private String password;
    private String icon;
    private String createTime;
    private String updateTime;

    public User(String id) {
        this.id = id;
    }

    public User() {
    }
}
