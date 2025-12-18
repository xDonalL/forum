package com.forum.forum;

import com.forum.forum.model.ForumTopic;
import com.forum.forum.model.User;

import java.util.List;

import static com.forum.forum.UserTestData.ADMIN;
import static com.forum.forum.UserTestData.USER;

public class ForumTopicTestData {

    public static final Integer TOPIC1_ID = 1;
    public static final Integer TOPIC2_ID = 2;

    public static final ForumTopic TOPIC1 = new ForumTopic(TOPIC1_ID, "Topic One", "Description for topic one", USER);
    public static final ForumTopic TOPIC2 = new ForumTopic(TOPIC2_ID, "Topic Two", "Description for topic two", ADMIN);

    public static final List<ForumTopic> ALL_TOPIC = List.of(TOPIC1, TOPIC2);

    public static ForumTopic getNewTopic(User author) {
        return new ForumTopic("New Topic", "New description", author);
    }
}
