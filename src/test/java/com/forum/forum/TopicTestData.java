package com.forum.forum;

import com.forum.forum.dto.TopicDto;
import com.forum.forum.dto.TopicPagesDto;
import com.forum.forum.model.Topic;
import com.forum.forum.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.List;

import static com.forum.forum.UserTestData.USER;
import static com.forum.forum.UserTestData.USER_ID;

public class TopicTestData {

    public static final Integer TOPIC1_ID = 1;

    public static final Long COMMENT_COUNT = 5L;
    public static final Long LIKES_COUNT = 5L;

    private static final String TITLE = "Topic Title";
    private static final String CONTENT = "Topic Content";
    private static final LocalDateTime CREATED_AT = LocalDateTime.now();


    public static final Topic TOPIC = new Topic(TOPIC1_ID, TITLE, CONTENT, USER);


    public static final TopicPagesDto PAGES_TOPIC_DTO1 = new TopicPagesDto(
            TOPIC1_ID, TITLE, CREATED_AT, null, USER_ID,
            USER.getAvatar(), USER.getLogin(), COMMENT_COUNT, LIKES_COUNT);

    public static final TopicPagesDto PAGES_TOPIC_DTO2 = new TopicPagesDto(
            TOPIC1_ID, TITLE, CREATED_AT, null, USER_ID,
            USER.getAvatar(), USER.getLogin(), COMMENT_COUNT, LIKES_COUNT);

    public static final TopicDto TOPIC_DTO = new TopicDto(
            TOPIC1_ID, TITLE, CONTENT, CREATED_AT, null,
            USER_ID, USER.getLogin(), USER.getAvatar(), LIKES_COUNT, false);

    public static final Page<TopicPagesDto> ALL_TOPIC_PAGE = new PageImpl<>(
            List.of(PAGES_TOPIC_DTO1, PAGES_TOPIC_DTO2));

    public static Topic getNewTopic(User author) {
        return new Topic("New Topic", "New description", author);
    }
}
