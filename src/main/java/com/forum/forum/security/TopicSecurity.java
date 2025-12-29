package com.forum.forum.security;

import com.forum.forum.model.Topic;
import com.forum.forum.model.User;
import com.forum.forum.repository.forum.DataJpaTopicRepository;
import com.forum.forum.service.UserService;
import com.forum.forum.util.exception.NotFoundException;
import com.forum.forum.util.exception.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Component("topicSecurity")
@RequiredArgsConstructor
public class TopicSecurity {
    private final DataJpaTopicRepository topicRepository;
    private final UserService userService;

    public boolean isOwner(Integer topicId) throws Exception {
        User user = userService.getCurrentUser();
        if (user == null) {
            throw new AuthenticationException("User is not authenticated");
        }

        Topic topic = topicRepository.get(topicId);

        if (topic == null) {
            throw new NotFoundException("Topic not found");
        }

        if (!topic.getAuthor().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not owner of this comment");
        }
        return true;
    }
}
