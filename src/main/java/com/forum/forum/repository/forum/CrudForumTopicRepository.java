package com.forum.forum.repository.forum;

import com.forum.forum.model.ForumTopic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrudForumTopicRepository extends JpaRepository<ForumTopic, Integer> {

}

