package com.forum.forum.dto;

import java.util.List;

public record TopicPageDto(
        TopicDto topic,
        List<TopicCommentDto> comments
) {
}
