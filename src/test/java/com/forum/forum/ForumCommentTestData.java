package com.forum.forum;

import com.forum.forum.model.ForumComment;
import com.forum.forum.model.ForumTopic;
import com.forum.forum.model.User;

import static com.forum.forum.ForumTopicTestData.TOPIC1;
import static com.forum.forum.UserTestData.ADMIN;
import static com.forum.forum.UserTestData.USER;

public class ForumCommentTestData {

    public static final Integer COMMENT1_ID = 1;
    public static final Integer COMMENT2_ID = 2;

    public static final ForumComment COMMENT1 = new ForumComment(COMMENT1_ID, "First comment", USER, TOPIC1);
    public static final ForumComment COMMENT2 = new ForumComment(COMMENT2_ID, "Second comment", ADMIN, TOPIC1);

    public static ForumComment getNewComment(User author, ForumTopic topic) {
        return new ForumComment("New message", author, topic);
    }
}
