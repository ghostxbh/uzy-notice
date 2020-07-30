package com.uzykj.notice.utils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

/**
 * @author ghostxbh
 * @date 2020/7/28
 * @description
 */
public class StringUtil {

    /**
     * 获取随机字符串
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 获取不带短横杠的uuid
     *
     * @return
     */
    public static String generateId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getReqBody(HttpServletRequest request) throws IOException {
        ServletInputStream is = request.getInputStream();
        StringBuilder sb = new StringBuilder();
        byte[] b = new byte[10 * 1024];
        for (int n; (n = is.read(b)) != -1; ) {
            sb.append(new String(b, 0, n));
        }
        return new String(sb);
    }
}
