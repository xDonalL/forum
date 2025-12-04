package com.forum.forum.repository.forum;

import com.forum.forum.model.ForumComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrudForumCommentRepository extends JpaRepository<ForumComment, Integer> {

}

