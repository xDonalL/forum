package com.forum.forum.dto;

import org.springframework.data.domain.Page;

public record TopicPageDto(
        TopicDto topic,
        Page<TopicCommentDto> comments
) {
}
