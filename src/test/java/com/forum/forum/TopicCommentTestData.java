package com.forum.forum;

import com.forum.forum.dto.TopicCommentDto;
import com.forum.forum.model.Topic;
import com.forum.forum.model.TopicComment;
import com.forum.forum.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.List;

import static com.forum.forum.TopicTestData.TOPIC;
import static com.forum.forum.UserTestData.USER;
import static com.forum.forum.UserTestData.USER_ID;

public class TopicCommentTestData {

    public static final Integer COMMENT1_ID = 1;

    private static final String COMMENT_TEXT = "This is a comment";
    private static final LocalDateTime CREATED_AT = LocalDateTime.now();

    private static final Long LIKES_COUNT = 5L;


    public static final TopicComment COMMENT1 = new TopicComment(
            COMMENT1_ID, COMMENT_TEXT, USER, TOPIC);

    public static final TopicCommentDto COMMENT_DTO1 = new TopicCommentDto(COMMENT1_ID, COMMENT_TEXT,
            CREATED_AT, null, USER_ID, USER.getLogin(), USER.getAvatar(), LIKES_COUNT, false);

    public static final Page<TopicCommentDto> COMMENT_PAGE = new PageImpl<>(List.of(COMMENT_DTO1));

    public static TopicComment getNewComment(User author, Topic topic) {
        return new TopicComment("New message", author, topic);
    }
}
