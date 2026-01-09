package com.forum.forum;

import com.forum.forum.model.Topic;
import com.forum.forum.model.TopicComment;
import com.forum.forum.model.User;

import static com.forum.forum.TopicTestData.TOPIC;
import static com.forum.forum.UserTestData.USER;

public class TopicCommentTestData {

    public static final Integer COMMENT1_ID = 1;

    public static final TopicComment COMMENT1 = new TopicComment(COMMENT1_ID, "First comment", USER, TOPIC);

    public static TopicComment getNewComment(User author, Topic topic) {
        return new TopicComment("New message", author, topic);
    }
}
