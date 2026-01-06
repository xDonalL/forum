package com.forum.forum.dto;

import java.time.LocalDateTime;

public record TopicDto(
        Integer id,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer authorId,
        String authorLogin,
        String authorAvatar,
        Long likesCount,
        boolean likedByMe
) {
}
