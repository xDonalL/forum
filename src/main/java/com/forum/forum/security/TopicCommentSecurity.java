package com.forum.forum.security;

import com.forum.forum.model.TopicComment;
import com.forum.forum.model.User;
import com.forum.forum.repository.forum.DataJpaTopicCommentRepository;
import com.forum.forum.service.UserService;
import com.forum.forum.util.exception.NotFoundException;
import com.forum.forum.util.exception.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Component("commentSecurity")
@RequiredArgsConstructor
public class TopicCommentSecurity {
    private final DataJpaTopicCommentRepository commentRepository;
    private final UserService userService;

    public boolean isOwner(Integer commentId) throws Exception{
        User user = userService.getCurrentUser();
        if (user == null) {
            throw new AuthenticationException("User is not authenticated");
        }

        TopicComment comment = commentRepository.get(commentId);

        if (comment == null) {
            throw new NotFoundException("Comment not found");
        }

        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not owner of this comment");
        }
        return true;
    }
}
