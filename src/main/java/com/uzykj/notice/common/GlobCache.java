package com.uzykj.notice.common;

import com.uzykj.notice.domian.Notice;
import com.uzykj.notice.domian.User;
import com.uzykj.notice.enums.Opear;
import com.uzykj.notice.utils.StringUtil;
import com.uzykj.notice.utils.TimeUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ghostxbh
 * @date 2020/7/28
 * @description 全局参数 缓存
 */
public class GlobCache {
    public static Map<String, Object> cacheMap = new ConcurrentHashMap<String, Object>();

    public static final String USER_CACHE = "user_cache";
    public static final String NOTICE_CACHE = "notice_cache";

    /**
     * 操作用户缓存
     *
     * @param user 参数
     * @param type 操作类型
     * @return null | User
     */
    public static User opearUser(User user, Opear type) {
        List<User> userList = (List<User>) cacheMap.get(USER_CACHE);
        switch (type) {
            case ADD:
                if (userList != null && userList.size() > 0) {
                    userList.add(user);
                    cacheMap.put(USER_CACHE, userList);
                } else {
                    List<User> users = new ArrayList<User>();
                    users.add(user);
                    cacheMap.put(USER_CACHE, users);
                }
                break;
            case DEL:
                if (userList != null && userList.size() > 0) {
                    int index = existUser(userList, user.getId());
                    if (index > -1) {
                        userList.remove(index);
                    }
                }
                break;
            case UPD:
                if (userList != null && userList.size() > 0) {
                    int index = existUser(userList, user.getId());
                    if (index > -1) {
                        userList.remove(index);
                    }
                    userList.add(user);
                    cacheMap.put(USER_CACHE, userList);
                }
                break;
            case GET:
                return userList.stream().filter(item -> user.getId().equals(item.getId())).findFirst().get();
            default:
                break;
        }
        return null;
    }

    /**
     * 操作用户集合
     *
     * @param users 用户集合
     * @param type  类型
     * @return List<User> | null
     */
    public static List<User> opearUserList(List<User> users, Opear type) {
        switch (type) {
            case GET:
                return (List<User>) cacheMap.get(USER_CACHE);
            case ADD:
                cacheMap.put(USER_CACHE, users);
                break;
            case DEL:
                cacheMap.remove(USER_CACHE);
                break;
        }
        return null;
    }

    /**
     * 判断缓存是否存在用户
     *
     * @param users
     * @param id
     * @return
     */
    private static int existUser(List<User> users, String id) {
        int index = -1;
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (id.equals(user.getId())) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * 获取缓存通知
     *
     * @param id
     * @return
     */
    public static Notice getNotice(String id) {
        List<Notice> noticeList = (List<Notice>) cacheMap.get(NOTICE_CACHE);
        if (noticeList != null && noticeList.size() > 0) {
            return noticeList.stream().filter(notice -> id.equals(notice.getId())).findFirst().get();
        }
        return null;
    }

    public static void main(String[] args) {
        String[] ids = {"8a38273c-d0a2-4baf-9044-d64c897e0d27", "ba243dc0-6b47-44cc-aac8-d07128fa432f", "f0377e82-aefd-44ba-a9e6-7f2b08039c2b"};
        ArrayList<User> users = new ArrayList<>(3);
        Arrays.asList(ids).forEach(id -> {
            User user = new User();
            user.setId(id);
            user.setCreateTime(TimeUtils.getCurrentTime());
            user.setLoginName(id.substring(0, 8));
            user.setName("李宁");
            user.setPassword(StringUtil.getRandomString(10));
            user.setUpdateTime(TimeUtils.getCurrentTime());
            users.add(user);
        });
        cacheMap.put(USER_CACHE, users);
        System.out.println(cacheMap.keySet().size());
        User user = opearUser(new User("8a38273c-d0a2-4baf-9044-d64c897e0d27"), Opear.GET);
        System.out.println(user);

        String loginName = "ba243dc0";
        List<User> userList = (List<User>) GlobCache.cacheMap.get(GlobCache.USER_CACHE);
        if (userList != null) {
            Optional<User> first = userList.stream().filter(item -> loginName.equals(item.getLoginName())).findFirst();
            System.out.println(first.orElse(null));
        }
    }
}
