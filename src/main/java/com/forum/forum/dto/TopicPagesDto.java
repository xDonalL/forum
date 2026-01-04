package com.forum.forum.dto;

import java.time.LocalDateTime;

public record TopicPagesDto(
        Integer id,
        String title,
        LocalDateTime createdAt,
        Integer authorId,
        String authorLogin,
        String avatar,
        Long commentCount,
        Long likesCount
) {
}
