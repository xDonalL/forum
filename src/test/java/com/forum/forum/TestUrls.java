package com.forum.forum;

public class TestUrls {

    public static final String LOGIN_VIEW = "**/login";

    public static final String ADMIN_PANEL = "/admin/panel";

    public static final String TOPIC_LIST = "/topic";
    public static final String TOPIC_ADD = "/topic/add";
    public static final String TOPIC_LIKE_ADD = "/topic/like/add";
    public static final String TOPIC_LIKE_DELETE = "/topic/like/delete";

    public static final String COMMENT_ADD = "/topic/comment/add";
    public static final String COMMENT_LIKE_ADD = "/topic/comment/like/add";
    public static final String COMMENT_LIKE_DELETE = "/topic/comment/like/delete";


    public static String topicView(Integer id) {
        return "/topic/" + id;
    }

    public static String topicEdit(Integer id) {
        return "/topic/edit/" + id;
    }

    public static String topicDelete(Integer id) {
        return "/topic/delete/" + id;
    }

    public static String commentDelete(Integer id) {
        return "/topic/comment/delete/" + id;
    }

    public static String commentEdit(Integer id) {
        return "/topic/comment/edit/" + id;
    }

    public static String adminBan(Integer userId) {
        return "/admin/ban/" + userId;
    }

    public static String adminUnban(Integer userId) {
        return "/admin/unban/" + userId;
    }
}
