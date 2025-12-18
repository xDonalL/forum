package com.forum.forum.security;

import com.forum.forum.model.Topic;
import com.forum.forum.model.User;
import com.forum.forum.repository.forum.DataJpaTopicRepository;
import com.forum.forum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("topicSecurity")
@RequiredArgsConstructor
public class TopicSecurity {
    private final DataJpaTopicRepository topicRepository;
    private final UserService userService;

    public boolean isOwner(Integer topicId) {
        User user = userService.getCurrentUser();
        Topic comment = topicRepository.get(topicId);
        return comment != null && comment.getAuthor().getId().equals(user.getId());
    }
}
