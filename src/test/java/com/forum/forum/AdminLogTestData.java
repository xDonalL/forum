package com.forum.forum;

import com.forum.forum.model.AdminLog;

import java.util.List;

import static com.forum.forum.TopicCommentTestData.COMMENT1;
import static com.forum.forum.TopicTestData.TOPIC1;
import static com.forum.forum.UserTestData.ADMIN;
import static com.forum.forum.UserTestData.USER;

public class AdminLogTestData {

    public static final AdminLog LOG_BAN = new AdminLog(ADMIN.getLogin(),
            "BAN", String.valueOf(USER.getLogin()), String.valueOf(USER.getId()));

    public static final AdminLog LOG_UNBAN = new AdminLog(ADMIN.getLogin(),
            "UNBAN", String.valueOf(USER.getLogin()), String.valueOf(USER.getId()));

    public static final AdminLog LOG_DELETE_TOPIC = new AdminLog(ADMIN.getLogin(),
            "DELETE_TOPIC", String.valueOf(USER.getLogin()), String.valueOf(TOPIC1.getId()));

    public static final AdminLog LOG_DELETE_COMMENT = new AdminLog(ADMIN.getLogin(),
            "DELETE_COMMENT", String.valueOf(USER.getLogin()), String.valueOf(COMMENT1.getId()));

    public static final List<AdminLog> TEST_LOG = List.of(LOG_BAN, LOG_UNBAN, LOG_DELETE_TOPIC, LOG_DELETE_COMMENT);

}
