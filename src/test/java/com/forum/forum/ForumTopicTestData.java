package com.forum.forum;

import com.forum.forum.model.Topic;
import com.forum.forum.model.User;

import java.util.List;

import static com.forum.forum.UserTestData.ADMIN;
import static com.forum.forum.UserTestData.USER;

public class ForumTopicTestData {

    public static final Integer TOPIC1_ID = 1;
    public static final Integer TOPIC2_ID = 2;

    public static final Topic TOPIC1 = new Topic(TOPIC1_ID, "Topic One", "Description for topic one", USER);
    public static final Topic TOPIC2 = new Topic(TOPIC2_ID, "Topic Two", "Description for topic two", ADMIN);

    public static final List<Topic> ALL_TOPIC = List.of(TOPIC1, TOPIC2);

    public static Topic getNewTopic(User author) {
        return new Topic("New Topic", "New description", author);
    }
}
