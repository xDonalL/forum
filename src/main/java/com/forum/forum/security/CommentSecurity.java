package com.forum.forum.security;

import com.forum.forum.model.ForumComment;
import com.forum.forum.model.User;
import com.forum.forum.repository.forum.DataJpaForumCommentRepository;
import com.forum.forum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("commentSecurity")
@RequiredArgsConstructor
public class CommentSecurity {

    private final DataJpaForumCommentRepository commentRepository;
    private final UserService userService;

    public boolean isOwner(Integer commentId) {
        User user = userService.getCurrentUser();
        ForumComment comment = commentRepository.get(commentId);
        return comment != null && comment.getAuthor().getId().equals(user.getId());
    }
}
