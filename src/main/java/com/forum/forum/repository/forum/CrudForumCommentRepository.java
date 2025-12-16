package com.forum.forum.repository.forum;

import com.forum.forum.model.ForumComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CrudForumCommentRepository extends JpaRepository<ForumComment, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM ForumComment c WHERE c.id=:id")
    int delete(@Param("id") int id);

    @Transactional
    @Modifying
    @Query("DELETE FROM ForumComment c WHERE c.topic.id = :topicId")
    int deleteByTopicId(@Param("topicId") int topicId);
}

