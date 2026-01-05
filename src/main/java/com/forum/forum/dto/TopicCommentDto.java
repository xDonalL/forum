package com.forum.forum.dto;

import java.time.LocalDateTime;

public record TopicCommentDto(
        Integer commentId,
        String comment,
        LocalDateTime dateCreated,
        LocalDateTime updatedAt,
        Integer authorId,
        String authorLogin,
        String authorAvatar,
        Long likesCount
) {
}
