package com.forum.forum;

import com.forum.forum.model.AdminLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static com.forum.forum.TopicCommentTestData.COMMENT1;
import static com.forum.forum.TopicTestData.TOPIC;
import static com.forum.forum.UserTestData.ADMIN;
import static com.forum.forum.UserTestData.USER;
import static com.forum.forum.model.ActionLog.*;

public class AdminLogTestData {

    public static final AdminLog LOG_BAN = new AdminLog(ADMIN.getLogin(),
            BAN_USER, String.valueOf(USER.getLogin()), USER.getId());

    public static final AdminLog LOG_UNBAN = new AdminLog(ADMIN.getLogin(),
            UNBAN_USER, String.valueOf(USER.getLogin()), USER.getId());

    public static final AdminLog LOG_DELETE_TOPIC = new AdminLog(ADMIN.getLogin(),
            DELETE_TOPIC, String.valueOf(USER.getLogin()), TOPIC.getId());

    public static final AdminLog LOG_DELETE_COMMENT = new AdminLog(ADMIN.getLogin(),
            DELETE_COMMENT, String.valueOf(USER.getLogin()), COMMENT1.getId());

    public static final Page<AdminLog> TEST_LOG = new PageImpl<>(
            List.of(LOG_BAN, LOG_UNBAN, LOG_DELETE_TOPIC, LOG_DELETE_COMMENT));
}
